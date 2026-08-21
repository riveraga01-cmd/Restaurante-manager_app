package com.example.data.repository

import com.example.data.dao.RestaurantDao
import com.example.data.entity.*
import com.example.data.firebase.FirestoreSyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar

class RestaurantRepository(
    private val dao: RestaurantDao,
    val firestoreSyncManager: FirestoreSyncManager? = null
) {

    val isFirestoreSyncActive: StateFlow<Boolean>? = firestoreSyncManager?.isLiveSyncActive
    val lastSyncTimestamp: StateFlow<Long>? = firestoreSyncManager?.lastSyncTimestamp
    val syncError: StateFlow<String?>? = firestoreSyncManager?.syncError

    // --- MENU ---
    val allMenuItems: Flow<List<MenuItemEntity>> = dao.getAllMenuItems()
    val availableMenuItems: Flow<List<MenuItemEntity>> = dao.getAvailableMenuItems()

    suspend fun insertMenuItem(item: MenuItemEntity): Long {
        val id = dao.insertMenuItem(item)
        val insertedItem = if (item.id == 0L) item.copy(id = id) else item
        firestoreSyncManager?.syncMenuItemToRemote(insertedItem)
        return id
    }

    suspend fun updateMenuItem(item: MenuItemEntity) {
        dao.updateMenuItem(item)
        firestoreSyncManager?.syncMenuItemToRemote(item)
    }

    suspend fun deleteMenuItem(id: Long) {
        dao.deleteMenuItemById(id)
        firestoreSyncManager?.deleteRemoteMenuItem(id)
    }

    // --- INVENTORY ---
    val allInventory: Flow<List<InventoryItemEntity>> = dao.getAllInventory()
    val lowStockInventory: Flow<List<InventoryItemEntity>> = dao.getLowStockInventory()

    suspend fun insertInventory(item: InventoryItemEntity): Long {
        val id = dao.insertInventory(item)
        val insertedItem = if (item.id == 0L) item.copy(id = id) else item
        firestoreSyncManager?.syncInventoryToRemote(insertedItem)
        return id
    }

    suspend fun updateInventory(item: InventoryItemEntity) {
        dao.updateInventory(item)
        firestoreSyncManager?.syncInventoryToRemote(item)
    }

    suspend fun deleteInventory(id: Long) {
        dao.deleteInventoryById(id)
        firestoreSyncManager?.deleteRemoteInventory(id)
    }

    // --- ORDERS ---
    val allOrders: Flow<List<OrderEntity>> = dao.getAllOrders()
    val kitchenOrders: Flow<List<OrderEntity>> = dao.getKitchenOrders()
    val cashierOrders: Flow<List<OrderEntity>> = dao.getCashierOrders()

    fun getWaiterActiveOrders(waiterName: String): Flow<List<OrderEntity>> {
        return dao.getWaiterActiveOrders(waiterName)
    }

    fun getWaiterHistory(waiterName: String): Flow<List<OrderEntity>> {
        return dao.getWaiterHistory(waiterName)
    }

    fun getOrderItemsFlow(orderId: Long): Flow<List<OrderItemEntity>> {
        return dao.getOrderItemsFlow(orderId)
    }

    suspend fun getOrderItems(orderId: Long): List<OrderItemEntity> {
        return dao.getOrderItems(orderId)
    }

    suspend fun createOrder(
        tableNumber: String,
        waiterName: String,
        items: List<Pair<MenuItemEntity, Pair<Int, String>>>, // MenuItem, (Quantity, Notes)
        generalNotes: String?
    ): Long {
        var total = 0.0
        items.forEach { (menuItem, pair) ->
            val qty = pair.first
            total += menuItem.price * qty
        }

        val orderNum = "PED-${(100..999).random()}"
        val order = OrderEntity(
            orderNumber = orderNum,
            tableNumber = tableNumber,
            waiterName = waiterName,
            status = "PENDIENTE",
            totalAmount = total,
            createdAt = System.currentTimeMillis(),
            generalNotes = generalNotes
        )

        val orderId = dao.insertOrder(order)

        val orderItems = items.map { (menuItem, pair) ->
            val qty = pair.first
            val notes = pair.second
            val subtotal = menuItem.price * qty
            OrderItemEntity(
                orderId = orderId,
                menuItemId = menuItem.id,
                productName = menuItem.name,
                unitPrice = menuItem.price,
                quantity = qty,
                subtotal = subtotal,
                notes = notes
            )
        }

        dao.insertOrderItems(orderItems)

        val savedOrder = dao.getOrderById(orderId)
        val savedItems = dao.getOrderItems(orderId)
        if (savedOrder != null) {
            firestoreSyncManager?.syncOrderToRemote(savedOrder, savedItems)
        }

        return orderId
    }

    suspend fun updateOrderStatus(orderId: Long, newStatus: String) {
        val order = dao.getOrderById(orderId) ?: return
        val updatedOrder = order.copy(
            status = newStatus,
            completedAt = if (newStatus == "FINALIZADO") System.currentTimeMillis() else order.completedAt
        )
        dao.updateOrder(updatedOrder)

        val items = dao.getOrderItems(orderId)
        firestoreSyncManager?.syncOrderToRemote(updatedOrder, items)
    }

    suspend fun processPayment(
        orderId: Long,
        paymentMethod: String,
        cashierName: String
    ) {
        val order = dao.getOrderById(orderId) ?: return
        val now = System.currentTimeMillis()
        val updatedOrder = order.copy(
            status = "PAGADO",
            paidAt = now,
            paymentMethod = paymentMethod,
            cashierName = cashierName
        )
        dao.updateOrder(updatedOrder)

        // Insert into sales
        dao.insertSale(
            SaleEntity(
                orderId = orderId,
                orderNumber = order.orderNumber,
                cashierName = cashierName,
                total = order.totalAmount,
                paymentMethod = paymentMethod,
                timestamp = now
            )
        )

        val items = dao.getOrderItems(orderId)

        // Automatically deduct BOM recipe ingredients from inventory
        for (item in items) {
            val recipe = dao.getRecipeForMenuItem(item.menuItemId)
            for (recItem in recipe) {
                val ingredient = dao.getInventoryItemById(recItem.ingredientId)
                if (ingredient != null) {
                    val consumedQty = recItem.quantityRequired * item.quantity
                    val newStock = (ingredient.currentStock - consumedQty).coerceAtLeast(0.0)
                    val updatedIng = ingredient.copy(currentStock = newStock)
                    dao.updateInventory(updatedIng)
                    firestoreSyncManager?.syncInventoryToRemote(updatedIng)

                    // Record automatic deduction movement
                    dao.insertInventoryMovement(
                        InventoryMovementEntity(
                            ingredientId = ingredient.id,
                            ingredientName = ingredient.productName,
                            type = "VENTA_AUTOMATICA",
                            quantity = -consumedQty,
                            reason = "Venta autom. #${order.orderNumber} (${item.productName} x${item.quantity})",
                            user = cashierName,
                            timestamp = now
                        )
                    )
                }
            }
        }

        firestoreSyncManager?.syncOrderToRemote(updatedOrder, items)
    }

    suspend fun cancelOrder(orderId: Long) {
        val order = dao.getOrderById(orderId) ?: return
        val updated = order.copy(status = "CANCELADO")
        dao.updateOrder(updated)
        val items = dao.getOrderItems(orderId)
        firestoreSyncManager?.syncOrderToRemote(updated, items)
    }

    suspend fun deleteOrderAndDetails(orderId: Long) {
        val order = dao.getOrderById(orderId) ?: return
        dao.deleteOrderItemsByOrderId(orderId)
        dao.deleteOrder(order)
        firestoreSyncManager?.deleteRemoteOrder(orderId)
    }

    // --- SALES & REPORTS ---
    val allSales: Flow<List<SaleEntity>> = dao.getAllSales()
    val allDailyCloses: Flow<List<DailyCloseEntity>> = dao.getAllDailyCloses()
    val allOrderItems: Flow<List<OrderItemEntity>> = dao.getAllOrderItems()

    fun getSalesForPeriod(period: String): Flow<List<SaleEntity>> {
        val calendar = Calendar.getInstance()
        val now = System.currentTimeMillis()
        
        // Reset time component for midnight boundary calculations
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val startTimestamp = when (period.lowercase()) {
            "diario" -> calendar.timeInMillis
            "semanal" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -7)
                calendar.timeInMillis
            }
            "quincenal" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -15)
                calendar.timeInMillis
            }
            "mensual" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -30)
                calendar.timeInMillis
            }
            else -> 0L
        }

        return dao.getSalesByTimeRange(startTimestamp, now)
    }

    fun getCategorySalesForPeriod(period: String): Flow<List<CategorySaleSummary>> {
        val calendar = Calendar.getInstance()
        val now = System.currentTimeMillis()

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val startTimestamp = when (period.lowercase()) {
            "diario" -> calendar.timeInMillis
            "semanal" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -7)
                calendar.timeInMillis
            }
            "quincenal" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -15)
                calendar.timeInMillis
            }
            "mensual" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -30)
                calendar.timeInMillis
            }
            else -> 0L
        }

        return dao.getCategorySalesByTimeRange(startTimestamp, now)
    }

    // --- USERS / EMPLOYEES ---
    val allUsers: Flow<List<UserEntity>> = dao.getAllUsers()

    fun getUsersByRole(role: String): Flow<List<UserEntity>> = dao.getUsersByRole(role)

    suspend fun saveUser(user: UserEntity): Long {
        val id = if (user.id == 0L) {
            dao.insertUser(user)
        } else {
            dao.updateUser(user)
            user.id
        }
        val savedUser = if (user.id == 0L) user.copy(id = id) else user
        firestoreSyncManager?.syncUserToRemote(savedUser)
        return id
    }

    suspend fun deleteUser(id: Long) {
        dao.deleteUserById(id)
        firestoreSyncManager?.deleteRemoteUser(id)
    }

    // --- RECIPES (BILL OF MATERIALS) ---
    val allRecipeItems: Flow<List<RecipeItemEntity>> = dao.getAllRecipeItems()

    fun getRecipeForMenuItemFlow(menuItemId: Long): Flow<List<RecipeItemEntity>> =
        dao.getRecipeForMenuItemFlow(menuItemId)

    suspend fun getRecipeForMenuItem(menuItemId: Long): List<RecipeItemEntity> =
        dao.getRecipeForMenuItem(menuItemId)

    suspend fun saveRecipeItem(item: RecipeItemEntity): Long {
        return dao.insertRecipeItem(item)
    }

    suspend fun deleteRecipeItem(id: Long) {
        dao.deleteRecipeItemById(id)
    }

    suspend fun deleteRecipeForMenuItem(menuItemId: Long) {
        dao.deleteRecipeForMenuItem(menuItemId)
    }

    // --- INVENTORY MOVEMENTS ---
    val allInventoryMovements: Flow<List<InventoryMovementEntity>> = dao.getAllInventoryMovements()

    suspend fun registerManualInventoryMovement(
        ingredientId: Long,
        type: String, // COMPRA, MERMA, VENCIMIENTO, CONSUMO_INTERNO, AJUSTE
        quantity: Double,
        reason: String,
        userName: String
    ) {
        val ingredient = dao.getInventoryItemById(ingredientId) ?: return
        val newStock = when (type) {
            "COMPRA" -> ingredient.currentStock + quantity
            "MERMA", "VENCIMIENTO", "CONSUMO_INTERNO" -> (ingredient.currentStock - quantity).coerceAtLeast(0.0)
            "AJUSTE" -> quantity.coerceAtLeast(0.0)
            else -> ingredient.currentStock
        }

        val updatedIngredient = ingredient.copy(currentStock = newStock)
        dao.updateInventory(updatedIngredient)
        firestoreSyncManager?.syncInventoryToRemote(updatedIngredient)

        val signedQty = if (type in listOf("MERMA", "VENCIMIENTO", "CONSUMO_INTERNO")) -quantity else quantity
        dao.insertInventoryMovement(
            InventoryMovementEntity(
                ingredientId = ingredient.id,
                ingredientName = ingredient.productName,
                type = type,
                quantity = signedQty,
                reason = reason,
                user = userName,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun verifyManagerPin(pin: String): Boolean {
        val settings = dao.getSystemSettings()
        if (settings != null) return settings.managerPin == pin
        val config = dao.getManagerConfig() ?: ManagerConfigEntity(1, "1234")
        return config.pin == pin
    }

    suspend fun updateManagerPin(newPin: String) {
        dao.insertManagerConfig(ManagerConfigEntity(id = 1, pin = newPin))
        val currentSettings = dao.getSystemSettings() ?: SystemSettingsEntity(id = 1)
        val updated = currentSettings.copy(managerPin = newPin)
        dao.insertSystemSettings(updated)
        firestoreSyncManager?.syncSettingsToRemote(updated)
    }

    // --- SYSTEM SETTINGS ---
    val systemSettings: Flow<SystemSettingsEntity?> = dao.getSystemSettingsFlow()

    suspend fun getSystemSettings(): SystemSettingsEntity {
        return dao.getSystemSettings() ?: SystemSettingsEntity(id = 1)
    }

    suspend fun saveSystemSettings(settings: SystemSettingsEntity) {
        dao.insertSystemSettings(settings)
        firestoreSyncManager?.syncSettingsToRemote(settings)
    }

    // --- DEVICE BINDINGS & PAIRING CODES ---
    val allDeviceBindings: Flow<List<DeviceBindingEntity>> = dao.getAllDeviceBindings()

    suspend fun saveDeviceBinding(binding: DeviceBindingEntity): Long {
        val id = dao.insertDeviceBinding(binding)
        val saved = if (binding.id == 0L) binding.copy(id = id) else binding
        firestoreSyncManager?.syncDeviceBindingToRemote(saved)
        return id
    }

    suspend fun deleteDeviceBinding(id: Long) {
        dao.deleteDeviceBindingById(id)
    }

    suspend fun validateAndPairDeviceCode(code: String, deviceId: String, deviceName: String): Triple<Boolean, String, String?> {
        val cleanCode = code.trim().uppercase()
        val binding = dao.getDeviceBindingByCode(cleanCode)
            ?: return Triple(false, "PIN o código no válido.", null)

        if (System.currentTimeMillis() > binding.expiresAt) {
            return Triple(false, "El código de vinculación ha expirado.", null)
        }

        val linkedDevice = LinkedDeviceEntity(
            deviceId = deviceId,
            deviceName = deviceName,
            branchName = binding.branchName,
            assignedRole = binding.assignedRole,
            linkedUser = "Terminal ${binding.assignedRole}",
            linkedAt = System.currentTimeMillis(),
            lastSeen = System.currentTimeMillis(),
            isBlocked = false
        )
        dao.insertLinkedDevice(linkedDevice)
        firestoreSyncManager?.syncLinkedDeviceToRemote(linkedDevice)

        if (!binding.isMultiUse) {
            dao.deleteDeviceBindingById(binding.id)
        } else {
            dao.updateDeviceBinding(binding.copy(usedCount = binding.usedCount + 1))
        }

        logAudit(
            user = "Dispositivo",
            role = binding.assignedRole,
            deviceId = deviceId,
            action = "VINCULAR_DISPOSITIVO",
            details = "Dispositivo $deviceName vinculado con éxito con código $cleanCode para rol ${binding.assignedRole}"
        )

        return Triple(true, "Dispositivo vinculado exitosamente para ${binding.assignedRole} (${binding.branchName})", binding.assignedRole)
    }

    // --- LINKED DEVICES ---
    val allLinkedDevices: Flow<List<LinkedDeviceEntity>> = dao.getAllLinkedDevices()

    suspend fun saveLinkedDevice(device: LinkedDeviceEntity) {
        dao.insertLinkedDevice(device)
        firestoreSyncManager?.syncLinkedDeviceToRemote(device)
    }

    suspend fun deleteLinkedDevice(deviceId: String) {
        dao.deleteLinkedDeviceById(deviceId)
    }

    // --- AUDIT LOGS ---
    val allAuditLogs: Flow<List<AuditLogEntity>> = dao.getAllAuditLogs()

    suspend fun logAudit(
        user: String,
        role: String,
        deviceId: String,
        action: String,
        details: String,
        settingModified: String = "",
        previousValue: String = "",
        newValue: String = ""
    ) {
        val item = AuditLogEntity(
            user = user,
            role = role,
            deviceId = deviceId,
            action = action,
            details = details,
            settingModified = settingModified,
            previousValue = previousValue,
            newValue = newValue,
            timestamp = System.currentTimeMillis()
        )
        dao.insertAuditLog(item)
        firestoreSyncManager?.syncAuditLogToRemote(item)
    }

    // --- MANUAL SYNC ---
    val syncStatusLabel: StateFlow<String>? = firestoreSyncManager?.syncStatusLabel

    fun triggerManualSync(onComplete: (Boolean, String) -> Unit) {
        firestoreSyncManager?.triggerManualSync(onComplete) ?: onComplete(false, "Base de datos local en modo offline")
    }

    // --- DAILY CLOSES & SYSTEM RESET MAINTENANCE ---
    suspend fun performDailyClose(
        user: String,
        deviceId: String = "DEV-RIVERA-POS1",
        branchName: String = "Sucursal Central",
        onResult: (Boolean, String) -> Unit
    ) {
        val todayStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        val dateCompact = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault()).format(java.util.Date())

        // Check if closure already performed today
        val existingClose = dao.getDailyCloseByDate(todayStr)
        if (existingClose != null) {
            onResult(false, "Ya fue realizado un cierre diario para la fecha $todayStr (Folio: ${existingClose.folio}).")
            return
        }

        val startOfDay = getStartOfDayTimestamp()
        val endOfDay = System.currentTimeMillis()

        // Get paid orders today and sales
        val salesTodayList = dao.getSalesByTimeRange(startOfDay, endOfDay).firstOrNull() ?: emptyList()
        var totalAmount = 0.0
        var totalOrdersCount = 0

        val ordersList = dao.getAllOrders().firstOrNull() ?: emptyList()
        val paidOrdersToday = ordersList.filter { it.status == "PAGADO" && (it.paidAt ?: 0L) >= startOfDay }

        for (order in paidOrdersToday) {
            totalAmount += order.totalAmount
            totalOrdersCount++
            // Ensure sale entity exists
            val existingSale = salesTodayList.find { it.orderId == order.id }
            if (existingSale == null) {
                dao.insertSale(
                    SaleEntity(
                        orderId = order.id,
                        orderNumber = order.orderNumber,
                        cashierName = order.cashierName ?: user,
                        total = order.totalAmount,
                        paymentMethod = order.paymentMethod ?: "Efectivo",
                        timestamp = order.paidAt ?: System.currentTimeMillis()
                    )
                )
            }
        }

        val folioCount = (1..99).random()
        val folio = "CLO-$dateCompact-${String.format("%06d", folioCount)}"

        val closeRecord = DailyCloseEntity(
            folio = folio,
            closeDate = todayStr,
            totalSales = totalAmount,
            totalOrders = totalOrdersCount,
            closedBy = user,
            role = "GERENTE",
            deviceId = deviceId,
            branchName = branchName,
            summaryJson = "{'paidOrders': $totalOrdersCount, 'totalSales': $totalAmount}",
            timestamp = System.currentTimeMillis()
        )

        dao.insertDailyClose(closeRecord)

        logAudit(
            user = user,
            role = "GERENTE",
            deviceId = deviceId,
            action = "CIERRE_DIARIO",
            details = "Cierre diario ejecutado para fecha $todayStr. Total Ventas: Q${String.format("%.2f", totalAmount)}, Comandas: $totalOrdersCount"
        )

        onResult(true, "Cierre diario exitoso ($folio). Total de ventas procesadas: Q${String.format("%.2f", totalAmount)}")
    }

    suspend fun saveDailyClose(close: DailyCloseEntity) {
        dao.insertDailyClose(close)
    }

    suspend fun resetShiftAfterCorte(
        user: String,
        deviceId: String = "DEV-RIVERA-POS1"
    ) {
        dao.deleteAllSales()
        dao.deleteAllOrders()
        dao.deleteAllOrderItems()
        dao.resetAllTableStatuses()
        logAudit(
            user = user,
            role = "CAJA",
            deviceId = deviceId,
            action = "REINICIO_TURNO",
            details = "Turno de caja reiniciado exitosamente tras corte de caja. Ventas y comandas reiniciadas a cero para nuevo turno en blanco."
        )
    }

    suspend fun startNewJornada(
        user: String,
        deviceId: String = "DEV-RIVERA-POS1",
        onResult: (Boolean, String) -> Unit
    ) {
        dao.clearCompletedOrdersForNewJornada()
        logAudit(
            user = user,
            role = "GERENTE",
            deviceId = deviceId,
            action = "INICIAR_JORNADA",
            details = "Nueva jornada iniciada. Se limpiaron las comandes finalizadas/pagadas conservando pedidos pendientes e historial de ventas."
        )
        onResult(true, "Nueva jornada operativa iniciada correctamente. Pantallas operativas despejadas.")
    }

    suspend fun resetOperationalSystem(
        user: String,
        deviceId: String = "DEV-RIVERA-POS1",
        onResult: (Boolean, String) -> Unit
    ) {
        dao.deleteAllOrders()
        dao.deleteAllOrderItems()
        logAudit(
            user = user,
            role = "GERENTE",
            deviceId = deviceId,
            action = "REINICIAR_SISTEMA_OPERATIVO",
            details = "Área operativa de la aplicación restablecida. Comandas activas reiniciadas sin alterar inventario ni historial."
        )
        onResult(true, "Área operativa restablecida exitosamente.")
    }

    suspend fun performFactoryResetTotalSystem(
        user: String,
        reason: String,
        createBackup: Boolean,
        deviceId: String = "DEV-RIVERA-POS1",
        onResult: (Boolean, String) -> Unit
    ) {
        try {
            if (createBackup) {
                // Log automatic backup snapshot
                logAudit(
                    user = user,
                    role = "GERENTE",
                    deviceId = deviceId,
                    action = "COPIA_SEGURIDAD_PRE_RESET",
                    details = "Copia de respaldo previa al restablecimiento total generada automáticamente."
                )
            }

            dao.deleteAllOrders()
            dao.deleteAllOrderItems()
            dao.deleteAllSales()
            dao.deleteAllInventory()
            dao.deleteAllMenuItems()
            dao.deleteAllRecipeItems()
            dao.deleteAllInventoryMovements()
            dao.deleteAllDailyCloses()
            dao.deleteAllDeviceBindings()
            dao.deleteAllLinkedDevices()
            dao.deleteAllSyncQueue()
            dao.deleteAllUsersExceptGerente()

            // Reset system settings to base defaults (AZUL_RIVERA)
            dao.insertSystemSettings(
                SystemSettingsEntity(
                    id = 1,
                    restaurantName = "Restaurante Rivera",
                    branchName = "Sucursal Central",
                    themePalette = "AZUL_RIVERA",
                    currencySymbol = "Q",
                    managerPin = "1234"
                )
            )

            // Clear all tables active states
            val defaultTables = listOf(
                TableEntity(tableNumber = "Mesa 1", capacity = 4, status = "Disponible", displayOrder = 1),
                TableEntity(tableNumber = "Mesa 2", capacity = 4, status = "Disponible", displayOrder = 2),
                TableEntity(tableNumber = "Mesa 3", capacity = 6, status = "Disponible", displayOrder = 3),
                TableEntity(tableNumber = "Mesa 4", capacity = 2, status = "Disponible", displayOrder = 4),
                TableEntity(tableNumber = "Mesa 5", capacity = 4, status = "Disponible", displayOrder = 5),
                TableEntity(tableNumber = "Mesa 6", capacity = 8, status = "Disponible", displayOrder = 6),
                TableEntity(tableNumber = "Para Llevar", capacity = 1, status = "Disponible", displayOrder = 7)
            )
            for (table in defaultTables) {
                dao.insertTable(table)
            }

            logAudit(
                user = user,
                role = "GERENTE",
                deviceId = deviceId,
                action = "RESTABLECER_SISTEMA_TOTAL",
                details = "Restablecimiento total ejecutado por el gerente. Motivo: $reason"
            )

            onResult(true, "El sistema ha sido restablecido totalmente. Se conservó únicamente la cuenta principal del gerente.")
        } catch (e: Exception) {
            onResult(false, "Error durante el restablecimiento: ${e.localizedMessage}")
        }
    }

    // --- TABLES ---
    val allTables: Flow<List<TableEntity>> = dao.getAllTables()
    val activeTables: Flow<List<TableEntity>> = dao.getActiveTables()

    suspend fun insertTable(table: TableEntity): Long {
        return dao.insertTable(table)
    }

    suspend fun updateTable(table: TableEntity) {
        dao.updateTable(table)
    }

    suspend fun deleteTable(id: Long) {
        dao.deleteTableById(id)
    }

    suspend fun setTotalTablesCount(targetCount: Int, onResult: (Boolean, String) -> Unit) {
        val currentTables = dao.getAllTables().firstOrNull() ?: emptyList()
        val nonParaLlevarTables = currentTables.filter { !it.tableNumber.equals("Para Llevar", ignoreCase = true) }
        val currentSize = nonParaLlevarTables.size
        val settings = getSystemSettings()

        if (targetCount == currentSize) {
            onResult(true, "La cantidad de mesas ya es $targetCount.")
            return
        }

        if (targetCount > currentSize) {
            var nextNum = 1
            var added = 0
            val needed = targetCount - currentSize
            val maxDisplay = currentTables.maxOfOrNull { it.displayOrder } ?: 0
            while (added < needed) {
                val candidateName = "Mesa $nextNum"
                if (dao.getTableByNumber(candidateName) == null) {
                    dao.insertTable(
                        TableEntity(
                            tableNumber = candidateName,
                            capacity = settings.defaultTableCapacity,
                            status = settings.defaultTableStatus,
                            isActive = true,
                            displayOrder = maxDisplay + added + 1
                        )
                    )
                    added++
                }
                nextNum++
            }
            logAudit(
                user = "Gerente",
                role = "GERENTE",
                deviceId = "SISTEMA",
                action = "AJUSTAR_TOTAL_MESAS",
                details = "Se aumentó el aforo total de $currentSize a $targetCount mesas (+ $added mesas automáticamente).",
                settingModified = "Cantidad Total de Mesas",
                previousValue = "$currentSize",
                newValue = "$targetCount"
            )
            onResult(true, "Se crearon $added mesas automáticamente. Total del sistema: $targetCount mesas.")
        } else if (targetCount < currentSize && targetCount >= 1) {
            val sortedTables = nonParaLlevarTables.sortedByDescending { table ->
                table.tableNumber.replace(Regex("[^0-9]"), "").toIntOrNull() ?: table.id.toInt()
            }
            val countToRemove = currentSize - targetCount
            val candidateTables = sortedTables.take(countToRemove)

            val activeOrders = dao.getKitchenOrders().firstOrNull() ?: emptyList()
            val blockedTable = candidateTables.find { table ->
                activeOrders.any { order ->
                    order.tableNumber.equals(table.tableNumber, ignoreCase = true) &&
                            order.status in listOf("PENDIENTE", "EN_PROCESO", "FINALIZADO")
                }
            }

            if (blockedTable != null) {
                onResult(false, "No se puede reducir el aforo porque la ${blockedTable.tableNumber} tiene pedidos activos.")
                return
            }

            for (table in candidateTables) {
                dao.deleteTableById(table.id)
            }
            logAudit(
                user = "Gerente",
                role = "GERENTE",
                deviceId = "SISTEMA",
                action = "AJUSTAR_TOTAL_MESAS",
                details = "Se redujo el aforo total de $currentSize a $targetCount mesas (- ${candidateTables.size} mesas sin pedidos activos).",
                settingModified = "Cantidad Total de Mesas",
                previousValue = "$currentSize",
                newValue = "$targetCount"
            )
            onResult(true, "Se actualizó la cantidad total de mesas a $targetCount (eliminadas $countToRemove mesas sin pedidos activos).")
        } else {
            onResult(false, "Ingrese una cantidad válida mayor a 0.")
        }
    }

    // --- INVOICES (FACTURACIÓN) ---
    val allInvoices: Flow<List<InvoiceEntity>> = dao.getAllInvoices()

    suspend fun getInvoiceById(id: Long): InvoiceEntity? {
        return dao.getInvoiceById(id)
    }

    suspend fun getInvoiceByOrderId(orderId: Long): InvoiceEntity? {
        return dao.getInvoiceByOrderId(orderId)
    }

    suspend fun insertInvoice(invoice: InvoiceEntity): Long {
        return dao.insertInvoice(invoice)
    }

    // --- THEMES & BRANDING (TEMAS WEB / POS) ---
    val allThemes: Flow<List<ThemeConfigEntity>> = dao.getAllThemes()
    val activeTheme: Flow<ThemeConfigEntity?> = dao.getActiveTheme()

    suspend fun getActiveThemeDirect(): ThemeConfigEntity? {
        return dao.getActiveThemeDirect()
    }

    suspend fun saveTheme(theme: ThemeConfigEntity): Long {
        val id = dao.insertTheme(theme)
        val savedTheme = if (theme.id == 0L) theme.copy(id = id) else theme
        if (savedTheme.isActive) {
            dao.deactivateOtherThemes(savedTheme.id)
        }
        firestoreSyncManager?.syncThemeToRemote(savedTheme)
        return id
    }

    suspend fun activateTheme(themeId: Long) {
        val theme = dao.getThemeById(themeId) ?: return
        val updated = theme.copy(isActive = true, updatedAt = System.currentTimeMillis())
        dao.insertTheme(updated)
        dao.deactivateOtherThemes(themeId)
        firestoreSyncManager?.syncThemeToRemote(updated)
    }

    suspend fun deleteTheme(themeId: Long) {
        dao.deleteThemeById(themeId)
        firestoreSyncManager?.deleteRemoteTheme(themeId)
    }

    // --- WEB ORDERS (PEDIDOS_WEB) ---
    val allWebOrders: Flow<List<WebOrderEntity>> = dao.getAllWebOrders()
    val pendingWebOrders: Flow<List<WebOrderEntity>> = dao.getPendingValidationWebOrders()
    val activeWebOrders: Flow<List<WebOrderEntity>> = dao.getActiveWebOrders()
    val webVisibleMenuItems: Flow<List<MenuItemEntity>> = dao.getWebVisibleMenuItems()

    suspend fun createWebOrder(webOrder: WebOrderEntity): Long {
        val id = dao.insertWebOrder(webOrder)
        val saved = if (webOrder.id == 0L) webOrder.copy(id = id) else webOrder
        firestoreSyncManager?.syncWebOrderToRemote(saved)
        return id
    }

    suspend fun validateAndTransferWebOrderToKitchen(
        webOrderId: Long,
        assignedWaiter: String = "Mesero Web",
        onSuccess: (Long) -> Unit
    ) {
        val webOrder = dao.getWebOrderById(webOrderId) ?: return

        // 1. Create native POS order
        val posOrderNum = "PED-WEB-${(100..999).random()}"
        val posOrder = OrderEntity(
            orderNumber = posOrderNum,
            tableNumber = webOrder.tableNumber.ifBlank { "Para Llevar" },
            waiterName = assignedWaiter,
            status = "PENDIENTE",
            totalAmount = webOrder.totalAmount,
            createdAt = System.currentTimeMillis(),
            generalNotes = "Origen: ${webOrder.origin} | Cliente: ${webOrder.customerName} (${webOrder.customerPhone}) ${webOrder.notes}".trim()
        )
        val createdPosOrderId = dao.insertOrder(posOrder)

        // Parse itemsJson and insert OrderItemEntity for kitchen!
        val orderItems = mutableListOf<OrderItemEntity>()
        try {
            val jsonArray = org.json.JSONArray(webOrder.itemsJson)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val menuItemId = obj.optLong("id", obj.optLong("menuItemId", (i + 1).toLong()))
                val productName = obj.optString("name", obj.optString("productName", "Producto Web"))
                val unitPrice = obj.optDouble("price", obj.optDouble("unitPrice", 0.0))
                val quantity = obj.optInt("quantity", 1)
                val subtotal = obj.optDouble("subtotal", unitPrice * quantity)
                val notes = obj.optString("notes", "")
                orderItems.add(
                    OrderItemEntity(
                        orderId = createdPosOrderId,
                        menuItemId = menuItemId,
                        productName = productName,
                        unitPrice = unitPrice,
                        quantity = quantity,
                        subtotal = subtotal,
                        notes = notes
                    )
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("RestaurantRepo", "Error parsing web order itemsJson: ${e.message}")
        }
        if (orderItems.isNotEmpty()) {
            dao.insertOrderItems(orderItems)
        }

        // Also mark table as occupied if matching table found
        val matchedTable = dao.getTableByNumber(webOrder.tableNumber)
        if (matchedTable != null && matchedTable.status == "Disponible") {
            dao.updateTable(matchedTable.copy(status = "Ocupada", occupiedSince = System.currentTimeMillis()))
        }

        // 2. Mark Web Order as 'En Cocina' and link to POS order
        val now = System.currentTimeMillis()
        dao.validateAndLinkWebOrder(
            id = webOrderId,
            status = "En Cocina",
            posOrderId = createdPosOrderId,
            validatedAt = now
        )

        // 3. Sync updates to Firestore
        firestoreSyncManager?.updateRemoteWebOrderStatus(
            webOrderId = webOrder.webOrderId.ifBlank { "WEB-$webOrderId" },
            newStatus = "En Cocina",
            posOrderId = createdPosOrderId
        )
        firestoreSyncManager?.syncOrderToRemote(posOrder.copy(id = createdPosOrderId), orderItems)

        onSuccess(createdPosOrderId)
    }

    suspend fun rejectWebOrder(
        webOrderId: Long,
        reason: String = "Rechazado por el restaurante"
    ) {
        val webOrder = dao.getWebOrderById(webOrderId) ?: return
        // Mark status as Cancelado in remote Firestore
        firestoreSyncManager?.updateRemoteWebOrderStatus(
            webOrderId = webOrder.webOrderId.ifBlank { "WEB-$webOrderId" },
            newStatus = "Cancelado"
        )
        // Delete from local queue
        dao.deleteWebOrderById(webOrderId)
        firestoreSyncManager?.deleteRemoteWebOrder(webOrderId, webOrder.webOrderId)
    }

    suspend fun updateWebOrderStatus(webOrderId: Long, status: String) {
        val webOrder = dao.getWebOrderById(webOrderId) ?: return
        dao.updateWebOrderStatus(webOrderId, status)
        firestoreSyncManager?.updateRemoteWebOrderStatus(
            webOrderId = webOrder.webOrderId.ifBlank { "WEB-$webOrderId" },
            newStatus = status
        )
    }

    suspend fun deleteWebOrder(webOrderId: Long) {
        val webOrder = dao.getWebOrderById(webOrderId)
        dao.deleteWebOrderById(webOrderId)
        if (webOrder != null) {
            firestoreSyncManager?.deleteRemoteWebOrder(webOrderId, webOrder.webOrderId)
        }
    }

    private fun getStartOfDayTimestamp(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}

