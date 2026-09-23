package com.example.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val role: String, // MESERO, COCINA, CAJA, GERENTE
    val pin: String = "",
    val email: String = "",
    val password: String = "123456",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val lastAccess: Long = System.currentTimeMillis(),
    val linkedDevice: String = "Dispositivo Principal",
    val branchName: String = "Sucursal Central"
)

@Entity(tableName = "device_bindings")
data class DeviceBindingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val code: String, // e.g. RIVERA-8923-X
    val branchName: String = "Sucursal Central",
    val assignedRole: String = "MESERO",
    val expiresAt: Long,
    val isMultiUse: Boolean = false,
    val usedCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val createdByUser: String = "Gerente Principal"
)

@Entity(tableName = "linked_devices")
data class LinkedDeviceEntity(
    @PrimaryKey val deviceId: String, // e.g. DEV-82391
    val deviceName: String,
    val branchName: String = "Sucursal Central",
    val assignedRole: String = "MESERO",
    val linkedUser: String = "Sin Asignar",
    val linkedAt: Long = System.currentTimeMillis(),
    val lastSeen: Long = System.currentTimeMillis(),
    val isBlocked: Boolean = false
)

@Entity(tableName = "system_settings")
data class SystemSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val restaurantName: String = "Restaurante Rivera",
    val branchName: String = "Sucursal Central",
    val taxId: String = "", // Unused/Eliminated
    val address: String = "Zona 1, Ciudad de Guatemala",
    val phone: String = "+502 2345-6789",
    val logoUri: String = "ic_restaurant",
    val themePalette: String = "AZUL_RIVERA",
    val isDarkMode: Boolean = false,
    val currencySymbol: String = "Q",
    val language: String = "Español",
    val timezone: String = "America/Guatemala",
    val dateTimeFormat: String = "dd/MM/yyyy HH:mm",
    val startScreenDefault: String = "INICIO",
    val taxPercent: Double = 0.0, // Impuestos eliminados
    val defaultTipPercent: Double = 10.0,
    val allowDiscounts: Boolean = true,
    val allowTableSplitting: Boolean = true,
    val enableDiscounts: Boolean = true,
    val autoDeductRecipeOnPayment: Boolean = true,
    val requireMermaReason: Boolean = true,
    val paymentMethodsJson: String = "Efectivo,Tarjeta,Transferencia",
    val kitchenPrinterIp: String = "192.168.1.100",
    val cashierPrinterIp: String = "192.168.1.101",
    val paperWidthMm: Int = 80,
    val printCopies: Int = 1,
    val inventoryAlertsEnabled: Boolean = true,
    val orderAlertsEnabled: Boolean = true,
    val syncAlertsEnabled: Boolean = true,
    val cashAlertsEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val notificationSoundEnabled: Boolean = true,
    val notificationVibrationEnabled: Boolean = true,
    val notificationRingtoneUri: String = "",
    val notificationRingtoneTitle: String = "Tono Predeterminado del Sistema",
    val kitchenSoundEnabled: Boolean = true,
    val kitchenRingtoneUri: String = "",
    val kitchenRingtoneTitle: String = "Tono de Cocina Predeterminado",
    val digitalOrderAlertsEnabled: Boolean = true,
    val lowStockAlertsEnabled: Boolean = true,
    val deliveryAlertsEnabled: Boolean = true,
    val notificationVolume: Float = 1.0f,
    val managerPin: String = "1234",
    val email: String = "contacto@restauranterivera.com",
    val footerMessage: String = "Gracias por su visita. Esperamos atenderle nuevamente.",
    val nextInvoiceNumber: Long = 1001,

    // Personalizar Factura - Switches de la Plantilla
    val showLogo: Boolean = true,
    val showRestaurantName: Boolean = true,
    val showAddress: Boolean = true,
    val showPhone: Boolean = true,
    val showEmail: Boolean = true,
    val showInvoiceNumber: Boolean = true,
    val showOrderNumber: Boolean = true,
    val showDate: Boolean = true,
    val showTime: Boolean = true,
    val showCashierName: Boolean = true,
    val showPaymentMethod: Boolean = true,
    val showCustomerType: Boolean = true,
    val showCustomerPhone: Boolean = true,
    val showQuantity: Boolean = true,
    val showProductName: Boolean = true,
    val showUnitPrice: Boolean = true,
    val showSubtotal: Boolean = true,
    val showTotal: Boolean = true,
    val showFooterMessage: Boolean = true,
    val showPrintTimestamp: Boolean = true,
    val showTaxId: Boolean = true,
    val showBranchName: Boolean = true,
    val showCustomerNit: Boolean = true,
    val showLegalNotice: Boolean = true,
    val showTipLine: Boolean = false,
    val showTaxBreakdown: Boolean = false,
    val legalNotice: String = "Resolución SAT No. 2026-10-9988 • Sujeto a Pagos Trimestrales ISR",
    val ticketDividerStyle: String = "----",

    // Ampliación de Datos del Restaurante
    val municipality: String = "Guatemala",
    val department: String = "Guatemala",
    val country: String = "Guatemala",
    val postalCode: String = "01001",
    val phoneSecondary: String = "+502 2345-6790",
    val whatsapp: String = "+502 5555-1234",
    val website: String = "https://riveraga01-cmd.github.io/Restaurante/",
    val facebook: String = "@RestauranteRiveraGT",
    val instagram: String = "@restaurante_rivera_gt",
    val openingHours: String = "Lunes a Domingo: 07:00 - 22:00",

    // Boletos / Tickets adicionales
    val ticketThanksMessage: String = "¡Muchas gracias por su preferencia!",
    val ticketPrefix: String = "TICK-",
    val showWaiterName: Boolean = true,
    val showTableNumber: Boolean = true,
    val showNotes: Boolean = true,
    val showQrCode: Boolean = true,
    val ticketElementsOrderJson: String = "Logo,Encabezado,DetallesOrden,Items,Totales,MensajePie,QR",

    // Configuración adicional de Impresoras
    val barPrinterIp: String = "192.168.1.102",
    val printerConnectionType: String = "Wi-Fi", // Bluetooth, Wi-Fi, USB

    // Configuración de Mesas e Inventario
    val qrOrderingEnabled: Boolean = true,
    val defaultTableCapacity: Int = 4,
    val defaultTableStatus: String = "Disponible",
    val stockUnitDefault: String = "Unidad",
    val stockMinDefault: Double = 10.0,
    val stockCriticalDefault: Double = 3.0,
    val taxesEnabled: Boolean = false,

    // Seguridad e Inactividad
    val inactivityTimeoutMinutes: Int = 15,
    val extraAuthEnabled: Boolean = false,
    val autoLockEnabled: Boolean = true,

    // Personalización Visual
    val primaryColorHex: String = "#1E3A8A",
    val secondaryColorHex: String = "#D97706",
    val fontSizeScale: String = "Normal", // Normal, Grande, Muy Grande
    val buttonSizeScale: String = "Normal", // Normal, Grande
    val backgroundImageUri: String = "",

    // Horarios y Control Operativo Web
    val webMasterStatus: String = "AUTOMATICO", // "ABIERTO", "AUTOMATICO", "CERRADO"
    val webIs24Hours: Boolean = false,
    val webOpenHour: String = "08:00",
    val webCloseHour: String = "22:30",
    val webActiveDays: String = "LUN,MAR,MIE,JUE,VIE,SAB,DOM",
    val webClosedMessage: String = "Nuestro horario de atención es de 08:00 a 22:30. Los pedidos se reanudarán al abrir."
)

