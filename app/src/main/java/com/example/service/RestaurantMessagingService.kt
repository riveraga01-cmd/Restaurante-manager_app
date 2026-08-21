package com.example.service

import android.util.Log
import com.example.util.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class RestaurantMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCMService", "Mensaje FCM recibido de: ${remoteMessage.from}")

        // Check if message contains notification payload
        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: "🔔 Nuevo Pedido en Cocina"

        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: "Se ha recibido un nuevo pedido para la cocina."

        val orderId = remoteMessage.data["orderId"]?.toLongOrNull()
        val tableNumber = remoteMessage.data["tableNumber"]

        Log.d("FCMService", "FCM Payload -> Title: $title, Body: $body, OrderId: $orderId")

        NotificationHelper.showKitchenNotification(
            context = applicationContext,
            title = title,
            body = body,
            orderId = orderId,
            tableNumber = tableNumber
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCMService", "Nuevo Token FCM generado: $token")
        // Automatically re-subscribe to topic 'cocina'
        NotificationHelper.subscribeToKitchenTopic(applicationContext)
    }
}
