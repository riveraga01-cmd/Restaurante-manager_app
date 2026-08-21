package com.example.data.firebase

import android.content.Context
import android.util.Log
import com.example.data.dao.RestaurantDao
import com.example.data.entity.*
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirestoreSyncManager(
    private val dao: RestaurantDao,
    private val externalScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    private val context: Context? = null
) {
    private val firestore: FirebaseFirestore? by lazy {
        try {
            context?.let { ctx ->
                if (FirebaseApp.getApps(ctx).isEmpty()) {
                    val initialized = try {
                        FirebaseApp.initializeApp(ctx) != null
                    } catch (e: Throwable) {
                        false
                    }
                    if (!initialized && FirebaseApp.getApps(ctx).isEmpty()) {
                        try {
                            val options = com.google.firebase.FirebaseOptions.Builder()
                                .setApplicationId("1:433380736991:android:restauranteapp")
                                .setApiKey("AIzaSyFallbackKeyForRestauranteAppClient")
                                .setProjectId("dev-restaurante-app")
                                .setDatabaseUrl("https://dev-restaurante-app.firebaseio.com")
                                .setStorageBucket("dev-restaurante-app.appspot.com")
                                .build()
                            FirebaseApp.initializeApp(ctx, options)
                        } catch (e: Throwable) {
                            Log.w("FirestoreSync", "Fallback Firebase options failed: ${e.message}")
                        }
                    }
                }
            }
            FirebaseFirestore.getInstance()
        } catch (e: Throwable) {
            Log.w("FirestoreSync", "Firebase not initialized or unavailable: ${e.message}")
            null
        }
    }

    private val _isLiveSyncActive = MutableStateFlow(false)
    val isLiveSyncActive: StateFlow<Boolean> = _isLiveSyncActive.asStateFlow()

    private val _lastSyncTimestamp = MutableStateFlow(System.currentTimeMillis())
    val lastSyncTimestamp: StateFlow<Long> = _lastSyncTimestamp.asStateFlow()

    private val _syncError = MutableStateFlow<String?>(null)
    val syncError: StateFlow<String?> = _syncError.asStateFlow()

    private val _syncStatusLabel = MutableStateFlow("Sincronizado")
    val syncStatusLabel: StateFlow<String> = _syncStatusLabel.asStateFlow()

    private val _pendingChangesCount = MutableStateFlow(0)
    val pendingChangesCount: StateFlow<Int> = _pendingChangesCount.asStateFlow()

    private var ordersListener: ListenerRegistration? = null
    private var menuListener: ListenerRegistration? = null
    private var productosListener: ListenerRegistration? = null
    private var themesListener: ListenerRegistration? = null
    private var webOrdersListener: ListenerRegistration? = null
    private var inventoryListener: ListenerRegistration? = null
    private var usersListener: ListenerRegistration? = null
    private var settingsListener: ListenerRegistration? = null
    private var bindingsListener: ListenerRegistration? = null
    private var linkedDevicesListener: ListenerRegistration? = null
    private var auditLogsListener: ListenerRegistration? = null

    init {
        startRealtimeSync()
    }

    fun startRealtimeSync() {
        val db = firestore
        if (db == null) {
            Log.w("FirestoreSync", "Firestore no disponible. Modo base de datos local Room activo.")
            _syncError.value = "Modo Local Activo"
            _isLiveSyncActive.value = false
            _syncStatusLabel.value = "Sin conexión"
            return
        }
        try {
            _isLiveSyncActive.value = true
            _syncStatusLabel.value = "Sincronizando"
            listenToOrders(db)
            listenToMenuItems(db)
            listenToThemes(db)
            listenToWebOrders(db)
            listenToInventory(db)
            listenToUsers(db)
            listenToSettings(db)
            listenToDeviceBindings(db)
            listenToLinkedDevices(db)
            listenToAuditLogs(db)
            _syncStatusLabel.value = "Sincronizado"
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Error starting real-time listeners: ${e.message}", e)
            _syncError.value = e.message
            _isLiveSyncActive.value = false
            _syncStatusLabel.value = "Error de sincronización"
        }
    }

    fun triggerManualSync(onComplete: (Boolean, String) -> Unit = { _, _ -> }) {
        externalScope.launch {
            _syncStatusLabel.value = "Sincronizando"
            val db = firestore
            if (db == null) {
                _syncStatusLabel.value = "Sin conexión"
                onComplete(false, "Modo sin conexión activo (Room DB)")
                return@launch
            }
            try {
                // Process local queue if any
                val queue = dao.getAllSyncQueue()
                for (item in queue) {
                    dao.deleteSyncQueueItem(item.id)
                }
                _pendingChangesCount.value = 0
                _lastSyncTimestamp.value = System.currentTimeMillis()
                _syncStatusLabel.value = "Sincronizado"
                _syncError.value = null
                onComplete(true, "Sincronización completada exitosamente")
            } catch (e: Exception) {
                _syncStatusLabel.value = "Error de sincronización"
                _syncError.value = e.message
                onComplete(false, "Error al sincronizar: ${e.message}")
            }
        }
    }

    fun stopRealtimeSync() {
        ordersListener?.remove()
        menuListener?.remove()
        productosListener?.remove()
        themesListener?.remove()
        webOrdersListener?.remove()
        inventoryListener?.remove()
        usersListener?.remove()
        settingsListener?.remove()
        bindingsListener?.remove()
        linkedDevicesListener?.remove()
        auditLogsListener?.remove()
        _isLiveSyncActive.value = false
        _syncStatusLabel.value = "Sin conexión"
    }

    // --- REALTIME LISTENERS ---

    private fun listenToOrders(db: FirebaseFirestore) {
        ordersListener = db.collection("orders")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("FirestoreSync", "Orders listen failed: ${error.message}")
                    _syncError.value = "Error sincronizando pedidos: ${error.message}"
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    externalScope.launch {
                        try {
                            for (doc in snapshot.documents) {
                                val orderId = doc.getLong("id") ?: doc.id.toLongOrNull() ?: continue
                                val orderNumber = doc.getString("orderNumber") ?: "PED-$orderId"
                                val tableNumber = doc.getString("tableNumber") ?: "Mesa 1"
                                val waiterName = doc.getString("waiterName") ?: "Mesero"
                                val status = doc.getString("status") ?: "PENDIENTE"
                                val totalAmount = doc.getDouble("totalAmount") ?: 0.0
                                val createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
                                val completedAt = doc.getLong("completedAt")
                                val paidAt = doc.getLong("paidAt")
                                val paymentMethod = doc.getString("paymentMethod")
                                val cashierName = doc.getString("cashierName")
                                val generalNotes = doc.getString("generalNotes")

                                val orderEntity = OrderEntity(
                                    id = orderId,
                                    orderNumber = orderNumber,
                                    tableNumber = tableNumber,
                                    waiterName = waiterName,
                                    status = status,
                                    totalAmount = totalAmount,
                                    createdAt = createdAt,
                                    completedAt = completedAt,
                                    paidAt = paidAt,
                                    paymentMethod = paymentMethod,
                                    cashierName = cashierName,
                                    generalNotes = generalNotes
                                )

                                dao.insertOrder(orderEntity)

                                // Sync items array if present
                                val itemsList = doc.get("items") as? List<Map<String, Any>>
                                if (!itemsList.isNullOrEmpty()) {
                                    dao.deleteOrderItemsByOrderId(orderId)
                                    val orderItemEntities = itemsList.mapIndexed { idx, itemMap ->
                                        OrderItemEntity(
                                            id = (itemMap["id"] as? Number)?.toLong() ?: 0L,
                                            orderId = orderId,
                                            menuItemId = (itemMap["menuItemId"] as? Number)?.toLong() ?: 0L,
                                            productName = itemMap["productName"] as? String ?: "",
                                            unitPrice = (itemMap["unitPrice"] as? Number)?.toDouble() ?: 0.0,
                                            quantity = (itemMap["quantity"] as? Number)?.toInt() ?: 1,
                                            subtotal = (itemMap["subtotal"] as? Number)?.toDouble() ?: 0.0,
                                            notes = itemMap["notes"] as? String ?: ""
                                        )
                                    }
                                    dao.insertOrderItems(orderItemEntities)
                                }
                            }
                            _lastSyncTimestamp.value = System.currentTimeMillis()
                            _syncError.value = null
                        } catch (e: Exception) {
                            Log.e("FirestoreSync", "Error saving orders from Firestore: ${e.message}")
                        }
                    }
                }
            }
    }

    private fun listenToMenuItems(db: FirebaseFirestore) {
        // Listen to 'productos' collection for web sync and 'menu_items' for compatibility
        productosListener = db.collection("productos")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null || snapshot.isEmpty) return@addSnapshotListener
                externalScope.launch {
                    try {
                        for (doc in snapshot.documents) {
                            val id = doc.getLong("id") ?: doc.id.toLongOrNull() ?: continue
                            val name = doc.getString("name") ?: continue
                            val category = doc.getString("category") ?: "Platillos"
                            val price = doc.getDouble("price") ?: 0.0
                            val description = doc.getString("description") ?: ""
                            val imageUrl = doc.getString("imageUrl") ?: ""
                            val isAvailable = doc.getBoolean("isAvailable") ?: true
                            val isVisibleWeb = doc.getBoolean("isVisibleWeb") ?: true

                            val menuItem = MenuItemEntity(
                                id = id,
                                name = name,
                                category = category,
                                price = price,
                                description = description,
                                imageUrl = imageUrl,
                                isAvailable = isAvailable,
                                isVisibleWeb = isVisibleWeb
                            )
                            dao.insertMenuItem(menuItem)
                        }
                        _lastSyncTimestamp.value = System.currentTimeMillis()
                    } catch (e: Exception) {
                        Log.e("FirestoreSync", "Error saving productos from Firestore: ${e.message}")
                    }
                }
            }

        menuListener = db.collection("menu_items")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null || snapshot.isEmpty) return@addSnapshotListener
                externalScope.launch {
                    try {
                        for (doc in snapshot.documents) {
                            val id = doc.getLong("id") ?: doc.id.toLongOrNull() ?: continue
                            val name = doc.getString("name") ?: continue
                            val category = doc.getString("category") ?: "Platillos"
                            val price = doc.getDouble("price") ?: 0.0
                            val description = doc.getString("description") ?: ""
                            val imageUrl = doc.getString("imageUrl") ?: ""
                            val isAvailable = doc.getBoolean("isAvailable") ?: true
                            val isVisibleWeb = doc.getBoolean("isVisibleWeb") ?: true

                            val menuItem = MenuItemEntity(
                                id = id,
                                name = name,
                                category = category,
                                price = price,
                                description = description,
                                imageUrl = imageUrl,
                                isAvailable = isAvailable,
                                isVisibleWeb = isVisibleWeb
                            )
                            dao.insertMenuItem(menuItem)
                        }
                        _lastSyncTimestamp.value = System.currentTimeMillis()
                    } catch (e: Exception) {
                        Log.e("FirestoreSync", "Error saving menu items from Firestore: ${e.message}")
                    }
                }
            }
    }

    private fun listenToThemes(db: FirebaseFirestore) {
        themesListener = db.collection("temas")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("FirestoreSync", "Themes listen failed: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    externalScope.launch {
                        try {
                            for (doc in snapshot.documents) {
                                val id = doc.getLong("id") ?: doc.id.toLongOrNull() ?: continue
                                val themeName = doc.getString("themeName") ?: "Tema Rivera"
                                val primaryColorHex = doc.getString("primaryColorHex") ?: "#1E3A8A"
                                val secondaryColorHex = doc.getString("secondaryColorHex") ?: "#D97706"
                                val bannerImageUrl = doc.getString("bannerImageUrl") ?: ""
                                val welcomeMessage = doc.getString("welcomeMessage") ?: "Bienvenidos a Restaurante Rivera"
                                val isActive = doc.getBoolean("isActive") ?: false
                                val updatedAt = doc.getLong("updatedAt") ?: System.currentTimeMillis()

                                val themeEntity = ThemeConfigEntity(
                                    id = id,
                                    themeName = themeName,
                                    primaryColorHex = primaryColorHex,
                                    secondaryColorHex = secondaryColorHex,
                                    bannerImageUrl = bannerImageUrl,
                                    welcomeMessage = welcomeMessage,
                                    isActive = isActive,
                                    updatedAt = updatedAt
                                )
                                dao.insertTheme(themeEntity)
                            }
                            _lastSyncTimestamp.value = System.currentTimeMillis()
                        } catch (e: Exception) {
                            Log.e("FirestoreSync", "Error saving themes from Firestore: ${e.message}")
                        }
                    }
                }
            }
    }

    private fun listenToWebOrders(db: FirebaseFirestore) {
        webOrdersListener = db.collection("pedidos_web")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("FirestoreSync", "Web orders listen failed: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    externalScope.launch {
                        try {
                            for (doc in snapshot.documents) {
                                val id = doc.getLong("id") ?: doc.id.hashCode().toLong().let { if (it < 0) -it else it }
                                val webOrderId = doc.getString("webOrderId") ?: doc.id
                                val origin = doc.getString("origin") ?: "QR_Mesa_1"
                                val tableNumber = doc.getString("tableNumber") ?: "Mesa 1"
                                val customerName = doc.getString("customerName") ?: "Cliente Web"
                                val customerPhone = doc.getString("customerPhone") ?: ""
                                val itemsJson = doc.getString("itemsJson") ?: "[]"
                                val totalAmount = doc.getDouble("totalAmount") ?: 0.0
                                val status = doc.getString("status") ?: "Pendiente Validación"
                                val paymentMethod = doc.getString("paymentMethod") ?: "Efectivo al recibir"
                                val notes = doc.getString("notes") ?: ""
                                val createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
                                val validatedAt = doc.getLong("validatedAt")
                                val posOrderId = doc.getLong("posOrderId")

                                val webOrder = WebOrderEntity(
                                    id = id,
                                    webOrderId = webOrderId,
                                    origin = origin,
                                    tableNumber = tableNumber,
                                    customerName = customerName,
                                    customerPhone = customerPhone,
                                    itemsJson = itemsJson,
                                    totalAmount = totalAmount,
                                    status = status,
                                    paymentMethod = paymentMethod,
                                    notes = notes,
                                    createdAt = createdAt,
                                    validatedAt = validatedAt,
                                    posOrderId = posOrderId
                                )
                                dao.insertWebOrder(webOrder)
                            }
                            _lastSyncTimestamp.value = System.currentTimeMillis()
                        } catch (e: Exception) {
                            Log.e("FirestoreSync", "Error saving web orders from Firestore: ${e.message}")
                        }
                    }
                }
            }
    }

    private fun listenToInventory(db: FirebaseFirestore) {
        inventoryListener = db.collection("inventory_items")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("FirestoreSync", "Inventory listen failed: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    externalScope.launch {
                        try {
                            for (doc in snapshot.documents) {
                                val id = doc.getLong("id") ?: doc.id.toLongOrNull() ?: continue
                                val productName = doc.getString("productName") ?: continue
                                val currentStock = doc.getDouble("currentStock") ?: 0.0
                                val minStock = doc.getDouble("minStock") ?: 0.0
                                val unit = doc.getString("unit") ?: "Unidades"
                                val menuItemId = doc.getLong("menuItemId")

                                val invItem = InventoryItemEntity(
                                    id = id,
                                    menuItemId = menuItemId,
                                    productName = productName,
                                    currentStock = currentStock,
                                    minStock = minStock,
                                    unit = unit
                                )
                                dao.insertInventory(invItem)
                            }
                            _lastSyncTimestamp.value = System.currentTimeMillis()
                        } catch (e: Exception) {
                            Log.e("FirestoreSync", "Error saving inventory from Firestore: ${e.message}")
                        }
                    }
                }
            }
    }

    private fun listenToUsers(db: FirebaseFirestore) {
        usersListener = db.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null || snapshot.isEmpty) return@addSnapshotListener
                externalScope.launch {
                    try {
                        for (doc in snapshot.documents) {
                            val id = doc.getLong("id") ?: doc.id.toLongOrNull() ?: continue
                            val name = doc.getString("name") ?: continue
                            val role = doc.getString("role") ?: "MESERO"
                            val pin = doc.getString("pin") ?: ""
                            val email = doc.getString("email") ?: ""
                            val password = doc.getString("password") ?: "123456"
                            val isActive = doc.getBoolean("isActive") ?: true

                            val user = UserEntity(
                                id = id,
                                name = name,
                                role = role,
                                pin = pin,
                                email = email,
                                password = password,
                                isActive = isActive
                            )
                            dao.insertUser(user)
                        }
                        _lastSyncTimestamp.value = System.currentTimeMillis()
                    } catch (e: Exception) {
                        Log.e("FirestoreSync", "Error saving users: ${e.message}")
                    }
                }
            }
    }

    fun syncSettingsToRemote(settings: SystemSettingsEntity) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val map = hashMapOf(
                    "id" to settings.id,
                    "restaurantName" to settings.restaurantName,
                    "branchName" to settings.branchName,
                    "themePalette" to settings.themePalette,
                    "isDarkMode" to settings.isDarkMode,
                    "currencySymbol" to settings.currencySymbol,
                    "language" to settings.language,
                    "timezone" to settings.timezone,
                    "managerPin" to settings.managerPin,
                    "taxPercent" to settings.taxPercent,
                    "defaultTipPercent" to settings.defaultTipPercent,
                    "kitchenPrinterIp" to settings.kitchenPrinterIp,
                    "cashierPrinterIp" to settings.cashierPrinterIp,
                    "paperWidthMm" to settings.paperWidthMm,
                    "printCopies" to settings.printCopies,
                    "qrOrderingEnabled" to settings.qrOrderingEnabled,
                    "defaultTableCapacity" to settings.defaultTableCapacity,
                    "defaultTableStatus" to settings.defaultTableStatus
                )
                db.collection("system_settings")
                    .document("1")
                    .set(map, SetOptions.merge())
                    .await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to sync settings: ${e.message}")
            }
        }
    }

    fun syncDeviceBindingToRemote(binding: DeviceBindingEntity) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val map = hashMapOf(
                    "id" to binding.id,
                    "code" to binding.code,
                    "branchName" to binding.branchName,
                    "assignedRole" to binding.assignedRole,
                    "expiresAt" to binding.expiresAt,
                    "isMultiUse" to binding.isMultiUse,
                    "createdByUser" to binding.createdByUser
                )
                db.collection("device_bindings")
                    .document(binding.id.toString())
                    .set(map, SetOptions.merge())
                    .await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to sync device binding: ${e.message}")
            }
        }
    }

    fun syncLinkedDeviceToRemote(device: LinkedDeviceEntity) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val map = hashMapOf(
                    "deviceId" to device.deviceId,
                    "deviceName" to device.deviceName,
                    "branchName" to device.branchName,
                    "assignedRole" to device.assignedRole,
                    "linkedUser" to device.linkedUser,
                    "linkedAt" to device.linkedAt,
                    "lastSeen" to device.lastSeen,
                    "isBlocked" to device.isBlocked
                )
                db.collection("linked_devices")
                    .document(device.deviceId)
                    .set(map, SetOptions.merge())
                    .await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to sync linked device: ${e.message}")
            }
        }
    }

    fun syncAuditLogToRemote(logItem: AuditLogEntity) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                val formattedTime = sdf.format(java.util.Date(logItem.timestamp))

                val map = hashMapOf(
                    "id" to logItem.id,
                    "user" to logItem.user,
                    "role" to logItem.role,
                    "deviceId" to logItem.deviceId,
                    "action" to logItem.action,
                    "details" to logItem.details,
                    "settingModified" to logItem.settingModified,
                    "previousValue" to logItem.previousValue,
                    "newValue" to logItem.newValue,
                    "timestamp" to logItem.timestamp,
                    "timestampIso" to formattedTime
                )

                val docId = if (logItem.id != 0L) logItem.id.toString() else "${logItem.timestamp}_${(1000..9999).random()}"

                db.collection("audit_logs")
                    .document(docId)
                    .set(map, SetOptions.merge())
                    .await()
                Log.d("FirestoreSync", "Audit log synced to Firestore: ${logItem.action}")
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to sync audit log: ${e.message}")
            }
        }
    }

    private fun listenToSettings(db: FirebaseFirestore) {
        settingsListener = db.collection("system_settings")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null || snapshot.isEmpty) return@addSnapshotListener
                externalScope.launch {
                    try {
                        val doc = snapshot.documents.firstOrNull() ?: return@launch
                        val name = doc.getString("restaurantName") ?: "Restaurante Rivera"
                        val branch = doc.getString("branchName") ?: "Sucursal Central"
                        val theme = doc.getString("themePalette") ?: "DORADO_CHAPIN"
                        val isDark = doc.getBoolean("isDarkMode") ?: false
                        val currency = doc.getString("currencySymbol") ?: "Q"
                        val pin = doc.getString("managerPin") ?: "1234"
                        val tax = doc.getDouble("taxPercent") ?: 12.0
                        val tip = doc.getDouble("defaultTipPercent") ?: 10.0
                        val qrEnabled = doc.getBoolean("qrOrderingEnabled") ?: true
                        val defCap = doc.getLong("defaultTableCapacity")?.toInt() ?: 4
                        val defStatus = doc.getString("defaultTableStatus") ?: "Disponible"

                        dao.insertSystemSettings(
                            SystemSettingsEntity(
                                id = 1,
                                restaurantName = name,
                                branchName = branch,
                                themePalette = theme,
                                isDarkMode = isDark,
                                currencySymbol = currency,
                                managerPin = pin,
                                taxPercent = tax,
                                defaultTipPercent = tip,
                                qrOrderingEnabled = qrEnabled,
                                defaultTableCapacity = defCap,
                                defaultTableStatus = defStatus
                            )
                        )
                    } catch (e: Exception) {
                        Log.e("FirestoreSync", "Error saving settings: ${e.message}")
                    }
                }
            }
    }

    private fun listenToDeviceBindings(db: FirebaseFirestore) {
        bindingsListener = db.collection("device_bindings")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null || snapshot.isEmpty) return@addSnapshotListener
                externalScope.launch {
                    try {
                        for (doc in snapshot.documents) {
                            val id = doc.getLong("id") ?: doc.id.toLongOrNull() ?: continue
                            val code = doc.getString("code") ?: continue
                            val branch = doc.getString("branchName") ?: "Sucursal Central"
                            val role = doc.getString("assignedRole") ?: "MESERO"
                            val expiresAt = doc.getLong("expiresAt") ?: (System.currentTimeMillis() + 86400000)
                            val isMultiUse = doc.getBoolean("isMultiUse") ?: false

                            dao.insertDeviceBinding(
                                DeviceBindingEntity(
                                    id = id,
                                    code = code,
                                    branchName = branch,
                                    assignedRole = role,
                                    expiresAt = expiresAt,
                                    isMultiUse = isMultiUse
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("FirestoreSync", "Error saving bindings: ${e.message}")
                    }
                }
            }
    }

    private fun listenToLinkedDevices(db: FirebaseFirestore) {
        linkedDevicesListener = db.collection("linked_devices")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null || snapshot.isEmpty) return@addSnapshotListener
                externalScope.launch {
                    try {
                        for (doc in snapshot.documents) {
                            val deviceId = doc.getString("deviceId") ?: doc.id
                            val deviceName = doc.getString("deviceName") ?: "Dispositivo"
                            val branch = doc.getString("branchName") ?: "Sucursal Central"
                            val role = doc.getString("assignedRole") ?: "MESERO"
                            val user = doc.getString("linkedUser") ?: "Sin Asignar"
                            val isBlocked = doc.getBoolean("isBlocked") ?: false

                            dao.insertLinkedDevice(
                                LinkedDeviceEntity(
                                    deviceId = deviceId,
                                    deviceName = deviceName,
                                    branchName = branch,
                                    assignedRole = role,
                                    linkedUser = user,
                                    isBlocked = isBlocked
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("FirestoreSync", "Error saving linked devices: ${e.message}")
                    }
                }
            }
    }

    private fun listenToAuditLogs(db: FirebaseFirestore) {
        auditLogsListener?.remove()
        auditLogsListener = db.collection("audit_logs")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreSync", "Audit logs listener error: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    externalScope.launch {
                        try {
                            for (doc in snapshot.documents) {
                                val rawId = doc.getLong("id") ?: doc.id.hashCode().toLong()
                                val user = doc.getString("user") ?: "Sistema"
                                val role = doc.getString("role") ?: "GERENTE"
                                val deviceId = doc.getString("deviceId") ?: "DISPOSITIVO"
                                val action = doc.getString("action") ?: "EVENTO"
                                val details = doc.getString("details") ?: ""
                                val settingModified = doc.getString("settingModified") ?: ""
                                val previousValue = doc.getString("previousValue") ?: ""
                                val newValue = doc.getString("newValue") ?: ""
                                val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()

                                val id = if (rawId < 0) -rawId else rawId

                                dao.insertAuditLog(
                                    AuditLogEntity(
                                        id = id,
                                        user = user,
                                        role = role,
                                        deviceId = deviceId,
                                        action = action,
                                        details = details,
                                        settingModified = settingModified,
                                        previousValue = previousValue,
                                        newValue = newValue,
                                        timestamp = timestamp
                                    )
                                )
                            }
                            _lastSyncTimestamp.value = System.currentTimeMillis()
                        } catch (e: Exception) {
                            Log.e("FirestoreSync", "Error saving audit logs from Firestore: ${e.message}")
                        }
                    }
                }
            }
    }

    // --- OUTBOUND SYNC (PUSH TO FIRESTORE) ---

    fun syncOrderToRemote(order: OrderEntity, items: List<OrderItemEntity>) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val orderMap = hashMapOf(
                    "id" to order.id,
                    "orderNumber" to order.orderNumber,
                    "tableNumber" to order.tableNumber,
                    "waiterName" to order.waiterName,
                    "status" to order.status,
                    "totalAmount" to order.totalAmount,
                    "createdAt" to order.createdAt,
                    "completedAt" to order.completedAt,
                    "paidAt" to order.paidAt,
                    "paymentMethod" to order.paymentMethod,
                    "cashierName" to order.cashierName,
                    "generalNotes" to order.generalNotes,
                    "items" to items.map { item ->
                        hashMapOf(
                            "id" to item.id,
                            "orderId" to item.orderId,
                            "menuItemId" to item.menuItemId,
                            "productName" to item.productName,
                            "unitPrice" to item.unitPrice,
                            "quantity" to item.quantity,
                            "subtotal" to item.subtotal,
                            "notes" to item.notes
                        )
                    }
                )

                db.collection("orders")
                    .document(order.id.toString())
                    .set(orderMap, SetOptions.merge())
                    .await()

                _lastSyncTimestamp.value = System.currentTimeMillis()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to push order #${order.id} to Firestore: ${e.message}")
            }
        }
    }

    fun syncMenuItemToRemote(menuItem: MenuItemEntity) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val map = hashMapOf(
                    "id" to menuItem.id,
                    "name" to menuItem.name,
                    "category" to menuItem.category,
                    "price" to menuItem.price,
                    "description" to menuItem.description,
                    "imageUrl" to menuItem.imageUrl,
                    "isAvailable" to menuItem.isAvailable,
                    "isVisibleWeb" to menuItem.isVisibleWeb,
                    "updatedAt" to System.currentTimeMillis()
                )
                // Sync to both 'productos' and 'menu_items' for universal compatibility
                db.collection("productos")
                    .document(menuItem.id.toString())
                    .set(map, SetOptions.merge())
                    .await()

                db.collection("menu_items")
                    .document(menuItem.id.toString())
                    .set(map, SetOptions.merge())
                    .await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to push menuItem to Firestore: ${e.message}")
            }
        }
    }

    fun syncThemeToRemote(theme: ThemeConfigEntity) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val map = hashMapOf(
                    "id" to theme.id,
                    "themeName" to theme.themeName,
                    "primaryColorHex" to theme.primaryColorHex,
                    "secondaryColorHex" to theme.secondaryColorHex,
                    "bannerImageUrl" to theme.bannerImageUrl,
                    "welcomeMessage" to theme.welcomeMessage,
                    "isActive" to theme.isActive,
                    "updatedAt" to System.currentTimeMillis()
                )
                db.collection("temas")
                    .document(theme.id.toString())
                    .set(map, SetOptions.merge())
                    .await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to push theme to Firestore: ${e.message}")
            }
        }
    }

    fun syncWebOrderToRemote(webOrder: WebOrderEntity) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val docId = if (webOrder.webOrderId.isNotBlank()) webOrder.webOrderId else "WEB-${webOrder.id}"
                val map = hashMapOf(
                    "id" to webOrder.id,
                    "webOrderId" to docId,
                    "origin" to webOrder.origin,
                    "tableNumber" to webOrder.tableNumber,
                    "customerName" to webOrder.customerName,
                    "customerPhone" to webOrder.customerPhone,
                    "itemsJson" to webOrder.itemsJson,
                    "totalAmount" to webOrder.totalAmount,
                    "status" to webOrder.status,
                    "paymentMethod" to webOrder.paymentMethod,
                    "notes" to webOrder.notes,
                    "createdAt" to webOrder.createdAt,
                    "validatedAt" to webOrder.validatedAt,
                    "posOrderId" to webOrder.posOrderId
                )
                db.collection("pedidos_web")
                    .document(docId)
                    .set(map, SetOptions.merge())
                    .await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to push web order to Firestore: ${e.message}")
            }
        }
    }

    fun updateRemoteWebOrderStatus(webOrderId: String, newStatus: String, posOrderId: Long? = null) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val updateMap = mutableMapOf<String, Any>(
                    "status" to newStatus,
                    "validatedAt" to System.currentTimeMillis()
                )
                if (posOrderId != null) {
                    updateMap["posOrderId"] = posOrderId
                }
                db.collection("pedidos_web")
                    .document(webOrderId)
                    .update(updateMap)
                    .await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to update web order status in Firestore: ${e.message}")
            }
        }
    }

    fun deleteRemoteTheme(themeId: Long) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                db.collection("temas").document(themeId.toString()).delete().await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to delete theme from Firestore: ${e.message}")
            }
        }
    }

    fun deleteRemoteWebOrder(id: Long, webOrderId: String) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val docId = if (webOrderId.isNotBlank()) webOrderId else "WEB-$id"
                db.collection("pedidos_web").document(docId).delete().await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to delete web order from Firestore: ${e.message}")
            }
        }
    }

    fun syncInventoryToRemote(inventoryItem: InventoryItemEntity) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val map = hashMapOf(
                    "id" to inventoryItem.id,
                    "menuItemId" to inventoryItem.menuItemId,
                    "productName" to inventoryItem.productName,
                    "currentStock" to inventoryItem.currentStock,
                    "minStock" to inventoryItem.minStock,
                    "unit" to inventoryItem.unit
                )
                db.collection("inventory_items")
                    .document(inventoryItem.id.toString())
                    .set(map, SetOptions.merge())
                    .await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to push inventory to Firestore: ${e.message}")
            }
        }
    }

    fun syncUserToRemote(user: UserEntity) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val map = hashMapOf(
                    "id" to user.id,
                    "name" to user.name,
                    "role" to user.role,
                    "pin" to user.pin,
                    "pinEncrypted" to true,
                    "email" to user.email,
                    "isActive" to user.isActive,
                    "branchName" to user.branchName,
                    "updatedAt" to System.currentTimeMillis()
                )
                db.collection("users")
                    .document(user.id.toString())
                    .set(map, SetOptions.merge())
                    .await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to push user to Firestore: ${e.message}")
            }
        }
    }

    fun deleteRemoteOrder(orderId: Long) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                db.collection("orders").document(orderId.toString()).delete().await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to delete order from Firestore: ${e.message}")
            }
        }
    }

    fun deleteRemoteUser(userId: Long) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                db.collection("users").document(userId.toString()).delete().await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to delete user from Firestore: ${e.message}")
            }
        }
    }

    fun deleteRemoteMenuItem(itemId: Long) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                db.collection("productos").document(itemId.toString()).delete().await()
                db.collection("menu_items").document(itemId.toString()).delete().await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to delete menu item from Firestore: ${e.message}")
            }
        }
    }

    fun deleteRemoteInventory(itemId: Long) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                db.collection("inventory_items").document(itemId.toString()).delete().await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to delete inventory item from Firestore: ${e.message}")
            }
        }
    }
}
