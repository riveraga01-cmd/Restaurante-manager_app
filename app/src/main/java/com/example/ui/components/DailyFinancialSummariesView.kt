package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.DailyCloseEntity
import com.example.data.entity.SaleEntity
import com.example.ui.theme.EmeraldSuccess
import com.example.util.ReportExportHelper
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DailyFinancialSummariesView(
    dailyCloses: List<DailyCloseEntity>,
    activeSales: List<SaleEntity>,
    cashierName: String,
    branchName: String
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("TODOS") } // "TODOS", "HOY", "HISTORICO"
    var expandedFolio by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Aggregate calculations for active shift (current unclosed sales)
    val activeCash = activeSales.filter { it.paymentMethod.equals("Efectivo", ignoreCase = true) }.sumOf { it.total }
    val activeCard = activeSales.filter { it.paymentMethod.equals("Tarjeta", ignoreCase = true) }.sumOf { it.total }
    val activeTransfer = activeSales.filter { it.paymentMethod.equals("Transferencia", ignoreCase = true) }.sumOf { it.total }
    val activeTotal = activeSales.sumOf { it.total }

    // Parse historical closes metrics robustly
    val parsedCloses = remember(dailyCloses) {
        dailyCloses.map { close ->
            var cash = 0.0
            var card = 0.0
            var transfer = 0.0
            var initialFund = 0.0
            var countedCash = 0.0
            var diff = 0.0

            try {
                if (close.summaryJson.isNotBlank()) {
                    // Handle potential single quotes in JSON string
                    val cleanJsonStr = if (close.summaryJson.contains("'")) {
                        close.summaryJson.replace("'", "\"")
                    } else {
                        close.summaryJson
                    }
                    val json = JSONObject(cleanJsonStr)

                    cash = json.optDouble("ventasEfectivo", json.optDouble("cash", json.optDouble("cashSales", 0.0)))
                    card = json.optDouble("ventasTarjeta", json.optDouble("card", json.optDouble("cardSales", 0.0)))
                    transfer = json.optDouble("ventasTransferencia", json.optDouble("transfer", json.optDouble("transferSales", 0.0)))
                    initialFund = json.optDouble("fondoInicial", json.optDouble("initialFund", 0.0))
                    countedCash = json.optDouble("efectivoContado", json.optDouble("countedCash", 0.0))
                    diff = json.optDouble("diferencia", json.optDouble("diff", 0.0))
                }
            } catch (e: Exception) {
                // If parsing fails, use close.totalSales directly without guessing artificial splits
                cash = close.totalSales
                card = 0.0
                transfer = 0.0
            }

            // Fallback: If no breakdown was stored but totalSales > 0 and sum of methods is 0
            if (cash == 0.0 && card == 0.0 && transfer == 0.0 && close.totalSales > 0.0) {
                cash = close.totalSales
            }

            ParsedDailyClose(
                entity = close,
                cashSales = cash,
                cardSales = card,
                transferSales = transfer,
                initialFund = initialFund,
                countedCash = countedCash,
                difference = diff
            )
        }
    }

    val totalHistoricalSales = parsedCloses.sumOf { it.entity.totalSales }
    val totalHistoricalCash = parsedCloses.sumOf { it.cashSales }
    val totalHistoricalCard = parsedCloses.sumOf { it.cardSales }
    val totalHistoricalTransfer = parsedCloses.sumOf { it.transferSales }

    val grandCombinedTotal = totalHistoricalSales + activeTotal
    val grandCombinedCash = totalHistoricalCash + activeCash
    val grandCombinedCard = totalHistoricalCard + activeCard
    val grandCombinedTransfer = totalHistoricalTransfer + activeTransfer

    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val filteredCloses = remember(parsedCloses, searchQuery, selectedFilter) {
        parsedCloses.filter { item ->
            val matchesSearch = searchQuery.isBlank() ||
                    item.entity.folio.contains(searchQuery, ignoreCase = true) ||
                    item.entity.closedBy.contains(searchQuery, ignoreCase = true)
            val matchesFilter = when (selectedFilter) {
                "HOY" -> item.entity.closeDate == todayStr
                "HISTORICO" -> item.entity.closeDate != todayStr
                else -> true
            }
            matchesSearch && matchesFilter
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Resumen Financiero y Cierres Diarios",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Consolidado de ventas por turno y desglose de formas de pago en Quetzales (Q)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                FilledTonalButton(
                    onClick = {
                        ReportExportHelper.printOrExportShiftPdf(
                            context = context,
                            cashierName = cashierName,
                            branchName = branchName,
                            initialFund = parsedCloses.firstOrNull()?.initialFund ?: 0.0,
                            sales = activeSales,
                            totalCash = grandCombinedCash,
                            totalCard = grandCombinedCard,
                            totalTransfer = grandCombinedTransfer,
                            grandTotal = grandCombinedTotal
                        )
                    },
                    modifier = Modifier.testTag("btn_exportar_resumen_financiero"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Imprimir / PDF", fontSize = 12.sp)
                }
            }
        }

        // Global Consolidated KPI Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOTAL VENTAS CONSOLIDADAS (TURNOS + TURNO ACTIVO)",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = "${parsedCloses.size + if (activeSales.isNotEmpty()) 1 else 0} turnos",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = formatQuetzales(grandCombinedTotal),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Breakdown by payment method
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PaymentMetricBox(
                            title = "Efectivo",
                            amount = grandCombinedCash,
                            icon = Icons.Default.Payments,
                            containerColor = Color(0xFFDCFCE7),
                            contentColor = Color(0xFF166534),
                            modifier = Modifier.weight(1f)
                        )
                        PaymentMetricBox(
                            title = "Tarjeta",
                            amount = grandCombinedCard,
                            icon = Icons.Default.CreditCard,
                            containerColor = Color(0xFFE0E7FF),
                            contentColor = Color(0xFF3730A3),
                            modifier = Modifier.weight(1f)
                        )
                        PaymentMetricBox(
                            title = "Transferencia",
                            amount = grandCombinedTransfer,
                            icon = Icons.Default.AccountBalance,
                            containerColor = Color(0xFFFEF3C7),
                            contentColor = Color(0xFF92400E),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Payment method distribution visual bar
                    if (grandCombinedTotal > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        val cashPct = (grandCombinedCash / grandCombinedTotal).toFloat().coerceIn(0f, 1f)
                        val cardPct = (grandCombinedCard / grandCombinedTotal).toFloat().coerceIn(0f, 1f)
                        val transPct = (grandCombinedTransfer / grandCombinedTotal).toFloat().coerceIn(0f, 1f)

                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            ) {
                                if (cashPct > 0) {
                                    Box(modifier = Modifier.weight(cashPct.coerceAtLeast(0.01f)).fillMaxHeight().background(Color(0xFF16A34A)))
                                }
                                if (cardPct > 0) {
                                    Box(modifier = Modifier.weight(cardPct.coerceAtLeast(0.01f)).fillMaxHeight().background(Color(0xFF4F46E5)))
                                }
                                if (transPct > 0) {
                                    Box(modifier = Modifier.weight(transPct.coerceAtLeast(0.01f)).fillMaxHeight().background(Color(0xFFD97706)))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Efectivo: ${String.format(Locale.US, "%.1f", cashPct * 100)}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF166534),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Tarjeta: ${String.format(Locale.US, "%.1f", cardPct * 100)}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF3730A3),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Transf: ${String.format(Locale.US, "%.1f", transPct * 100)}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF92400E),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Turn Summary Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = CircleShape, color = EmeraldSuccess, modifier = Modifier.size(10.dp)) {}
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Turno en Vivo (Actual)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Text(
                            text = "${activeSales.size} transacciones registradas",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Ventas Efectivo:", style = MaterialTheme.typography.bodySmall)
                            Text(formatQuetzales(activeCash), fontWeight = FontWeight.Bold, color = Color(0xFF166534))
                        }
                        Column {
                            Text("Ventas Tarjeta:", style = MaterialTheme.typography.bodySmall)
                            Text(formatQuetzales(activeCard), fontWeight = FontWeight.Bold, color = Color(0xFF3730A3))
                        }
                        Column {
                            Text("Ventas Transf:", style = MaterialTheme.typography.bodySmall)
                            Text(formatQuetzales(activeTransfer), fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                        }
                        Column {
                            Text("Total Turno:", style = MaterialTheme.typography.bodySmall)
                            Text(formatQuetzales(activeTotal), fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        // Search & Filter Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar por folio o responsable...", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    trailingIcon = if (searchQuery.isNotEmpty()) {
                        {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpiar", modifier = Modifier.size(16.dp))
                            }
                        }
                    } else null,
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Historial de Cortes y Cierres (${filteredCloses.size})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    val filters = listOf("TODOS" to "Todos", "HOY" to "Hoy", "HISTORICO" to "Histórico")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(filters) { (key, label) ->
                            FilterChip(
                                selected = selectedFilter == key,
                                onClick = { selectedFilter = key },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }
        }

        // List of Historical Daily Closes or Empty State
        if (filteredCloses.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = Icons.Default.ReceiptLong,
                    title = "No hay registros de cierre de turno",
                    message = "Al realizar un 'Corte de Caja Diario' en el Módulo Caja o 'Cierre Diario' en Configuración, los folios y reportes financieros se almacenarán y mostrarán aquí."
                )
            }
        } else {
            items(filteredCloses, key = { it.entity.id }) { item ->
                DailyCloseRowCard(
                    parsedClose = item,
                    isExpanded = expandedFolio == item.entity.folio,
                    onToggleExpand = {
                        expandedFolio = if (expandedFolio == item.entity.folio) null else item.entity.folio
                    },
                    onShareClose = {
                        shareDailyCloseText(context, item, branchName)
                    }
                )
            }
        }
    }
}

