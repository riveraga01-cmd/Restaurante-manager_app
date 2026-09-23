package com.example.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.data.entity.SystemSettingsEntity
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

object NotificationHelper {

    const val KITCHEN_CHANNEL_ID = "kitchen_notifications_channel"
    const val GENERAL_CHANNEL_ID = "general_notifications_channel"
    const val ALERTS_CHANNEL_ID = "system_alerts_channel"

    private var activePreviewRingtone: Ringtone? = null

    /**
     * Obtains list of available system ringtones on the device (Notifications, Alarms, Ringtones)
     */
    fun getAvailableSystemRingtones(context: Context): List<Pair<String, String>> {
        val toneList = mutableListOf<Pair<String, String>>()
        toneList.add(Pair("Tono Predeterminado del Teléfono", ""))

        try {
            val ringtoneManager = RingtoneManager(context).apply {
                setType(RingtoneManager.TYPE_NOTIFICATION or RingtoneManager.TYPE_RINGTONE)
            }
            val cursor: Cursor? = ringtoneManager.cursor
            cursor?.let {
                var position = 0
                while (cursor.moveToNext() && position < 25) { // Top 25 system tones to avoid overload
                    val title = try {
                        if (cursor.columnCount > RingtoneManager.TITLE_COLUMN_INDEX && RingtoneManager.TITLE_COLUMN_INDEX >= 0) {
                            cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
                        } else {
                            ringtoneManager.getRingtone(cursor.position)?.getTitle(context)
                        }
                    } catch (_: Throwable) {
                        try {
                            ringtoneManager.getRingtone(cursor.position)?.getTitle(context)
                        } catch (_: Throwable) {
                            "Tono del Sistema ${position + 1}"
                        }
                    }
                    val uri = try {
                        ringtoneManager.getRingtoneUri(cursor.position)
                    } catch (_: Throwable) {
                        null
                    }
                    if (uri != null) {
                        toneList.add(Pair(title ?: "Tono ${position + 1}", uri.toString()))
                    }
                    position++
                }
            }
        } catch (e: Throwable) {
            Log.w("NotificationHelper", "Could not query system ringtones: ${e.message}")
        }

        // Fallback common system sounds
        if (toneList.size <= 1) {
            try {
                val notifUri = Settings.System.DEFAULT_NOTIFICATION_URI.toString()
                toneList.add(Pair("Alerta de Notificación", notifUri))
                val ringtoneUri = Settings.System.DEFAULT_RINGTONE_URI.toString()
                toneList.add(Pair("Timbre del Sistema", ringtoneUri))
                val alarmUri = Settings.System.DEFAULT_ALARM_ALERT_URI.toString()
                toneList.add(Pair("Alarma del Teléfono", alarmUri))
            } catch (_: Throwable) {}
        }

        return toneList
    }

    /**
     * Gets user-friendly title for a ringtone URI string
     */
    fun getRingtoneTitle(context: Context, uriString: String?): String {
        if (uriString.isNullOrBlank()) {
            return "Tono Predeterminado del Teléfono"
        }
        return try {
            val uri = Uri.parse(uriString)
            val ringtone = RingtoneManager.getRingtone(context, uri)
            ringtone?.getTitle(context) ?: "Tono Personalizado"
        } catch (e: Throwable) {
            "Tono del Teléfono"
        }
    }