@Entity(tableName = "restaurant_tables")
data class TableEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tableNumber: String, // e.g., "Mesa 1", "Mesa 2", "VIP 1"
    val capacity: Int = 4,
    val status: String = "Disponible", // "Disponible", "Ocupada", "Reservada", "En limpieza"
    val isActive: Boolean = true,
    val occupiedSince: Long? = null,
    val reservedName: String? = null,
    val reservedTime: String? = null,
    val displayOrder: Int = 0
)

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val invoiceNumber: String, // e.g., "FAC-001001"
    val orderId: Long,
    val orderNumber: String, // e.g., "PED-101"
    val cashierName: String,
    val paymentMethod: String, // Efectivo, Tarjeta, Transferencia
    val customerType: String = "Consumidor Final", // Consumidor Final, Clientes Varios, Cliente Registrado
    val customerName: String = "Consumidor Final",
    val customerNit: String = "",
    val customerPhone: String = "",
    val subtotal: Double,
    val discount: Double = 0.0,
    val totalAmount: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val restaurantName: String = "Restaurante Rivera",
    val branchName: String = "Sucursal Central",
    val restaurantTaxId: String = "1234567-8",
    val restaurantAddress: String = "Zona 1, Ciudad de Guatemala",
    val restaurantPhone: String = "+502 2345-6789",
    val restaurantEmail: String = "contacto@restauranterivera.com",
    val footerMessage: String = "Gracias por su visita. Esperamos atenderle nuevamente.",
    val legalNotice: String = "Resolución SAT No. 2026-10-9988 • Sujeto a Pagos Trimestrales ISR",
    val paperWidthMm: Int = 80,
    val logoUri: String = "ic_restaurant",
    val currencySymbol: String = "Q",
    val waiterName: String = "",
    val tableNumber: String = "",
    val ticketDividerStyle: String = "----",

    // Snapshot of invoice template switches at creation
    val showLogo: Boolean = true,
    val showRestaurantName: Boolean = true,
    val showBranchName: Boolean = true,
    val showTaxId: Boolean = true,
    val showAddress: Boolean = true,
    val showPhone: Boolean = true,
    val showEmail: Boolean = true,
    val showInvoiceNumber: Boolean = true,
    val showOrderNumber: Boolean = true,
    val showDate: Boolean = true,
    val showTime: Boolean = true,
    val showCashierName: Boolean = true,
    val showWaiterName: Boolean = true,
    val showTableNumber: Boolean = true,
    val showPaymentMethod: Boolean = true,
    val showCustomerType: Boolean = true,
    val showCustomerNit: Boolean = true,
    val showCustomerPhone: Boolean = true,
    val showQuantity: Boolean = true,
    val showProductName: Boolean = true,
    val showUnitPrice: Boolean = true,
    val showSubtotal: Boolean = true,
    val showTotal: Boolean = true,
    val showTaxBreakdown: Boolean = false,
    val showTipLine: Boolean = false,
    val showLegalNotice: Boolean = true,
    val showFooterMessage: Boolean = true,
    val showQrCode: Boolean = true,
    val showPrintTimestamp: Boolean = true
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val user: String,
    val role: String,
    val deviceId: String,
    val action: String,
    val details: String,
    val settingModified: String = "",
    val previousValue: String = "",
    val newValue: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val collectionName: String,
    val entityId: String,
    val actionType: String, // SAVE, DELETE
    val payloadJson: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_closes")
