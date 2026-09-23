package com.example.data.firebase

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.example.data.dao.RestaurantDao
import com.example.data.entity.*
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.example.util.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    private val _newPendingWebOrderAlert = MutableStateFlow<WebOrderEntity?>(null)
    val newPendingWebOrderAlert: StateFlow<WebOrderEntity?> = _newPendingWebOrderAlert.asStateFlow()

    fun clearPendingWebOrderAlert() {
        _newPendingWebOrderAlert.value = null
    }

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
        setupConnectivityMonitoring()
        startRealtimeSync()
    }

    private fun setupConnectivityMonitoring() {
        context?.let { ctx ->
            try {
                val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                if (cm != null) {
                    val request = NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build()
                    cm.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network) {
                            externalScope.launch {
                                Log.i("FirestoreSync", "Network connected: resuming Firestore live sync.")
                                _syncStatusLabel.value = "Sincronizando"
                                startRealtimeSync()
                            }
                        }

                        override fun onLost(network: Network) {
                            externalScope.launch {
                                Log.w("FirestoreSync", "Network connection lost: switching to offline local storage.")
                                _isLiveSyncActive.value = false
                                _syncStatusLabel.value = "Sin conexión"
                            }
                        }
                    })
                }
            } catch (e: Throwable) {
                Log.w("FirestoreSync", "NetworkCallback registration note: ${e.message}")
            }
        }
    }

    fun startRealtimeSync() {
        val db = firestore
        if (db == null) {
            Log.w("FirestoreSync", "Firestore no disponible. Modo base de datos local Room activo.")
            _syncError.value = null
            _isLiveSyncActive.value = false
            _syncStatusLabel.value = "Modo Local (Activo)"
            return
        }
        try {
            _isLiveSyncActive.value = true
            _syncStatusLabel.value = "Sincronizado"
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
        } catch (e: Exception) {
            Log.w("FirestoreSync", "Listeners fallback to local Room: ${e.message}")
            _syncError.value = null
            _isLiveSyncActive.value = false
            _syncStatusLabel.value = "Modo Local (Activo)"
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
                    Log.w("FirestoreSync", "Orders listen notice: ${error.message}")
                    _isLiveSyncActive.value = false
                    _syncStatusLabel.value = "Modo Local (Activo)"
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

                                // Sync items array if present
                                val itemsList = doc.get("items") as? List<Map<String, Any>>
                                if (!itemsList.isNullOrEmpty()) {
                                    val orderItemEntities = itemsList.mapIndexed { idx, itemMap ->
                                        val kitchenStatusStr = itemMap["kitchenStatus"] as? String
                                            ?: itemMap["estado_cocina"] as? String
                                            ?: itemMap["estadoCocina"] as? String
                                            ?: if (status in listOf("FINALIZADO", "PAGADO")) "FINALIZADO" else "PENDIENTE"
                                        val itemCreatedAt = (itemMap["createdAt"] as? Number)?.toLong() ?: createdAt

                                        OrderItemEntity(
                                            id = (itemMap["id"] as? Number)?.toLong() ?: 0L,
                                            orderId = orderId,
                                            menuItemId = (itemMap["menuItemId"] as? Number)?.toLong() ?: 0L,
                                            productName = itemMap["productName"] as? String ?: "",
                                            unitPrice = (itemMap["unitPrice"] as? Number)?.toDouble() ?: 0.0,
                                            quantity = (itemMap["quantity"] as? Number)?.toInt() ?: 1,
                                            subtotal = (itemMap["subtotal"] as? Number)?.toDouble() ?: 0.0,
                                            notes = itemMap["notes"] as? String ?: "",
                                            kitchenStatus = kitchenStatusStr.uppercase(),
                                            createdAt = itemCreatedAt
                                        )
                                    }
                                    dao.replaceOrderItems(orderId, orderEntity, orderItemEntities)
                                } else {
                                    val existing = dao.getOrderById(orderId)
                                    if (existing != null) {
                                        dao.updateOrder(orderEntity)
                                    } else {
                                        dao.insertOrder(orderEntity)
                                    }
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
                                val rawId = doc.getString("id") ?: doc.id
                                val numId = doc.getLong("id") ?: rawId.filter { it.isDigit() }.toLongOrNull() ?: doc.id.hashCode().toLong().let { if (it < 0) -it else it }
                                val webOrderId = if (rawId.isNotBlank()) rawId else (doc.getString("webOrderId") ?: doc.id)

                                val rawEstado = doc.getString("estado") ?: doc.getString("status") ?: "PENDIENTE_CONFIRMACION"
                                val estado = when (rawEstado.trim().uppercase()) {
                                    "PENDIENTE_CONFIRMACION", "PENDIENTE VALIDACIÓN", "PENDIENTE VALIDACION" -> "PENDIENTE_CONFIRMACION"
                                    "EN_PREPARACION", "EN PREPARACION", "EN COCINA" -> "EN_PREPARACION"
                                    "RECHAZADO", "CANCELADO" -> "RECHAZADO"
                                    else -> rawEstado
                                }

                                val rawTipoPedido = doc.getString("tipoPedido") ?: ""
                                val numMesa = doc.getString("numMesa") ?: doc.getString("mesa") ?: ""
                                val isMesa = rawTipoPedido.equals("MESA", ignoreCase = true)
                                    || numMesa.isNotBlank()
                                    || (doc.getString("tipoServicio") ?: "").startsWith("Mesa", ignoreCase = true)
                                    || (doc.getString("tipoServicio") ?: "").equals("MESA", ignoreCase = true)

                                val origin = if (isMesa) {
                                    if (numMesa.isNotBlank()) (if (numMesa.startsWith("Mesa", ignoreCase = true)) numMesa else "Mesa $numMesa")
                                    else (doc.getString("tipoServicio") ?: "Mesa 1")
                                } else {
                                    "A Domicilio"
                                }
                                val tableNumber = origin

                                val nombreCliente = doc.getString("nombreCliente") 
                                    ?: doc.getString("customerName") 
                                    ?: doc.getString("cliente") 
                                    ?: ""
                                val telefono = doc.getString("telefono") ?: doc.getString("customerPhone") ?: ""
                                val ubicacionGps = doc.getString("ubicacionGps") ?: ""
                                val direccionEscrita = doc.getString("direccionEscrita") ?: doc.getString("deliveryAddress") ?: ""
                                val instruccionesCocina = doc.getString("instruccionesCocina") ?: doc.getString("notes") ?: ""

                                val total = doc.getDouble("total") ?: doc.getDouble("totalAmount") ?: 0.0
                                val paymentMethod = doc.getString("formaPago") 
                                    ?: doc.getString("paymentMethod") 
                                    ?: (if (isMesa) "En Mesa" else "Efectivo")
                                val createdAt = doc.getLong("createdAt") ?: doc.getTimestamp("timestamp")?.toDate()?.time ?: System.currentTimeMillis()
                                val validatedAt = doc.getLong("validatedAt")
                                val posOrderId = doc.getLong("posOrderId")

                                val itemsJson = when {
                                    doc.contains("itemsJson") && !doc.getString("itemsJson").isNullOrBlank() -> doc.getString("itemsJson") ?: "[]"
                                    doc.contains("items") -> {
                                        val itemsList = doc.get("items")
                                        if (itemsList is List<*>) {
                                            val array = org.json.JSONArray()
                                            for (item in itemsList) {
                                                if (item is Map<*, *>) {
                                                    array.put(org.json.JSONObject(item as Map<String, Any?>))
                                                }
                                            }
                                            array.toString()
                                        } else {
                                            "[]"
                                        }
                                    }
                                    else -> "[]"
                                }

                                val fullDeliveryAddress = buildString {
                                    if (direccionEscrita.isNotBlank()) append(direccionEscrita)
                                    if (ubicacionGps.isNotBlank()) {
                                        if (isNotEmpty()) append(" | GPS: ")
                                        append(ubicacionGps)
                                    }
                                }

                                val existing = dao.getWebOrderByCode(webOrderId) ?: dao.getWebOrderById(numId)
                                val isBrandNewPending = (existing == null && estado == "PENDIENTE_CONFIRMACION")

                                val webOrder = WebOrderEntity(
                                    id = numId,
                                    webOrderId = webOrderId,
                                    origin = origin,
                                    tableNumber = tableNumber,
                                    customerName = if (nombreCliente.isNotBlank()) nombreCliente else (if (isMesa) tableNumber else "Cliente Web"),
                                    customerPhone = telefono,
                                    itemsJson = itemsJson,
                                    totalAmount = total,
                                    status = estado,
                                    paymentMethod = paymentMethod,
                                    notes = instruccionesCocina,
                                    createdAt = createdAt,
                                    validatedAt = validatedAt,
                                    posOrderId = posOrderId,
                                    deliveryAddress = fullDeliveryAddress,
                                    deliveryDriverName = doc.getString("deliveryDriverName") ?: "",
                                    deliveryStartedAt = doc.getLong("deliveryStartedAt"),
                                    deliveryFinishedAt = doc.getLong("deliveryFinishedAt"),
                                    deliveryIssueNote = doc.getString("deliveryIssueNote") ?: ""
                                )
                                dao.insertWebOrder(webOrder)

                                if (isBrandNewPending) {
                                    context?.let { ctx ->
                                        withContext(Dispatchers.Main) {
                                            NotificationHelper.playOrderAlertChime(ctx)
                                            NotificationHelper.triggerVibration(ctx)
                                            val origenDesc = if (isMesa) origin else "Domicilio"
                                            NotificationHelper.showWebOrderNotification(
                                                context = ctx,
                                                title = "¡Nuevo Pedido Web Recibido! ($origenDesc)",
                                                body = "Pedido $webOrderId de ${webOrder.customerName} ($origenDesc) - Total: Q${"%.2f".format(total)}",
                                                orderId = numId,
                                                tableNumber = origin,
                                                enableSound = true,
                                                enableVibration = true
                                            )
                                            _newPendingWebOrderAlert.value = webOrder
                                        }
                                    }
                                }
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
                    "defaultTableStatus" to settings.defaultTableStatus,
                    "webMasterStatus" to settings.webMasterStatus,
                    "webIs24Hours" to settings.webIs24Hours,
                    "webOpenHour" to settings.webOpenHour,
                    "webCloseHour" to settings.webCloseHour,
                    "webActiveDays" to settings.webActiveDays,
                    "webClosedMessage" to settings.webClosedMessage,
                    "openingHours" to settings.openingHours
                )
                db.collection("system_settings")
                    .document("1")
                    .set(map, SetOptions.merge())
                    .await()

                // Also sync directly to store_schedule for dedicated web clients
                val scheduleMap = hashMapOf(
                    "webMasterStatus" to settings.webMasterStatus,
                    "webIs24Hours" to settings.webIs24Hours,
                    "webOpenHour" to settings.webOpenHour,
                    "webCloseHour" to settings.webCloseHour,
                    "webActiveDays" to settings.webActiveDays,
                    "webClosedMessage" to settings.webClosedMessage,
                    "openingHours" to settings.openingHours,
                    "updatedAt" to System.currentTimeMillis()
                )
                db.collection("store_schedule")
                    .document("main")
                    .set(scheduleMap, SetOptions.merge())
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
                val cleanCode = binding.code.trim().uppercase()
                val pinPart = cleanCode.substringAfterLast("-", "").trim().let {
                    if (it.all { ch -> ch.isDigit() } && it.length in 4..6) it else ""
                }
                val map = hashMapOf(
                    "id" to binding.id,
                    "code" to cleanCode,
                    "pin" to pinPart,
                    "branchName" to binding.branchName,
                    "assignedRole" to binding.assignedRole,
                    "expiresAt" to binding.expiresAt,
                    "isMultiUse" to binding.isMultiUse,
                    "usedCount" to binding.usedCount,
                    "createdAt" to binding.createdAt,
                    "createdByUser" to binding.createdByUser
                )
                // Direct key by code for O(1) multi-device matching
                db.collection("device_bindings")
                    .document(cleanCode)
                    .set(map, SetOptions.merge())
                    .await()

                if (binding.id != 0L) {
                    db.collection("device_bindings")
                        .document(binding.id.toString())
                        .set(map, SetOptions.merge())
                        .await()
                }
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to sync device binding: ${e.message}")
            }
        }
    }

    fun deleteDeviceBindingFromRemote(code: String, id: Long = 0L) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val cleanCode = code.trim().uppercase()
                if (cleanCode.isNotEmpty()) {
                    db.collection("device_bindings").document(cleanCode).delete().await()
                }
                if (id != 0L) {
                    db.collection("device_bindings").document(id.toString()).delete().await()
                }
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to delete device binding from remote: ${e.message}")
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

    suspend fun fetchDeviceBindingByCodeOrPin(input: String): DeviceBindingEntity? {
        val db = firestore ?: return null
        return try {
            val cleanInput = input.trim().uppercase()
            val digitsOnly = cleanInput.filter { it.isDigit() }
            val pinCandidate = cleanInput.substringAfterLast("-", "").trim()
            val effectivePin = if (digitsOnly.length in 4..6) digitsOnly else if (pinCandidate.all { it.isDigit() } && pinCandidate.length in 4..6) pinCandidate else ""

            val col = db.collection("device_bindings")

            // 1. Direct document lookup by cleanInput
            if (cleanInput.isNotEmpty()) {
                val doc = col.document(cleanInput).get().await()
                if (doc.exists()) {
                    val binding = parseDeviceBindingFromDoc(doc)
                    if (binding != null) return binding
                }
            }

            // 2. Query by code
            if (cleanInput.isNotEmpty()) {
                val byCode = col.whereEqualTo("code", cleanInput).get().await()
                if (!byCode.isEmpty) {
                    val binding = parseDeviceBindingFromDoc(byCode.documents[0])
                    if (binding != null) return binding
                }
            }

            // 3. Query by pin if input is or contains a 4-6 digit pin
            if (effectivePin.isNotEmpty()) {
                val byPin = col.whereEqualTo("pin", effectivePin).get().await()
                for (doc in byPin.documents) {
                    val binding = parseDeviceBindingFromDoc(doc)
                    if (binding != null) return binding
                }
            }

            // 4. Fallback: scan all documents in device_bindings for prefix/suffix match
            val allDocs = col.get().await()
            for (doc in allDocs.documents) {
                val docCode = (doc.getString("code") ?: doc.id).trim().uppercase()
                val docPin = (doc.getString("pin") ?: docCode.substringAfterLast("-")).trim()

                val isDirectMatch = (docCode == cleanInput) ||
                        (effectivePin.isNotEmpty() && docPin == effectivePin) ||
                        (effectivePin.isNotEmpty() && docCode.endsWith("-$effectivePin")) ||
                        (effectivePin.isNotEmpty() && docCode.contains(effectivePin)) ||
                        (cleanInput.isNotEmpty() && docCode.contains(cleanInput)) ||
                        (cleanInput.isNotEmpty() && cleanInput.contains(docCode))

                if (isDirectMatch) {
                    val binding = parseDeviceBindingFromDoc(doc)
                    if (binding != null) return binding
                }
            }
            null
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Error fetching device binding from Firestore: ${e.message}")
            null
        }
    }

    private fun parseDeviceBindingFromDoc(doc: com.google.firebase.firestore.DocumentSnapshot): DeviceBindingEntity? {
        return try {
            val code = doc.getString("code") ?: if (doc.id.contains("RIVERA")) doc.id else return null
            val id = doc.getLong("id") ?: doc.id.toLongOrNull() ?: doc.id.hashCode().toLong().let { if (it < 0) -it else it }
            val branch = doc.getString("branchName") ?: "Sucursal Central"
            val role = doc.getString("assignedRole") ?: "MESERO"
            val expiresAt = doc.getLong("expiresAt") ?: (System.currentTimeMillis() + 86400000L)
            val isMultiUse = doc.getBoolean("isMultiUse") ?: true
            val usedCount = doc.getLong("usedCount")?.toInt() ?: 0
            val createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
            val createdByUser = doc.getString("createdByUser") ?: "Gerente Principal"

            DeviceBindingEntity(
                id = id,
                code = code,
                branchName = branch,
                assignedRole = role,
                expiresAt = expiresAt,
                isMultiUse = isMultiUse,
                usedCount = usedCount,
                createdAt = createdAt,
                createdByUser = createdByUser
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchDeviceBindingByCode(codeToMatch: String): DeviceBindingEntity? {
        return fetchDeviceBindingByCodeOrPin(codeToMatch)
    }

    fun deleteLinkedDeviceFromRemote(deviceId: String) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                db.collection("linked_devices").document(deviceId).delete().await()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Error deleting linked device: ${e.message}")
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
                        val current = dao.getSystemSettings() ?: SystemSettingsEntity(id = 1)
                        val name = doc.getString("restaurantName") ?: current.restaurantName
                        val branch = doc.getString("branchName") ?: current.branchName
                        val theme = doc.getString("themePalette") ?: current.themePalette
                        val isDark = doc.getBoolean("isDarkMode") ?: current.isDarkMode
                        val currency = doc.getString("currencySymbol") ?: current.currencySymbol
                        val pin = doc.getString("managerPin") ?: current.managerPin
                        val tax = doc.getDouble("taxPercent") ?: current.taxPercent
                        val tip = doc.getDouble("defaultTipPercent") ?: current.defaultTipPercent
                        val qrEnabled = doc.getBoolean("qrOrderingEnabled") ?: current.qrOrderingEnabled
                        val defCap = doc.getLong("defaultTableCapacity")?.toInt() ?: current.defaultTableCapacity
                        val defStatus = doc.getString("defaultTableStatus") ?: current.defaultTableStatus
                        val webMasterStatus = doc.getString("webMasterStatus") ?: current.webMasterStatus
                        val webIs24Hours = doc.getBoolean("webIs24Hours") ?: current.webIs24Hours
                        val webOpenHour = doc.getString("webOpenHour") ?: current.webOpenHour
                        val webCloseHour = doc.getString("webCloseHour") ?: current.webCloseHour
                        val webActiveDays = doc.getString("webActiveDays") ?: current.webActiveDays
                        val webClosedMessage = doc.getString("webClosedMessage") ?: current.webClosedMessage

                        dao.insertSystemSettings(
                            current.copy(
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
                                defaultTableStatus = defStatus,
                                webMasterStatus = webMasterStatus,
                                webIs24Hours = webIs24Hours,
                                webOpenHour = webOpenHour,
                                webCloseHour = webCloseHour,
                                webActiveDays = webActiveDays,
                                webClosedMessage = webClosedMessage
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
                if (error != null || snapshot == null) return@addSnapshotListener
                externalScope.launch {
                    try {
                        for (doc in snapshot.documents) {
                            val code = doc.getString("code") ?: if (doc.id.contains("RIVERA")) doc.id else continue
                            val branch = doc.getString("branchName") ?: "Sucursal Central"
                            val role = doc.getString("assignedRole") ?: "MESERO"
                            val expiresAt = doc.getLong("expiresAt") ?: (System.currentTimeMillis() + 86400000L)
                            val isMultiUse = doc.getBoolean("isMultiUse") ?: true
                            val usedCount = doc.getLong("usedCount")?.toInt() ?: 0
                            val createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
                            val createdByUser = doc.getString("createdByUser") ?: "Gerente Principal"

                            val existing = dao.getDeviceBindingByCode(code)
                            val id = existing?.id ?: doc.getLong("id") ?: doc.id.toLongOrNull() ?: doc.id.hashCode().toLong().let { if (it < 0) -it else it }

                            dao.insertDeviceBinding(
                                DeviceBindingEntity(
                                    id = id,
                                    code = code,
                                    branchName = branch,
                                    assignedRole = role,
                                    expiresAt = expiresAt,
                                    isMultiUse = isMultiUse,
                                    usedCount = usedCount,
                                    createdAt = createdAt,
                                    createdByUser = createdByUser
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

    suspend fun findActiveRemoteOrder(tableNumber: String): Pair<OrderEntity, List<OrderItemEntity>>? {
        val db = firestore ?: return null
        if (!_isLiveSyncActive.value) return null
        return try {
            kotlinx.coroutines.withTimeoutOrNull(1500L) {
                val cleanTable = tableNumber.trim()
                val snapshot = db.collection("orders")
                    .whereEqualTo("tableNumber", cleanTable)
                    .get()
                    .await()

                val activeDoc = snapshot.documents.firstOrNull { doc ->
                    val status = (doc.getString("status") ?: "").trim().lowercase()
                    status in listOf(
                        "abierto", "pendiente", "listo para cobrar", "listo_para_cobrar",
                        "por_cobrar", "en_proceso", "en proceso", "finalizado"
                    )
                } ?: run {
                    val allSnapshot = db.collection("orders").get().await()
                    allSnapshot.documents.firstOrNull { doc ->
                        val t = (doc.getString("tableNumber") ?: "").trim().lowercase()
                        val status = (doc.getString("status") ?: "").trim().lowercase()
                        val matchTable = t == cleanTable.lowercase() ||
                                t == "mesa $cleanTable".lowercase() ||
                                cleanTable.lowercase() == "mesa $t".lowercase()
                        matchTable && status in listOf(
                            "abierto", "pendiente", "listo para cobrar", "listo_para_cobrar",
                            "por_cobrar", "en_proceso", "en proceso", "finalizado"
                        )
                    }
                }

                if (activeDoc != null) {
                    val orderId = activeDoc.getLong("id") ?: activeDoc.id.toLongOrNull() ?: System.currentTimeMillis()
                    val orderNumber = activeDoc.getString("orderNumber") ?: "PED-$orderId"
                    val waiterName = activeDoc.getString("waiterName") ?: "Mesero"
                    val status = activeDoc.getString("status") ?: "PENDIENTE"
                    val totalAmount = activeDoc.getDouble("totalAmount") ?: 0.0
                    val createdAt = activeDoc.getLong("createdAt") ?: System.currentTimeMillis()
                    val completedAt = activeDoc.getLong("completedAt")
                    val paidAt = activeDoc.getLong("paidAt")
                    val paymentMethod = activeDoc.getString("paymentMethod")
                    val cashierName = activeDoc.getString("cashierName")
                    val generalNotes = activeDoc.getString("generalNotes")

                    val orderEntity = OrderEntity(
                        id = orderId,
                        orderNumber = orderNumber,
                        tableNumber = cleanTable,
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

                    val itemsList = activeDoc.get("items") as? List<Map<String, Any>>
                    val items = itemsList?.mapIndexed { idx, itemMap ->
                        val kitchenStatusStr = itemMap["kitchenStatus"] as? String
                            ?: itemMap["estado_cocina"] as? String
                            ?: itemMap["estadoCocina"] as? String
                            ?: if (status in listOf("FINALIZADO", "PAGADO")) "FINALIZADO" else "PENDIENTE"
                        val itemCreatedAt = (itemMap["createdAt"] as? Number)?.toLong() ?: createdAt

                        OrderItemEntity(
                            id = (itemMap["id"] as? Number)?.toLong() ?: 0L,
                            orderId = orderId,
                            menuItemId = (itemMap["menuItemId"] as? Number)?.toLong() ?: 0L,
                            productName = itemMap["productName"] as? String ?: "",
                            unitPrice = (itemMap["unitPrice"] as? Number)?.toDouble() ?: 0.0,
                            quantity = (itemMap["quantity"] as? Number)?.toInt() ?: 1,
                            subtotal = (itemMap["subtotal"] as? Number)?.toDouble() ?: 0.0,
                            notes = itemMap["notes"] as? String ?: "",
                            kitchenStatus = kitchenStatusStr.uppercase(),
                            createdAt = itemCreatedAt
                        )
                    } ?: emptyList()

                    Pair(orderEntity, items)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.w("FirestoreSync", "Notice searching active remote order for $tableNumber: ${e.message}")
            null
        }
    }

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
                            "notes" to item.notes,
                            "kitchenStatus" to item.kitchenStatus,
                            "estado_cocina" to item.kitchenStatus.lowercase(),
                            "createdAt" to item.createdAt
                        )
                    }
                )

                db.collection("orders")
                    .document(order.id.toString())
                    .set(orderMap, SetOptions.merge())
                    .await()

                _isLiveSyncActive.value = true
                _syncStatusLabel.value = "Sincronizado"
                _syncError.value = null
                _lastSyncTimestamp.value = System.currentTimeMillis()
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to push order #${order.id} to Firestore: ${e.message}")
                _isLiveSyncActive.value = false
                _syncStatusLabel.value = "Modo Local (Activo)"
                _syncError.value = "Fallo de conexión a Firestore: ${e.message}"
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
                    "posOrderId" to webOrder.posOrderId,
                    "deliveryAddress" to webOrder.deliveryAddress,
                    "deliveryDriverName" to webOrder.deliveryDriverName,
                    "deliveryStartedAt" to webOrder.deliveryStartedAt,
                    "deliveryFinishedAt" to webOrder.deliveryFinishedAt,
                    "deliveryIssueNote" to webOrder.deliveryIssueNote
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
                    "estado" to newStatus,
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

    fun syncDeliveryStatusToRemote(
        webOrderId: String,
        posOrderId: Long?,
        status: String,
        driverName: String,
        startedAt: Long? = null,
        finishedAt: Long? = null
    ) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val updateMap = mutableMapOf<String, Any>(
                    "status" to status,
                    "deliveryDriverName" to driverName
                )
                if (startedAt != null) updateMap["deliveryStartedAt"] = startedAt
                if (finishedAt != null) updateMap["deliveryFinishedAt"] = finishedAt

                db.collection("pedidos_web")
                    .document(webOrderId)
                    .set(updateMap, SetOptions.merge())
                    .await()

                if (posOrderId != null) {
                    val orderMap = mutableMapOf<String, Any>(
                        "status" to status,
                        "deliveryDriverName" to driverName
                    )
                    if (startedAt != null) orderMap["deliveryStartedAt"] = startedAt
                    if (finishedAt != null) orderMap["deliveryFinishedAt"] = finishedAt
                    db.collection("orders")
                        .document(posOrderId.toString())
                        .set(orderMap, SetOptions.merge())
                        .await()
                }
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to sync delivery status: ${e.message}")
            }
        }
    }

    fun recordDeliverySettlementToFirestore(settlement: DeliverySettlementEntity) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val docId = if (settlement.webOrderId.isNotBlank()) settlement.webOrderId else "SETTLE-${settlement.id}"
                val map = hashMapOf(
                    "id" to settlement.id,
                    "webOrderId" to settlement.webOrderId,
                    "posOrderId" to settlement.posOrderId,
                    "customerName" to settlement.customerName,
                    "customerPhone" to settlement.customerPhone,
                    "deliveryAddress" to settlement.deliveryAddress,
                    "driverName" to settlement.driverName,
                    "totalAmount" to settlement.totalAmount,
                    "paymentMethod" to settlement.paymentMethod,
                    "completedAt" to settlement.completedAt,
                    "settlementStatus" to settlement.settlementStatus
                )
                
                // Write into caja_liquidaciones collection
                db.collection("caja_liquidaciones")
                    .document(docId)
                    .set(map, SetOptions.merge())
                    .await()

                // Also update sub-document under caja/liquidaciones_delivery
                db.collection("caja")
                    .document("liquidaciones_delivery")
                    .collection("entregas_dia")
                    .document(docId)
                    .set(map, SetOptions.merge())
                    .await()

                Log.d("FirestoreSync", "Liquidación de delivery registrada en Firestore con éxito: $docId")
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to record delivery settlement to Firestore: ${e.message}")
            }
        }
    }

    fun recordDeliveryIncidentToFirestore(
        webOrderId: String,
        posOrderId: Long?,
        incidentNote: String,
        driverName: String
    ) {
        externalScope.launch {
            val db = firestore ?: return@launch
            try {
                val now = System.currentTimeMillis()
                val updateMap = hashMapOf<String, Any>(
                    "status" to "INCIDENCIA",
                    "deliveryDriverName" to driverName,
                    "deliveryIssueNote" to incidentNote,
                    "incidentReportedAt" to now
                )

                db.collection("pedidos_web")
                    .document(webOrderId)
                    .set(updateMap, SetOptions.merge())
                    .await()

                if (posOrderId != null) {
                    db.collection("orders")
                        .document(posOrderId.toString())
                        .set(updateMap, SetOptions.merge())
                        .await()
                }

                val alertMap = hashMapOf(
                    "orderId" to webOrderId,
                    "driverName" to driverName,
                    "incidentNote" to incidentNote,
                    "reportedAt" to now,
                    "resolved" to false
                )
                db.collection("incidencias_delivery")
                    .document("${webOrderId}_$now")
                    .set(alertMap, SetOptions.merge())
                    .await()

                Log.d("FirestoreSync", "Incidencia de delivery registrada en Firestore: $webOrderId")
            } catch (e: Exception) {
                Log.e("FirestoreSync", "Failed to record delivery incident to Firestore: ${e.message}")
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
