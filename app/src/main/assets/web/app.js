// ======================================================================
// RESTAURANTE RIVERA - WEB ORDERING & REAL-TIME SYNC LOGIC (app.js)
// ======================================================================

// --- 1. FIREBASE CONFIGURATION & INITIALIZATION ---
// Sincronizado dinámicamente con el proyecto de la app Android (dev-restaurante-app)
const urlParamsInit = new URLSearchParams(window.location.search);
const activeProjectId = urlParamsInit.get('projectId') || window.FIREBASE_PROJECT_ID || "dev-restaurante-app";

const firebaseConfig = {
    apiKey: "AIzaSyDummyKeyForStudioBuildSync12345",
    authDomain: `${activeProjectId}.firebaseapp.com`,
    projectId: activeProjectId,
    storageBucket: `${activeProjectId}.appspot.com`,
    messagingSenderId: "433380736991"
};

let db = null;
try {
    if (typeof firebase !== 'undefined') {
        if (!firebase.apps || !firebase.apps.length) {
            firebase.initializeApp(firebaseConfig);
        }
        db = firebase.firestore();
        console.log("Firebase Firestore inicializado exitosamente en app.js");
    }
} catch (err) {
    console.warn("Firestore fallback mode (offline/local):", err);
}

// Service Worker Registration for Notifications & PWA (solo en navegadores web estándar, evitando sobrecarga en WebView)
const isAndroidWebView = /wv|Android.*Version\/[\d.]+.*Chrome/i.test(navigator.userAgent) || window.location.protocol === 'file:';
if ('serviceWorker' in navigator && !isAndroidWebView) {
    window.addEventListener('load', () => {
        navigator.serviceWorker.register('firebase-messaging-sw.js')
            .then(reg => console.log("Service Worker registrado con éxito:", reg.scope))
            .catch(err => console.log("Service Worker notice (opcional):", err));
    });
}

// --- 2. GLOBAL STATE ---
let currentOrderType = 'MESA'; // 'MESA' | 'DOMICILIO'
let currentMesaNum = '1';
let currentMesaFormatted = 'Mesa 1';

let currentTheme = {
    themeName: "Azul Rivera Clásico",
    primaryColorHex: "#1E3A8A",
    secondaryColorHex: "#D97706",
    bannerImageUrl: "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&q=80",
    welcomeMessage: "¡Bienvenidos a Restaurante Rivera! Auténtica gastronomía guatemalteca con los mejores ingredientes.",
    isActive: true
};

let menuProducts = [
    {
        id: 1,
        name: "Pepián Tradicional de Pollo",
        category: "Platillos",
        price: 65.00,
        description: "Receta ancestral con recado de semillas tostadas, arroz blanco, tamalito y ensalada fresca.",
        imageUrl: "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&q=80",
        isAvailable: true,
        isVisibleWeb: true
    },
    {
        id: 2,
        name: "Churrasco Típico Rivera",
        category: "Platillos",
        price: 85.00,
        description: "Corte de carne de res a la parrilla, guacamol, frijoles volteados, chirmol y cebollitas asadas.",
        imageUrl: "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=600&q=80",
        isAvailable: true,
        isVisibleWeb: true
    },
    {
        id: 3,
        name: "Hamburguesa Gourmet de la Casa",
        category: "Platillos",
        price: 55.00,
        description: "Carne 100% res premium, queso fundido, tocino crujiente, cebolla caramelizada y papas fritas.",
        imageUrl: "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600&q=80",
        isAvailable: true,
        isVisibleWeb: true
    },
    {
        id: 4,
        name: "Cerveza Gallo Fría",
        category: "Bebidas",
        price: 25.00,
        description: "Nuestra cerveza nacional por excelencia, servida bien fría en vaso escarchado.",
        imageUrl: "https://images.unsplash.com/photo-1608270199042-3e2840c83a1b?w=600&q=80",
        isAvailable: true,
        isVisibleWeb: true
    },
    {
        id: 5,
        name: "Limonada Natural con Hierbabuena",
        category: "Bebidas",
        price: 18.00,
        description: "Refrescante jugo de limón natural, hojas de hierbabuena fresca y hielo triturado.",
        imageUrl: "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=600&q=80",
        isAvailable: true,
        isVisibleWeb: true
    },
    {
        id: 6,
        name: "Pastel Tres Leches Casero",
        category: "Postres",
        price: 30.00,
        description: "Bizcocho suave bañado en mezcla cremosa de tres leches y toque de canela fina.",
        imageUrl: "https://images.unsplash.com/photo-1551024709-8f23befc6f87?w=600&q=80",
        isAvailable: false,
        isVisibleWeb: true
    },
    {
        id: 7,
        name: "Guacamol Especial con Nachos",
        category: "Entradas",
        price: 35.00,
        description: "Aguacate criollo sazonado, pico de gallo, queso fresco y totopos crujientes de maíz.",
        imageUrl: "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=600&q=80",
        isAvailable: true,
        isVisibleWeb: true
    }
];