data class DailyCloseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val folio: String = "", // e.g. CLO-20260728-000001
    val closeDate: String, // e.g. "2026-07-28"
    val totalSales: Double,
    val totalOrders: Int,
    val closedBy: String,
    val role: String = "GERENTE",
    val deviceId: String = "DEV-RIVERA-POS1",
    val branchName: String = "Sucursal Central",
    val summaryJson: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "menu_items")
data class MenuItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String, // Platillos, Bebidas, Postres, Entradas
    val price: Double, // Price in Quetzales (Q)
    val description: String = "",
    val imageUrl: String = "",
    val isAvailable: Boolean = true, // disponible / agotado
    val isVisibleWeb: Boolean = true // público / oculto
)

@Entity(tableName = "inventory_items")
data class InventoryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val menuItemId: Long? = null,
    val productName: String,
    val currentStock: Double,
    val minStock: Double,
    val idealStock: Double = minStock * 2.5,
    val unitCost: Double = 0.0,
    val supplier: String = "Distribuidora Rivera",
    val expirationDate: Long? = null,
    val unit: String // g, kg, lb, ml, L, unidad
)

@Entity(
    tableName = "recipe_items",
    indices = [Index(value = ["menuItemId"]), Index(value = ["ingredientId"])]
)
data class RecipeItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val menuItemId: Long,
    val ingredientId: Long,
    val ingredientName: String,
    val quantityRequired: Double,
    val unit: String
)