private fun shareDailyCloseText(context: Context, item: ParsedDailyClose, branchName: String) {
    val close = item.entity
    val dateStr = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(Date(close.timestamp))
    val text = buildString {
        appendLine("📊 *REPORTE DE CORTE DE CAJA - RESTAURANTE RIVERA* 📊")
        appendLine("🏢 *Sucursal:* $branchName")
        appendLine("📋 *Folio:* ${close.folio.ifBlank { "CORTE #${close.id}" }}")
        appendLine("📅 *Fecha:* $dateStr")
        appendLine("👤 *Responsable:* ${close.closedBy}")
        appendLine("🧾 *Comandas:* ${close.totalOrders}")
        appendLine("------------------------------------------")
        appendLine("💰 *Ventas Totales:* ${formatQuetzales(close.totalSales)}")
        appendLine("💵 *Efectivo:* ${formatQuetzales(item.cashSales)}")
        appendLine("💳 *Tarjeta:* ${formatQuetzales(item.cardSales)}")
        appendLine("📲 *Transferencia:* ${formatQuetzales(item.transferSales)}")
        if (item.initialFund > 0 || item.countedCash > 0) {
            appendLine("------------------------------------------")
            appendLine("🏦 *Fondo Inicial:* ${formatQuetzales(item.initialFund)}")
            appendLine("📥 *Efectivo Contado:* ${formatQuetzales(item.countedCash)}")
            val diffPrefix = if (item.difference >= 0) "+ " else "- "
            appendLine("⚖️ *Diferencia / Arqueo:* $diffPrefix${formatQuetzales(Math.abs(item.difference))}")
        }
    }

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(sendIntent, "Compartir Corte ${close.folio}"))
}

