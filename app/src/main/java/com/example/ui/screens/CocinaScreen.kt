package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.OrderEntity
import com.example.data.entity.InventoryItemEntity
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.InventoryManagerView
import com.example.ui.components.KitchenAvailabilityView
import com.example.ui.components.KitchenAvailabilitySidePanel
import com.example.ui.components.KitchenAvailabilitySideSheetModal
import com.example.ui.components.ModuleTopBar
import com.example.ui.components.RuntimePermissionBanner
import com.example.ui.components.StatusBadge
import com.example.ui.components.UserSwitchDialog
import com.example.ui.theme.*
import com.example.ui.viewmodel.RestaurantViewModel
import com.example.util.HapticHelper
import com.example.util.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class KitchenPriority(
    val title: String,
    val primaryColor: Color,
    val containerColor: Color,
    val icon: @Composable () -> Unit
) {
    CRITICA(
        title = "CRÍTICA",
        primaryColor = StatusCanceladoBadge,
        containerColor = StatusCanceladoContainer,
        icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = StatusCanceladoBadge, modifier = Modifier.size(16.dp)) }
    ),
    ALTA(
        title = "ALTA",
        primaryColor = StatusCocinaBadge,
        containerColor = StatusCocinaContainer,
        icon = { Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = StatusCocinaBadge, modifier = Modifier.size(16.dp)) }
    ),
    NORMAL(
        title = "NORMAL",
        primaryColor = StatusLibreBadge,
        containerColor = StatusLibreContainer,
        icon = { Icon(Icons.Default.AccessTime, contentDescription = null, tint = StatusLibreBadge, modifier = Modifier.size(16.dp)) }
    )
}

fun calculatePriority(elapsedMinutes: Long): KitchenPriority {
    return when {
        elapsedMinutes >= 20 -> KitchenPriority.CRITICA
        elapsedMinutes >= 10 -> KitchenPriority.ALTA
        else -> KitchenPriority.NORMAL
    }
}

fun formatWaitTime(elapsedMinutes: Long): String {
    return when {
        elapsedMinutes < 1 -> "Hace un momento"
        elapsedMinutes < 60 -> "$elapsedMinutes min de espera"
        else -> {
            val hours = elapsedMinutes / 60
            val mins = elapsedMinutes % 60
            "${hours}h ${mins}m de espera"
        }
    }
}