@Entity(tableName = "inventory_movements")
data class InventoryMovementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ingredientId: Long,
    val ingredientName: String,
    val type: String, // COMPRA, MERMA, VENCIMIENTO, CONSUMO_INTERNO, AJUSTE, VENTA_AUTOMATICA
    val quantity: Double,
    val reason: String,
    val user: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderNumber: String, // e.g. PED-101
    val tableNumber: String, // e.g. Mesa 1, Mesa 2, Para Llevar, Delivery
    val waiterName: String,
    val status: String, // PENDIENTE, EN_PROCESO, FINALIZADO, PAGADO, CANCELADO, EN_CAMINO, ENTREGADO, INCIDENCIA
    val totalAmount: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val paidAt: Long? = null,
    val paymentMethod: String? = null, // Efectivo, Tarjeta, Transferencia
    val cashierName: String? = null,
    val generalNotes: String? = null,
    val deliveryAddress: String? = null,
    val deliveryDriverName: String? = null,
    val deliveryStartedAt: Long? = null,
    val deliveryFinishedAt: Long? = null,
    val deliveryIssueNote: String? = null
)

@Entity(
    tableName = "order_items",
    indices = [Index(value = ["orderId"])]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val menuItemId: Long,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int,
    val subtotal: Double,
    val notes: String = "",
    val kitchenStatus: String = "PENDIENTE", // "PENDIENTE", "EN_PROCESO", "FINALIZADO", "ENTREGADO"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val orderNumber: String,
    val cashierName: String,
    val total: Double,
    val paymentMethod: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "manager_config")
data class ManagerConfigEntity(
    @PrimaryKey val id: Int = 1,
    val pin: String = "1234"
)

data class CategorySaleSummary(
    val category: String,
    val totalAmount: Double,
    val itemCount: Int = 0
)

@Entity(tableName = "themes")
data class ThemeConfigEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val themeName: String = "Tema Rivera Principal",
    val primaryColorHex: String = "#1E3A8A",
    val secondaryColorHex: String = "#D97706",
    val bannerImageUrl: String = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&q=80",
    val welcomeMessage: String = "¡Bienvenidos a Restaurante Rivera! Descubre la auténtica gastronomía guatemalteca.",
    val isActive: Boolean = true, // activo / inactivo
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "web_orders")
data class WebOrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val webOrderId: String = "", // e.g. QR_MESA_1-2026, WA-502-8812
    val origin: String = "QR_Mesa_1", // QR_Mesa_X, WhatsApp, Web_Menu, Delivery/WhatsApp
    val tableNumber: String = "Mesa 1",
    val customerName: String = "Cliente Web",
    val customerPhone: String = "",
    val itemsJson: String = "[]", // Serialized list of items
    val totalAmount: Double = 0.0, // En Quetzales (Q)
    val status: String = "Pendiente Validación", // Pendiente Validación, En Cocina, Listo para Entrega, En Camino, Entregado, INCIDENCIA, Cancelado
    val paymentMethod: String = "Efectivo al recibir",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val validatedAt: Long? = null,
    val posOrderId: Long? = null,
    val deliveryAddress: String = "",
    val deliveryDriverName: String = "",
    val deliveryStartedAt: Long? = null,
    val deliveryFinishedAt: Long? = null,
    val deliveryIssueNote: String = ""
)

@Entity(tableName = "delivery_settlements")
data class DeliverySettlementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val webOrderId: String,
    val posOrderId: Long? = null,
    val customerName: String,
    val customerPhone: String = "",
    val deliveryAddress: String = "",
    val driverName: String,
    val totalAmount: Double,
    val paymentMethod: String,
    val completedAt: Long = System.currentTimeMillis(),
    val settlementStatus: String = "LIQUIDADO"
)

data class WebOrderItem(
    val id: Long = 0,
    val menuItemId: Long,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int,
    val subtotal: Double,
    val notes: String = ""
)