data class ParsedDailyClose(
    val entity: DailyCloseEntity,
    val cashSales: Double,
    val cardSales: Double,
    val transferSales: Double,
    val initialFund: Double,
    val countedCash: Double,
    val difference: Double
)

@Composable
fun PaymentMetricBox(
    title: String,
    amount: Double,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(title, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = contentColor)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatQuetzales(amount),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = contentColor
            )
        }
    }
}

@Composable
fun DailyCloseRowCard(
    parsedClose: ParsedDailyClose,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onShareClose: () -> Unit = {}
) {
    val close = parsedClose.entity
    val dateStr = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(Date(close.timestamp))
    val isBalanced = Math.abs(parsedClose.difference) < 0.01
    val isSurplus = parsedClose.difference > 0

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleExpand() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = close.folio.ifBlank { "CORTE #${close.id}" },
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "${close.totalOrders} comandas",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Responsable: ${close.closedBy} • $dateStr",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatQuetzales(close.totalSales),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Desglose de Formas de Pago del Turno:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("💵 Efectivo: ${formatQuetzales(parsedClose.cashSales)}", style = MaterialTheme.typography.bodySmall)
                        Text("💳 Tarjeta: ${formatQuetzales(parsedClose.cardSales)}", style = MaterialTheme.typography.bodySmall)
                        Text("📲 Transf: ${formatQuetzales(parsedClose.transferSales)}", style = MaterialTheme.typography.bodySmall)
                    }

                    if (parsedClose.initialFund > 0 || parsedClose.countedCash > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Fondo Inicial: ${formatQuetzales(parsedClose.initialFund)}", style = MaterialTheme.typography.bodySmall)
                            Text("Efectivo Contado: ${formatQuetzales(parsedClose.countedCash)}", style = MaterialTheme.typography.bodySmall)
                            val diffColor = if (isBalanced) EmeraldSuccess else if (isSurplus) Color(0xFF166534) else Color(0xFFDC2626)
                            Text(
                                "Diferencia: ${if (isSurplus) "+" else ""}${formatQuetzales(parsedClose.difference)}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = diffColor)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onShareClose,
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Compartir Corte", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