let cart = []; // Array de { product, quantity, notes }
let selectedCategory = "Todos";
let searchQuery = "";

// --- 3. URL PARAMETERS EXTRACTION & SERVICE TYPE LOGIC ---
function parseUrlParams() {
    const urlParams = new URLSearchParams(window.location.search);
    const tipoParam = (urlParams.get('tipo') || '').toLowerCase().trim();
    const numParam = (urlParams.get('num') || '').trim();
    const mesaParam = (urlParams.get('mesa') || urlParams.get('table') || '').trim();

    const prominentBanner = document.getElementById('prominentMesaBanner');
    const prominentText = document.getElementById('prominentMesaText');
    const tableLabel = document.getElementById('tableLabel');
    const selectTipo = document.getElementById('tipoServicio') || document.getElementById('selectOrigin');
    const optionMesa = document.getElementById('optionMesa');

    // CASO A: ESCANEO DESDE MESA (?tipo=mesa&num=X o ?mesa=X)
    if (tipoParam === 'mesa' || (mesaParam && tipoParam !== 'domicilio')) {
        currentOrderType = 'MESA';
        
        let rawNum = numParam;
        if (!rawNum && mesaParam) {
            rawNum = mesaParam.replace(/[^0-9]/g, '');
            if (!rawNum) rawNum = mesaParam;
        }
        if (!rawNum) rawNum = '1';
        
        currentMesaNum = rawNum;
        currentMesaFormatted = rawNum.toLowerCase().startsWith('mesa') ? rawNum : `Mesa ${rawNum}`;

        if (selectTipo) {
            if (optionMesa) {
                optionMesa.style.display = 'block';
                optionMesa.value = currentMesaFormatted;
                optionMesa.textContent = `🍽️ En Mesa (${currentMesaFormatted})`;
                optionMesa.selected = true;
            } else {
                const opt = new Option(`🍽️ En Mesa (${currentMesaFormatted})`, currentMesaFormatted, true, true);
                selectTipo.add(opt, 0);
            }
            selectTipo.value = currentMesaFormatted;
        }

        if (prominentBanner) {
            prominentBanner.style.display = 'flex';
            prominentBanner.style.background = 'linear-gradient(135deg, #1E3A8A 0%, #2563EB 100%)';
            if (prominentText) prominentText.textContent = `Ordenando desde: ${currentMesaFormatted}`;
        }
        if (tableLabel) {
            tableLabel.textContent = currentMesaFormatted;
        }

        console.log(`Modo MESA activado: ${currentMesaFormatted}`);

    } else {
        // CASO B: ESCANEO SERVICIO A DOMICILIO / REDES SOCIALES (?tipo=domicilio o URL normal)
        currentOrderType = 'DOMICILIO';
        if (optionMesa) optionMesa.style.display = 'none';

        if (selectTipo) {
            selectTipo.value = 'A Domicilio';
        }

        if (prominentBanner) {
            prominentBanner.style.display = 'flex';
            prominentBanner.style.background = 'linear-gradient(135deg, #059669 0%, #10B981 100%)';
            if (prominentText) prominentText.textContent = "🛵 Pedidos Rivera: A Domicilio & Para Llevar";
        }
        if (tableLabel) {
            tableLabel.textContent = "A Domicilio";
        }

        console.log("Modo EXTERNO / A DOMICILIO activado por defecto.");
    }

    applyCartVisibilityForCurrentOrderType();
}

