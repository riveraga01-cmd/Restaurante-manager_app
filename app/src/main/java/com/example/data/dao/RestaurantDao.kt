package com.example.data.dao

import androidx.room.*
import com.example.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    // --- USERS / EMPLOYEES ---
    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE role = :role ORDER BY name ASC")
    fun getUsersByRole(role: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Long)


    // --- MENU ---
    @Query("SELECT * FROM menu_items ORDER BY category ASC, name ASC")
    fun getAllMenuItems(): Flow<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_items WHERE isAvailable = 1 ORDER BY category ASC, name ASC")
    fun getAvailableMenuItems(): Flow<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_items WHERE isVisibleWeb = 1 AND isAvailable = 1 ORDER BY category ASC, name ASC")
    fun getWebVisibleMenuItems(): Flow<List<MenuItemEntity>>

    @Query("SELECT * FROM menu_items WHERE id = :id LIMIT 1")
    suspend fun getMenuItemById(id: Long): MenuItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(item: MenuItemEntity): Long

    @Update
    suspend fun updateMenuItem(item: MenuItemEntity)

    @Query("DELETE FROM menu_items WHERE id = :id")
    suspend fun deleteMenuItemById(id: Long)


    // --- INVENTORY & INGREDIENTS ---
    @Query("SELECT * FROM inventory_items ORDER BY productName ASC")
    fun getAllInventory(): Flow<List<InventoryItemEntity>>

    @Query("SELECT * FROM inventory_items WHERE id = :id")
    suspend fun getInventoryItemById(id: Long): InventoryItemEntity?

    @Query("SELECT * FROM inventory_items WHERE currentStock <= minStock ORDER BY productName ASC")
    fun getLowStockInventory(): Flow<List<InventoryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventory(item: InventoryItemEntity): Long

    @Update
    suspend fun updateInventory(item: InventoryItemEntity)

    @Query("DELETE FROM inventory_items WHERE id = :id")
    suspend fun deleteInventoryById(id: Long)

    // --- RECIPES (BILL OF MATERIALS) ---
    @Query("SELECT * FROM recipe_items WHERE menuItemId = :menuItemId ORDER BY ingredientName ASC")
    fun getRecipeForMenuItemFlow(menuItemId: Long): Flow<List<RecipeItemEntity>>

    @Query("SELECT * FROM recipe_items WHERE menuItemId = :menuItemId ORDER BY ingredientName ASC")
    suspend fun getRecipeForMenuItem(menuItemId: Long): List<RecipeItemEntity>

    @Query("SELECT * FROM recipe_items ORDER BY menuItemId ASC, ingredientName ASC")
    fun getAllRecipeItems(): Flow<List<RecipeItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeItem(item: RecipeItemEntity): Long

    @Query("DELETE FROM recipe_items WHERE id = :id")
    suspend fun deleteRecipeItemById(id: Long)

    @Query("DELETE FROM recipe_items WHERE menuItemId = :menuItemId")
    suspend fun deleteRecipeForMenuItem(menuItemId: Long)

    // --- INVENTORY MOVEMENTS ---
    @Query("SELECT * FROM inventory_movements ORDER BY timestamp DESC")
    fun getAllInventoryMovements(): Flow<List<InventoryMovementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryMovement(movement: InventoryMovementEntity): Long


    // --- ORDERS ---
    @Query("SELECT * FROM orders ORDER BY id DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("""
        SELECT DISTINCT o.* FROM orders o
        JOIN order_items oi ON o.id = oi.orderId
        WHERE oi.kitchenStatus IN ('PENDIENTE', 'EN_PROCESO', 'NUEVO')
        ORDER BY o.id ASC
    """)
    fun getKitchenOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE status = 'FINALIZADO' ORDER BY id ASC")
    fun getCashierOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE waiterName = :waiterName AND status IN ('PENDIENTE', 'EN_PROCESO', 'FINALIZADO') ORDER BY id DESC")
    fun getWaiterActiveOrders(waiterName: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE waiterName = :waiterName AND status = 'PAGADO' ORDER BY paidAt DESC")
    fun getWaiterHistory(waiterName: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: Long): OrderEntity?

    @Query("""
        SELECT * FROM orders 
        WHERE (tableNumber = :tableNumber OR LOWER(TRIM(tableNumber)) = LOWER(TRIM(:tableNumber)))
          AND status IN ('PENDIENTE', 'EN_PROCESO', 'FINALIZADO', 'ABIERTO', 'POR_COBRAR', 'LISTO_PARA_COBRAR')
        ORDER BY id DESC 
        LIMIT 1
    """)
    suspend fun getActiveOrderByTable(tableNumber: String): OrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun deleteOrderById(id: Long)

    @Transaction
    suspend fun replaceOrderItems(orderId: Long, order: OrderEntity, items: List<OrderItemEntity>) {
        updateOrder(order)
        deleteOrderItemsByOrderId(orderId)
        insertOrderItems(items)
    }


    // --- ORDER ITEMS ---
    @Query("SELECT * FROM order_items WHERE id = :itemId LIMIT 1")
    suspend fun getOrderItemById(itemId: Long): OrderItemEntity?

    @Query("SELECT * FROM order_items WHERE orderId = :orderId ORDER BY id ASC")
    fun getOrderItemsFlow(orderId: Long): Flow<List<OrderItemEntity>>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId AND kitchenStatus IN ('PENDIENTE', 'EN_PROCESO', 'NUEVO') ORDER BY id ASC")
    fun getPendingKitchenOrderItemsFlow(orderId: Long): Flow<List<OrderItemEntity>>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId ORDER BY id ASC")
    suspend fun getOrderItems(orderId: Long): List<OrderItemEntity>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId AND kitchenStatus IN (:statuses) ORDER BY id ASC")
    suspend fun getOrderItemsByKitchenStatuses(orderId: Long, statuses: List<String>): List<OrderItemEntity>

    @Query("SELECT * FROM order_items")
    fun getAllOrderItems(): Flow<List<OrderItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Query("UPDATE order_items SET kitchenStatus = :kitchenStatus WHERE id = :itemId")
    suspend fun updateOrderItemKitchenStatus(itemId: Long, kitchenStatus: String)

    @Query("UPDATE order_items SET kitchenStatus = :kitchenStatus WHERE orderId = :orderId AND kitchenStatus IN ('PENDIENTE', 'EN_PROCESO', 'NUEVO')")
    suspend fun updateActiveOrderItemsKitchenStatus(orderId: Long, kitchenStatus: String)

    @Query("UPDATE order_items SET kitchenStatus = :kitchenStatus WHERE orderId = :orderId")
    suspend fun updateAllOrderItemsKitchenStatus(orderId: Long, kitchenStatus: String)

    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    suspend fun deleteOrderItemsByOrderId(orderId: Long)


    // --- SALES & HISTORY ---
    @Query("SELECT * FROM sales ORDER BY timestamp DESC")
    fun getAllSales(): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE timestamp >= :startTimestamp AND timestamp <= :endTimestamp ORDER BY timestamp DESC")
    fun getSalesByTimeRange(startTimestamp: Long, endTimestamp: Long): Flow<List<SaleEntity>>

    @Query("""
        SELECT 
            COALESCE(mi.category, 'Platillos') AS category, 
            COALESCE(SUM(oi.subtotal), 0.0) AS totalAmount,
            COALESCE(SUM(oi.quantity), 0) AS itemCount
        FROM order_items oi 
        JOIN orders o ON oi.orderId = o.id 
        LEFT JOIN menu_items mi ON oi.menuItemId = mi.id 
        WHERE o.status = 'PAGADO' AND o.paidAt >= :startTimestamp AND o.paidAt <= :endTimestamp 
        GROUP BY mi.category
    """)
    fun getCategorySalesByTimeRange(startTimestamp: Long, endTimestamp: Long): Flow<List<CategorySaleSummary>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SaleEntity): Long


    // --- MANAGER CONFIG ---
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM manager_config WHERE id = 1")
    fun getManagerConfigFlow(): Flow<ManagerConfigEntity?>

    @Query("SELECT * FROM manager_config WHERE id = 1")
    suspend fun getManagerConfig(): ManagerConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManagerConfig(config: ManagerConfigEntity)


    // --- SYSTEM SETTINGS ---
    @Query("SELECT * FROM system_settings WHERE id = 1")
    fun getSystemSettingsFlow(): Flow<SystemSettingsEntity?>

    @Query("SELECT * FROM system_settings WHERE id = 1")
    suspend fun getSystemSettings(): SystemSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSystemSettings(settings: SystemSettingsEntity)


    // --- DEVICE BINDINGS & PAIRING CODES ---
    @Query("SELECT * FROM device_bindings ORDER BY createdAt DESC")
    fun getAllDeviceBindings(): Flow<List<DeviceBindingEntity>>

    @Query("SELECT * FROM device_bindings WHERE code = :code OR code LIKE '%' || :code || '%' LIMIT 1")
    suspend fun getDeviceBindingByCode(code: String): DeviceBindingEntity?

    @Query("SELECT * FROM device_bindings")
    suspend fun getAllDeviceBindingsDirect(): List<DeviceBindingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeviceBinding(binding: DeviceBindingEntity): Long

    @Update
    suspend fun updateDeviceBinding(binding: DeviceBindingEntity)

    @Query("DELETE FROM device_bindings WHERE id = :id")
    suspend fun deleteDeviceBindingById(id: Long)


    // --- LINKED DEVICES ---
    @Query("SELECT * FROM linked_devices ORDER BY lastSeen DESC")
    fun getAllLinkedDevices(): Flow<List<LinkedDeviceEntity>>

    @Query("SELECT * FROM linked_devices WHERE deviceId = :deviceId LIMIT 1")
    suspend fun getLinkedDeviceById(deviceId: String): LinkedDeviceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkedDevice(device: LinkedDeviceEntity)

    @Query("DELETE FROM linked_devices WHERE deviceId = :deviceId")
    suspend fun deleteLinkedDeviceById(deviceId: String)


    // --- AUDIT LOGS ---
    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC LIMIT 500")
    fun getAllAuditLogs(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLogEntity): Long


    // --- OFFLINE SYNC QUEUE ---
    @Query("SELECT * FROM sync_queue ORDER BY timestamp ASC")
    suspend fun getAllSyncQueue(): List<SyncQueueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncQueue(item: SyncQueueEntity): Long

    @Query("DELETE FROM sync_queue WHERE id = :id")
    suspend fun deleteSyncQueueItem(id: Long)


    // --- DAILY CLOSES ---
    @Query("SELECT * FROM daily_closes ORDER BY timestamp DESC")
    fun getAllDailyCloses(): Flow<List<DailyCloseEntity>>

    @Query("SELECT * FROM daily_closes WHERE closeDate = :closeDate LIMIT 1")
    suspend fun getDailyCloseByDate(closeDate: String): DailyCloseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyClose(close: DailyCloseEntity): Long


    // --- JORNADA & RESET OPERATIONAL QUERIES ---
    @Query("DELETE FROM orders WHERE status IN ('FINALIZADO', 'PAGADO', 'CANCELADO')")
    suspend fun clearCompletedOrdersForNewJornada()

    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()

    @Query("DELETE FROM order_items")
    suspend fun deleteAllOrderItems()


    // --- TOTAL SYSTEM RESET QUERIES ---
    @Query("DELETE FROM sales")
    suspend fun deleteAllSales()

    @Query("DELETE FROM inventory_items")
    suspend fun deleteAllInventory()

    @Query("DELETE FROM menu_items")
    suspend fun deleteAllMenuItems()

    @Query("DELETE FROM recipe_items")
    suspend fun deleteAllRecipeItems()

    @Query("DELETE FROM inventory_movements")
    suspend fun deleteAllInventoryMovements()

    @Query("DELETE FROM daily_closes")
    suspend fun deleteAllDailyCloses()

    @Query("DELETE FROM device_bindings")
    suspend fun deleteAllDeviceBindings()

    @Query("DELETE FROM linked_devices")
    suspend fun deleteAllLinkedDevices()

    @Query("DELETE FROM audit_logs")
    suspend fun deleteAllAuditLogs()

    @Query("DELETE FROM sync_queue")
    suspend fun deleteAllSyncQueue()

    @Query("DELETE FROM users WHERE role != 'GERENTE'")
    suspend fun deleteAllUsersExceptGerente()

    // --- RESTAURANT TABLES ---
    @Query("SELECT * FROM restaurant_tables ORDER BY displayOrder ASC, id ASC")
    fun getAllTables(): Flow<List<TableEntity>>

    @Query("SELECT * FROM restaurant_tables WHERE isActive = 1 ORDER BY displayOrder ASC, id ASC")
    fun getActiveTables(): Flow<List<TableEntity>>

    @Query("SELECT * FROM restaurant_tables WHERE id = :id LIMIT 1")
    suspend fun getTableById(id: Long): TableEntity?

    @Query("SELECT * FROM restaurant_tables WHERE tableNumber = :tableNumber LIMIT 1")
    suspend fun getTableByNumber(tableNumber: String): TableEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTable(table: TableEntity): Long

    @Update
    suspend fun updateTable(table: TableEntity)

    @Query("DELETE FROM restaurant_tables WHERE id = :id")
    suspend fun deleteTableById(id: Long)

    @Query("UPDATE restaurant_tables SET status = 'Disponible', occupiedSince = NULL")
    suspend fun resetAllTableStatuses()

    @Query("DELETE FROM restaurant_tables")
    suspend fun deleteAllTables()


    // --- INVOICES (FACTURACIÓN) ---
    @Query("SELECT * FROM invoices ORDER BY timestamp DESC")
    fun getAllInvoices(): Flow<List<InvoiceEntity>>

    @Query("SELECT * FROM invoices WHERE id = :id LIMIT 1")
    suspend fun getInvoiceById(id: Long): InvoiceEntity?

    @Query("SELECT * FROM invoices WHERE orderId = :orderId LIMIT 1")
    suspend fun getInvoiceByOrderId(orderId: Long): InvoiceEntity?

    @Query("SELECT * FROM invoices WHERE timestamp >= :startTimestamp AND timestamp <= :endTimestamp ORDER BY timestamp DESC")
    fun getInvoicesByTimeRange(startTimestamp: Long, endTimestamp: Long): Flow<List<InvoiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoice(invoice: InvoiceEntity): Long

    @Update
    suspend fun updateInvoice(invoice: InvoiceEntity)

    @Query("DELETE FROM invoices")
    suspend fun deleteAllInvoices()


    // --- THEMES & BRANDING (TEMAS) ---
    @Query("SELECT * FROM themes ORDER BY updatedAt DESC")
    fun getAllThemes(): Flow<List<ThemeConfigEntity>>

    @Query("SELECT * FROM themes WHERE isActive = 1 LIMIT 1")
    fun getActiveTheme(): Flow<ThemeConfigEntity?>

    @Query("SELECT * FROM themes WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveThemeDirect(): ThemeConfigEntity?

    @Query("SELECT * FROM themes WHERE id = :id LIMIT 1")
    suspend fun getThemeById(id: Long): ThemeConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(theme: ThemeConfigEntity): Long

    @Update
    suspend fun updateTheme(theme: ThemeConfigEntity)

    @Query("UPDATE themes SET isActive = 0 WHERE id != :activeThemeId")
    suspend fun deactivateOtherThemes(activeThemeId: Long)

    @Query("DELETE FROM themes WHERE id = :id")
    suspend fun deleteThemeById(id: Long)


    // --- WEB ORDERS (PEDIDOS_WEB) ---
    @Query("SELECT * FROM web_orders ORDER BY createdAt DESC")
    fun getAllWebOrders(): Flow<List<WebOrderEntity>>

    @Query("SELECT * FROM web_orders WHERE status IN ('PENDIENTE_CONFIRMACION', 'PENDIENTE', 'Pendiente Validación') ORDER BY createdAt DESC")
    fun getPendingValidationWebOrders(): Flow<List<WebOrderEntity>>

    @Query("SELECT * FROM web_orders WHERE status IN ('PENDIENTE_CONFIRMACION', 'PENDIENTE', 'Pendiente Validación', 'EN_PREPARACION', 'En Cocina', 'Listo') ORDER BY createdAt DESC")
    fun getActiveWebOrders(): Flow<List<WebOrderEntity>>

    @Query("SELECT * FROM web_orders WHERE id = :id LIMIT 1")
    suspend fun getWebOrderById(id: Long): WebOrderEntity?

    @Query("SELECT * FROM web_orders WHERE webOrderId = :webOrderId LIMIT 1")
    suspend fun getWebOrderByCode(webOrderId: String): WebOrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWebOrder(webOrder: WebOrderEntity): Long

    @Update
    suspend fun updateWebOrder(webOrder: WebOrderEntity)

    @Query("UPDATE web_orders SET status = :status WHERE id = :id")
    suspend fun updateWebOrderStatus(id: Long, status: String)

    @Query("UPDATE web_orders SET status = :status, posOrderId = :posOrderId, validatedAt = :validatedAt WHERE id = :id")
    suspend fun validateAndLinkWebOrder(id: Long, status: String, posOrderId: Long, validatedAt: Long)

    @Query("SELECT * FROM web_orders WHERE posOrderId = :posOrderId LIMIT 1")
    suspend fun getWebOrderByPosOrderId(posOrderId: Long): WebOrderEntity?

    @Query("SELECT * FROM web_orders WHERE origin = 'Delivery/WhatsApp' OR origin LIKE '%Delivery%' OR origin LIKE '%WhatsApp%' OR deliveryAddress != '' ORDER BY createdAt DESC")
    fun getAllDeliveryOrders(): Flow<List<WebOrderEntity>>

    @Query("SELECT * FROM web_orders WHERE (origin = 'Delivery/WhatsApp' OR origin LIKE '%Delivery%' OR origin LIKE '%WhatsApp%' OR deliveryAddress != '') AND status IN ('Listo para Entrega', 'Listo', 'FINALIZADO') ORDER BY createdAt ASC")
    fun getReadyDeliveryOrders(): Flow<List<WebOrderEntity>>

    @Query("SELECT * FROM web_orders WHERE (origin = 'Delivery/WhatsApp' OR origin LIKE '%Delivery%' OR origin LIKE '%WhatsApp%' OR deliveryAddress != '') AND status IN ('En Camino', 'EN_CAMINO') ORDER BY deliveryStartedAt DESC, createdAt DESC")
    fun getInTransitDeliveryOrders(): Flow<List<WebOrderEntity>>

    @Query("SELECT * FROM web_orders WHERE (origin = 'Delivery/WhatsApp' OR origin LIKE '%Delivery%' OR origin LIKE '%WhatsApp%' OR deliveryAddress != '') AND status IN ('Entregado', 'ENTREGADO', 'PAGADO') ORDER BY deliveryFinishedAt DESC, createdAt DESC")
    fun getCompletedDeliveryOrders(): Flow<List<WebOrderEntity>>

    @Query("SELECT * FROM web_orders WHERE (origin = 'Delivery/WhatsApp' OR origin LIKE '%Delivery%' OR origin LIKE '%WhatsApp%' OR deliveryAddress != '') AND status = 'INCIDENCIA' ORDER BY createdAt DESC")
    fun getIncidentDeliveryOrders(): Flow<List<WebOrderEntity>>

    @Query("UPDATE web_orders SET status = :status, deliveryDriverName = :driverName, deliveryStartedAt = :startedAt, deliveryFinishedAt = :finishedAt, notes = CASE WHEN :notes IS NOT NULL THEN :notes ELSE notes END WHERE id = :id")
    suspend fun updateDeliveryOrderStatus(id: Long, status: String, driverName: String, startedAt: Long?, finishedAt: Long?, notes: String?)

    @Query("UPDATE web_orders SET status = :status, deliveryDriverName = :driverName, deliveryIssueNote = :incidentNote WHERE id = :id")
    suspend fun updateDeliveryIncident(id: Long, status: String, incidentNote: String, driverName: String)

    @Query("DELETE FROM web_orders WHERE id = :id")
    suspend fun deleteWebOrderById(id: Long)


    // --- DELIVERY SETTLEMENTS (LIQUIDACIONES CAJA) ---
    @Query("SELECT * FROM delivery_settlements ORDER BY completedAt DESC")
    fun getAllDeliverySettlements(): Flow<List<DeliverySettlementEntity>>

    @Query("SELECT * FROM delivery_settlements WHERE completedAt >= :startTimestamp ORDER BY completedAt DESC")
    fun getTodayDeliverySettlements(startTimestamp: Long): Flow<List<DeliverySettlementEntity>>

    @Query("SELECT * FROM delivery_settlements WHERE driverName = :driverName ORDER BY completedAt DESC")
    fun getDeliverySettlementsByDriver(driverName: String): Flow<List<DeliverySettlementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeliverySettlement(settlement: DeliverySettlementEntity): Long

    @Query("DELETE FROM delivery_settlements WHERE id = :id")
    suspend fun deleteDeliverySettlementById(id: Long)
}
