package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import com.example.data.entity.MenuItemEntity
import com.example.data.entity.OrderEntity
import com.example.data.entity.OrderItemEntity
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.ModuleTopBar
import com.example.ui.components.NuevosPedidosDigitalesAlert
import com.example.ui.components.StatusBadge
import com.example.ui.components.ThermalPrinterDialog
import com.example.ui.components.UserSwitchDialog
import com.example.ui.components.formatQuetzales
import com.example.ui.theme.*
import com.example.ui.viewmodel.RestaurantViewModel
import com.example.ui.viewmodel.WaiterTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeseroScreen(
    viewModel: RestaurantViewModel,
    onBackToInicio: () -> Unit
) {
    val waiterTab by viewModel.waiterTab.collectAsState()
    val availableMenuItems by viewModel.availableMenuItems.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val selectedTable by viewModel.selectedTable.collectAsState()
    val waiterName by viewModel.waiterName.collectAsState()
    val searchQuery by viewModel.menuSearchQuery.collectAsState()
    val selectedCategory by viewModel.menuCategoryFilter.collectAsState()
    val generalNotes by viewModel.generalOrderNotes.collectAsState()

    val activeOrders by viewModel.waiterActiveOrders.collectAsState()
    val historyOrders by viewModel.waiterHistoryOrders.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    val waiterUsers by viewModel.waiterUsers.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showCartBottomSheet by remember { mutableStateOf(false) }
    var showLargeOrderConfirmation by remember { mutableStateOf(false) }
    var selectedMenuItemForCustomization by remember { mutableStateOf<MenuItemEntity?>(null) }
    var showUserSwitchDialog by remember { mutableStateOf(false) }
    var orderToPrint by remember { mutableStateOf<Pair<OrderEntity, List<OrderItemEntity>>?>(null) }

    androidx.activity.compose.BackHandler(enabled = true) {
        if (showCartBottomSheet) {
            showCartBottomSheet = false
        } else if (selectedMenuItemForCustomization != null) {
            selectedMenuItemForCustomization = null
        } else if (showUserSwitchDialog) {
            showUserSwitchDialog = false
        } else if (orderToPrint != null) {
            orderToPrint = null
        } else if (waiterTab != WaiterTab.MAPA_MESAS) {
            viewModel.setWaiterTab(WaiterTab.MAPA_MESAS)
        } else {
            onBackToInicio()
        }
    }

    // Real-Time Validated Order Form Fields (Nombre, Teléfono, Ubicación)
    var orderCustomerName by remember { mutableStateOf("") }
    var orderCustomerPhone by remember { mutableStateOf("") }
    var orderLocation by remember { mutableStateOf("") }
    var hasAttemptedSubmitOrder by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current

    val waiterOrdersCount = remember(allOrders, waiterName) {
        allOrders.count { it.waiterName.equals(waiterName, ignoreCase = true) && it.status != "CANCELADO" }
    }
    val waiterShiftTotal = remember(allOrders, waiterName) {
        allOrders.filter { it.waiterName.equals(waiterName, ignoreCase = true) && it.status != "CANCELADO" }.sumOf { it.totalAmount }
    }

    val systemTables by viewModel.allTables.collectAsState()
    val dynamicTables = remember(systemTables) {
        if (systemTables.isEmpty()) {
            listOf(RestaurantTableInfo("Mesa 1", "Salón", 4))
        } else {
            systemTables.map { entity ->
                RestaurantTableInfo(
                    name = entity.tableNumber,
                    zone = if (entity.tableNumber.equals("Para Llevar", ignoreCase = true)) "Barra" else "Salón",
                    capacity = entity.capacity
                )
            }
        }
    }

    val categories = listOf("Todos", "Platillos", "Bebidas", "Postres", "Entradas")

    val kitchenOrders by viewModel.kitchenOrders.collectAsState()
    val activeKitchenOrdersCount = kitchenOrders.size
    val estimatedWaitMinutes = 15 + (activeKitchenOrdersCount * 5)
    val isHighKitchenDemand = estimatedWaitMinutes > 30

    val totalCartAmount = cartItems.entries.sumOf { (item, pair) -> item.price * pair.first }
    val totalCartQuantity = cartItems.values.sumOf { it.first }

    val filteredMenu = availableMenuItems.filter { item ->
        val matchesCategory = (selectedCategory == "Todos" || item.category.equals(selectedCategory, ignoreCase = true))
        val matchesQuery = searchQuery.isBlank() ||
                item.name.contains(searchQuery, ignoreCase = true) ||
                item.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    val isFirestoreSyncActive by viewModel.isFirestoreSyncActive.collectAsState()
    val syncStatusLabel by viewModel.syncStatusLabel.collectAsState()

    Scaffold(
        topBar = {
            ModuleTopBar(
                title = "Módulo Mesero",
                subtitle = "Mesero actual: $waiterName",
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
                    IconButton(onClick = { showUserSwitchDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.SwitchAccount,
                            contentDescription = "Cambiar Mesero Activo",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (waiterTab == WaiterTab.NUEVO_PEDIDO && cartItems.isNotEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "$totalCartQuantity ítems en carrito",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isHighKitchenDemand) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (isHighKitchenDemand) "⚠️ ~$estimatedWaitMinutes min" else "⏱️ ~$estimatedWaitMinutes min",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isHighKitchenDemand) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Total: ${formatQuetzales(totalCartAmount)}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }

                        Button(
                            onClick = { showCartBottomSheet = true },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                            modifier = Modifier.testTag("btn_ver_carrito")
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ver Carrito")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Sub Tabs
            val pendingWebOrdersList by viewModel.pendingWebOrders.collectAsState()

            SecondaryTabRow(
                selectedTabIndex = waiterTab.ordinal,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = waiterTab == WaiterTab.MAPA_MESAS,
                    onClick = { viewModel.setWaiterTab(WaiterTab.MAPA_MESAS) },
                    text = { Text("🗺️ Mapa") }
                )
                Tab(
                    selected = waiterTab == WaiterTab.NUEVO_PEDIDO,
                    onClick = { viewModel.setWaiterTab(WaiterTab.NUEVO_PEDIDO) },
                    text = { Text("📝 Nuevo Pedido") }
                )
                Tab(
                    selected = waiterTab == WaiterTab.PEDIDOS_ACTIVOS,
                    onClick = { viewModel.setWaiterTab(WaiterTab.PEDIDOS_ACTIVOS) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📋 Activos (${activeOrders.size})")
                            if (pendingWebOrdersList.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = StatusCocinaBadge,
                                    modifier = Modifier.size(8.dp)
                                ) {}
                            }
                        }
                    }
                )
                Tab(
                    selected = waiterTab == WaiterTab.HISTORIAL,
                    onClick = { viewModel.setWaiterTab(WaiterTab.HISTORIAL) },
                    text = { Text("📜 Historial") }
                )
            }

            // Real-time alert for Web/QR/WhatsApp Digital Orders
            NuevosPedidosDigitalesAlert(
                viewModel = viewModel,
                assignedRole = waiterName
            )

            when (waiterTab) {
                WaiterTab.MAPA_MESAS -> {
                    TableMapTabContent(
                        allOrders = allOrders,
                        selectedTable = selectedTable,
                        onSelectTableAndNewOrder = { table ->
                            viewModel.setSelectedTable(table)
                            viewModel.setWaiterTab(WaiterTab.NUEVO_PEDIDO)
                        },
                        onViewActiveOrders = {
                            viewModel.setWaiterTab(WaiterTab.PEDIDOS_ACTIVOS)
                        },
                        onPrintOrder = { ord ->
                            coroutineScope.launch {
                                val items = viewModel.getOrderItemsFlow(ord.id).firstOrNull() ?: emptyList()
                                orderToPrint = Pair(ord, items)
                            }
                        },
                        viewModel = viewModel
                    )
                }
                WaiterTab.NUEVO_PEDIDO -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))

                        // Active Shift Selector Card (Selector de Turno Activo para Mesero)
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("card_turno_activo")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(38.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Badge,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = waiterName,
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = EmeraldSuccess
                                            ) {
                                                Text(
                                                    text = "TURNO ACTIVO",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = "ID: MESERO-101 • Ventas: ${formatQuetzales(waiterShiftTotal)} ($waiterOrdersCount pedidos)",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                OutlinedButton(
                                    onClick = { showUserSwitchDialog = true },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("btn_selector_turno_activo")
                                ) {
                                    Icon(Icons.Default.SwitchAccount, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Cambiar", fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Seleccionar Mesa:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            items(dynamicTables) { tableInfo ->
                                val (status, _) = resolveTableStatus(tableInfo.name, allOrders)
                                FilterChip(
                                    selected = selectedTable == tableInfo.name,
                                    onClick = { viewModel.setSelectedTable(tableInfo.name) },
                                    label = { Text(tableInfo.name) },
                                    leadingIcon = {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(status.badgeColor)
                                        )
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Search and Categories
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.setMenuSearchQuery(it) },
                            placeholder = { Text("Buscar platillo, bebida...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = if (searchQuery.isNotEmpty()) {
                                {
                                    IconButton(onClick = { viewModel.setMenuSearchQuery("") }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                                    }
                                }
                            } else null,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            items(categories) { category ->
                                FilterChip(
                                    selected = selectedCategory == category,
                                    onClick = { viewModel.setMenuCategoryFilter(category) },
                                    label = { Text(category) }
                                )
                            }
                        }

                        // Product Menu List
                        if (filteredMenu.isEmpty()) {
                            EmptyStateCard(
                                icon = Icons.Default.Restaurant,
                                title = "No hay productos encontrados",
                                message = "Intente cambiando la búsqueda o categoría."
                            )
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(bottom = 8.dp)
                            ) {
                                items(filteredMenu, key = { it.id }) { item ->
                                    val inCartQty = cartItems[item]?.first ?: 0
                                    MenuItemCard(
                                        menuItem = item,
                                        quantityInCart = inCartQty,
                                        onAddItem = {
                                            selectedMenuItemForCustomization = item
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                WaiterTab.PEDIDOS_ACTIVOS -> {
                    if (activeOrders.isEmpty()) {
                        EmptyStateCard(
                            icon = Icons.Default.ReceiptLong,
                            title = "No tienes pedidos activos",
                            message = "Crea un nuevo pedido desde la pestaña 'Nuevo Pedido'."
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(activeOrders, key = { it.id }) { order ->
                                OrderCard(
                                    order = order,
                                    viewModel = viewModel,
                                    onPrintClick = { items ->
                                        orderToPrint = Pair(order, items)
                                    }
                                )
                            }
                        }
                    }
                }

                WaiterTab.HISTORIAL -> {
                    if (historyOrders.isEmpty()) {
                        EmptyStateCard(
                            icon = Icons.Default.History,
                            title = "Historial vacío",
                            message = "Los pedidos cobrados y cerrados aparecerán aquí."
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(historyOrders, key = { it.id }) { order ->
                                OrderCard(
                                    order = order,
                                    viewModel = viewModel,
                                    onPrintClick = { items ->
                                        orderToPrint = Pair(order, items)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Customization Dialog when tapping a product
    selectedMenuItemForCustomization?.let { menuItem ->
        AddItemCustomizationDialog(
            menuItem = menuItem,
            onDismiss = { selectedMenuItemForCustomization = null },
            onConfirm = { qty, notes ->
                repeat(qty) {
                    viewModel.addItemToCart(menuItem, notes)
                }
                selectedMenuItemForCustomization = null
            }
        )
    }

    // Cart Bottom Sheet
    if (showCartBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showCartBottomSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Resumen del Pedido",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "$selectedTable • Atiende: $waiterName",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    TextButton(onClick = { viewModel.clearCart() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Vaciar", tint = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Vaciar", color = MaterialTheme.colorScheme.error)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isHighKitchenDemand) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHighKitchenDemand) "⚠️" else "⏱️",
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (isHighKitchenDemand) "Alta Demanda en Cocina (~$estimatedWaitMinutes min)" else "Tiempo Estimado en Cocina: ~$estimatedWaitMinutes min",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isHighKitchenDemand) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                            Text(
                                text = if (isHighKitchenDemand) "Hay $activeKitchenOrdersCount pedidos en preparación. Notificar al comensal sobre el tiempo estimado." else "Cocina con $activeKitchenOrdersCount pedidos activos en cola.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isHighKitchenDemand) MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Divider()

                LazyColumn(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .heightIn(max = 280.dp)
                        .padding(vertical = 8.dp)
                ) {
                    items(cartItems.entries.toList()) { (item, pair) ->
                        val qty = pair.first
                        val notes = pair.second
                        val subtotal = item.price * qty

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "${formatQuetzales(item.price)} c/u",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (notes.isNotBlank()) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Text(
                                            text = "Obs: $notes",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { viewModel.updateCartItemQuantity(item, -1) }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Restar")
                                }
                                Text(
                                    text = "$qty",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(onClick = { viewModel.updateCartItemQuantity(item, 1) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Sumar")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = formatQuetzales(subtotal),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // --- FORMULARIO DE PEDIDO CON VALIDACIÓN EN TIEMPO REAL (Nombre, Teléfono, Ubicación) ---
                val isNameStrictlyValid = remember(orderCustomerName) {
                    orderCustomerName.trim().length >= 3 && orderCustomerName.trim().matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s.'-]+$"))
                }
                val isNameError = remember(orderCustomerName, hasAttemptedSubmitOrder) {
                    (orderCustomerName.isNotBlank() && !isNameStrictlyValid)
                }

                val phoneDigits = remember(orderCustomerPhone) {
                    orderCustomerPhone.filter { it.isDigit() }
                }
                val isPhoneStrictlyValid = remember(phoneDigits) {
                    phoneDigits.length in 8..15
                }
                val isPhoneError = remember(orderCustomerPhone, isPhoneStrictlyValid) {
                    orderCustomerPhone.isNotBlank() && !isPhoneStrictlyValid
                }

                val effectiveLocation = if (orderLocation.isNotBlank()) orderLocation.trim() else selectedTable
                val isLocationValid = effectiveLocation.isNotBlank() && effectiveLocation.length >= 2
                val isLocationError = hasAttemptedSubmitOrder && !isLocationValid

                val isOrderFormValid = !isNameError && !isPhoneError && isLocationValid

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Datos del Pedido y Entrega",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (isOrderFormValid && (orderCustomerName.isNotBlank() || orderCustomerPhone.isNotBlank())) {
                                Surface(
                                    color = EmeraldSuccess.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = EmeraldSuccess,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Validado",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldSuccess
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        // Campo: Nombre de quien ordena (Validación en tiempo real)
                        OutlinedTextField(
                            value = orderCustomerName,
                            onValueChange = { orderCustomerName = it },
                            label = { Text("Nombre del Comensal / Cliente") },
                            placeholder = { Text("Ej. Carlos Rivera (Mínimo 3 letras)") },
                            singleLine = true,
                            isError = isNameError,
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (isNameError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingIcon = {
                                if (isNameStrictlyValid) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = "Nombre Válido",
                                        tint = EmeraldSuccess
                                    )
                                } else if (isNameError) {
                                    Icon(
                                        Icons.Default.Error,
                                        contentDescription = "Error en nombre",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            supportingText = {
                                if (isNameError) {
                                    Text(
                                        text = "⚠️ Ingresa un nombre válido (mínimo 3 letras, solo texto).",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                } else if (isNameStrictlyValid) {
                                    Text(
                                        text = "✓ Nombre verificado",
                                        color = EmeraldSuccess,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                } else {
                                    Text(
                                        text = "Opcional para salón / Requerido para envío",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_nombre_cliente_pedido")
                        )

                        // Campo: Teléfono de contacto (Validación en tiempo real)
                        OutlinedTextField(
                            value = orderCustomerPhone,
                            onValueChange = { orderCustomerPhone = it },
                            label = { Text("Teléfono / WhatsApp") },
                            placeholder = { Text("Ej. 5555-1234 (8 a 15 dígitos)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            isError = isPhoneError,
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = if (isPhoneError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingIcon = {
                                if (isPhoneStrictlyValid) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = "Teléfono Válido",
                                        tint = EmeraldSuccess
                                    )
                                } else if (isPhoneError) {
                                    Icon(
                                        Icons.Default.Error,
                                        contentDescription = "Error en teléfono",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            supportingText = {
                                if (isPhoneError) {
                                    Text(
                                        text = "⚠️ Ingresa un número de 8 a 15 dígitos numéricos.",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                } else if (isPhoneStrictlyValid) {
                                    Text(
                                        text = "✓ Teléfono verificado (${phoneDigits.length} dígitos)",
                                        color = EmeraldSuccess,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                } else {
                                    Text(
                                        text = "Para enviar notificaciones y confirmación por WhatsApp",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_telefono_cliente_pedido")
                        )

                        // Campo: Ubicación / Mesa / Destino (Validación en tiempo real)
                        OutlinedTextField(
                            value = if (orderLocation.isNotBlank()) orderLocation else selectedTable,
                            onValueChange = { orderLocation = it },
                            label = { Text("Ubicación / Mesa / Dirección de Entrega") },
                            placeholder = { Text("Ej. Mesa 3, Terraza o Dirección de entrega") },
                            singleLine = true,
                            isError = isLocationError,
                            leadingIcon = {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = if (isLocationError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingIcon = {
                                if (isLocationValid) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = "Ubicación Válida",
                                        tint = EmeraldSuccess
                                    )
                                }
                            },
                            supportingText = {
                                if (isLocationError) {
                                    Text(
                                        text = "⚠️ La ubicación o mesa no puede estar vacía.",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                } else {
                                    Text(
                                        text = "✓ Ubicación seleccionada: $effectiveLocation",
                                        color = EmeraldSuccess,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_ubicacion_cliente_pedido")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = generalNotes,
                    onValueChange = { viewModel.setGeneralOrderNotes(it) },
                    label = { Text("Observaciones generales para cocina") },
                    placeholder = { Text("Ej: Entregar entradas primero") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (!isFirestoreSyncActive) {
                    Surface(
                        color = Color(0xFFFEF3C7),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WifiOff,
                                contentDescription = null,
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Sin conexión a Firestore: Este pedido se guardará localmente en el dispositivo y se sincronizará automáticamente al reconectar.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF92400E),
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TOTAL A PAGAR:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = formatQuetzales(totalCartAmount),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.clearCart()
                            orderCustomerName = ""
                            orderCustomerPhone = ""
                            orderLocation = ""
                            hasAttemptedSubmitOrder = false
                            showCartBottomSheet = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar Pedido")
                    }

                    Button(
                        onClick = {
                            hasAttemptedSubmitOrder = true
                            if (!isOrderFormValid) {
                                com.example.util.HapticHelper.triggerErrorVibration(context)
                                return@Button
                            }

                            if (orderLocation.isNotBlank() && orderLocation != selectedTable) {
                                viewModel.setSelectedTable(orderLocation.trim())
                            }

                            val customerMeta = buildString {
                                if (orderCustomerName.isNotBlank()) append("Comensal: ${orderCustomerName.trim()} | ")
                                if (orderCustomerPhone.isNotBlank()) append("Tel: ${orderCustomerPhone.trim()} | ")
                                if (orderLocation.isNotBlank()) append("Ubicación: ${orderLocation.trim()} | ")
                            }.trimEnd(' ', '|')

                            if (customerMeta.isNotBlank()) {
                                val currentNotes = generalNotes.trim()
                                val mergedNotes = if (currentNotes.isNotBlank()) "[$customerMeta] $currentNotes" else "[$customerMeta]"
                                viewModel.setGeneralOrderNotes(mergedNotes)
                            }

                            if (totalCartAmount >= 200.0 || totalCartQuantity >= 5) {
                                showLargeOrderConfirmation = true
                            } else {
                                showCartBottomSheet = false
                                com.example.util.HapticHelper.triggerSuccessVibration(context)
                                viewModel.submitOrderToKitchen {
                                    if (!isFirestoreSyncActive) {
                                        android.widget.Toast.makeText(
                                            context,
                                            "🛡️ Pedido guardado localmente de forma segura. Se sincronizará con Firestore al recuperar la conexión.",
                                            android.widget.Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    orderCustomerName = ""
                                    orderCustomerPhone = ""
                                    orderLocation = ""
                                    hasAttemptedSubmitOrder = false
                                }
                            }
                        },
                        enabled = isOrderFormValid && cartItems.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_enviar_cocina")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Enviar a Cocina")
                    }
                }
            }
        }
    }

    // Large Order Visual Validation Dialog
    if (showLargeOrderConfirmation) {
        AlertDialog(
            onDismissRequest = { showLargeOrderConfirmation = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = "⚠️ Confirmación Visual de Pedido Grande",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Este pedido contiene $totalCartQuantity ítems por un total de ${formatQuetzales(totalCartAmount)}. Por favor revise el resumen para evitar errores de producción antes de enviar a cocina.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Mesa: $selectedTable • Atiende: $waiterName",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            )
                            Divider(modifier = Modifier.padding(vertical = 6.dp))
                            cartItems.forEach { (item, pair) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("• ${pair.first}x ${item.name}", style = MaterialTheme.typography.bodySmall)
                                    Text(formatQuetzales(item.price * pair.first), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                }
                            }
                            if (generalNotes.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Obs: $generalNotes", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLargeOrderConfirmation = false
                        showCartBottomSheet = false
                        com.example.util.HapticHelper.triggerSuccessVibration(context)
                        viewModel.submitOrderToKitchen {
                            if (!isFirestoreSyncActive) {
                                android.widget.Toast.makeText(
                                    context,
                                    "🛡️ Pedido guardado localmente de forma segura. Se sincronizará con Firestore al recuperar la conexión.",
                                    android.widget.Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                    modifier = Modifier.testTag("btn_confirmar_pedido_grande")
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Confirmar y Enviar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLargeOrderConfirmation = false }) {
                    Text("Revisar Pedido")
                }
            }
        )
    }

    orderToPrint?.let { (order, items) ->
        ThermalPrinterDialog(
            title = "Imprimir Comanda de Cocina",
            order = order,
            items = items,
            isKitchenComanda = true,
            onDismiss = { orderToPrint = null }
        )
    }

    if (showUserSwitchDialog) {
        UserSwitchDialog(
            title = "Seleccionar Mesero",
            role = "MESERO",
            users = waiterUsers,
            activeUserName = waiterName,
            onSelectUser = { user ->
                viewModel.setCurrentUser(user)
                viewModel.setWaiterName(user.name)
            },
            onAddNewUser = { name, role ->
                viewModel.saveUser(0, name, role, "")
            },
            onDismiss = { showUserSwitchDialog = false }
        )
    }
}

@Composable
fun MenuItemCard(
    menuItem: MenuItemEntity,
    quantityInCart: Int,
    onAddItem: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable(onClick = onAddItem)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = menuItem.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (quantityInCart > 0) {
                            Surface(
                                shape = CircleShape,
                                color = EmeraldSuccess
                            ) {
                                Text(
                                    text = "$quantityInCart",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = menuItem.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2
                    )

                    if (menuItem.description.isNotBlank()) {
                        Text(
                            text = menuItem.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatQuetzales(menuItem.price),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(6.dp)
                                .size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddItemCustomizationDialog(
    menuItem: MenuItemEntity,
    onDismiss: () -> Unit,
    onConfirm: (quantity: Int, notes: String) -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }
    var notesText by remember { mutableStateOf("") }

    val quickNotes = listOf("Sin cebolla", "Bien cocido", "Poco hielo", "Salsa aparte", "Extra picante")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar ${menuItem.name}") },
        text = {
            Column {
                Text(
                    text = "Precio unitario: ${formatQuetzales(menuItem.price)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        enabled = quantity > 1
                    ) {
                        Icon(Icons.Default.RemoveCircle, contentDescription = "Restar")
                    }

                    Text(
                        text = "$quantity",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    IconButton(onClick = { quantity++ }) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Sumar")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Observaciones del platillo:",
                    style = MaterialTheme.typography.labelMedium
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    items(quickNotes) { note ->
                        FilterChip(
                            selected = notesText.contains(note),
                            onClick = {
                                notesText = if (notesText.contains(note)) {
                                    notesText.replace(note, "").trim()
                                } else {
                                    if (notesText.isBlank()) note else "$notesText, $note"
                                }
                            },
                            label = { Text(note) }
                        )
                    }
                }

                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    placeholder = { Text("Ej. Término medio, sin tomate...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Subtotal: ${formatQuetzales(menuItem.price * quantity)}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(quantity, notesText) }) {
                Text("Agregar al Pedido")
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
fun OrderCard(
    order: OrderEntity,
    viewModel: RestaurantViewModel,
    onPrintClick: (List<OrderItemEntity>) -> Unit = {}
) {
    val items by viewModel.getOrderItemsFlow(order.id).collectAsState(initial = emptyList())

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = order.orderNumber,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${order.tableNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }

                StatusBadge(status = order.status)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Atiende: ${order.waiterName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Order Items List
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${item.quantity}x ${item.productName}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        if (item.notes.isNotBlank()) {
                            Text(
                                text = "  ⚠️ ${item.notes}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    Text(
                        text = formatQuetzales(item.subtotal),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (order.generalNotes != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Nota general: ${order.generalNotes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = formatQuetzales(order.totalAmount),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                OutlinedButton(
                    onClick = { onPrintClick(items) },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Print,
                        contentDescription = "Imprimir Comanda",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Imprimir Comanda")
                }
            }
        }
    }
}

// --- VISUAL TABLE MAP DATA & COMPONENTS ---

data class RestaurantTableInfo(
    val name: String,
    val zone: String, // "Salón", "Terraza", "VIP", "Barra"
    val capacity: Int
)

enum class TableStatusType(
    val label: String,
    val badgeColor: Color,
    val containerColor: Color,
    val borderColor: Color,
    val contentColor: Color,
    val icon: ImageVector
) {
    LIBRE(
        label = "Libre",
        badgeColor = StatusLibreBadge,
        containerColor = StatusLibreContainer,
        borderColor = StatusLibreBorder,
        contentColor = StatusLibreText,
        icon = Icons.Default.CheckCircle
    ),
    RESERVADA(
        label = "Reservada 📅",
        badgeColor = StatusReservadaBadge,
        containerColor = StatusReservadaContainer,
        borderColor = StatusReservadaBorder,
        contentColor = StatusReservadaText,
        icon = Icons.Default.EventSeat
    ),
    ESPERANDO_COCINA(
        label = "En cocina ⏳",
        badgeColor = StatusCocinaBadge,
        containerColor = StatusCocinaContainer,
        borderColor = StatusCocinaBorder,
        contentColor = StatusCocinaText,
        icon = Icons.Default.SoupKitchen
    ),
    LISTA_PARA_CUENTA(
        label = "Lista para cobrar 💵",
        badgeColor = StatusCobrarBadge,
        containerColor = StatusCobrarContainer,
        borderColor = StatusCobrarBorder,
        contentColor = StatusCobrarText,
        icon = Icons.Default.ReceiptLong
    ),
    OCUPADA(
        label = "Ocupada 👥",
        badgeColor = StatusOcupadaBadge,
        containerColor = StatusOcupadaContainer,
        borderColor = StatusOcupadaBorder,
        contentColor = StatusOcupadaText,
        icon = Icons.Default.People
    )
}

fun resolveTableStatus(tableName: String, allOrders: List<OrderEntity>, customStatus: String? = null): Pair<TableStatusType, OrderEntity?> {
    val activeOrder = allOrders.firstOrNull {
        it.tableNumber.equals(tableName, ignoreCase = true) &&
                it.status != "PAGADO" && it.status != "CANCELADO"
    }

    if (activeOrder == null) {
        if (customStatus?.equals("Reservada", ignoreCase = true) == true) {
            return Pair(TableStatusType.RESERVADA, null)
        }
        if (customStatus?.equals("Ocupada", ignoreCase = true) == true) {
            // Última orden asociada aunque ya esté pagada/cerrada, para mantener el contexto
            val lastOrder = allOrders.filter { it.tableNumber.equals(tableName, ignoreCase = true) }
                .maxByOrNull { it.createdAt }
            return Pair(TableStatusType.OCUPADA, lastOrder)
        }
        return Pair(TableStatusType.LIBRE, null)
    }

    return when (activeOrder.status) {
        "PENDIENTE", "EN_PROCESO" -> Pair(TableStatusType.ESPERANDO_COCINA, activeOrder)
        "FINALIZADO", "SERVIDO", "LISTO" -> Pair(TableStatusType.LISTA_PARA_CUENTA, activeOrder)
        else -> Pair(TableStatusType.OCUPADA, activeOrder)
    }
}

@Composable
fun TableMapTabContent(
    allOrders: List<OrderEntity>,
    selectedTable: String,
    onSelectTableAndNewOrder: (String) -> Unit,
    onViewActiveOrders: () -> Unit,
    onPrintOrder: (OrderEntity) -> Unit,
    viewModel: RestaurantViewModel
) {
    val systemTables by viewModel.allTables.collectAsState()
    val allRestaurantTables = remember(systemTables) {
        if (systemTables.isEmpty()) {
            listOf(RestaurantTableInfo("Mesa 1", "Salón", 4))
        } else {
            systemTables.map { entity ->
                RestaurantTableInfo(
                    name = entity.tableNumber,
                    zone = if (entity.tableNumber.equals("Para Llevar", ignoreCase = true)) "Barra" else "Salón",
                    capacity = entity.capacity
                )
            }
        }
    }

    var selectedZoneFilter by remember { mutableStateOf("Todas") }
    var selectedTableForDetail by remember { mutableStateOf<RestaurantTableInfo?>(null) }

    val zones = listOf("Todas", "Salón", "Terraza", "VIP", "Barra")

    val filteredTables = allRestaurantTables.filter { table ->
        selectedZoneFilter == "Todas" || table.zone.equals(selectedZoneFilter, ignoreCase = true)
    }

    val tableStatuses = allRestaurantTables.map { table ->
        val entity = systemTables.find { it.tableNumber.equals(table.name, ignoreCase = true) }
        Pair(table, resolveTableStatus(table.name, allOrders, entity?.status))
    }

    val libreCount = tableStatuses.count { it.second.first == TableStatusType.LIBRE }
    val esperandoCocinaCount = tableStatuses.count { it.second.first == TableStatusType.ESPERANDO_COCINA }
    val listaCuentaCount = tableStatuses.count { it.second.first == TableStatusType.LISTA_PARA_CUENTA }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Stat Summary Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MapStatCard(
                title = "Libres",
                count = libreCount,
                color = TableStatusType.LIBRE.badgeColor,
                containerColor = TableStatusType.LIBRE.containerColor,
                icon = Icons.Default.CheckCircle,
                modifier = Modifier.weight(1f)
            )
            MapStatCard(
                title = "En Cocina",
                count = esperandoCocinaCount,
                color = TableStatusType.ESPERANDO_COCINA.badgeColor,
                containerColor = TableStatusType.ESPERANDO_COCINA.containerColor,
                icon = Icons.Default.SoupKitchen,
                modifier = Modifier.weight(1f)
            )
            MapStatCard(
                title = "p/ Cuenta",
                count = listaCuentaCount,
                color = TableStatusType.LISTA_PARA_CUENTA.badgeColor,
                containerColor = TableStatusType.LISTA_PARA_CUENTA.containerColor,
                icon = Icons.Default.ReceiptLong,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Zone Filters
        Text(
            text = "Filtrar por Área:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            items(zones) { zone ->
                FilterChip(
                    selected = selectedZoneFilter == zone,
                    onClick = { selectedZoneFilter = zone },
                    label = { Text(zone) },
                    leadingIcon = if (selectedZoneFilter == zone) {
                        { Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    } else null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Interactive Floor Map Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 16.dp)
        ) {
            items(filteredTables) { table ->
                val entity = systemTables.find { it.tableNumber.equals(table.name, ignoreCase = true) }
                val (status, order) = resolveTableStatus(table.name, allOrders, entity?.status)
                val isSelected = selectedTable.equals(table.name, ignoreCase = true)

                TableVisualCard(
                    table = table,
                    status = status,
                    order = order,
                    isSelected = isSelected,
                    onClick = {
                        selectedTableForDetail = table
                    }
                )
            }
        }
    }

    // Modal Table Detail Dialog
    selectedTableForDetail?.let { table ->
        val entity = systemTables.find { it.tableNumber.equals(table.name, ignoreCase = true) }
        val (status, order) = resolveTableStatus(table.name, allOrders, entity?.status)
        TableDetailDialog(
            table = table,
            status = status,
            order = order,
            viewModel = viewModel,
            onDismiss = { selectedTableForDetail = null },
            onSelectAndNewOrder = {
                onSelectTableAndNewOrder(table.name)
                selectedTableForDetail = null
            },
            onViewActiveOrders = {
                onViewActiveOrders()
                selectedTableForDetail = null
            },
            onPrintOrder = { ord ->
                onPrintOrder(ord)
                selectedTableForDetail = null
            },
            onTableFreed = {
                selectedTableForDetail = null
            }
        )
    }
}

@Composable
fun TableVisualCard(
    table: RestaurantTableInfo,
    status: TableStatusType,
    order: OrderEntity?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val elapsedMinutes = order?.let {
        ((System.currentTimeMillis() - it.createdAt) / 60000).toInt()
    } ?: 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("table_card_${table.name.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = status.containerColor
        ),
        border = BorderStroke(
            width = 2.dp,
            color = status.borderColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            // Top Header: Table Name & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = status.icon,
                        contentDescription = null,
                        tint = status.badgeColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = table.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = status.contentColor
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (status == TableStatusType.LIBRE) SemanticMesaLibreBadgeContainer else status.badgeColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = status.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = status.badgeColor,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Visual Table Layout Graphic
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(status.borderColor.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (table.capacity > 0) {
                        repeat(minOf(table.capacity, 6)) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(status.badgeColor)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = status.badgeColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Zone & Order Info
            Text(
                text = "Zona: ${table.zone}" + if (table.capacity > 0) " • ${table.capacity} p." else "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (order != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ped: ${order.orderNumber}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = status.contentColor
                    )
                    Text(
                        text = "⏳ ${elapsedMinutes}m",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = status.badgeColor
                    )
                }

                Text(
                    text = "Total: ${formatQuetzales(order.totalAmount)}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = status.contentColor
                    )
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Limpia y lista",
                    style = MaterialTheme.typography.bodySmall,
                    color = SemanticMesaLibreText
                )
            }
        }
    }
}

@Composable
fun TableDetailDialog(
    table: RestaurantTableInfo,
    status: TableStatusType,
    order: OrderEntity?,
    viewModel: RestaurantViewModel,
    onDismiss: () -> Unit,
    onSelectAndNewOrder: () -> Unit,
    onViewActiveOrders: () -> Unit,
    onPrintOrder: (OrderEntity) -> Unit,
    onTableFreed: () -> Unit = {}
) {
    val itemsFlow = remember(order?.id) {
        if (order != null) viewModel.getOrderItemsFlow(order.id) else flowOf(emptyList())
    }
    val orderItems by itemsFlow.collectAsState(initial = emptyList())

    var showReleaseConfirmDialog by remember { mutableStateOf(false) }
    var releaseWarningMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = status.icon,
                contentDescription = null,
                tint = status.badgeColor,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${table.name} (${table.zone})",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = status.containerColor,
                    border = BorderStroke(1.dp, status.borderColor),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = status.label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = status.badgeColor
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (order != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Nº Pedido:", style = MaterialTheme.typography.bodySmall)
                                Text(order.orderNumber, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Mesero:", style = MaterialTheme.typography.bodySmall)
                                Text(order.waiterName, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total:", style = MaterialTheme.typography.bodySmall)
                                Text(
                                    formatQuetzales(order.totalAmount),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Estado de Cobro:", style = MaterialTheme.typography.bodySmall)
                                Text(
                                    text = if (order.status == "PAGADO") "PAGADO ✓" else "PENDIENTE DE PAGO",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (order.status == "PAGADO") EmeraldSuccess else MaterialTheme.colorScheme.error
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Productos ordenados (${orderItems.size}):",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 160.dp)
                    ) {
                        items(orderItems) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${item.quantity}x ${item.productName}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = formatQuetzales(item.subtotal),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "La mesa está completamente libre y lista para atender a nuevos comensales.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón destacado: "Cerrar Servicio / Liberar Mesa"
                OutlinedButton(
                    onClick = {
                        showReleaseConfirmDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_cerrar_servicio_liberar_mesa"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (status == TableStatusType.LIBRE) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (status == TableStatusType.LIBRE) MaterialTheme.colorScheme.outline.copy(alpha = 0.5f) else MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MeetingRoom,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cerrar Servicio / Liberar Mesa",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSelectAndNewOrder,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (order == null) "Tomar Pedido" else "Agregar Pedido")
            }
        },
        dismissButton = {
            if (order != null) {
                OutlinedButton(
                    onClick = { onPrintOrder(order) },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Comanda")
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text("Cerrar")
                }
            }
        }
    )

    // Diálogo de Confirmación: "¿Deseas finalizar el servicio y liberar la Mesa X?"
    if (showReleaseConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showReleaseConfirmDialog = false },
            icon = {
                Icon(
                    Icons.Default.HelpOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = "Finalizar Servicio",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Deseas finalizar el servicio y liberar la ${table.name}?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showReleaseConfirmDialog = false
                        viewModel.releaseTableService(table.name) { success, message ->
                            if (success) {
                                onTableFreed()
                            } else {
                                releaseWarningMessage = message
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Sí, Finalizar y Liberar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReleaseConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Alerta de advertencia cuando existen consumos pendientes
    if (releaseWarningMessage != null) {
        AlertDialog(
            onDismissRequest = { releaseWarningMessage = null },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = "Consumos Pendientes",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            },
            text = {
                Text(
                    text = releaseWarningMessage ?: "Hay consumos pendientes de cobro antes de liberar la mesa",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = { releaseWarningMessage = null },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Entendido")
                }
            }
        )
    }
}

@Composable
fun MapStatCard(
    title: String,
    count: Int,
    color: Color,
    containerColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = color
                    )
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = color,
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