@Composable
fun CocinaScreen(
    viewModel: RestaurantViewModel,
    onBackToInicio: () -> Unit
) {
    val kitchenOrders by viewModel.kitchenOrders.collectAsState()
    val kitchenUsers by viewModel.kitchenUsers.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val allInventory by viewModel.allInventory.collectAsState()
    val lowStockInventory by viewModel.lowStockInventory.collectAsState()
    val allMenuItems by viewModel.allMenuItems.collectAsState()
    val allRecipeItems by viewModel.allRecipeItems.collectAsState()
    val allInventoryMovements by viewModel.allInventoryMovements.collectAsState()

    var activeKitchenName by remember { mutableStateOf(currentUser?.name ?: "Cocina Central") }
    var showUserSwitchDialog by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("TODOS") }
    var sortByPriority by remember { mutableStateOf(true) }
    var kitchenScreenTab by remember { mutableStateOf(0) } // 0: Comandas, 1: Disponibilidad, 2: Inventario
    var showAvailabilitySideSheet by remember { mutableStateOf(false) }
    val systemSettings by viewModel.systemSettings.collectAsState()

    androidx.activity.compose.BackHandler(enabled = true) {
        if (showAvailabilitySideSheet) {
            showAvailabilitySideSheet = false
        } else if (showUserSwitchDialog) {
            showUserSwitchDialog = false
        } else if (kitchenScreenTab != 0) {
            kitchenScreenTab = 0
        } else {
            onBackToInicio()
        }
    }

    // Ticker to re-calculate wait time every 10 seconds
    var currentTimeMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (isActive) {
            currentTimeMillis = System.currentTimeMillis()
            delay(10000)
        }
    }

    val pendingCount = kitchenOrders.count { it.status == "PENDIENTE" }
    val inProgressCount = kitchenOrders.count { it.status == "EN_PROCESO" }

    // Filter logic
    val filteredOrders = kitchenOrders.filter { order ->
        when (selectedFilter) {
            "PENDIENTE" -> order.status == "PENDIENTE"
            "EN_PROCESO" -> order.status == "EN_PROCESO"
            "CRITICO" -> {
                val elapsed = (currentTimeMillis - order.createdAt) / 60000
                elapsed >= 20
            }
            else -> true
        }
    }.sortedWith(
        if (sortByPriority) {
            // Sort by creation time ascending (oldest first = highest priority)
            compareBy<OrderEntity> { it.createdAt }
        } else {
            compareByDescending { it.createdAt }
        }
    )

    // Calculate metrics for dashboard
    val totalActive = kitchenOrders.size
    val criticalCount = kitchenOrders.count {
        ((currentTimeMillis - it.createdAt) / 60000) >= 20
    }
    val avgWaitMinutes = if (totalActive > 0) {
        kitchenOrders.map { (currentTimeMillis - it.createdAt) / 60000 }.average().toInt()
    } else 0

    val outOfStockCount = allMenuItems.count { !it.isAvailable }
    val context = LocalContext.current

    // Audible alert chime when a new order is marked as 'PENDIENTE'
    var previousPendingOrderIds by remember { mutableStateOf<Set<Long>>(emptySet()) }
    LaunchedEffect(kitchenOrders) {
        val currentPendingIds = kitchenOrders.filter { it.status == "PENDIENTE" }.map { it.id }.toSet()
        if (previousPendingOrderIds.isNotEmpty()) {
            val newOrders = currentPendingIds - previousPendingOrderIds
            if (newOrders.isNotEmpty()) {
                if (systemSettings.kitchenSoundEnabled && systemSettings.notificationSoundEnabled) {
                    val customTone = systemSettings.kitchenRingtoneUri.ifBlank { systemSettings.notificationRingtoneUri }
                    NotificationHelper.playOrderAlertChime(context, customTone, systemSettings.notificationVolume)
                }
                if (systemSettings.notificationVibrationEnabled) {
                    HapticHelper.triggerAlertVibration(context)
                }
            }
        }
        previousPendingOrderIds = currentPendingIds
    }

    Scaffold(
        topBar = {
            ModuleTopBar(
                title = "Módulo Cocina",
                subtitle = "Estación: $activeKitchenName • $pendingCount pendientes • $inProgressCount en proceso",
                onBackClick = onBackToInicio,
                actions = {
                    // Quick Disponibilidad Lateral Drawer Action
                    IconButton(
                        onClick = { showAvailabilitySideSheet = true },
                        modifier = Modifier.testTag("btn_topbar_disponibilidad_cocina")
                    ) {
                        BadgedBox(
                            badge = {
                                if (outOfStockCount > 0) {
                                    Badge(containerColor = Color(0xFFDC2626)) {
                                        Text("$outOfStockCount", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = "Disponibilidad Rápida Lateral",
                                tint = if (showAvailabilitySideSheet) Color(0xFFFDE047) else Color.White
                            )
                        }
                    }

                    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
                    IconButton(onClick = { viewModel.toggleDarkTheme() }) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Cambiar Tema",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        NotificationHelper.showKitchenNotification(
                            context = context,
                            title = "🔔 Prueba FCM Cocina",
                            body = "Notificación de prueba enviada a la estación de cocina. ¡FCM operativo!"
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Probar Notificación FCM",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showUserSwitchDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.SwitchAccount,
                            contentDescription = "Cambiar Pantalla Cocina",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (kitchenScreenTab == 0) {
                ExtendedFloatingActionButton(
                    onClick = { showAvailabilitySideSheet = true },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = if (outOfStockCount > 0) Color(0xFFDC2626) else MaterialTheme.colorScheme.primary
                        )
                    },
                    text = {
                        Text(
                            text = if (outOfStockCount > 0) "⚡ $outOfStockCount Agotados" else "⚡ Disponibilidad",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    containerColor = if (outOfStockCount > 0) Color(0xFFFEE2E2) else MaterialTheme.colorScheme.surface,
                    contentColor = if (outOfStockCount > 0) Color(0xFF991B1B) else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("fab_disponibilidad_lateral")
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Push Notification Permission Banner on-demand for Kitchen orders (Android 13+)
            RuntimePermissionBanner(
                title = "Notificaciones de Comandas en Vivo",
                description = "Permite recibir alertas sonoras y avisos en la barra de estado cuando ingresen nuevos pedidos a cocina desde las mesas o mozos.",
                permission = android.Manifest.permission.POST_NOTIFICATIONS,
                icon = Icons.Default.NotificationsActive,
                accentColor = Color(0xFFD97706),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Main Kitchen Screen Mode Tabs
            TabRow(
                selectedTabIndex = kitchenScreenTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Tab(
                    selected = kitchenScreenTab == 0,
                    onClick = { kitchenScreenTab = 0 },
                    text = { Text("👨‍🍳 Comandas ($pendingCount)", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = kitchenScreenTab == 1,
                    onClick = { kitchenScreenTab = 1 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("⚡ Disponibilidad Rápida", fontWeight = FontWeight.Bold)
                            if (outOfStockCount > 0) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFDC2626)
                                ) {
                                    Text(
                                        text = "$outOfStockCount",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                )
                Tab(
                    selected = kitchenScreenTab == 2,
                    onClick = { kitchenScreenTab = 2 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("📦 Inventario", fontWeight = FontWeight.Bold)
                            if (lowStockInventory.isNotEmpty()) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFDC2626)
                                ) {
                                    Text(
                                        text = "${lowStockInventory.size}",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                )
            }

            if (kitchenScreenTab == 1) {
                KitchenAvailabilityView(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (kitchenScreenTab == 2) {
                InventoryManagerView(
                    inventory = allInventory,
                    menuItems = allMenuItems,
                    recipeItems = allRecipeItems,
                    movements = allInventoryMovements,
                    currentUser = currentUser,
                    isManagerRole = false, // Read-only mode for Kitchen staff
                    onSaveIngredient = { _, _, _, _, _, _, _, _, _ -> },
                    onDeleteIngredient = { _ -> },
                    onRegisterMovement = { _, _, _, _, _ -> },
                    onSaveRecipeItem = { _, _, _, _, _, _ -> },
                    onDeleteRecipeItem = { _ -> }
                )
            } else {
                // Low Stock Alert Banner for Kitchen
                if (lowStockInventory.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFDC2626)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("banner_low_stock_cocina")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Color(0xFFDC2626),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "⚠️ INSUMOS EN STOCK CRÍTICO (${lowStockInventory.size})",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color(0xFF991B1B)
                                    )
                                }

                                TextButton(
                                    onClick = { kitchenScreenTab = 2 },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Ver Insumos", fontWeight = FontWeight.Bold, color = Color(0xFFDC2626), fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(lowStockInventory) { ing ->
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color.White,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
                                    ) {
                                        Text(
                                            text = "${ing.productName}: ${ing.currentStock} ${ing.unit} (Mín: ${ing.minStock})",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Color(0xFFDC2626),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Priority Dashboard Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (criticalCount > 0) Color(0xFFFEF2F2) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.SoupKitchen,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Panel de Tiempo y Prioridades",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = EmeraldSuccess.copy(alpha = 0.12f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Circle,
                                            contentDescription = null,
                                            tint = EmeraldSuccess,
                                            modifier = Modifier.size(6.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Notificaciones FCM Cocina Activas",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldSuccess,
                                                fontSize = 10.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        // Toggle Sorting Button
                        IconButton(onClick = { sortByPriority = !sortByPriority }) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "Ordenar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Metric 1: Avg Wait Time
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "Espera Prom.",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "$avgWaitMinutes min",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }

                        // Metric 2: Critical / High priority count
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (criticalCount > 0) Color(0xFFFEE2E2) else MaterialTheme.colorScheme.surface,
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PriorityHigh,
                                    contentDescription = null,
                                    tint = if (criticalCount > 0) Color(0xFFDC2626) else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "Críticas (>20m)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (criticalCount > 0) Color(0xFF991B1B) else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "$criticalCount órdenes",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (criticalCount > 0) Color(0xFFDC2626) else MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Filter Chips Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedFilter == "TODOS",
                        onClick = { selectedFilter = "TODOS" },
                        label = { Text("Todas ($kitchenOrders.size)") }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == "PENDIENTE",
                        onClick = { selectedFilter = "PENDIENTE" },
                        label = { Text("⏳ Pendientes ($pendingCount)") }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == "EN_PROCESO",
                        onClick = { selectedFilter = "EN_PROCESO" },
                        label = { Text("👨‍🍳 En Proceso ($inProgressCount)") }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == "CRITICO",
                        onClick = { selectedFilter = "CRITICO" },
                        label = { Text("🚨 Urgentes ($criticalCount)") }
                    )
                }
            }

            if (filteredOrders.isEmpty()) {
                EmptyStateCard(
                    icon = Icons.Default.SoupKitchen,
                    title = "¡No hay comandas pendientes!",
                    message = "Todas las órdenes enviadas por los meseros han sido procesadas o no coinciden con el filtro."
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredOrders, key = { it.id }) { order ->
                        KitchenTicketCard(
                            order = order,
                            currentTimeMillis = currentTimeMillis,
                            viewModel = viewModel
                        )
                    }
                }
            }
            }
        }
    }

    if (showAvailabilitySideSheet) {
        KitchenAvailabilitySideSheetModal(
            viewModel = viewModel,
            onDismiss = { showAvailabilitySideSheet = false }
        )
    }

    if (showUserSwitchDialog) {
        UserSwitchDialog(
            title = "Seleccionar Pantalla Cocina",
            role = "COCINA",
            users = kitchenUsers,
            activeUserName = activeKitchenName,
            onSelectUser = { user ->
                activeKitchenName = user.name
                viewModel.setCurrentUser(user)
            },
            onAddNewUser = { name, role ->
                viewModel.saveUser(0, name, role, "")
            },
            onDismiss = { showUserSwitchDialog = false }
        )
    }
}

@Composable
fun KitchenTicketCard(
    order: OrderEntity,
    currentTimeMillis: Long,
    viewModel: RestaurantViewModel
) {
    val items by viewModel.getOrderItemsFlow(order.id).collectAsState(initial = emptyList())

    val isPending = order.status == "PENDIENTE"
    val isInProgress = order.status == "EN_PROCESO"
    val isFinished = order.status == "FINALIZADO" || order.status == "SERVIDO" || order.status == "LISTO"

    val elapsedMinutes = (currentTimeMillis - order.createdAt) / 60000
    val priority = calculatePriority(elapsedMinutes)

    val createdTimeStr = remember(order.createdAt) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        sdf.format(Date(order.createdAt))
    }

    // SLA Progress (Target wait time = 20 minutes)
    val slaRatio = (elapsedMinutes / 20f).coerceIn(0f, 1f)
    val animatedSlaRatio by animateFloatAsState(
        targetValue = slaRatio,
        animationSpec = tween(durationMillis = 500),
        label = "slaRatio"
    )

    val context = LocalContext.current

    // Smooth scaling animation on order status change
    val cardScale = remember { Animatable(1f) }
    var previousStatus by remember { mutableStateOf(order.status) }

    LaunchedEffect(order.status) {
        if (previousStatus != order.status) {
            previousStatus = order.status
            // Small scale bounce effect (1.0 -> 1.03 -> 0.98 -> 1.0)
            cardScale.animateTo(
                targetValue = 1.035f,
                animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing)
            )
            cardScale.animateTo(
                targetValue = 0.985f,
                animationSpec = tween(durationMillis = 120, easing = LinearOutSlowInEasing)
            )
            cardScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f)
            )
        }
    }

    // Animated border color based on status and priority
    val targetBorderColor = when {
        isInProgress -> ActionBtnComenzarPreparacion.copy(alpha = 0.7f)
        isFinished -> ActionBtnMarcarFinalizado.copy(alpha = 0.7f)
        priority == KitchenPriority.CRITICA -> KitchenPriority.CRITICA.primaryColor
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    val animatedBorderColor by animateColorAsState(
        targetValue = targetBorderColor,
        animationSpec = tween(durationMillis = 400),
        label = "ticketBorderColor"
    )

    val animatedBorderWidth by animateFloatAsState(
        targetValue = if (isInProgress || priority == KitchenPriority.CRITICA) 2f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "ticketBorderWidth"
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isInProgress) 6.dp else 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = cardScale.value
                scaleY = cardScale.value
            }
            .border(
                width = animatedBorderWidth.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Visual Priority & Waiting Time Header Badge + Semáforo Traffic Light
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Priority Tag with Traffic Light (Semáforo) Indicator
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = priority.containerColor
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            priority.icon()
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PRIORIDAD ${priority.title}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = priority.primaryColor
                                )
                            )
                        }
                    }

                    // Semáforo Indicator (Rojo, Amarillo, Verde)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .background(Color(0xFF0F172A), shape = RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (priority == KitchenPriority.NORMAL) Color(0xFF22C55E) else Color(0xFF334155),
                                    shape = CircleShape
                                )
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (priority == KitchenPriority.ALTA) Color(0xFFEAB308) else Color(0xFF334155),
                                    shape = CircleShape
                                )
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (priority == KitchenPriority.CRITICA) Color(0xFFEF4444) else Color(0xFF334155),
                                    shape = CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = when (priority) {
                                KitchenPriority.NORMAL -> "<10m"
                                KitchenPriority.ALTA -> "10-20m"
                                KitchenPriority.CRITICA -> ">20m"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                            color = Color.White
                        )
                    }
                }

                // Live Wait Time Pill
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = priority.primaryColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${formatWaitTime(elapsedMinutes)} ($createdTimeStr)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            // Waiting time visual progress bar (SLA)
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tiempo transcurrido:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "$elapsedMinutes min / 20 min límite",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = priority.primaryColor
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatedSlaRatio)
                            .clip(CircleShape)
                            .background(priority.primaryColor)
                    )
                }
            }

            Divider(modifier = Modifier.padding(bottom = 10.dp))

            // Ticket Main Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = priority.primaryColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = order.orderNumber,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = priority.primaryColor
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = order.tableNumber,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )
                }

                AnimatedContent(
                    targetState = order.status,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(220, delayMillis = 50)) +
                                scaleIn(initialScale = 0.85f, animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f)))
                            .togetherWith(
                                fadeOut(animationSpec = tween(150)) +
                                        scaleOut(targetScale = 0.85f, animationSpec = tween(150))
                            )
                    },
                    label = "statusBadgeAnimation"
                ) { targetStatus ->
                    StatusBadge(status = targetStatus)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Mesero: ${order.waiterName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (order.generalNotes != null) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📌 NOTA GENERAL: ",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = order.generalNotes,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            Divider()

            val pendingAndActiveItems = items.filter { it.kitchenStatus in listOf("PENDIENTE", "EN_PROCESO", "NUEVO") }
            val alreadyCompletedItems = items.filter { it.kitchenStatus in listOf("FINALIZADO", "LISTO", "ENTREGADO") }

            // Active / Pending Items to prepare in Kitchen
            Column(
                modifier = Modifier.padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (pendingAndActiveItems.isEmpty() && alreadyCompletedItems.isNotEmpty()) {
                    Surface(
                        color = Color(0xFFDCFCE7),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "¡Todos los platillos de esta comanda han sido preparados!",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                            )
                        }
                    }
                } else {
                    Text(
                        text = "PLATILLOS POR PREPARAR (${pendingAndActiveItems.size}):",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    pendingAndActiveItems.forEach { item ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (item.kitchenStatus == "EN_PROCESO") {
                                Color(0xFFFEF3C7)
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.Top) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (item.kitchenStatus == "EN_PROCESO") Color(0xFFD97706) else MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(
                                            text = "${item.quantity}x",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
                                        Text(
                                            text = item.productName,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        if (item.notes.isNotBlank()) {
                                            Surface(
                                                color = MaterialTheme.colorScheme.errorContainer,
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.padding(top = 4.dp)
                                            ) {
                                                Text(
                                                    text = "⚠️ OBS: ${item.notes}",
                                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                // Per-item action controls
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    if (item.kitchenStatus == "PENDIENTE" || item.kitchenStatus == "NUEVO") {
                                        AssistChip(
                                            onClick = {
                                                com.example.util.HapticHelper.triggerLightClick(context)
                                                viewModel.updateOrderItemKitchenStatus(item.id, "EN_PROCESO")
                                            },
                                            label = { Text("👨‍🍳 Cocinar", style = MaterialTheme.typography.labelSmall) },
                                            colors = AssistChipDefaults.assistChipColors(
                                                containerColor = Color(0xFFFEF3C7),
                                                labelColor = Color(0xFFB45309)
                                            )
                                        )
                                        IconButton(
                                            onClick = {
                                                com.example.util.HapticHelper.triggerSuccessVibration(context)
                                                viewModel.updateOrderItemKitchenStatus(item.id, "FINALIZADO")
                                            },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Listo",
                                                tint = Color(0xFF16A34A)
                                            )
                                        }
                                    } else if (item.kitchenStatus == "EN_PROCESO") {
                                        Button(
                                            onClick = {
                                                com.example.util.HapticHelper.triggerSuccessVibration(context)
                                                viewModel.updateOrderItemKitchenStatus(item.id, "FINALIZADO")
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF16A34A),
                                                contentColor = Color.White
                                            ),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Listo", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Previously completed dishes in this order (No duplicate alert)
                if (alreadyCompletedItems.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Ya servidos / preparados anteriormente (${alreadyCompletedItems.size}):",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            alreadyCompletedItems.forEach { compItem ->
                                Row(
                                    modifier = Modifier.padding(start = 6.dp, top = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "• ${compItem.quantity}x ${compItem.productName}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "(No preparar de nuevo)",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF16A34A),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Divider()

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (pendingAndActiveItems.any { it.kitchenStatus == "PENDIENTE" || it.kitchenStatus == "NUEVO" }) {
                    Button(
                        onClick = {
                            com.example.util.HapticHelper.triggerSuccessVibration(context)
                            viewModel.updateOrderStatus(order.id, "EN_PROCESO")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ActionBtnComenzarPreparacion,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("btn_comenzar_preparacion")
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Comenzar Preparación 👨‍🍳")
                    }
                } else if (pendingAndActiveItems.isNotEmpty()) {
                    Button(
                        onClick = {
                            com.example.util.HapticHelper.triggerSuccessVibration(context)
                            viewModel.updateOrderStatus(order.id, "FINALIZADO")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ActionBtnMarcarFinalizado,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("btn_marcar_finalizado")
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Marcar Todos Listos ✅")
                    }
                }
            }
        }
    }
}