    /**
     * Plays a preview of the specified ringtone
     */
    fun playRingtonePreview(context: Context, uriString: String?, volume: Float = 1.0f): Boolean {
        stopRingtonePreview()
        return try {
            val soundUri = if (uriString.isNullOrBlank()) {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            } else {
                Uri.parse(uriString)
            }
            val ringtone = RingtoneManager.getRingtone(context, soundUri)
            if (ringtone != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ringtone.volume = volume.coerceIn(0f, 1f)
                }
                activePreviewRingtone = ringtone
                ringtone.play()
                true
            } else {
                false
            }
        } catch (e: Throwable) {
            Log.w("NotificationHelper", "Error playing ringtone preview: ${e.message}")
            false
        }
    }

    /**
     * Stops currently playing preview ringtone
     */
    fun stopRingtonePreview() {
        try {
            activePreviewRingtone?.let {
                if (it.isPlaying) {
                    it.stop()
                }
            }
        } catch (e: Throwable) {
            Log.w("NotificationHelper", "Error stopping preview: ${e.message}")
        } finally {
            activePreviewRingtone = null
        }
    }

    /**
     * Plays an audible alert chime using user's configured ringtone or fallback
     */
    fun playAlertSound(context: Context, ringtoneUriString: String? = null, volume: Float = 1.0f) {
        try {
            val soundUri = if (!ringtoneUriString.isNullOrBlank()) {
                Uri.parse(ringtoneUriString)
            } else {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            }
            val ringtone = RingtoneManager.getRingtone(context, soundUri)
            if (ringtone != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ringtone.volume = volume.coerceIn(0.1f, 1.0f)
                }
                ringtone.play()
            } else {
                val toneGen = android.media.ToneGenerator(AudioManager.STREAM_NOTIFICATION, 90)
                toneGen.startTone(android.media.ToneGenerator.TONE_PROP_BEEP2, 350)
            }
        } catch (e: Throwable) {
            try {
                val toneGen = android.media.ToneGenerator(AudioManager.STREAM_NOTIFICATION, 90)
                toneGen.startTone(android.media.ToneGenerator.TONE_PROP_BEEP2, 350)
            } catch (_: Throwable) {}
        }
    }

    /**
     * Triggers device haptic vibration
     */
    fun triggerVibration(context: Context, pattern: LongArray = longArrayOf(0, 250, 100, 250)) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(pattern, -1)
                }
            }
        } catch (e: Throwable) {
            Log.w("NotificationHelper", "Vibration failed: ${e.message}")
        }
    }

    /**
     * Creates notification channels with custom sound attributes
     */
    fun createNotificationChannel(context: Context, ringtoneUriString: String? = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val soundUri = if (!ringtoneUriString.isNullOrBlank()) {
                Uri.parse(ringtoneUriString)
            } else {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            }

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .build()

            val kitchenChannel = NotificationChannel(
                KITCHEN_CHANNEL_ID,
                "Notificaciones de Cocina y Pedidos",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Avisos en tiempo real para nuevos pedidos de comanda y cocina"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 100, 300)
                setSound(soundUri, audioAttributes)
            }

            val generalChannel = NotificationChannel(
                GENERAL_CHANNEL_ID,
                "Alertas Generales y Facturación",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alertas de cobro, repartidores, stock bajo y cierres de caja"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 200, 100, 200)
                setSound(soundUri, audioAttributes)
            }

            notificationManager.createNotificationChannel(kitchenChannel)
            notificationManager.createNotificationChannel(generalChannel)
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
        tableNumber: String? = null,
        soundUriString: String? = null,
        enableSound: Boolean = true,
        enableVibration: Boolean = true
    ) {
        if (!PermissionHelper.isNotificationPermissionGranted(context)) {
            Log.w("NotificationHelper", "Permiso POST_NOTIFICATIONS no concedido. Omitiendo notificación.")
            return
        }

        createNotificationChannel(context, soundUriString)

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

        val soundUri = if (enableSound) {
            if (!soundUriString.isNullOrBlank()) Uri.parse(soundUriString)
            else RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        } else null

        val builder = NotificationCompat.Builder(context, KITCHEN_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (enableSound && soundUri != null) {
            builder.setSound(soundUri)
        }

        if (enableVibration) {
            val vibPattern = longArrayOf(0, 300, 100, 300)
            builder.setVibrate(vibPattern)
            triggerVibration(context, vibPattern)
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = (orderId ?: System.currentTimeMillis()).toInt()
        notificationManager.notify(notificationId, builder.build())
        Log.d("NotificationHelper", "Notificación mostrada: ID=$notificationId, Title=$title")
    }

    /**
     * Plays order alert chime specifically with custom tone support
     */
    fun playOrderAlertChime(context: Context, ringtoneUriString: String? = null, volume: Float = 1.0f) {
        playAlertSound(context, ringtoneUriString, volume)
    }

    fun showWebOrderNotification(
        context: Context,
        title: String,
        body: String,
        orderId: Long? = null,
        tableNumber: String? = null,
        enableSound: Boolean = true,
        enableVibration: Boolean = true
    ) {
        showKitchenNotification(
            context = context,
            title = title,
            body = body,
            orderId = orderId,
            tableNumber = tableNumber,
            enableSound = enableSound,
            enableVibration = enableVibration
        )
    }

    fun sendKitchenNotificationLocally(
        context: Context,
        orderNumber: String,
        tableNumber: String,
        itemCount: Int,
        waiterName: String,
        soundUriString: String? = null,
        enableSound: Boolean = true,
        enableVibration: Boolean = true
    ) {
        val title = "🔔 ¡Nuevo Pedido en Cocina! ($orderNumber)"
        val body = "Mesa: $tableNumber • Items: $itemCount • Mesero: $waiterName"
        showKitchenNotification(
            context = context,
            title = title,
            body = body,
            tableNumber = tableNumber,
            soundUriString = soundUriString,
            enableSound = enableSound,
            enableVibration = enableVibration
        )
    }

    /**
     * Sends a test notification to verify chosen ringtone and vibration in system settings
     */
    fun sendTestNotification(context: Context, settings: SystemSettingsEntity) {
        val title = "🔔 Prueba de Notificación - Restaurante Rivera"
        val body = "Tono: ${settings.notificationRingtoneTitle} • Sonido: ${if (settings.notificationSoundEnabled) "Activo" else "Silenciado"} • Vibración: ${if (settings.notificationVibrationEnabled) "Activa" else "Desactivada"}"

        showKitchenNotification(
            context = context,
            title = title,
            body = body,
            orderId = 99999L,
            tableNumber = "Mesa Test",
            soundUriString = settings.notificationRingtoneUri,
            enableSound = settings.notificationSoundEnabled,
            enableVibration = settings.notificationVibrationEnabled
        )

        if (settings.notificationSoundEnabled) {
            playAlertSound(context, settings.notificationRingtoneUri, settings.notificationVolume)
        }
    }
}

