package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.InventoryItemEntity
import com.example.data.entity.MenuItemEntity
import com.example.ui.components.AuditLogsAdminView
import com.example.ui.components.CategorySalesBarChart
import com.example.ui.components.CompletedOrdersHistoryView
import com.example.ui.components.DailyFinancialSummariesView
import com.example.ui.components.DetailedSalesReportView
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.InventoryManagerView
import com.example.ui.components.InvoiceManagerView
import com.example.ui.components.MenuVisualEditorView
import com.example.ui.components.ModuleTopBar
import com.example.ui.components.MonthlySalesBarChart
import com.example.ui.components.StatusBadge
import com.example.ui.components.UserManagementAdminView
import com.example.ui.components.WebThemeCustomizerView
import com.example.ui.components.formatQuetzales
import com.example.ui.theme.*
import com.example.ui.viewmodel.ManagerTab
import com.example.ui.viewmodel.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GerenteScreen(
    viewModel: RestaurantViewModel,
    onBackToInicio: () -> Unit
) {
    val managerTab by viewModel.managerTab.collectAsState()
    val allMenuItems by viewModel.allMenuItems.collectAsState()
    val allInventory by viewModel.allInventory.collectAsState()
    val lowStockInventory by viewModel.lowStockInventory.collectAsState()
    val allRecipeItems by viewModel.allRecipeItems.collectAsState()
    val allInventoryMovements by viewModel.allInventoryMovements.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val salesPeriod by viewModel.salesPeriod.collectAsState()
    val filteredSales by viewModel.filteredSales.collectAsState()
    val categorySales by viewModel.categorySalesForPeriod.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val allDailyCloses by viewModel.allDailyCloses.collectAsState()
    val allSales by viewModel.allSales.collectAsState()
    val allThemes by viewModel.allThemes.collectAsState()
    val activeTheme by viewModel.activeTheme.collectAsState()
    val systemSettings by viewModel.systemSettings.collectAsState()
    val isFirestoreSyncActive by viewModel.isFirestoreSyncActive.collectAsState()
    val syncStatusLabel by viewModel.syncStatusLabel.collectAsState()
    val lastSyncTimestamp by viewModel.lastSyncTimestamp.collectAsState()
    val syncError by viewModel.syncError.collectAsState()

    val detailedReport by viewModel.detailedSalesReport.collectAsState()
    val reportCategoryFilter by viewModel.reportCategoryFilter.collectAsState()
    val reportSearchQuery by viewModel.reportSearchQuery.collectAsState()
    val reportWaiterFilter by viewModel.reportWaiterFilter.collectAsState()
    val reportSortOption by viewModel.reportSortOption.collectAsState()

    val waitersList = remember(allUsers, allOrders) {
        val fromUsers = allUsers.filter { it.role.equals("MESERO", ignoreCase = true) }.map { it.name }
        val fromOrders = allOrders.map { it.waiterName }
        (fromUsers + fromOrders).filter { it.isNotBlank() }.distinct().sorted()
    }

    var showAddEditInventoryDialog by remember { mutableStateOf<InventoryItemEntity?>(null) }
    var isNewInventoryItem by remember { mutableStateOf(false) }

    var showAddEditUserDialog by remember { mutableStateOf<com.example.data.entity.UserEntity?>(null) }
    var isNewUser by remember { mutableStateOf(false) }

    var showChangePinDialog by remember { mutableStateOf(false) }

    androidx.activity.compose.BackHandler(enabled = true) {
        if (showAddEditInventoryDialog != null) {
            showAddEditInventoryDialog = null
        } else if (showAddEditUserDialog != null) {
            showAddEditUserDialog = null
        } else if (showChangePinDialog) {
            showChangePinDialog = false
        } else if (managerTab != ManagerTab.MENU) {
            viewModel.setManagerTab(ManagerTab.MENU)
        } else {
            onBackToInicio()
        }
    }

    val totalPeriodSales = filteredSales.sumOf { it.total }
    val avgTicket = if (filteredSales.isNotEmpty()) totalPeriodSales / filteredSales.size else 0.0

    Scaffold(
        topBar = {
            ModuleTopBar(
                title = "Módulo Gerente",
                subtitle = "Control total: Menú, Precios, Inventario & Reportes",
                onBackClick = onBackToInicio,
                isSyncActive = isFirestoreSyncActive,
                syncStatusLabel = syncStatusLabel,
                onSyncClick = { viewModel.triggerManualSync() },
                actions = {
                    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
                    IconButton(onClick = { viewModel.toggleDarkTheme() }) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Cambiar Tema",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        viewModel.lockManager()
                        onBackToInicio()
                    }) {
                        Icon(Icons.Default.Lock, contentDescription = "Bloquear Módulo Gerente", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Manager Tabs
            ScrollableTabRow(
                selectedTabIndex = managerTab.ordinal,
                edgePadding = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = managerTab == ManagerTab.MENU,
                    onClick = { viewModel.setManagerTab(ManagerTab.MENU) },
                    text = { Text("🍔 Menú & Precios") }
                )
                Tab(
                    selected = managerTab == ManagerTab.TEMAS_WEB,
                    onClick = { viewModel.setManagerTab(ManagerTab.TEMAS_WEB) },
                    text = { Text("🎨 Personalización Web") }
                )
                Tab(
                    selected = managerTab == ManagerTab.INVENTARIO,
                    onClick = { viewModel.setManagerTab(ManagerTab.INVENTARIO) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📦 Inventario")
                            if (lowStockInventory.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFDC2626)
                                ) {
                                    Text(
                                        text = "${lowStockInventory.size}",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                )
                Tab(
                    selected = managerTab == ManagerTab.FACTURACION,
                    onClick = { viewModel.setManagerTab(ManagerTab.FACTURACION) },
                    text = { Text("🧾 Facturación") }
                )
                Tab(
                    selected = managerTab == ManagerTab.RESUMEN_FINANCIERO,
                    onClick = { viewModel.setManagerTab(ManagerTab.RESUMEN_FINANCIERO) },
                    text = { Text("💰 Resumen Financiero") }
                )
                Tab(
                    selected = managerTab == ManagerTab.VENTAS,
                    onClick = { viewModel.setManagerTab(ManagerTab.VENTAS) },
                    text = { Text("📊 Ventas & Reportes") }
                )
                Tab(
                    selected = managerTab == ManagerTab.HISTORIAL,
                    onClick = { viewModel.setManagerTab(ManagerTab.HISTORIAL) },
                    text = { Text("📜 Historial & Corrección") }
                )
                Tab(
                    selected = managerTab == ManagerTab.EMPLEADOS,
                    onClick = { viewModel.setManagerTab(ManagerTab.EMPLEADOS) },
                    text = { Text("👥 Personal & Roles") }
                )
                Tab(
                    selected = managerTab == ManagerTab.AUDIT_LOGS,
                    onClick = { viewModel.setManagerTab(ManagerTab.AUDIT_LOGS) },
                    text = { Text("📋 Audit Trail Firestore") }
                )
                Tab(
                    selected = managerTab == ManagerTab.SEGURIDAD,
                    onClick = { viewModel.setManagerTab(ManagerTab.SEGURIDAD) },
                    text = { Text("⚡ Firebase & Reglas") }
                )
                Tab(
                    selected = managerTab == ManagerTab.QR_MENU,
                    onClick = { viewModel.setManagerTab(ManagerTab.QR_MENU) },
                    text = { Text("📱 Menú QR & WhatsApp") }
                )
                Tab(
                    selected = managerTab == ManagerTab.CONFIGURACION,
                    onClick = { viewModel.setManagerTab(ManagerTab.CONFIGURACION) },
                    text = { Text("🔑 Seguridad & PIN") }
                )
            }

            when (managerTab) {
                ManagerTab.MENU -> {
                    MenuVisualEditorView(
                        menuItems = allMenuItems,
                        onSaveMenuItem = { id, name, category, price, description, imageUrl, isAvailable, isVisibleWeb ->
                            viewModel.saveMenuItem(id, name, category, price, description, imageUrl, isAvailable, isVisibleWeb)
                        },
                        onDeleteMenuItem = { id -> viewModel.deleteMenuItem(id) },
                        onToggleAvailability = { item -> viewModel.toggleMenuItemAvailability(item) },
                        onToggleWebVisibility = { item -> viewModel.toggleMenuItemVisibilityWeb(item) },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                ManagerTab.TEMAS_WEB -> {
                    WebThemeCustomizerView(
                        themes = allThemes,
                        activeTheme = activeTheme,
                        systemSettings = systemSettings,
                        onSaveTheme = { id, themeName, primaryColorHex, secondaryColorHex, bannerImageUrl, welcomeMessage, isActive ->
                            viewModel.saveTheme(id, themeName, primaryColorHex, secondaryColorHex, bannerImageUrl, welcomeMessage, isActive)
                        },
                        onActivateTheme = { themeId -> viewModel.activateTheme(themeId) },
                        onDeleteTheme = { themeId -> viewModel.deleteTheme(themeId) },
                        onSaveScheduleSettings = { updatedSettings ->
                            viewModel.saveSystemSettings(updatedSettings)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                ManagerTab.INVENTARIO -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        InventoryManagerView(
                            inventory = allInventory,
                            menuItems = allMenuItems,
                            recipeItems = allRecipeItems,
                            movements = allInventoryMovements,
                            currentUser = currentUser,
                            isManagerRole = true,
                            onSaveIngredient = { id, name, stock, minStock, idealStock, cost, supplier, unit, expDate ->
                                viewModel.saveInventoryItem(id, name, stock, minStock, idealStock, cost, supplier, unit, expDate)
                            },
                            onDeleteIngredient = { id -> viewModel.deleteInventoryItem(id) },
                            onRegisterMovement = { ingId, type, qty, reason, user ->
                                viewModel.registerManualMovement(ingId, type, qty, reason, user)
                            },
                            onSaveRecipeItem = { id, menuItemId, ingredientId, ingredientName, qty, unit ->
                                viewModel.saveRecipeItem(id, menuItemId, ingredientId, ingredientName, qty, unit)
                            },
                            onDeleteRecipeItem = { id -> viewModel.deleteRecipeItem(id) }
                        )
                    }
                }

                ManagerTab.FACTURACION -> {
                    InvoiceManagerView(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                ManagerTab.RESUMEN_FINANCIERO -> {
                    DailyFinancialSummariesView(
                        dailyCloses = allDailyCloses,
                        activeSales = allSales,
                        cashierName = currentUser?.name ?: "Gerente",
                        branchName = systemSettings.branchName
                    )
                }

                ManagerTab.VENTAS -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Low Stock Alert Banner for Manager Quick View
                        if (lowStockInventory.isNotEmpty()) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("alert_low_stock_manager")
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Warning,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "⚠️ ALERTA: ${lowStockInventory.size} INSUMOS EN BAJO STOCK",
                                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = MaterialTheme.colorScheme.onErrorContainer
                                                )
                                            }

                                            TextButton(onClick = { viewModel.setManagerTab(ManagerTab.INVENTARIO) }) {
                                                Text("Gestionar", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "Insumos críticos por debajo del umbral mínimo configurado:",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            lowStockInventory.take(3).forEach { item ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = "• ${item.productName}",
                                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                                        color = MaterialTheme.colorScheme.onErrorContainer
                                                    )
                                                    Text(
                                                        text = "${item.currentStock} ${item.unit} (Mín: ${item.minStock})",
                                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                                        color = MaterialTheme.colorScheme.onErrorContainer
                                                    )
                                                }
                                            }
                                            if (lowStockInventory.size > 3) {
                                                Text(
                                                    text = "... y ${lowStockInventory.size - 3} insumos más.",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Periodo de Ventas:",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                listOf("Diario", "Semanal", "Quincenal", "Mensual").forEach { period ->
                                    FilterChip(
                                        selected = salesPeriod == period,
                                        onClick = { viewModel.setSalesPeriod(period) },
                                        label = { Text(period) }
                                    )
                                }
                            }
                        }

                        // Stats Summary Card with Direct PDF Export Button
                        item {
                            val context = LocalContext.current
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "VENTAS REGISTRADAS ($salesPeriod.uppercase())",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                                        ) {
                                            Text(
                                                text = "Moneda: Quetzales (Q)",
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = formatQuetzales(totalPeriodSales),
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Transacciones: ${filteredSales.size}", style = MaterialTheme.typography.bodyMedium)
                                        Text("Ticket Promedio: ${formatQuetzales(avgTicket)}", style = MaterialTheme.typography.bodyMedium)
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Quick PDF & Report Export Action Bar
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                com.example.util.ReportExportHelper.printOrExportPdf(context, detailedReport)
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier
                                                .weight(1.2f)
                                                .testTag("btn_exportar_pdf_resumen")
                                        ) {
                                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Generar PDF", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                com.example.util.ReportExportHelper.shareViaWhatsApp(context, detailedReport)
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Enviar WS", fontSize = 11.sp)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                com.example.util.ReportExportHelper.exportToCsvAndShare(context, detailedReport)
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.TableChart, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Excel", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Daily Monthly Cumulative Sales Bar Chart in Quetzales (Q)
                        item {
                            val allSalesList by viewModel.allSales.collectAsState()
                            MonthlySalesBarChart(salesList = allSalesList)
                        }

                        // Bar Chart for Category Sales in Quetzales
                        item {
                            CategorySalesBarChart(
                                categorySales = categorySales,
                                salesPeriod = salesPeriod
                            )
                        }

                        // Detailed Sales & Metrics Report
                        item {
                            DetailedSalesReportView(
                                report = detailedReport,
                                categoryFilter = reportCategoryFilter,
                                searchQuery = reportSearchQuery,
                                waiterFilter = reportWaiterFilter,
                                sortOption = reportSortOption,
                                waiterList = waitersList,
                                onCategoryFilterChange = { viewModel.setReportCategoryFilter(it) },
                                onSearchQueryChange = { viewModel.setReportSearchQuery(it) },
                                onWaiterFilterChange = { viewModel.setReportWaiterFilter(it) },
                                onSortOptionChange = { viewModel.setReportSortOption(it) }
                            )
                        }

                        item {
                            Text(
                                text = "Detalle de Transacciones ($salesPeriod):",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        if (filteredSales.isEmpty()) {
                            item {
                                EmptyStateCard(
                                    icon = Icons.Default.Analytics,
                                    title = "Sin ventas en este período",
                                    message = "Seleccione otro período o registre ventas en el módulo de caja."
                                )
                            }
                        } else {
                            items(filteredSales, key = { it.id }) { sale ->
                                Card(
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "${sale.orderNumber} • ${sale.paymentMethod}",
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = "Cajero: ${sale.cashierName}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        Text(
                                            text = formatQuetzales(sale.total),
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldSuccess
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                ManagerTab.HISTORIAL -> {
                    CompletedOrdersHistoryView(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                ManagerTab.EMPLEADOS -> {
                    UserManagementAdminView(viewModel = viewModel)
                }

                ManagerTab.AUDIT_LOGS -> {
                    AuditLogsAdminView(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                }

                ManagerTab.SEGURIDAD -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Surface(
                                                shape = CircleShape,
                                                color = if (isFirestoreSyncActive) EmeraldSuccess.copy(alpha = 0.15f) else Color(0xFFFEF3C7),
                                                modifier = Modifier.size(42.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(
                                                        imageVector = Icons.Default.CloudSync,
                                                        contentDescription = null,
                                                        tint = if (isFirestoreSyncActive) EmeraldSuccess else Color(0xFFD97706)
                                                    )
                                                }
                                            }

                                            Column {
                                                Text(
                                                    text = "Sincronización Firestore en Vivo",
                                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                                )
                                                Text(
                                                    text = if (isFirestoreSyncActive) "Escuchando cambios multidispositivo en tiempo real" else "Modo Local / Reconectando",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (isFirestoreSyncActive) EmeraldSuccess else Color(0xFFD97706)
                                        ) {
                                            Text(
                                                text = if (isFirestoreSyncActive) "CONECTADO" else "OFFLINE",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                ),
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Divider()
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "Colecciones Activas:",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        listOf("orders", "menu_items", "inventory_items", "users", "sales").forEach { col ->
                                            AssistChip(
                                                onClick = { },
                                                label = { Text("/$col", fontSize = 11.sp) },
                                                leadingIcon = { Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Matriz de Reglas de Seguridad por Rol",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        // Mesero Rules Card
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.Assignment, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Text(
                                            text = "Rol: MESERO",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("• Lectura: Pedidos activos e historial de su turno, menú de platillos.", style = MaterialTheme.typography.bodySmall)
                                    Text("• Crear: Pedidos nuevos con estado 'PENDIENTE' y notas.", style = MaterialTheme.typography.bodySmall)
                                    Text("• Modificar: Modificar ítems o notas antes de confirmación.", style = MaterialTheme.typography.bodySmall)
                                    Text("• Denegado: Modificar precios del menú, inventario o usuarios.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }

                        // Cocina Rules Card
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.SoupKitchen, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                                        Text(
                                            text = "Rol: COCINA",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("• Lectura: Comandas pendientes y en preparación, insumos de inventario.", style = MaterialTheme.typography.bodySmall)
                                    Text("• Modificar: Estado de pedidos ('EN_PROCESO', 'FINALIZADO'), stock de ingredientes.", style = MaterialTheme.typography.bodySmall)
                                    Text("• Denegado: Cobros, facturación, creación de usuarios.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }

                        // Caja Rules Card
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.PointOfSale, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                                        Text(
                                            text = "Rol: CAJA",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("• Lectura: Pedidos finalizados por cobrar y resumen de ventas diarias.", style = MaterialTheme.typography.bodySmall)
                                    Text("• Modificar: Procesar pago (Estado 'PAGADO', método de pago, caja abierta).", style = MaterialTheme.typography.bodySmall)
                                    Text("• Crear: Registro en colección /sales.", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }

                        // Gerente Rules Card
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = EmeraldSuccess)
                                        Text(
                                            text = "Rol: GERENTE / ADMINISTRADOR",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("• Control Total: Lectura, escritura, actualización y eliminación en todas las colecciones.", style = MaterialTheme.typography.bodySmall)
                                    Text("• Gestión de Menú: Crear, editar y eliminar platillos y precios.", style = MaterialTheme.typography.bodySmall)
                                    Text("• Gestión de Personal: Asignar y editar roles de meseros, cocineros y cajeros.", style = MaterialTheme.typography.bodySmall)
                                    Text("• Auditoría: Corrección de historial y eliminación de registros.", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }

                        // Code Rule Snippet
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.Code, contentDescription = null, tint = Color(0xFF38BDF8))
                                        Text(
                                            text = "Reglas de Seguridad Configurada (firestore.rules)",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = """
match /orders/{orderId} {
  allow read: if true;
  allow create: if request.resource.data.status == 'PENDIENTE';
  allow update: if request.resource.data.status in ['PENDIENTE', 'EN_PROCESO', 'FINALIZADO', 'PAGADO', 'CANCELADO'];
  allow delete: if getUserRole() == 'GERENTE';
}
match /menu_items/{itemId} {
  allow read: if true;
  allow write: if getUserRole() == 'GERENTE';
}
match /users/{userId} {
  allow read: if true;
  allow write: if getUserRole() == 'GERENTE';
}
                                        """.trimIndent(),
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF94A3B8)),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }

                ManagerTab.QR_MENU -> {
                    com.example.ui.components.DigitalMenuQrGeneratorView(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                ManagerTab.CONFIGURACION -> {
                    com.example.ui.components.SystemSettingsAdminView(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    // Add / Edit Inventory Dialog
    showAddEditInventoryDialog?.let { invItem ->
        AddEditInventoryDialog(
            item = invItem,
            isNew = isNewInventoryItem,
            onDismiss = { showAddEditInventoryDialog = null },
            onSave = { id, name, stock, minStock, unit ->
                viewModel.saveInventoryItem(id, name, stock, minStock, minStock * 2, 0.0, "Proveedor Principal", unit, null)
                showAddEditInventoryDialog = null
            }
        )
    }

    // Add / Edit User Dialog
    showAddEditUserDialog?.let { user ->
        AddEditUserDialog(
            user = user,
            isNew = isNewUser,
            onDismiss = { showAddEditUserDialog = null },
            onSave = { id, name, role, pin ->
                viewModel.saveUser(id, name, role, pin)
                showAddEditUserDialog = null
            }
        )
    }

    // Change PIN Dialog
    if (showChangePinDialog) {
        ChangePinDialog(
            onDismiss = { showChangePinDialog = false },
            onSavePin = { newPin ->
                viewModel.changeManagerPin(newPin) {
                    showChangePinDialog = false
                }
            }
        )
    }
}

@Composable
fun AddEditInventoryDialog(
    item: InventoryItemEntity,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (id: Long, name: String, stock: Double, minStock: Double, unit: String) -> Unit
) {
    var name by remember { mutableStateOf(item.productName) }
    var stockText by remember { mutableStateOf(if (item.currentStock > 0) item.currentStock.toString() else "0") }
    var minStockText by remember { mutableStateOf(if (item.minStock > 0) item.minStock.toString() else "5") }
    var unit by remember { mutableStateOf(item.unit) }

    val units = listOf("Libras", "Unidades", "Litros", "Porciones")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isNew) "Agregar Insumo de Inventario" else "Editar Insumo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del Insumo / Producto") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = stockText,
                        onValueChange = { stockText = it },
                        label = { Text("Stock Actual") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = minStockText,
                        onValueChange = { minStockText = it },
                        label = { Text("Stock Mínimo") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Text("Unidad de Medida:", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    units.forEach { u ->
                        FilterChip(
                            selected = unit == u,
                            onClick = { unit = u },
                            label = { Text(u) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val st = stockText.toDoubleOrNull() ?: 0.0
                    val minSt = minStockText.toDoubleOrNull() ?: 0.0
                    if (name.isNotBlank()) {
                        onSave(item.id, name, st, minSt, unit)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Guardar Insumo")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun ChangePinDialog(
    onDismiss: () -> Unit,
    onSavePin: (String) -> Unit
) {
    var newPinText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cambiar PIN / Llave de Acceso") },
        text = {
            Column {
                Text(
                    text = "Ingrese la nueva clave o PIN para el Gerente:",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newPinText,
                    onValueChange = { if (it.length <= 8) newPinText = it },
                    label = { Text("Nuevo PIN") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSavePin(newPinText) },
                enabled = newPinText.isNotBlank()
            ) {
                Text("Guardar Nuevo PIN")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun AddEditUserDialog(
    user: com.example.data.entity.UserEntity,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (id: Long, name: String, role: String, pin: String) -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var role by remember { mutableStateOf(user.role) }
    var pin by remember { mutableStateOf(user.pin) }
    val roles = listOf("MESERO", "COCINA", "CAJA", "GERENTE")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isNew) "Nuevo Empleado" else "Editar Empleado") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre Completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Rol de Sistema:", style = MaterialTheme.typography.labelMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    roles.forEach { r ->
                        FilterChip(
                            selected = role == r,
                            onClick = { role = r },
                            label = { Text(r, fontSize = 11.sp) }
                        )
                    }
                }

                if (role == "GERENTE") {
                    OutlinedTextField(
                        value = pin,
                        onValueChange = { pin = it.take(8) },
                        label = { Text("PIN de Seguridad") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(user.id, name.trim(), role, pin)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
