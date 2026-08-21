package com.example.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

object NotificationHelper {

    const val KITCHEN_CHANNEL_ID = "kitchen_notifications"
    private const val KITCHEN_CHANNEL_NAME = "Notificaciones de Cocina"
    private const val KITCHEN_CHANNEL_DESC = "Avisos en tiempo real para nuevos pedidos de cocina"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(KITCHEN_CHANNEL_ID, KITCHEN_CHANNEL_NAME, importance).apply {
                description = KITCHEN_CHANNEL_DESC
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 100, 300)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d("NotificationHelper", "Canal de notificaciones creado: $KITCHEN_CHANNEL_ID")
        }
    }

    fun subscribeToKitchenTopic(context: Context? = null) {
        try {
            context?.let { ctx ->
                if (FirebaseApp.getApps(ctx).isEmpty()) {
                    try {
                        FirebaseApp.initializeApp(ctx)
                    } catch (e: Throwable) {
                        try {
                            val options = com.google.firebase.FirebaseOptions.Builder()
                                .setApplicationId("1:1234567890:android:mockapp")
                                .setApiKey("AIzaSyMockApiKeyForLocalNotifications")
                                .setProjectId("mock-restaurant-app")
                                .setGcmSenderId("1234567890")
                                .build()
                            FirebaseApp.initializeApp(ctx, options)
                        } catch (ex: Throwable) {
                            Log.w("NotificationHelper", "Fallback FirebaseApp init failed: ${ex.message}")
                        }
                    }
                }
            }

            if (context != null && FirebaseApp.getApps(context).isNotEmpty()) {
                FirebaseMessaging.getInstance().subscribeToTopic("cocina")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("NotificationHelper", "Suscrito con éxito al tema 'cocina' de FCM")
                        } else {
                            Log.w("NotificationHelper", "Error al suscribirse al tema 'cocina'", task.exception)
                        }
                    }
            }
        } catch (e: Throwable) {
            Log.e("NotificationHelper", "Excepción en FCM subscribeToTopic: ${e.message}")
        }
    }

    fun showKitchenNotification(
        context: Context,
        title: String,
        body: String,
        orderId: Long? = null,
        tableNumber: String? = null
    ) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("target_screen", "COCINA")
            orderId?.let { putExtra("order_id", it) }
        }

        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            (orderId ?: System.currentTimeMillis()).toInt(),
            intent,
            pendingIntentFlags
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, KITCHEN_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 300, 100, 300))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = (orderId ?: System.currentTimeMillis()).toInt()
        notificationManager.notify(notificationId, builder.build())
        Log.d("NotificationHelper", "Notificación mostrada: ID=$notificationId, Title=$title")
    }

    fun sendKitchenNotificationLocally(
        context: Context,
        orderNumber: String,
        tableNumber: String,
        itemCount: Int,
        waiterName: String
    ) {
        val title = "🔔 ¡Nuevo Pedido en Cocina! ($orderNumber)"
        val body = "Mesa: $tableNumber • Items: $itemCount • Mesero: $waiterName"
        showKitchenNotification(context, title, body, tableNumber = tableNumber)
    }
}