// Control de visibilidad estricta de campos del carrito según MESA vs DOMICILIO
function applyCartVisibilityForCurrentOrderType() {
    const headerMesa = document.getElementById('headerServicioMesa');
    const numMesaSpan = document.getElementById('numMesaSpan');
    const groupItems = document.getElementById('groupCartItems');
    const groupName = document.getElementById('groupCustomerName');
    const groupPhone = document.getElementById('groupCustomerPhone');
    const groupGps = document.getElementById('groupCustomerGps');
    const groupAddress = document.getElementById('groupCustomerAddress');
    const groupFormaPago = document.getElementById('groupFormaPago');
    const groupNotas = document.getElementById('groupNotas');

    if (currentOrderType === 'MESA') {
        // CASO A: ESCANEO DESDE MESA
        // 1. Encabezado destacado arriba del carrito: "Servicio en Mesa X"
        if (headerMesa) {
            headerMesa.style.display = 'block';
            if (numMesaSpan) {
                numMesaSpan.textContent = currentMesaFormatted;
            } else {
                headerMesa.textContent = `🍽️ Servicio en ${currentMesaFormatted}`;
            }
        }
        // 2. CAMPOS VISIBLES:
        if (groupItems) groupItems.style.display = 'block';
        if (groupNotas) groupNotas.style.display = 'block';

        // 3. CAMPOS OCULTOS / ELIMINADOS (NO MOSTRAR EN MESA):
        if (groupPhone) groupPhone.style.display = 'none';
        if (groupGps) groupGps.style.display = 'none';
        if (groupAddress) groupAddress.style.display = 'none';
        if (groupFormaPago) groupFormaPago.style.display = 'none';
        if (groupName) groupName.style.display = 'none';
    } else {
        // CASO B: ESCANEO SERVICIO A DOMICILIO / REDES SOCIALES
        if (headerMesa) {
            headerMesa.style.display = 'none';
        }
        // CAMPOS VISIBLES EN ESTE ORDEN EXACTO:
        if (groupItems) groupItems.style.display = 'block';
        if (groupName) groupName.style.display = 'block';
        if (groupPhone) groupPhone.style.display = 'block';
        if (groupGps) groupGps.style.display = 'block';
        if (groupAddress) groupAddress.style.display = 'block';
        if (groupFormaPago) groupFormaPago.style.display = 'block';
        if (groupNotas) groupNotas.style.display = 'block';
    }
}

// Controla la visibilidad condicional según selección manual de tipo (si existe select)
function handleTipoServicioChange() {
    const select = document.getElementById('tipoServicio') || document.getElementById('selectOrigin');
    if (!select) return;
    const val = select.value;
    if (val.toLowerCase().includes('mesa')) {
        currentOrderType = 'MESA';
    } else {
        currentOrderType = 'DOMICILIO';
    }
    applyCartVisibilityForCurrentOrderType();
}

function handleOriginChange() {
    handleTipoServicioChange();
}

// GPS Location helper
function obtenerUbicacionGps() {
    const inputGps = document.getElementById('gpsLink') || document.getElementById('inputCustomerGps');
    if (!inputGps) return;
    if (!navigator.geolocation) {
        alert("La geolocalización no está soportada por tu navegador.");
        return;
    }
    inputGps.value = "Obteniendo ubicación GPS...";
    navigator.geolocation.getCurrentPosition(
        (position) => {
            const lat = position.coords.latitude.toFixed(6);
            const lng = position.coords.longitude.toFixed(6);
            const mapsUrl = `https://maps.google.com/?q=${lat},${lng}`;
            inputGps.value = mapsUrl;
        },
        (error) => {
            alert("No se pudo obtener la ubicación GPS automática: " + error.message);
            inputGps.value = "";
        },
        { timeout: 10000, enableHighAccuracy: true }
    );
}

// --- 4. THEME & CATALOG RENDERING ---
function applyTheme(theme) {
    if (!theme) return;
    currentTheme = theme;
    const root = document.documentElement;
    if (theme.primaryColorHex) root.style.setProperty('--primary-color', theme.primaryColorHex);
    if (theme.secondaryColorHex) root.style.setProperty('--secondary-color', theme.secondaryColorHex);

    if (theme.themeName) {
        const themeTitle = document.getElementById('themeTitle');
        const themeTag = document.getElementById('themeTag');
        if (themeTitle) themeTitle.textContent = theme.themeName;
        if (themeTag) themeTag.textContent = "Tema Web: " + theme.themeName;
    }
    if (theme.welcomeMessage) {
        const welcome = document.getElementById('themeWelcome');
        if (welcome) welcome.textContent = theme.welcomeMessage;
    }
    if (theme.bannerImageUrl) {
        const banner = document.getElementById('bannerImage');
        if (banner) banner.src = theme.bannerImageUrl;
    }
}

