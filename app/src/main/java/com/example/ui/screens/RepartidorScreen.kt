package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.DeliverySettlementEntity
import com.example.data.entity.UserEntity
import com.example.data.entity.WebOrderEntity
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.ModuleTopBar
import com.example.ui.components.formatQuetzales
import com.example.ui.theme.*
import com.example.ui.viewmodel.DeliveryTab
import com.example.ui.viewmodel.RestaurantViewModel
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepartidorScreen(
    viewModel: RestaurantViewModel,
    onBackToInicio: () -> Unit
) {
    val context = LocalContext.current
    val deliveryTab by viewModel.deliveryTab.collectAsState()
    val deliveryDriverName by viewModel.deliveryDriverName.collectAsState()
    val repartidorUsers by viewModel.repartidorUsers.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    val readyOrders by viewModel.readyDeliveryOrders.collectAsState()
    val inTransitOrders by viewModel.inTransitDeliveryOrders.collectAsState()
    val completedOrders by viewModel.completedDeliveryOrders.collectAsState()
    val incidentOrders by viewModel.incidentDeliveryOrders.collectAsState()
    val todaySettlements by viewModel.todayDeliverySettlements.collectAsState()

    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    var showDriverSwitchDialog by remember { mutableStateOf(false) }
    var orderToReportIncident by remember { mutableStateOf<WebOrderEntity?>(null) }
    var orderToViewDetails by remember { mutableStateOf<WebOrderEntity?>(null) }

    // Active driver name prioritization
    val activeDriver = currentUser?.name ?: deliveryDriverName

    Scaffold(
        topBar = {
            ModuleTopBar(
                title = "Módulo Repartidor",
                subtitle = "Repartidor: $activeDriver • En camino: ${inTransitOrders.size}",
                onBackClick = onBackToInicio,
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleDarkTheme() },
                        modifier = Modifier.testTag("btn_toggle_dark_repartidor")
                    ) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Cambiar Tema",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = { showDriverSwitchDialog = true },
                        modifier = Modifier.testTag("btn_switch_driver")
                    ) {
                        Icon(
                            imageVector = Icons.Default.TwoWheeler,
                            contentDescription = "Cambiar Repartidor",
                            tint = Color.White
                        )
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
            // Top Tab Filter (Pedidos Listos, En Camino, Incidencias, Historial)
            SecondaryTabRow(
                selectedTabIndex = deliveryTab.ordinal,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = deliveryTab == DeliveryTab.LISTOS,
                    onClick = { viewModel.setDeliveryTab(DeliveryTab.LISTOS) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Listos", fontWeight = FontWeight.SemiBold)
                            if (readyOrders.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Badge(containerColor = MaterialTheme.colorScheme.error) {
                                    Text(readyOrders.size.toString(), color = Color.White)
                                }
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.RestaurantMenu, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("tab_delivery_listos")
                )

                Tab(
                    selected = deliveryTab == DeliveryTab.EN_CAMINO,
                    onClick = { viewModel.setDeliveryTab(DeliveryTab.EN_CAMINO) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("En Camino", fontWeight = FontWeight.SemiBold)
                            if (inTransitOrders.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                    Text(inTransitOrders.size.toString(), color = Color.White)
                                }
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.Moped, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("tab_delivery_en_camino")
                )

                Tab(
                    selected = deliveryTab == DeliveryTab.INCIDENCIAS,
                    onClick = { viewModel.setDeliveryTab(DeliveryTab.INCIDENCIAS) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Incidencias", fontWeight = FontWeight.SemiBold)
                            if (incidentOrders.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Badge(containerColor = AmberWarning) {
                                    Text(incidentOrders.size.toString(), color = Color.White)
                                }
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.WarningAmber, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("tab_delivery_incidencias")
                )

                Tab(
                    selected = deliveryTab == DeliveryTab.HISTORIAL,
                    onClick = { viewModel.setDeliveryTab(DeliveryTab.HISTORIAL) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Liquidación", fontWeight = FontWeight.SemiBold)
                            if (todaySettlements.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Badge(containerColor = SuccessGreen) {
                                    Text(todaySettlements.size.toString(), color = Color.White)
                                }
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("tab_delivery_historial")
                )
            }

            // Body content according to selected tab
            when (deliveryTab) {
                DeliveryTab.LISTOS -> {
                    ReadyDeliveryOrdersView(
                        orders = readyOrders,
                        activeDriver = activeDriver,
                        onStartDelivery = { orderId ->
                            viewModel.startDelivery(orderId) {
                                Toast.makeText(context, "Pedido en camino", Toast.LENGTH_SHORT).show()
                                viewModel.setDeliveryTab(DeliveryTab.EN_CAMINO)
                            }
                        },
                        onViewDetails = { orderToViewDetails = it }
                    )
                }
                DeliveryTab.EN_CAMINO -> {
                    InTransitDeliveryOrdersView(
                        orders = inTransitOrders,
                        activeDriver = activeDriver,
                        onCompleteDelivery = { orderId ->
                            viewModel.completeDelivery(orderId) {
                                Toast.makeText(context, "Entrega completada y cobro liquidado en Caja", Toast.LENGTH_LONG).show()
                            }
                        },
                        onReportProblem = { order ->
                            orderToReportIncident = order
                        },
                        onViewDetails = { orderToViewDetails = it }
                    )
                }
                DeliveryTab.INCIDENCIAS -> {
                    IncidentDeliveryOrdersView(
                        orders = incidentOrders,
                        onRetryDelivery = { orderId ->
                            viewModel.startDelivery(orderId) {
                                Toast.makeText(context, "Reintentando entrega", Toast.LENGTH_SHORT).show()
                                viewModel.setDeliveryTab(DeliveryTab.EN_CAMINO)
                            }
                        },
                        onViewDetails = { orderToViewDetails = it }
                    )
                }
                DeliveryTab.HISTORIAL -> {
                    DeliverySettlementHistoryView(
                        settlements = todaySettlements,
                        completedOrders = completedOrders,
                        activeDriver = activeDriver
                    )
                }
            }
        }
    }

    // Modal: Report Incident
    orderToReportIncident?.let { order ->
        ReportDeliveryProblemDialog(
            order = order,
            onDismiss = { orderToReportIncident = null },
            onSubmitIncident = { incidentNote ->
                viewModel.reportDeliveryIncident(order.id, incidentNote) {
                    Toast.makeText(context, "Incidencia reportada a Caja", Toast.LENGTH_LONG).show()
                    orderToReportIncident = null
                    viewModel.setDeliveryTab(DeliveryTab.INCIDENCIAS)
                }
            }
        )
    }

    // Modal: Order Details
    orderToViewDetails?.let { order ->
        DeliveryOrderDetailDialog(
            order = order,
            onDismiss = { orderToViewDetails = null }
        )
    }

    // Modal: Switch Driver
    if (showDriverSwitchDialog) {
        DriverSelectionDialog(
            currentDriver = activeDriver,
            driverList = repartidorUsers,
            onDismiss = { showDriverSwitchDialog = false },
            onSelectDriver = { name ->
                viewModel.setDeliveryDriverName(name)
                showDriverSwitchDialog = false
                Toast.makeText(context, "Repartidor activo: $name", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

// ==========================================
// 1. LISTOS PARA ENTREGA VIEW
// ==========================================
@Composable
private fun ReadyDeliveryOrdersView(
    orders: List<WebOrderEntity>,
    activeDriver: String,
    onStartDelivery: (Long) -> Unit,
    onViewDetails: (WebOrderEntity) -> Unit
) {
    if (orders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            EmptyStateCard(
                icon = Icons.Default.CheckCircle,
                title = "No hay pedidos listos",
                message = "Los pedidos de Delivery aprobados por Caja y finalizados por Cocina aparecerán aquí automáticamente."
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Hay ${orders.size} pedido(s) listos para salir con $activeDriver. Presiona 'En Camino' al tomar el paquete.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            items(orders, key = { it.id }) { order ->
                DeliveryOrderCard(
                    order = order,
                    actionSlot = {
                        Button(
                            onClick = { onStartDelivery(order.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_iniciar_entrega_${order.id}")
                        ) {
                            Icon(Icons.Default.Moped, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tomar Pedido y Salir (En Camino)", fontWeight = FontWeight.Bold)
                        }
                    },
                    onViewDetails = { onViewDetails(order) }
                )
            }
        }
    }
}

// ==========================================
// 2. EN CAMINO VIEW
// ==========================================
@Composable
private fun InTransitDeliveryOrdersView(
    orders: List<WebOrderEntity>,
    activeDriver: String,
    onCompleteDelivery: (Long) -> Unit,
    onReportProblem: (WebOrderEntity) -> Unit,
    onViewDetails: (WebOrderEntity) -> Unit
) {
    if (orders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            EmptyStateCard(
                icon = Icons.Default.TwoWheeler,
                title = "Sin entregas en ruta",
                message = "Selecciona pedidos en la pestaña 'Listos' para iniciar tu ruta de entrega."
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Entregas Activas en Ruta (${orders.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Total a recaudar: ${formatQuetzales(orders.sumOf { it.totalAmount })}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SuccessGreen
                    )
                }
            }

            items(orders, key = { it.id }) { order ->
                DeliveryOrderCard(
                    order = order,
                    highlightInTransit = true,
                    actionSlot = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Report Incident Button
                                OutlinedButton(
                                    onClick = { onReportProblem(order) },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("btn_reportar_problema_${order.id}")
                                ) {
                                    Icon(Icons.Default.ReportProblem, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Reportar Problema", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }

                                // Mark Delivered Button
                                Button(
                                    onClick = { onCompleteDelivery(order.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1.3f)
                                        .testTag("btn_entregado_${order.id}")
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Entregado", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    },
                    onViewDetails = { onViewDetails(order) }
                )
            }
        }
    }
}

// ==========================================
// 3. INCIDENCIAS VIEW
// ==========================================
@Composable
private fun IncidentDeliveryOrdersView(
    orders: List<WebOrderEntity>,
    onRetryDelivery: (Long) -> Unit,
    onViewDetails: (WebOrderEntity) -> Unit
) {
    if (orders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            EmptyStateCard(
                icon = Icons.Default.SentimentSatisfiedAlt,
                title = "Sin incidencias activas",
                message = "Excelente, no hay entregas con problemas reportados."
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders, key = { it.id }) { order ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "INCIDENCIA EN PEDIDO #${order.webOrderId}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 14.sp
                            )
                            Badge(containerColor = MaterialTheme.colorScheme.error) {
                                Text("Revisión Caja", color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Cliente: ${order.customerName} (${order.customerPhone})",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Dirección: ${order.deliveryAddress.ifBlank { "No especificada" }}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Motivo de la incidencia:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = order.deliveryIssueNote.ifBlank { "Sin detalle especificado" },
                                    fontSize = 13.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val context = LocalContext.current
                            OutlinedButton(
                                onClick = { openDialer(context, order.customerPhone) },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Llamar", fontSize = 12.sp)
                            }

                            Button(
                                onClick = { onRetryDelivery(order.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1.2f)
                            ) {
                                Icon(Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reintentar Entrega", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 4. HISTORIAL Y LIQUIDACIÓN VIEW
// ==========================================
@Composable
private fun DeliverySettlementHistoryView(
    settlements: List<DeliverySettlementEntity>,
    completedOrders: List<WebOrderEntity>,
    activeDriver: String
) {
    val totalCollected = settlements.sumOf { it.totalAmount }
    val cashCollected = settlements.filter { it.paymentMethod.contains("Efectivo", ignoreCase = true) }.sumOf { it.totalAmount }
    val cardOrTransfer = totalCollected - cashCollected

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Summary Card for Driver and Manager
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Liquidación de Entregas Hoy", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Repartidor: $activeDriver", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Recaudado", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatQuetzales(totalCollected), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = SuccessGreen)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Entregas Realizadas", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${settlements.size} pedidos", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("• Efectivo en mano: ${formatQuetzales(cashCollected)}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text("• Prepago / Tarjeta: ${formatQuetzales(cardOrTransfer)}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        item {
            Text(
                text = "Detalle de Entregas Liquidadas",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (settlements.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay entregas liquidadas registradas el día de hoy.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                }
            }
        } else {
            items(settlements, key = { it.id }) { item ->
                val timeFormatted = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(item.completedAt))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Orden #${item.webOrderId}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• $timeFormatted",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "${item.customerName} • ${item.customerPhone}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Dir: ${item.deliveryAddress.ifBlank { "Entrega a domicilio" }}",
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = formatQuetzales(item.totalAmount),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = SuccessGreen
                            )
                            Badge(
                                containerColor = if (item.paymentMethod.contains("Efectivo", ignoreCase = true)) SuccessGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = item.paymentMethod,
                                    color = if (item.paymentMethod.contains("Efectivo", ignoreCase = true)) SuccessGreen else MaterialTheme.colorScheme.primary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// REUSABLE DELIVERY ORDER CARD COMPONENT
// ==========================================
@Composable
fun DeliveryOrderCard(
    order: WebOrderEntity,
    highlightInTransit: Boolean = false,
    actionSlot: @Composable () -> Unit,
    onViewDetails: () -> Unit
) {
    val context = LocalContext.current
    val address = order.deliveryAddress.ifBlank { "Entrega a Domicilio" }

    // Parse products json
    val itemsSummary = remember(order.itemsJson) {
        try {
            val arr = JSONArray(order.itemsJson)
            val list = mutableListOf<String>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val qty = obj.optInt("quantity", obj.optInt("qty", 1))
                val name = obj.optString("name", obj.optString("productName", "Producto"))
                list.add("$qty x $name")
            }
            list
        } catch (_: Exception) {
            listOf("1 x Pedido Delivery")
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (highlightInTransit) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            1.dp,
            if (highlightInTransit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("delivery_card_${order.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Order Code, Origin badge, and Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Moped, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Pedido #${order.webOrderId}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = order.origin,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatQuetzales(order.totalAmount),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = SuccessGreen
                    )
                    Badge(
                        containerColor = if (order.paymentMethod.contains("Efectivo", ignoreCase = true)) SuccessGreen.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = order.paymentMethod,
                            color = if (order.paymentMethod.contains("Efectivo", ignoreCase = true)) SuccessGreen else MaterialTheme.colorScheme.primary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            // Customer Info Box with Quick Actions (Call, WhatsApp, Maps Navigate)
            Surface(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // Customer Name & Quick Call / WhatsApp Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = order.customerName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                if (order.customerPhone.isNotBlank()) {
                                    Text(
                                        text = order.customerPhone,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        if (order.customerPhone.isNotBlank()) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                // Call Button
                                IconButton(
                                    onClick = { openDialer(context, order.customerPhone) },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                                        .testTag("btn_call_${order.id}")
                                ) {
                                    Icon(
                                        Icons.Default.Phone,
                                        contentDescription = "Llamar al cliente",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                // WhatsApp Button
                                IconButton(
                                    onClick = { openWhatsAppChat(context, order.customerPhone, order.customerName, order.webOrderId) },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(SuccessGreen.copy(alpha = 0.2f), CircleShape)
                                        .testTag("btn_whatsapp_${order.id}")
                                ) {
                                    Icon(
                                        Icons.Default.Chat,
                                        contentDescription = "Abrir WhatsApp",
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Address Row with "Navegar" Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(top = 2.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = address,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Navegar Button (Google Maps Navigation)
                        Button(
                            onClick = { openGoogleMapsNavigation(context, address) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("btn_navegar_${order.id}")
                        ) {
                            Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Navegar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Products Breakdown
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onViewDetails() }
            ) {
                Text(
                    text = "Productos (${itemsSummary.size}):",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                itemsSummary.take(3).forEach { itemStr ->
                    Text(
                        text = "• $itemStr",
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (itemsSummary.size > 3) {
                    Text(
                        text = "+ ${itemsSummary.size - 3} producto(s) más...",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (order.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Nota: ${order.notes}",
                        fontSize = 11.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Custom Action Slot
            actionSlot()
        }
    }
}

// ==========================================
// REPORT PROBLEM / INCIDENT DIALOG
// ==========================================
@Composable
fun ReportDeliveryProblemDialog(
    order: WebOrderEntity,
    onDismiss: () -> Unit,
    onSubmitIncident: (String) -> Unit
) {
    var incidentText by remember { mutableStateOf("") }
    var selectedPreset by remember { mutableStateOf("") }

    val presetReasons = listOf(
        "Cliente no contesta teléfono",
        "Dirección no encontrada / no existe",
        "Zona inaccesible o peligrosa",
        "Cliente rechazó el pedido",
        "Sin cambio suficiente para billete",
        "Demora excesiva en tráfico"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ReportProblem, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reportar Problema de Entrega")
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Pedido #${order.webOrderId} • ${order.customerName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = "Selecciona un motivo común o escribe el detalle para que Caja sea notificada:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Preset Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(presetReasons) { preset ->
                        FilterChip(
                            selected = selectedPreset == preset,
                            onClick = {
                                selectedPreset = preset
                                incidentText = preset
                            },
                            label = { Text(preset, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = incidentText,
                    onValueChange = { incidentText = it },
                    label = { Text("Detalle de la incidencia") },
                    placeholder = { Text("Ej. Toqué timbre y nadie atiende, llamé 3 veces.") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_incident_note")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (incidentText.isNotBlank()) {
                        onSubmitIncident(incidentText.trim())
                    }
                },
                enabled = incidentText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.testTag("btn_confirm_incident")
            ) {
                Text("Enviar Reporte a Caja", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// ==========================================
// DRIVER SELECTION DIALOG
// ==========================================
@Composable
fun DriverSelectionDialog(
    currentDriver: String,
    driverList: List<UserEntity>,
    onDismiss: () -> Unit,
    onSelectDriver: (String) -> Unit
) {
    var customName by remember { mutableStateOf("") }

    val defaultDrivers = if (driverList.isNotEmpty()) {
        driverList.map { it.name }
    } else {
        listOf("Héctor Soto", "Luis Morales", "Carlos Repartidor", "Motorista 1")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Seleccionar Repartidor Activo")
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Selecciona quién está utilizando este dispositivo:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                defaultDrivers.forEach { driver ->
                    val isSelected = driver.equals(currentDriver, ignoreCase = true)
                    Surface(
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectDriver(driver) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = driver,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = customName,
                    onValueChange = { customName = it },
                    label = { Text("O ingresar otro nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            if (customName.isNotBlank()) {
                Button(onClick = { onSelectDriver(customName.trim()) }) {
                    Text("Usar este nombre")
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text("Cerrar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// ==========================================
// ORDER DETAIL MODAL
// ==========================================
@Composable
fun DeliveryOrderDetailDialog(
    order: WebOrderEntity,
    onDismiss: () -> Unit
) {
    val items = remember(order.itemsJson) {
        try {
            val arr = JSONArray(order.itemsJson)
            val list = mutableListOf<Triple<String, Int, Double>>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val qty = obj.optInt("quantity", obj.optInt("qty", 1))
                val name = obj.optString("name", obj.optString("productName", "Producto"))
                val price = obj.optDouble("price", 0.0)
                list.add(Triple(name, qty, price))
            }
            list
        } catch (_: Exception) {
            listOf(Triple("Pedido Completo", 1, order.totalAmount))
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Detalle del Pedido #${order.webOrderId}", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Cliente: ${order.customerName}", fontWeight = FontWeight.SemiBold)
                Text("Teléfono: ${order.customerPhone.ifBlank { "No registrado" }}")
                Text("Dirección: ${order.deliveryAddress.ifBlank { "A domicilio" }}")
                Text("Método de Pago: ${order.paymentMethod}", fontWeight = FontWeight.Medium)

                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                Text("Productos:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                items.forEach { (name, qty, price) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("$qty x $name", fontSize = 12.sp, modifier = Modifier.weight(1f))
                        if (price > 0) {
                            Text(formatQuetzales(price * qty), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total a Cobrar:", fontWeight = FontWeight.Bold)
                    Text(formatQuetzales(order.totalAmount), fontWeight = FontWeight.ExtraBold, color = SuccessGreen)
                }

                if (order.notes.isNotBlank()) {
                    Text("Notas: ${order.notes}", fontSize = 11.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Entendido")
            }
        }
    )
}

// ==========================================
// INTENT HELPERS (PHONE, WHATSAPP, MAPS)
// ==========================================
private fun openDialer(context: Context, phone: String) {
    if (phone.isBlank()) return
    try {
        val cleanPhone = phone.replace(Regex("[^0-9+]"), "")
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanPhone"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir el marcador telefónico", Toast.LENGTH_SHORT).show()
    }
}

private fun openWhatsAppChat(context: Context, phone: String, customerName: String, orderId: String) {
    if (phone.isBlank()) return
    try {
        var cleanPhone = phone.replace(Regex("[^0-9]"), "")
        if (!cleanPhone.startsWith("502") && cleanPhone.length == 8) {
            cleanPhone = "502$cleanPhone"
        }
        val message = "¡Hola $customerName! Te saluda el repartidor de Restaurante Rivera. Voy en camino con tu pedido #$orderId."
        val url = "https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(message)}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show()
    }
}

private fun openGoogleMapsNavigation(context: Context, address: String) {
    if (address.isBlank()) {
        Toast.makeText(context, "Dirección no disponible", Toast.LENGTH_SHORT).show()
        return
    }
    try {
        // Attempt opening standard Google Maps Navigation intent
        val uri = Uri.parse("google.navigation:q=" + Uri.encode(address))
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            // Fallback to generic geo intent or browser maps
            val fallbackUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(address))
            val webIntent = Intent(Intent.ACTION_VIEW, fallbackUri)
            webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(webIntent)
        }
    } catch (e: Exception) {
        val browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(address))
        val browserIntent = Intent(Intent.ACTION_VIEW, browserUri)
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(browserIntent)
    }
}
