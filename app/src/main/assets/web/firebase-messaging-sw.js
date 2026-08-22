// Unified Service Worker: Firebase Cloud Messaging & Offline Cache Storage
const CACHE_NAME = 'restaurante-rivera-v2';
const STATIC_ASSETS = [
    './',
    './index.html',
    './images/cat_entradas.jpg',
    './images/cat_fuertes.jpg',
    './images/cat_bebidas.jpg',
    './images/cat_postres.jpg',
    'https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Playfair+Display:wght@700;800&display=swap'
];

// 1. Install Event: Pre-cache static UI assets
self.addEventListener('install', (event) => {
    console.log('[Service Worker] Instalando y precacheando activos...');
    event.waitUntil(
        caches.open(CACHE_NAME).then((cache) => {
            return cache.addAll(STATIC_ASSETS).catch((err) => {
                console.warn('[Service Worker] Advertencia al precachear algunos recursos:', err);
            });
        }).then(() => self.skipWaiting())
    );
});

// 2. Activate Event: Clean up older cache versions
self.addEventListener('activate', (event) => {
    console.log('[Service Worker] Activado y listo.');
    event.waitUntil(
        caches.keys().then((cacheNames) => {
            return Promise.all(
                cacheNames.map((cache) => {
                    if (cache !== CACHE_NAME) {
                        console.log('[Service Worker] Limpiando caché anterior:', cache);
                        return caches.delete(cache);
                    }
                })
            );
        }).then(() => self.clients.claim())
    );
});

// 3. Fetch Event: Stale-While-Revalidate Strategy for web assets
self.addEventListener('fetch', (event) => {
    // Only handle GET requests and skip firebase firestore/google apis streaming websockets
    if (event.request.method !== 'GET') return;
    const url = new URL(event.request.url);

    // Skip firestore synchronization endpoints from SW caching
    if (url.hostname.includes('firestore.googleapis.com') || url.hostname.includes('firebaseio.com') || url.pathname.includes('/google.firestore.')) {
        return;
    }

    event.respondWith(
        caches.match(event.request).then((cachedResponse) => {
            const fetchPromise = fetch(event.request).then((networkResponse) => {
                if (networkResponse && networkResponse.status === 200 && (url.origin === self.location.origin || url.hostname.includes('fonts.gstatic.com') || url.hostname.includes('fonts.googleapis.com') || url.hostname.includes('unsplash.com'))) {
                    const responseClone = networkResponse.clone();
                    caches.open(CACHE_NAME).then((cache) => {
                        cache.put(event.request, responseClone);
                    });
                }
                return networkResponse;
            }).catch((fetchErr) => {
                // If offline and request is an HTML navigation, fallback to cached index.html
                if (event.request.mode === 'navigate') {
                    return caches.match('./index.html') || caches.match('./');
                }
                return cachedResponse;
            });

            return cachedResponse || fetchPromise;
        })
    );
});

// 4. Firebase Cloud Messaging Setup for Background Push Notifications
try {
    importScripts('https://www.gstatic.com/firebasejs/10.8.0/firebase-app-compat.js');
    importScripts('https://www.gstatic.com/firebasejs/10.8.0/firebase-messaging-compat.js');

    const firebaseConfig = {
        apiKey: "AIzaSyDummyKeyForStudioBuildSync12345",
        authDomain: "restaurante-rivera.firebaseapp.com",
        projectId: "restaurante-rivera",
        storageBucket: "restaurante-rivera.appspot.com",
        messagingSenderId: "1234567890"
    };

    firebase.initializeApp(firebaseConfig);
    const messaging = firebase.messaging();

    messaging.onBackgroundMessage((payload) => {
        console.log('[firebase-messaging-sw.js] Mensaje FCM recibido en segundo plano:', payload);
        const orderId = payload.data?.orderId || payload.notification?.tag || 'pedido';
        const status = (payload.data?.status || payload.notification?.body || 'actualizado').toLowerCase();
        
        let title = payload.notification?.title || payload.data?.title || '🍽️ Restaurante Rivera';
        let body = payload.notification?.body || payload.data?.body || 'El estado de tu orden ha cambiado.';
        let icon = 'images/cat_fuertes.jpg';

        if (status.includes('cocina') || status.includes('preparaci')) {
            title = `👨‍🍳 ¡Orden #${orderId} en Cocina!`;
            body = body || 'Nuestros cocineros han comenzado a preparar tus deliciosos platillos.';
            icon = 'images/cat_fuertes.jpg';
        } else if (status.includes('camino') || status.includes('ruta') || status.includes('repartidor')) {
            title = `🛵 ¡Orden #${orderId} en Camino!`;
            body = body || 'Tu pedido ha salido del restaurante y va en camino a tu dirección.';
            icon = 'images/cat_entradas.jpg';
        } else if (status.includes('listo') || status.includes('servir')) {
            title = `🎉 ¡Orden #${orderId} Lista!`;
            body = body || 'Tu orden está lista para ser servida o retirada en barra.';
            icon = 'images/cat_fuertes.jpg';
        } else if (status.includes('entregado') || status.includes('completado')) {
            title = `🎁 ¡Orden #${orderId} Entregada!`;
            body = body || '¡Buen provecho! Gracias por ordenar en Restaurante Rivera.';
            icon = 'images/cat_postres.jpg';
        }

        const notificationOptions = {
            body: body,
            icon: icon,
            badge: icon,
            tag: `order-${orderId}`,
            renotify: true,
            data: {
                orderId: orderId,
                status: status,
                url: './?order=' + orderId
            },
            vibrate: [300, 150, 300, 150, 400]
        };

        self.registration.showNotification(title, notificationOptions);
    });
} catch (e) {
    console.warn("[firebase-messaging-sw.js] FCM background worker warning:", e);
}

self.addEventListener('notificationclick', (event) => {
    event.notification.close();
    event.waitUntil(
        clients.matchAll({ type: 'window', includeUncontrolled: true }).then((windowClients) => {
            for (let client of windowClients) {
                if ('focus' in client) {
                    return client.focus();
                }
            }
            if (clients.openWindow) {
                return clients.openWindow('./');
            }
        })
    );
});
