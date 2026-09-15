package com.example.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat

enum class PermissionState {
    GRANTED,
    DENIED,
    NOT_REQUIRED
}

data class AppPermissionInfo(
    val id: String,
    val title: String,
    val description: String,
    val requiredForFeature: String,
    val state: PermissionState,
    val isSystemOrNormal: Boolean = false,
    val permissions: List<String> = emptyList(),
    val iconName: String = "info"
)

object PermissionHelper {

    fun isInternetConnected(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val network = cm?.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(network) ?: return false
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        } catch (_: Exception) {
            true
        }
    }

    fun isNotificationPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Auto-granted on Android 12 and below
        }
    }

    fun isCameraPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isGalleryPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun getGalleryRequiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    fun isBluetoothPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun getBluetoothRequiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
            )
        }
    }

    fun getAllPermissionsStatus(context: Context): List<AppPermissionInfo> {
        val list = mutableListOf<AppPermissionInfo>()

        // 1. Internet & Red
        list.add(
            AppPermissionInfo(
                id = "internet",
                title = "Internet y Estado de Red",
                description = "Permite la sincronización en la nube con Firestore, carga de imágenes de platillos y acceso al Menú Digital Web.",
                requiredForFeature = "Sincronización Cloud, Menú Web, Base de Datos Firebase",
                state = if (isInternetConnected(context)) PermissionState.GRANTED else PermissionState.GRANTED,
                isSystemOrNormal = true,
                permissions = listOf(Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE),
                iconName = "wifi"
            )
        )

        // 2. Notificaciones
        list.add(
            AppPermissionInfo(
                id = "notifications",
                title = "Notificaciones del Sistema",
                description = "Envía avisos de nuevas comandas a Cocina, alertas de stock bajo y cambios de estado de pedidos en tiempo real.",
                requiredForFeature = "Módulo Cocina, Alertas de Pedidos, Avisos de Gerencia",
                state = if (isNotificationPermissionGranted(context)) PermissionState.GRANTED else PermissionState.DENIED,
                isSystemOrNormal = false,
                permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    listOf(Manifest.permission.POST_NOTIFICATIONS)
                } else emptyList(),
                iconName = "notifications"
            )
        )

        // 3. Cámara
        list.add(
            AppPermissionInfo(
                id = "camera",
                title = "Cámara y Escáner QR",
                description = "Se utiliza exclusivamente para escanear códigos QR de vinculación de terminales y validación dinámica de mesas.",
                requiredForFeature = "Vinculación de Terminales, Escaneo QR de Mesas",
                state = if (isCameraPermissionGranted(context)) PermissionState.GRANTED else PermissionState.DENIED,
                isSystemOrNormal = false,
                permissions = listOf(Manifest.permission.CAMERA),
                iconName = "camera"
            )
        )

        // 4. Bluetooth / Dispositivos Cercanos
        list.add(
            AppPermissionInfo(
                id = "bluetooth",
                title = "Bluetooth e Impresoras Térmicas",
                description = "Permite detectar y enviar tickets y comandas a impresoras térmicas ESC/POS emparejadas (58mm/80mm).",
                requiredForFeature = "Impresión de Tickets de Caja y Comandas de Cocina",
                state = if (isBluetoothPermissionGranted(context)) PermissionState.GRANTED else PermissionState.DENIED,
                isSystemOrNormal = false,
                permissions = getBluetoothRequiredPermissions().toList(),
                iconName = "bluetooth"
            )
        )

        // 5. Fotos, Logotipos y Galería del Dispositivo
        list.add(
            AppPermissionInfo(
                id = "photos_files",
                title = "Fotos y Galería del Dispositivo",
                description = "Permite seleccionar fotografías de platillos, banners y logotipos del restaurante directamente desde la galería y memoria del dispositivo.",
                requiredForFeature = "Catálogo de Menú, Personalización de Temas Web, Logotipo",
                state = if (isGalleryPermissionGranted(context)) PermissionState.GRANTED else PermissionState.DENIED,
                isSystemOrNormal = false,
                permissions = getGalleryRequiredPermissions().toList(),
                iconName = "photo"
            )
        )

        // 6. Almacenamiento y FileProvider
        list.add(
            AppPermissionInfo(
                id = "storage_sharing",
                title = "Generación y Compartición de Archivos (FileProvider)",
                description = "Permite generar y exportar facturas en PDF, reportes financieros en CSV y listas de compras de forma aislada y segura.",
                requiredForFeature = "Exportación PDF, Reportes CSV, Compartir por Email/WhatsApp",
                state = PermissionState.GRANTED,
                isSystemOrNormal = true,
                permissions = emptyList(),
                iconName = "file"
            )
        )

        // 7. Ubicación y Navegación de Repartidores
        list.add(
            AppPermissionInfo(
                id = "location_maps",
                title = "Navegación de Entregas (Google Maps)",
                description = "Abre directamente la ruta de navegación hacia la dirección del cliente en Google Maps mediante Intents seguros sin rastreo invasivo de GPS en background.",
                requiredForFeature = "Módulo Repartidor / Delivery",
                state = PermissionState.GRANTED,
                isSystemOrNormal = true,
                permissions = emptyList(),
                iconName = "location"
            )
        )

        // 8. Mensajería WhatsApp
        list.add(
            AppPermissionInfo(
                id = "whatsapp_messaging",
                title = "Envío de Comandas por WhatsApp",
                description = "Abre chats de WhatsApp directamente con el cliente para confirmar pedidos o enviar tickets sin solicitar acceso a su agenda de contactos.",
                requiredForFeature = "Notificaciones directas a Clientes, Delivery",
                state = PermissionState.GRANTED,
                isSystemOrNormal = true,
                permissions = emptyList(),
                iconName = "chat"
            )
        )

        // 9. Feedback Háptico
        list.add(
            AppPermissionInfo(
                id = "vibrate",
                title = "Vibración y Feedback Táctil",
                description = "Proporciona confirmación táctil al registrar pagos, enviar comandas o escanear códigos.",
                requiredForFeature = "Punto de Venta (POS), Terminal Móvil",
                state = PermissionState.GRANTED,
                isSystemOrNormal = true,
                permissions = listOf(Manifest.permission.VIBRATE),
                iconName = "vibration"
            )
        )

        return list
    }

    fun openAppSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            try {
                val intent = Intent(Settings.ACTION_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } catch (_: Exception) {}
        }
    }

    fun openNotificationSettings(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } else {
                openAppSettings(context)
            }
        } catch (_: Exception) {
            openAppSettings(context)
        }
    }
}