function setupFirestoreListeners() {
    if (!db) return;
    try {
        // Escuchar Tema Activo
        db.collection("temas").where("isActive", "==", true).onSnapshot(snapshot => {
            if (snapshot && !snapshot.empty) {
                applyTheme(snapshot.docs[0].data());
            }
        }, err => console.log("Theme listener error:", err));

        // Escuchar Catálogo de Productos ('productos' con fallback a 'menu_items')
        const onProductsSnapshot = (snapshot) => {
            if (snapshot && !snapshot.empty) {
                const remoteItems = [];
                snapshot.forEach(doc => {
                    const data = doc.data();
                    remoteItems.push({
                        id: data.id || doc.id,
                        name: data.name || "Sin nombre",
                        category: data.category || "Platillos",
                        price: Number(data.price) || 0,
                        description: data.description || "",
                        imageUrl: data.imageUrl || "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&q=80",
                        isAvailable: data.isAvailable !== false,
                        isVisibleWeb: data.isVisibleWeb !== false
                    });
                });
                if (remoteItems.length > 0) {
                    menuProducts = remoteItems;
                    renderCategories();
                    renderProductsGrid();
                }
            }
        };

        db.collection("productos").onSnapshot(onProductsSnapshot, () => {
            db.collection("menu_items").onSnapshot(onProductsSnapshot, err2 => console.log("Menu items sync:", err2));
        });
    } catch (e) {
        console.warn("Firestore listeners error:", e);
    }
}

function renderCategories() {
    const bar = document.getElementById('categoriesBar');
    if (!bar) return;

    const uniqueCategories = ["Todos", ...new Set(menuProducts.map(p => p.category).filter(Boolean))];
    bar.innerHTML = uniqueCategories.map(cat => `
        <button class="category-chip ${cat.toLowerCase() === selectedCategory.toLowerCase() ? 'active' : ''}" data-cat="${cat}" onclick="selectCategory('${cat}')">
            ${cat}
        </button>
    `).join('');
}

function selectCategory(category) {
    selectedCategory = category;
    renderCategories();
    renderProductsGrid();
}

