package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.DetailedSalesReportData
import com.example.data.entity.SoldProductReportItem
import com.example.ui.theme.EmeraldSuccess
import com.example.util.ReportExportHelper
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedSalesReportView(
    report: DetailedSalesReportData,
    categoryFilter: String,
    searchQuery: String,
    waiterFilter: String,
    sortOption: String,
    waiterList: List<String>,
    onCategoryFilterChange: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onWaiterFilterChange: (String) -> Unit,
    onSortOptionChange: (String) -> Unit
) {
    val context = LocalContext.current
    var showPrintDialog by remember { mutableStateOf(false) }
    var showWaiterDropdown by remember { mutableStateOf(false) }
    var showSortDropdown by remember { mutableStateOf(false) }

    val categories = listOf("Todas", "Platillos", "Bebidas", "Postres", "Entradas")
    val sortOptions = listOf("Más vendidos", "Menos vendidos", "Mayor ingreso", "Cantidad")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📑 Reporte Detallado de Ventas",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "${report.itemsByCategory.values.sumOf { it.size }} productos",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Action Buttons Bar (Print, PDF, Excel, WhatsApp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = { showPrintDialog = true },
                modifier = Modifier
                    .weight(1f)
                    .testTag("btn_imprimir_reporte"),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Imprimir", fontSize = 11.sp, maxLines = 1)
            }

            OutlinedButton(
                onClick = { ReportExportHelper.printOrExportPdf(context, report) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("btn_exportar_pdf"),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFFDC2626))
                Spacer(modifier = Modifier.width(4.dp))
                Text("PDF", fontSize = 11.sp, maxLines = 1)
            }

            OutlinedButton(
                onClick = { ReportExportHelper.exportToCsvAndShare(context, report) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("btn_exportar_excel"),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.TableChart, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF16A34A))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Excel", fontSize = 11.sp, maxLines = 1)
            }

            Button(
                onClick = { ReportExportHelper.shareViaWhatsApp(context, report) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("btn_compartir_whatsapp"),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("WhatsApp", fontSize = 11.sp, color = Color.White, maxLines = 1)
            }
        }

        // Search & Filters Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Product Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar producto...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    trailingIcon = if (searchQuery.isNotEmpty()) {
                        {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpiar", modifier = Modifier.size(16.dp))
                            }
                        }
                    } else null,
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                // Category Filter Chips
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Categoría:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { cat ->
                            FilterChip(
                                selected = categoryFilter.equals(cat, ignoreCase = true),
                                onClick = { onCategoryFilterChange(cat) },
                                label = { Text(cat, fontSize = 11.sp) }
                            )
                        }
                    }
                }

                // Waiter & Sort Dropdown Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Waiter Filter Dropdown
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(
                            onClick = { showWaiterDropdown = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Mesero: $waiterFilter",
                                fontSize = 11.sp,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                        }

                        DropdownMenu(
                            expanded = showWaiterDropdown,
                            onDismissRequest = { showWaiterDropdown = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Todos los meseros") },
                                onClick = {
                                    onWaiterFilterChange("Todos")
                                    showWaiterDropdown = false
                                }
                            )
                            waiterList.forEach { waiter ->
                                DropdownMenuItem(
                                    text = { Text(waiter) },
                                    onClick = {
                                        onWaiterFilterChange(waiter)
                                        showWaiterDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    // Sort Filter Dropdown
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(
                            onClick = { showSortDropdown = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Sort, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Orden: $sortOption",
                                fontSize = 11.sp,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                        }

                        DropdownMenu(
                            expanded = showSortDropdown,
                            onDismissRequest = { showSortDropdown = false }
                        ) {
                            sortOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        onSortOptionChange(option)
                                        showSortDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Grouped Tables by Category
        val categoriesToShow = listOf("Platillos", "Bebidas", "Postres", "Entradas")
        val displayCategories = (categoriesToShow + report.itemsByCategory.keys).distinct()

        displayCategories.forEach { catName ->
            val itemsInCat = report.itemsByCategory[catName] ?: emptyList()

            if (categoryFilter == "Todas" || categoryFilter.equals(catName, ignoreCase = true)) {
                CategoryReportTableCard(
                    categoryName = catName,
                    items = itemsInCat
                )
            }
        }

        // Executive Summary Card (Resumen Final)
        ExecutiveSummaryCard(report = report)
    }

    // Modal Thermal Print Dialog
    if (showPrintDialog) {
        val printableText = ReportExportHelper.buildTextSummary(report)
        AlertDialog(
            onDismissRequest = { showPrintDialog = false },
            icon = { Icon(Icons.Default.Print, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text("Vista Previa de Impresión", fontWeight = FontWeight.Bold) },
            text = {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp)
                ) {
                    Text(
                        text = printableText,
                        color = Color(0xFF38BDF8),
                        fontSize = 11.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(context, "Reporte enviado a la cola de impresión", Toast.LENGTH_SHORT).show()
                        showPrintDialog = false
                    }
                ) {
                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Imprimir Ahora")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPrintDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun CategoryReportTableCard(
    categoryName: String,
    items: List<SoldProductReportItem>
) {
    val totalQty = items.sumOf { it.quantitySold }
    val totalCatRevenue = items.sumOf { it.totalRevenue }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Category Table Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = categoryName.uppercase(),
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "($totalQty uds)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = formatQuetzales(totalCatRevenue),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSuccess
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (items.isEmpty()) {
                Text(
                    text = "No hay ventas registradas para esta categoría en el período.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            } else {
                // Table Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Producto", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(2.2f))
                    Text("P. Unit", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1.2f))
                    Text("Cant", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(0.9f))
                    Text("Total (Q)", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1.5f))
                    if (categoryName.equals("Platillos", ignoreCase = true)) {
                        Text("% Part.", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1.1f))
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outlineVariant)

                // Item Rows
                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.productName,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.weight(2.2f),
                            maxLines = 2
                        )
                        Text(
                            text = formatQuetzales(item.unitPrice),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1.2f)
                        )
                        Text(
                            text = "${item.quantitySold}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(0.9f)
                        )
                        Text(
                            text = formatQuetzales(item.totalRevenue),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.weight(1.5f)
                        )

                        if (categoryName.equals("Platillos", ignoreCase = true)) {
                            Column(modifier = Modifier.weight(1.1f)) {
                                Text(
                                    text = "${String.format(Locale.US, "%.1f", item.participationPercentage)}%",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = EmeraldSuccess
                                )
                                LinearProgressIndicator(
                                    progress = (item.participationPercentage.toFloat() / 100f).coerceIn(0f, 1f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp),
                                    color = EmeraldSuccess,
                                    trackColor = EmeraldSuccess.copy(alpha = 0.2f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExecutiveSummaryCard(report: DetailedSalesReportData) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📊 RESUMEN EJECUTIVO DE VENTAS",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF38BDF8)
                    )
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF1E293B)
                ) {
                    Text(
                        text = report.periodName.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Divider(color = Color(0xFF334155))

            // 2 Column Grid for Metrics
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ExecutiveMetricTile(
                        icon = Icons.Default.Star,
                        iconColor = Color(0xFFF59E0B),
                        title = "Producto Más Vendido",
                        value = report.mostSoldProduct?.let { "${it.first} (${it.second} uds)" } ?: "N/A",
                        modifier = Modifier.weight(1f)
                    )
                    ExecutiveMetricTile(
                        icon = Icons.Default.TrendingDown,
                        iconColor = Color(0xFFEF4444),
                        title = "Producto Menos Vendido",
                        value = report.leastSoldProduct?.let { "${it.first} (${it.second} uds)" } ?: "N/A",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ExecutiveMetricTile(
                        icon = Icons.Default.ShoppingBag,
                        iconColor = Color(0xFF10B981),
                        title = "Total Productos Vendidos",
                        value = "${report.totalProductsSold} unidades",
                        modifier = Modifier.weight(1f)
                    )
                    ExecutiveMetricTile(
                        icon = Icons.Default.AttachMoney,
                        iconColor = Color(0xFF10B981),
                        title = "Total de Ingresos",
                        value = formatQuetzales(report.totalRevenue),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ExecutiveMetricTile(
                        icon = Icons.Default.Receipt,
                        iconColor = Color(0xFF3B82F6),
                        title = "Ticket Promedio",
                        value = formatQuetzales(report.averageTicket),
                        modifier = Modifier.weight(1f)
                    )
                    ExecutiveMetricTile(
                        icon = Icons.Default.ConfirmationNumber,
                        iconColor = Color(0xFF8B5CF6),
                        title = "Número de Pedidos",
                        value = "${report.orderCount} pedidos",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ExecutiveMetricTile(
                        icon = Icons.Default.AccessTime,
                        iconColor = Color(0xFFEC4899),
                        title = "Hora de Mayor Venta",
                        value = report.peakSalesHour,
                        modifier = Modifier.weight(1f)
                    )
                    ExecutiveMetricTile(
                        icon = Icons.Default.Person,
                        iconColor = Color(0xFF06B6D4),
                        title = "Mesero que Más Vendió",
                        value = report.topSellingWaiter?.let { "${it.first} (${formatQuetzales(it.second)})" } ?: "N/A",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    ExecutiveMetricTile(
                        icon = Icons.Default.TableBar,
                        iconColor = Color(0xFFF97316),
                        title = "Mesa que Más Consumió",
                        value = report.topConsumingTable?.let { "${it.first} (${formatQuetzales(it.second)})" } ?: "N/A",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ExecutiveMetricTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF1E293B),
        border = BorderStroke(1.dp, Color(0xFF334155)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8), fontSize = 10.sp)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    maxLines = 2
                )
            }
        }
    }
}
