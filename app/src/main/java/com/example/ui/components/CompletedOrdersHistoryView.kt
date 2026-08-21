package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.OrderEntity
import com.example.data.entity.OrderItemEntity
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.RestaurantViewModel
import java.text.SimpleDateFormat
import java.util.*

enum class HistoryDateRange(val label: String) {
    TODOS("Todos"),
    HOY("Hoy"),
    AYER("Ayer"),
    ULTIMOS_7_DIAS("Últimos 7 Días"),
    ESTE_MES("Este Mes")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedOrdersHistoryView(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val allOrders by viewModel.allOrders.collectAsState()
    val allSales by viewModel.allSales.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedDateRange by remember { mutableStateOf(HistoryDateRange.TODOS) }
    var selectedEmployeeFilter by remember { mutableStateOf("TODOS") }
    var selectedOrderForDetail by remember { mutableStateOf<OrderEntity?>(null) }
    var ticketToPrint by remember { mutableStateOf<Triple<OrderEntity, List<OrderItemEntity>, Triple<String, Double, Double>>?>(null) }

    // Collect all unique employee names (waiters & cashiers) for dropdown/filter
    val employeeNames = remember(allOrders) {
        val names = mutableSetOf<String>()
        allOrders.forEach { order ->
            if (order.waiterName.isNotBlank()) names.add(order.waiterName)
            if (!order.cashierName.isNullOrBlank()) names.add(order.cashierName)
        }
        names.toList().sorted()
    }

    // Filter completed orders (status == PAGADO or FINALIZADO)
    val now = System.currentTimeMillis()
    val calendar = Calendar.getInstance()

    val filteredOrders = remember(allOrders, searchQuery, selectedDateRange, selectedEmployeeFilter) {
        allOrders.filter { order ->
            val isCompleted = order.status == "PAGADO" || order.status == "FINALIZADO"
            if (!isCompleted) return@filter false

            // 1. Text Search Filter
            val matchesQuery = if (searchQuery.isBlank()) true else {
                val q = searchQuery.trim().lowercase()
                order.orderNumber.lowercase().contains(q) ||
                        order.tableNumber.lowercase().contains(q) ||
                        order.waiterName.lowercase().contains(q) ||
                        (order.cashierName?.lowercase()?.contains(q) == true) ||
                        (order.paymentMethod?.lowercase()?.contains(q) == true) ||
                        (order.generalNotes?.lowercase()?.contains(q) == true)
            }
            if (!matchesQuery) return@filter false

            // 2. Employee Filter
            val matchesEmployee = if (selectedEmployeeFilter == "TODOS") true else {
                order.waiterName.equals(selectedEmployeeFilter, ignoreCase = true) ||
                        order.cashierName?.equals(selectedEmployeeFilter, ignoreCase = true) == true
            }
            if (!matchesEmployee) return@filter false

            // 3. Date Range Filter
            val orderTime = order.paidAt ?: order.completedAt ?: order.createdAt
            val matchesDate = when (selectedDateRange) {
                HistoryDateRange.TODOS -> true
                HistoryDateRange.HOY -> {
                    val calToday = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    orderTime >= calToday.timeInMillis
                }
                HistoryDateRange.AYER -> {
                    val calYesterdayStart = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_YEAR, -1)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    val calYesterdayEnd = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_YEAR, -1)
                        set(Calendar.HOUR_OF_DAY, 23)
                        set(Calendar.MINUTE, 59)
                        set(Calendar.SECOND, 59)
                        set(Calendar.MILLISECOND, 999)
                    }
                    orderTime >= calYesterdayStart.timeInMillis && orderTime <= calYesterdayEnd.timeInMillis
                }
                HistoryDateRange.ULTIMOS_7_DIAS -> {
                    val cal7Days = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_YEAR, -7)
                    }
                    orderTime >= cal7Days.timeInMillis
                }
                HistoryDateRange.ESTE_MES -> {
                    val calMonth = Calendar.getInstance().apply {
                        set(Calendar.DAY_OF_MONTH, 1)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    orderTime >= calMonth.timeInMillis
                }
            }
            matchesDate
        }
    }

    val totalAmountFiltered = filteredOrders.sumOf { it.totalAmount }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar por N° pedido, mesa, mesero, cajero...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_order_history"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Date Range Chips
        Text(
            text = "Filtrar por fecha:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            items(HistoryDateRange.values()) { range ->
                FilterChip(
                    selected = selectedDateRange == range,
                    onClick = { selectedDateRange = range },
                    label = { Text(range.label) },
                    leadingIcon = if (selectedDateRange == range) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    } else null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Employee Filter Row
        if (employeeNames.isNotEmpty()) {
            Text(
                text = "Filtrar por empleado (Mesero / Cajero):",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedEmployeeFilter == "TODOS",
                        onClick = { selectedEmployeeFilter = "TODOS" },
                        label = { Text("👥 Todos los empleados") }
                    )
                }
                items(employeeNames) { empName ->
                    FilterChip(
                        selected = selectedEmployeeFilter == empName,
                        onClick = { selectedEmployeeFilter = empName },
                        label = { Text(empName) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Results Summary Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pedidos encontrades: ${filteredOrders.size}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Text(
                    text = "Total: ${formatQuetzales(totalAmountFiltered)}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldSuccess
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Orders List
        if (filteredOrders.isEmpty()) {
            EmptyStateCard(
                icon = Icons.Default.HistoryToggleOff,
                title = "No se encontraron pedidos completados",
                message = "Intente ajustar el término de búsqueda o modifique los filtros de fecha y empleado."
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredOrders, key = { it.id }) { order ->
                    OrderHistoryCard(
                        order = order,
                        onClick = { selectedOrderForDetail = order }
                    )
                }
            }
        }
    }

    // Detail Ticket Modal
    selectedOrderForDetail?.let { order ->
        OrderDetailTicketDialog(
            order = order,
            viewModel = viewModel,
            onDismiss = { selectedOrderForDetail = null },
            onPrintTicket = { items ->
                ticketToPrint = Triple(order, items, Triple(order.cashierName ?: "Cajero", 0.0, 0.0))
            }
        )
    }

    // Thermal Printer Dialog
    ticketToPrint?.let { (order, items, meta) ->
        ThermalPrinterDialog(
            title = "Imprimir Copia de Ticket",
            order = order,
            items = items,
            isKitchenComanda = false,
            cashierName = meta.first,
            amountReceived = meta.second,
            change = meta.third,
            onDismiss = { ticketToPrint = null }
        )
    }
}

@Composable
fun OrderHistoryCard(
    order: OrderEntity,
    onClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val dateStr = dateFormat.format(Date(order.paidAt ?: order.completedAt ?: order.createdAt))

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("item_order_history_${order.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = order.orderNumber,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = order.tableNumber,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Text(
                    text = formatQuetzales(order.totalAmount),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldSuccess
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "👤 Mesero: ${order.waiterName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (!order.cashierName.isNullOrBlank()) {
                        Text(
                            text = "💳 Cajero: ${order.cashierName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = order.paymentMethod ?: "PAGADO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🕒 $dateStr",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Ver Ticket",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderDetailTicketDialog(
    order: OrderEntity,
    viewModel: RestaurantViewModel,
    onDismiss: () -> Unit,
    onPrintTicket: (List<OrderItemEntity>) -> Unit
) {
    var orderItems by remember { mutableStateOf<List<OrderItemEntity>>(emptyList()) }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    LaunchedEffect(order.id) {
        orderItems = viewModel.getOrderItems(order.id)
    }

    val subtotal = orderItems.sumOf { it.subtotal }
    val tax = subtotal * 0.12

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ticket Detallado ${order.orderNumber}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${order.tableNumber} • ${order.paymentMethod ?: "PAGADO"}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                // Metadata
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mesero:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    Text(order.waiterName, style = MaterialTheme.typography.bodySmall)
                }
                if (!order.cashierName.isNullOrBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Cajero:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        Text(order.cashierName!!, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Fecha Cobro:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    Text(
                        dateFormat.format(Date(order.paidAt ?: order.createdAt)),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "DETALLE DE CONSUMO:",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Item breakdown list
                if (orderItems.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        orderItems.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${item.quantity}x ${item.productName}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    if (item.notes.isNotBlank()) {
                                        Text(
                                            text = "Nota: ${item.notes}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Text(
                                    text = formatQuetzales(item.subtotal),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                // Totals
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subtotal:", style = MaterialTheme.typography.bodyMedium)
                    Text(formatQuetzales(subtotal), style = MaterialTheme.typography.bodyMedium)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("IVA Incluido (12%):", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(formatQuetzales(tax), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("TOTAL PAGADO:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Text(
                        formatQuetzales(order.totalAmount),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldSuccess
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onPrintTicket(orderItems)
                },
                modifier = Modifier.testTag("btn_reimprimir_ticket")
            ) {
                Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Reimprimir Ticket")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