function renderProductsGrid() {
    const container = document.getElementById('productsContainer') || document.getElementById('productsGrid');
    if (!container) return;

    let filtered = menuProducts.filter(p => p.isVisibleWeb !== false);
    if (selectedCategory !== "Todos") {
        filtered = filtered.filter(p => p.category && p.category.toLowerCase() === selectedCategory.toLowerCase());
    }
    if (searchQuery.trim() !== "") {
        const normalizeStr = (s) => (s || "").toLowerCase().normalize("NFD").replace(/[\u0300-\u036f]/g, "").trim();
        const q = normalizeStr(searchQuery);
        filtered = filtered.filter(p => 
            normalizeStr(p.name).includes(q) || 
            normalizeStr(p.description).includes(q) ||
            normalizeStr(p.category).includes(q)
        );
    }

    if (filtered.length === 0) {
        container.innerHTML = `
            <div class="empty-catalog" style="text-align: center; padding: 40px 20px; color: var(--text-muted);">
                <p style="font-size: 36px; margin-bottom: 8px;">🍽️</p>
                <h3 style="font-size: 18px; font-weight: 700; color: var(--text-main);">No se encontraron platillos</h3>
                <p style="font-size: 13px;">Prueba buscando con otra palabra o categoría.</p>
            </div>
        `;
        return;
    }

    container.innerHTML = filtered.map(product => {
        const isAvail = product.isAvailable;
        const inCartItem = cart.find(c => String(c.product.id) === String(product.id));
        const qtyInCart = inCartItem ? inCartItem.quantity : 0;
        const fallbackImg = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&q=80";
        const displayImg = product.imageUrl && product.imageUrl.trim().length > 0 ? product.imageUrl : fallbackImg;
        const safeId = String(product.id).replace(/'/g, "\\'");

        return `
            <div class="product-card" id="product-${safeId}">
                <div class="product-img-wrapper">
                    <img class="product-img" src="${displayImg}" alt="${product.name}" loading="lazy" onerror="this.src='${fallbackImg}'">
                    ${!isAvail ? `<span class="product-status-tag">AGOTADO</span>` : ''}
                </div>
                <div class="product-details">
                    <div>
                        <div class="product-header-row">
                            <h3 class="product-name">${product.name}</h3>
                            <span class="product-cat-badge">${product.category}</span>
                        </div>
                        <p class="product-desc">${product.description || 'Deliciosa preparación artesanal elaborada con ingredientes frescos.'}</p>
                    </div>
                    <div class="product-footer">
                        <span class="product-price">Q${Number(product.price).toFixed(2)}</span>
                        
                        ${!isAvail ? `
                            <button class="btn-add" disabled>Agotado</button>
                        ` : (qtyInCart > 0 ? `
                            <div class="cart-qty-control" style="background: #F1F5F9; border-radius: 8px; padding: 2px 6px;">
                                <button class="btn-qty" onclick="updateCartQuantity('${safeId}', -1)">−</button>
                                <span class="qty-val" style="min-width: 20px; font-weight: 800;">${qtyInCart}</span>
                                <button class="btn-qty" onclick="updateCartQuantity('${safeId}', 1)">+</button>
                            </div>
                        ` : `
                            <button class="btn-add" onclick="addToCart('${safeId}')">
                                <span>+ Agregar</span>
                            </button>
                        `)}
                    </div>
                </div>
            </div>
        `;
    }).join('');
}

// --- 5. CART OPERATIONS ---
function addToCart(productId) {
    const product = menuProducts.find(p => String(p.id) === String(productId));
    if (!product || !product.isAvailable) return;
    const existing = cart.find(c => String(c.product.id) === String(productId));
    if (existing) {
        existing.quantity += 1;
    } else {
        cart.push({ product, quantity: 1, notes: "" });
    }
    updateCartUI();
    renderProductsGrid();
}

function updateCartQuantity(productId, delta) {
    const index = cart.findIndex(c => String(c.product.id) === String(productId));
    if (index === -1) return;
    cart[index].quantity += delta;
    if (cart[index].quantity <= 0) {
        cart.splice(index, 1);
    }
    updateCartUI();
    renderProductsGrid();
    const cartModal = document.getElementById('cartModal');
    if (cartModal && cartModal.classList.contains('open')) {
        renderCartModalItems();
    }
}

function updateCartUI() {
    const totalCount = cart.reduce((acc, item) => acc + item.quantity, 0);
    const subtotal = cart.reduce((acc, item) => acc + (item.product.price * item.quantity), 0);
    const tip = subtotal * 0.10;
    const grandTotal = subtotal + tip;

    const badge = document.getElementById('cartCountBadge') || document.getElementById('cartBarCount');
    const summary = document.getElementById('cartBarItemsSummary');
    const cartBarTotal = document.getElementById('cartBarTotal');

    if (badge) badge.textContent = totalCount;
    if (summary) summary.textContent = `${totalCount} producto${totalCount !== 1 ? 's' : ''} seleccionado${totalCount !== 1 ? 's' : ''}`;
    if (cartBarTotal) cartBarTotal.textContent = `Q${grandTotal.toFixed(2)}`;

    const bar = document.getElementById('cartBar');
    if (bar) {
        if (totalCount > 0) {
            bar.classList.add('visible');
        } else {
            bar.classList.remove('visible');
            closeCartModal();
        }
    }
}

function openCartModal() {
    if (cart.length === 0) return;
    applyCartVisibilityForCurrentOrderType();
    renderCartModalItems();
    const backdrop = document.getElementById('modalBackdrop');
    const modal = document.getElementById('cartModal');
    if (backdrop) backdrop.classList.add('open');
    if (modal) modal.classList.add('open');
}

function closeCartModal() {
    const backdrop = document.getElementById('modalBackdrop');
    const modal = document.getElementById('cartModal');
    if (backdrop) backdrop.classList.remove('open');
    if (modal) modal.classList.remove('open');
}

function renderCartModalItems() {
    const listContainer = document.getElementById('cartItemsList');
    if (!listContainer) return;
    const subtotal = cart.reduce((acc, item) => acc + (item.product.price * item.quantity), 0);

    if (cart.length === 0) {
        listContainer.innerHTML = "<p style='color: var(--text-muted); text-align: center; padding: 20px;'>Tu carrito está vacío.</p>";
        return;
    }

    listContainer.innerHTML = cart.map(item => {
        const safeItemId = String(item.product.id).replace(/'/g, "\\'");
        return `
        <div class="cart-item-row">
            <div class="cart-item-info">
                <h4>${item.product.name}</h4>
                <p>Q${Number(item.product.price).toFixed(2)} c/u</p>
            </div>
            <div class="cart-qty-control">
                <button class="btn-qty" onclick="updateCartQuantity('${safeItemId}', -1)">−</button>
                <span class="qty-val">${item.quantity}</span>
                <button class="btn-qty" onclick="updateCartQuantity('${safeItemId}', 1)">+</button>
            </div>
        </div>
        `;
    }).join('');

    const subEl = document.getElementById('summarySubtotal');
    const totEl = document.getElementById('summaryTotal');
    if (subEl) subEl.textContent = `Q${subtotal.toFixed(2)}`;
    if (totEl) totEl.textContent = `Q${subtotal.toFixed(2)}`;
}

// --- 6. SUBMIT WEB ORDER (Strict Schema & Real-Time Sync) ---
async function submitWebOrder() {
    if (cart.length === 0) {
        alert("Por favor agrega al menos un platillo o bebida a tu pedido.");
        return;
    }

    const btnConfirm = document.getElementById('btnConfirmOrder');
    if (btnConfirm) {
        btnConfirm.disabled = true;
        btnConfirm.innerHTML = `<span>Confirmando y Enviar Pedido...</span>`;
    }

    try {
        const orderId = "PED-WEB-" + Date.now();
        const subtotal = cart.reduce((acc, item) => acc + (item.product.price * item.quantity), 0);
        const montoTotal = Number(subtotal.toFixed(2));

        const arrayDeProductos = cart.map((c, idx) => ({
            id: c.product.id || (idx + 1),
            menuItemId: c.product.id || (idx + 1),
            nombre: c.product.name,
            name: c.product.name,
            productName: c.product.name,
            precio: Number(c.product.price),
            price: Number(c.product.price),
            unitPrice: Number(c.product.price),
            cantidad: c.quantity,
            quantity: c.quantity,
            subtotal: Number((c.product.price * c.quantity).toFixed(2)),
            notas: c.notes || "",
            notes: c.notes || "",
            estado_cocina: "PENDIENTE",
            kitchenStatus: "PENDIENTE"
        }));

        const notasEl = document.getElementById("notas") || document.getElementById("inputGeneralNotes");
        const instruccionesCocina = notasEl ? notasEl.value.trim() : "";

        let webOrderDocument;

        if (currentOrderType === 'MESA') {
            // ==================================================================
            // CASO A: ESCANEO DESDE MESA (?tipo=mesa&num=X)
            // ==================================================================
            webOrderDocument = {
                "id": orderId,
                "tipoPedido": "MESA",
                "numMesa": currentMesaFormatted || "Mesa 1",
                "items": arrayDeProductos,
                "instruccionesCocina": instruccionesCocina,
                "total": montoTotal,
                "estado": "PENDIENTE_CONFIRMACION",
                "timestamp": (typeof firebase !== 'undefined' && firebase.firestore && firebase.firestore.FieldValue)
                    ? firebase.firestore.FieldValue.serverTimestamp()
                    : new Date(),
                // Campos de compatibilidad con POS / Entidad Room
                "webOrderId": orderId,
                "tipoServicio": currentMesaFormatted || "Mesa 1",
                "origin": currentMesaFormatted || "Mesa 1",
                "tableNumber": currentMesaFormatted || "Mesa 1",
                "customerName": currentMesaFormatted || "Mesa 1",
                "customerPhone": "",
                "itemsJson": JSON.stringify(arrayDeProductos),
                "totalAmount": montoTotal,
                "status": "PENDIENTE_CONFIRMACION",
                "paymentMethod": "En Mesa",
                "notes": instruccionesCocina,
                "createdAt": Date.now(),
                "deliveryAddress": ""
            };

        } else {
            // ==================================================================
            // CASO B: ESCANEO SERVICIO A DOMICILIO / REDES SOCIALES
            // ==================================================================
            const nombreEl = document.getElementById("nombre") || document.getElementById("inputCustomerName");
            const telefonoEl = document.getElementById("telefono") || document.getElementById("inputCustomerPhone");
            const gpsEl = document.getElementById("gpsLink") || document.getElementById("inputCustomerGps");
            const direccionEl = document.getElementById("direccion") || document.getElementById("inputCustomerAddress");
            const formaPagoEl = document.getElementById("formaPago");

            const nombreCliente = nombreEl ? nombreEl.value.trim() : "";
            const telefono = telefonoEl ? telefonoEl.value.trim() : "";
            const ubicacionGps = gpsEl ? gpsEl.value.trim() : "";
            const direccionEscrita = direccionEl ? direccionEl.value.trim() : "";
            const formaPago = formaPagoEl ? formaPagoEl.value.trim() : "Efectivo";

            // Validaciones obligatorias de Domicilio
            if (!nombreCliente) {
                alert("Por favor ingresa tu Nombre (Obligatorio).");
                if (nombreEl) nombreEl.focus();
                if (btnConfirm) {
                    btnConfirm.disabled = false;
                    btnConfirm.innerHTML = `<span>Confirmar y Enviar Pedido</span> ➔`;
                }
                return;
            }
            if (!telefono) {
                alert("Por favor ingresa tu Teléfono / WhatsApp (Obligatorio).");
                if (telefonoEl) telefonoEl.focus();
                if (btnConfirm) {
                    btnConfirm.disabled = false;
                    btnConfirm.innerHTML = `<span>Confirmar y Enviar Pedido</span> ➔`;
                }
                return;
            }
            if (!ubicacionGps) {
                alert("Por favor pega tu enlace de Ubicación GPS (Google Maps o Waze) o presiona '📍 Mi GPS'.");
                if (gpsEl) gpsEl.focus();
                if (btnConfirm) {
                    btnConfirm.disabled = false;
                    btnConfirm.innerHTML = `<span>Confirmar y Enviar Pedido</span> ➔`;
                }
                return;
            }

            webOrderDocument = {
                "id": orderId,
                "tipoPedido": "DOMICILIO",
                "nombreCliente": nombreCliente,
                "telefono": telefono,
                "ubicacionGps": ubicacionGps,
                "direccionEscrita": direccionEscrita,
                "formaPago": formaPago,
                "items": arrayDeProductos,
                "instruccionesCocina": instruccionesCocina,
                "total": montoTotal,
                "estado": "PENDIENTE_CONFIRMACION",
                "timestamp": (typeof firebase !== 'undefined' && firebase.firestore && firebase.firestore.FieldValue)
                    ? firebase.firestore.FieldValue.serverTimestamp()
                    : new Date(),
                // Campos de compatibilidad con POS / Entidad Room
                "webOrderId": orderId,
                "tipoServicio": "A Domicilio",
                "origin": "A Domicilio",
                "tableNumber": "A Domicilio",
                "customerName": nombreCliente,
                "customerPhone": telefono,
                "itemsJson": JSON.stringify(arrayDeProductos),
                "totalAmount": montoTotal,
                "status": "PENDIENTE_CONFIRMACION",
                "paymentMethod": formaPago,
                "notes": instruccionesCocina,
                "createdAt": Date.now(),
                "deliveryAddress": (direccionEscrita + (ubicacionGps ? (direccionEscrita ? " | GPS: " : "GPS: ") + ubicacionGps : ""))
            };
        }

        // 1. Guardar en Firestore con timeout de seguridad (previene bloqueos si la red es inestable)
        if (db) {
            try {
                const savePromise = db.collection("pedidos_web").doc(orderId).set(webOrderDocument);
                const timeoutPromise = new Promise((_, reject) => setTimeout(() => reject(new Error("Timeout Firestore")), 5000));
                await Promise.race([savePromise, timeoutPromise]);
                console.log("Pedido guardado exitosamente en Firestore (pedidos_web):", orderId);
            } catch (fsErr) {
                console.warn("Aviso al guardar en Firestore (continuando con respaldo y notificación):", fsErr);
            }
        }

        // 2. Respaldo en localStorage
        try {
            const saved = JSON.parse(localStorage.getItem('pedidos_web_cache') || '[]');
            saved.push(webOrderDocument);
            localStorage.setItem('pedidos_web_cache', JSON.stringify(saved));
        } catch (cacheErr) {
            console.warn("LocalStorage cache note:", cacheErr);
        }

        // 3. Puente nativo con Android WebView si se ejecuta embebido
        if (window.AndroidBridge && typeof window.AndroidBridge.onNewWebOrder === 'function') {
            try {
                window.AndroidBridge.onNewWebOrder(JSON.stringify(webOrderDocument));
            } catch (bErr) {
                console.log("Bridge notification error:", bErr);
            }
        }

        // 4. Mostrar pantalla de confirmación garantizada en Español
        const displayLabel = (currentOrderType === 'MESA') ? (currentMesaFormatted || "Mesa 1") : "A Domicilio";
        const displayName = (currentOrderType === 'MESA') ? (currentMesaFormatted || "Servicio en Mesa") : webOrderDocument.nombreCliente;
        const displayPayment = (currentOrderType === 'MESA') ? "Pago en Mesa" : webOrderDocument.formaPago;

        showOrderSuccessScreen(
            orderId,
            displayName,
            displayLabel,
            montoTotal,
            arrayDeProductos,
            displayPayment
        );

        // 5. Limpiar carrito
        cart = [];
        updateCartUI();

    } catch (error) {
        console.error("Error en submitWebOrder:", error);
        alert("Ocurrió un error al enviar el pedido: " + (error.message || "Por favor intenta nuevamente."));
        if (btnConfirm) {
            btnConfirm.disabled = false;
            btnConfirm.innerHTML = `<span>Confirmar y Enviar Pedido</span> ➔`;
        }
    }
}

function showOrderSuccessScreen(orderId, name, table, total, items, paymentMethod) {
    const modalBody = document.getElementById('modalBody');
    const modalFooter = document.getElementById('modalFooter');
    if (!modalBody) return;

    const itemsSummaryText = items.map(i => `• ${i.quantity}x ${i.name} (Q${i.subtotal.toFixed(2)})`).join('%0A');
    const whatsappText = `¡Hola! Acabo de enviar el pedido *#${orderId}* en Restaurante Rivera:%0A%0A*Tipo/Ubicación:* ${encodeURIComponent(table)}%0A*Cliente:* ${encodeURIComponent(name)}%0A*Detalle:*%0A${itemsSummaryText}%0A%0A*Total:* Q${total.toFixed(2)}%0A*Pago:* ${encodeURIComponent(paymentMethod)}%0A%0A_Por favor confirmen la recepción en cocina._`;
    const whatsappLink = `https://wa.me/50255551234?text=${whatsappText}`;

    modalBody.innerHTML = `
        <div class="success-card" style="text-align: center; padding: 20px 10px;">
            <div class="success-icon-box" style="width: 60px; height: 60px; background: #ECFDF5; color: #10B981; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 32px; font-weight: 800; margin: 0 auto 16px;">✓</div>
            <h3 style="font-size: 22px; font-weight: 800; color: var(--text-main); margin-bottom: 6px;">¡Pedido Recibido con Éxito!</h3>
            <p style="font-size: 14px; color: var(--text-muted); margin-bottom: 20px;">Tu comanda ha sido enviada al sistema de Meseros y Cocina.</p>
            
            <div style="background: #F8FAFC; border: 1px solid var(--border-color); border-radius: 14px; padding: 16px; margin-bottom: 20px; text-align: left;">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
                    <span style="font-size: 13px; color: var(--text-muted); font-weight: 600;">Número de Pedido:</span>
                    <span style="font-size: 11px; font-weight: 800; color: #D97706; background: #FEF3C7; padding: 4px 10px; border-radius: 20px;">PENDIENTE_CONFIRMACION</span>
                </div>
                <div style="font-size: 24px; font-weight: 900; color: var(--primary-color);">${orderId}</div>
                <div style="margin-top: 12px; font-size: 14px; color: var(--text-main); border-top: 1px dashed var(--border-color); padding-top: 10px;">
                    <div>📍 <strong>${table}</strong></div>
                    <div style="margin-top: 4px;">💵 Total a Cancelar: <strong>Q${total.toFixed(2)}</strong> (${paymentMethod})</div>
                </div>
            </div>

            <p style="font-size: 13px; color: var(--text-muted); margin-bottom: 16px;">
                El equipo del restaurante validará tu comanda al instante. También puedes abrir una copia en WhatsApp:
            </p>

            <a href="${whatsappLink}" target="_blank" class="btn-whatsapp-share" style="display: inline-flex; align-items: center; justify-content: center; gap: 8px; width: 100%; background: #25D366; color: white; text-decoration: none; padding: 14px; border-radius: 12px; font-weight: 800; font-size: 15px; box-shadow: 0 4px 12px rgba(37, 211, 102, 0.35);">
                <span>💬 Enviar Copia por WhatsApp</span>
            </a>
        </div>
    `;

    if (modalFooter) {
        modalFooter.innerHTML = `
            <button class="btn-confirm-order" onclick="closeCartModal(); window.location.reload();" style="background: var(--surface-color); color: var(--text-main); border: 1px solid var(--border-color); width: 100%; padding: 14px; border-radius: 12px; font-weight: 700; cursor: pointer;">
                <span>Seguir Viendo el Menú</span>
            </button>
        `;
    }
}

// --- 7. INITIALIZATION ON WINDOW LOAD ---
window.addEventListener('DOMContentLoaded', () => {
    // 1. Extraer y procesar parámetros de URL (?tipo=mesa&num=X / ?tipo=domicilio)
    parseUrlParams();

    // 2. Renderizar categorías dinámicas
    renderCategories();

    // 3. Renderizar catálogo de productos
    renderProductsGrid();

    // 4. Búsqueda en tiempo real
    const searchInput = document.getElementById('searchInput') || document.getElementById('searchMenuInput');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            searchQuery = e.target.value;
            renderProductsGrid();
        });
    }

    // 5. Conectar escucha activa en Firebase Firestore
    setupFirestoreListeners();
});
