package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.RestaurantDao
import com.example.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        MenuItemEntity::class,
        InventoryItemEntity::class,
        RecipeItemEntity::class,
        InventoryMovementEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        SaleEntity::class,
        ManagerConfigEntity::class,
        DeviceBindingEntity::class,
        LinkedDeviceEntity::class,
        SystemSettingsEntity::class,
        AuditLogEntity::class,
        SyncQueueEntity::class,
        DailyCloseEntity::class,
        TableEntity::class,
        InvoiceEntity::class,
        ThemeConfigEntity::class,
        WebOrderEntity::class,
        DeliverySettlementEntity::class
    ],
    version = 14,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "restaurante_q_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.restaurantDao())
                }
            }
        }

        private suspend fun populateInitialData(dao: RestaurantDao) {
            // Seed Default Multi-User Staff
            val defaultUsers = listOf(
                UserEntity(name = "Carlos López", role = "MESERO"),
                UserEntity(name = "María Gómez", role = "MESERO"),
                UserEntity(name = "Pedro Martínez", role = "MESERO"),
                UserEntity(name = "Lucía Morales", role = "MESERO"),
                UserEntity(name = "Ana Rivas", role = "CAJA"),
                UserEntity(name = "Juan Pérez", role = "CAJA"),
                UserEntity(name = "Héctor Soto", role = "REPARTIDOR"),
                UserEntity(name = "Luis Morales", role = "REPARTIDOR"),
                UserEntity(name = "Cocina Central", role = "COCINA"),
                UserEntity(name = "Cocina Parrilla", role = "COCINA"),
                UserEntity(name = "Gerente Principal", role = "GERENTE", pin = "1234")
            )
            for (user in defaultUsers) {
                dao.insertUser(user)
            }

            // Seed System Settings (Default Azul Rivera)
            dao.insertSystemSettings(
                SystemSettingsEntity(
                    id = 1,
                    restaurantName = "Restaurante Rivera",
                    branchName = "Sucursal Central",
                    themePalette = "AZUL_RIVERA",
                    currencySymbol = "Q",
                    website = "https://riveraga01-cmd.github.io/Restaurante/",
                    managerPin = "1234"
                )
            )

            // Seed Default Theme for Web & POS Branding
            dao.insertTheme(
                ThemeConfigEntity(
                    id = 1,
                    themeName = "Azul Rivera Clásico",
                    primaryColorHex = "#1E3A8A",
                    secondaryColorHex = "#D97706",
                    bannerImageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&q=80",
                    welcomeMessage = "¡Bienvenidos a Restaurante Rivera! Disfruta la mejor gastronomía guatemalteca.",
                    isActive = true
                )
            )
            dao.insertTheme(
                ThemeConfigEntity(
                    id = 2,
                    themeName = "Dorado Chapín Gourmet",
                    primaryColorHex = "#92400E",
                    secondaryColorHex = "#F59E0B",
                    bannerImageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&q=80",
                    welcomeMessage = "Sabor, tradición y excelencia culinaria en cada platillo.",
                    isActive = false
                )
            )

            // Seed Initial Tables (All strictly 'Disponible' / Libre)
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

            // Seed Device Pairing Codes
            val now = System.currentTimeMillis()
            dao.insertDeviceBinding(
                DeviceBindingEntity(
                    code = "RIVERA-7892-X",
                    branchName = "Sucursal Central",
                    assignedRole = "MESERO",
                    expiresAt = now + 86400000L * 30, // 30 days
                    isMultiUse = true,
                    createdByUser = "Gerente Principal"
                )
            )
            dao.insertDeviceBinding(
                DeviceBindingEntity(
                    code = "RIVERA-5510-K",
                    branchName = "Sucursal Central",
                    assignedRole = "COCINA",
                    expiresAt = now + 86400000L * 30,
                    isMultiUse = true,
                    createdByUser = "Gerente Principal"
                )
            )

            // Seed Linked Device
            dao.insertLinkedDevice(
                LinkedDeviceEntity(
                    deviceId = "DEV-RIVERA-01",
                    deviceName = "Terminal Pos Principal",
                    branchName = "Sucursal Central",
                    assignedRole = "GERENTE",
                    linkedUser = "Gerente Principal",
                    linkedAt = now,
                    lastSeen = now
                )
            )

            // Seed Audit Log
            dao.insertAuditLog(
                AuditLogEntity(
                    user = "Sistema Rivera",
                    role = "SISTEMA",
                    deviceId = "DEV-RIVERA-01",
                    action = "INICIALIZACION",
                    details = "Sistema inicializado en limpio con mesas libres y catálogo preparado."
                )
            )

            // Seed Menu
            val defaultMenu = listOf(
                MenuItemEntity(name = "Puyaso Chapín (12oz)", category = "Platillos", price = 95.00, description = "Corte tierno servido con papas, guacamol y frijoles"),
                MenuItemEntity(name = "Pepían de Pollo Tradicional", category = "Platillos", price = 75.00, description = "Receta ancestral con recado de semillas tostadas y arroz"),
                MenuItemEntity(name = "Churrasco Típico Con Guacamol", category = "Platillos", price = 85.00, description = "Carne asada con chirmol, guacamol, cebollines y frijol"),
                MenuItemEntity(name = "Chuchitos Chapines (3 uds)", category = "Platillos", price = 35.00, description = "Rellenos de pollo/cerdo con salsa de tomate y queso seco"),
                MenuItemEntity(name = "Desayuno Típico Completo", category = "Platillos", price = 45.00, description = "Huevos al gusto, frijoles volteados, plátanos y queso"),
                
                MenuItemEntity(name = "Horchata Artesanal (Vaso)", category = "Bebidas", price = 18.00, description = "Bebida fría de arroz y canela"),
                MenuItemEntity(name = "Atol de Elote Caliente", category = "Bebidas", price = 20.00, description = "Tradicional de maíz dulce con canela"),
                MenuItemEntity(name = "Jarra de Rosa de Jamaica", category = "Bebidas", price = 38.00, description = "1 Litro de refresco natural"),
                MenuItemEntity(name = "Cerveza Gallo Nacional", category = "Bebidas", price = 25.00, description = "350ml fría"),
                MenuItemEntity(name = "Café de Altura Cobán", category = "Bebidas", price = 15.00, description = "Taza de café recién pasado"),

                MenuItemEntity(name = "Rellenitos de Plátano (2 uds)", category = "Postres", price = 25.00, description = "Plátano frito relleno de frijol dulce con azúcar"),
                MenuItemEntity(name = "Pie de Queso Casero", category = "Postres", price = 28.00, description = "Rebanada cremosa con mermelada de fresa"),
                MenuItemEntity(name = "Flan de Leche Condensada", category = "Postres", price = 22.00, description = "Caramelo suave casero"),

                MenuItemEntity(name = "Guacamol con Totopos", category = "Entradas", price = 32.00, description = "Aguacate fresco preparado con totopos crujientes"),
                MenuItemEntity(name = "Sopa de Frijol con Crema", category = "Entradas", price = 30.00, description = "Acompañada de queso seco y chicharrones")
            )
            val menuIds = mutableListOf<Long>()
            for (item in defaultMenu) {
                val id = dao.insertMenuItem(item)
                menuIds.add(id)
            }

            // Seed Ingredients (BOM)
            val ing1 = dao.insertInventory(InventoryItemEntity(productName = "Carne de Res (Churrasco)", currentStock = 20.0, minStock = 5.0, idealStock = 30.0, unitCost = 35.0, supplier = "Carnes El Arreo", unit = "lb"))
            val ing2 = dao.insertInventory(InventoryItemEntity(productName = "Corte Puyaso 12oz", currentStock = 25.0, minStock = 8.0, idealStock = 35.0, unitCost = 45.0, supplier = "Carnes El Arreo", unit = "lb"))
            val ing3 = dao.insertInventory(InventoryItemEntity(productName = "Pollo Entero Fresco", currentStock = 18.0, minStock = 6.0, idealStock = 25.0, unitCost = 18.0, supplier = "Avícola San Jorge", unit = "lb"))
            val ing4 = dao.insertInventory(InventoryItemEntity(productName = "Arroz Blanco", currentStock = 8000.0, minStock = 2000.0, idealStock = 12000.0, unitCost = 0.015, supplier = "Granos de Guatemala", unit = "g"))
            val ing5 = dao.insertInventory(InventoryItemEntity(productName = "Frijol Negro", currentStock = 6000.0, minStock = 2500.0, idealStock = 10000.0, unitCost = 0.018, supplier = "Granos de Guatemala", unit = "g"))
            val ing6 = dao.insertInventory(InventoryItemEntity(productName = "Aguacates Frescos", currentStock = 25.0, minStock = 15.0, idealStock = 35.0, unitCost = 3.50, supplier = "Frutas del Valle", unit = "unidad"))
            val ing7 = dao.insertInventory(InventoryItemEntity(productName = "Cebollín", currentStock = 400.0, minStock = 100.0, idealStock = 800.0, unitCost = 0.05, supplier = "Verduras Frescas", unit = "g"))
            val ing8 = dao.insertInventory(InventoryItemEntity(productName = "Chimichurri Casero", currentStock = 1200.0, minStock = 300.0, idealStock = 2000.0, unitCost = 0.08, supplier = "Salsas Doña Ana", unit = "g"))
            val ing9 = dao.insertInventory(InventoryItemEntity(productName = "Ajo Picado", currentStock = 300.0, minStock = 200.0, idealStock = 600.0, unitCost = 0.04, supplier = "Verduras Frescas", unit = "g"))
            val ing10 = dao.insertInventory(InventoryItemEntity(productName = "Tomate Rojo", currentStock = 3500.0, minStock = 1000.0, idealStock = 6000.0, unitCost = 0.012, supplier = "Verduras Frescas", unit = "g"))
            val ing11 = dao.insertInventory(InventoryItemEntity(productName = "Cebolla Blanca", currentStock = 2800.0, minStock = 800.0, idealStock = 4000.0, unitCost = 0.01, supplier = "Verduras Frescas", unit = "g"))
            val ing12 = dao.insertInventory(InventoryItemEntity(productName = "Aceite Vegetal", currentStock = 5000.0, minStock = 1500.0, idealStock = 8000.0, unitCost = 0.02, supplier = "Aceites Rivera", unit = "ml"))
            val ing13 = dao.insertInventory(InventoryItemEntity(productName = "Sal y Condimentos", currentStock = 1500.0, minStock = 500.0, idealStock = 3000.0, unitCost = 0.02, supplier = "Especias del Campo", unit = "g"))
            val ing14 = dao.insertInventory(InventoryItemEntity(productName = "Plátanos Maduros", currentStock = 20.0, minStock = 10.0, idealStock = 30.0, unitCost = 2.0, supplier = "Frutas del Valle", expirationDate = System.currentTimeMillis() + 86400000L * 7, unit = "unidad"))
            val ing15 = dao.insertInventory(InventoryItemEntity(productName = "Leche Entera", currentStock = 10.0, minStock = 8.0, idealStock = 16.0, unitCost = 12.0, supplier = "Lácteos Xela", unit = "L"))

            // Seed Recipes (Bill of Materials) for Menu Items
            val puyasoId = menuIds.getOrNull(0) ?: 1L
            val pepianId = menuIds.getOrNull(1) ?: 2L
            val churrascoId = menuIds.getOrNull(2) ?: 3L
            val chuchitosId = menuIds.getOrNull(3) ?: 4L
            val desayunoId = menuIds.getOrNull(4) ?: 5L
            val guacamolId = menuIds.getOrNull(13) ?: 14L

            // Churrasco Recipe
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = churrascoId, ingredientId = ing1, ingredientName = "Carne de Res (Churrasco)", quantityRequired = 0.25, unit = "lb"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = churrascoId, ingredientId = ing4, ingredientName = "Arroz Blanco", quantityRequired = 150.0, unit = "g"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = churrascoId, ingredientId = ing5, ingredientName = "Frijol Negro", quantityRequired = 100.0, unit = "g"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = churrascoId, ingredientId = ing6, ingredientName = "Aguacates Frescos", quantityRequired = 0.5, unit = "unidad"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = churrascoId, ingredientId = ing7, ingredientName = "Cebollín", quantityRequired = 10.0, unit = "g"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = churrascoId, ingredientId = ing8, ingredientName = "Chimichurri Casero", quantityRequired = 30.0, unit = "g"))

            // Puyaso Recipe
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = puyasoId, ingredientId = ing2, ingredientName = "Corte Puyaso 12oz", quantityRequired = 0.75, unit = "lb"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = puyasoId, ingredientId = ing5, ingredientName = "Frijol Negro", quantityRequired = 100.0, unit = "g"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = puyasoId, ingredientId = ing6, ingredientName = "Aguacates Frescos", quantityRequired = 0.5, unit = "unidad"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = puyasoId, ingredientId = ing8, ingredientName = "Chimichurri Casero", quantityRequired = 40.0, unit = "g"))

            // Pepián Recipe
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = pepianId, ingredientId = ing3, ingredientName = "Pollo Entero Fresco", quantityRequired = 0.5, unit = "lb"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = pepianId, ingredientId = ing4, ingredientName = "Arroz Blanco", quantityRequired = 150.0, unit = "g"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = pepianId, ingredientId = ing10, ingredientName = "Tomate Rojo", quantityRequired = 120.0, unit = "g"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = pepianId, ingredientId = ing11, ingredientName = "Cebolla Blanca", quantityRequired = 40.0, unit = "g"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = pepianId, ingredientId = ing9, ingredientName = "Ajo Picado", quantityRequired = 10.0, unit = "g"))

            // Desayuno Recipe
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = desayunoId, ingredientId = ing5, ingredientName = "Frijol Negro", quantityRequired = 120.0, unit = "g"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = desayunoId, ingredientId = ing14, ingredientName = "Plátanos Maduros", quantityRequired = 1.0, unit = "unidad"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = desayunoId, ingredientId = ing12, ingredientName = "Aceite Vegetal", quantityRequired = 30.0, unit = "ml"))

            // Guacamol Recipe
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = guacamolId, ingredientId = ing6, ingredientName = "Aguacates Frescos", quantityRequired = 2.0, unit = "unidad"))
            dao.insertRecipeItem(RecipeItemEntity(menuItemId = guacamolId, ingredientId = ing11, ingredientName = "Cebolla Blanca", quantityRequired = 20.0, unit = "g"))

            // Note: Orders, active orders, and sales history remain strictly 0 / empty on clean initialization!
        }
    }
}
