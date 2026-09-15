package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.SaleEntity
import com.example.ui.theme.EmeraldSuccess
import java.text.SimpleDateFormat
import java.util.*

data class DaySaleSummary(
    val dayOfMonth: Int,
    val dateString: String, // e.g. "28 Jul"
    val totalSales: Double,
    val transactionCount: Int,
    val isToday: Boolean = false
)

@Composable
fun MonthlySalesBarChart(
    salesList: List<SaleEntity>,
    modifier: Modifier = Modifier
) {
    val calendar = Calendar.getInstance()
    val currentMonthName = SimpleDateFormat("MMMM yyyy", Locale("es", "GT")).format(calendar.time).replaceFirstChar { it.uppercase() }
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val todayDay = calendar.get(Calendar.DAY_OF_MONTH)
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)

    // Group sales by day of current month
    val dailySalesMap = remember(salesList) {
        val map = mutableMapOf<Int, MutableList<SaleEntity>>()
        val cal = Calendar.getInstance()
        salesList.forEach { sale ->
            cal.timeInMillis = sale.timestamp
            if (cal.get(Calendar.YEAR) == currentYear && cal.get(Calendar.MONTH) == currentMonth) {
                val day = cal.get(Calendar.DAY_OF_MONTH)
                map.getOrPut(day) { mutableListOf() }.add(sale)
            }
        }
        map
    }

    // Build complete daily breakdown list for days 1 to daysInMonth (or up to today)
    val dailySummaries = remember(dailySalesMap, daysInMonth, todayDay) {
        val cal = Calendar.getInstance()
        (1..daysInMonth).map { day ->
            cal.set(Calendar.DAY_OF_MONTH, day)
            val dateLabel = SimpleDateFormat("d MMM", Locale("es", "GT")).format(cal.time)
            val salesForDay = dailySalesMap[day] ?: emptyList()
            DaySaleSummary(
                dayOfMonth = day,
                dateString = dateLabel,
                totalSales = salesForDay.sumOf { it.total },
                transactionCount = salesForDay.size,
                isToday = day == todayDay
            )
        }
    }

    val totalMonthSales = dailySummaries.sumOf { it.totalSales }
    val maxDaySale = dailySummaries.maxOfOrNull { it.totalSales }?.coerceAtLeast(100.0) ?: 1000.0
    val peakDay = dailySummaries.maxByOrNull { it.totalSales }
    val daysWithSalesCount = dailySummaries.count { it.totalSales > 0 }.coerceAtLeast(1)
    val averageDailySales = totalMonthSales / daysWithSalesCount

    var selectedDaySummary by remember { mutableStateOf<DaySaleSummary?>(dailySummaries.find { it.isToday } ?: dailySummaries.lastOrNull()) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("chart_ventas_mensuales_por_dia")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "📈 Ventas Diarias del Mes ($currentMonthName)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Ventas totales acumuladas por día expresadas en Quetzales (Q)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = formatQuetzales(totalMonthSales),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Stats Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Promedio Diario", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            formatQuetzales(averageDailySales),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                if (peakDay != null && peakDay.totalSales > 0) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EmeraldSuccess.copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Día Réplica Máx (${peakDay.dayOfMonth})", style = MaterialTheme.typography.labelSmall, color = EmeraldSuccess)
                            Text(
                                formatQuetzales(peakDay.totalSales),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BAR CHART AREA WITH HORIZONTAL SCROLL FOR FULL MONTH VISIBILITY
            val scrollState = rememberScrollState()
            LaunchedEffect(Unit) {
                // Scroll near today's bar automatically
                if (todayDay > 5) {
                    scrollState.scrollTo((todayDay - 3) * 110)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                // Y-Axis background gridlines
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    val steps = 3
                    for (i in steps downTo 0) {
                        val levelValue = (maxDaySale / steps) * i
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Q${levelValue.toInt()}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.outline
                                ),
                                modifier = Modifier.width(42.dp)
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                thickness = 0.8.dp
                            )
                        }
                    }
                }

                // Scrollable Row of Bars
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 46.dp, bottom = 4.dp)
                        .horizontalScroll(scrollState),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    dailySummaries.forEach { daySummary ->
                        val isSelected = selectedDaySummary?.dayOfMonth == daySummary.dayOfMonth
                        val fillRatio = if (maxDaySale > 0) (daySummary.totalSales / maxDaySale).toFloat().coerceIn(0.03f, 1f) else 0.03f

                        val animatedRatio by animateFloatAsState(
                            targetValue = fillRatio,
                            animationSpec = tween(durationMillis = 600),
                            label = "monthlyBarRatio"
                        )

                        val barColor = when {
                            isSelected -> MaterialTheme.colorScheme.primary
                            daySummary.isToday -> EmeraldSuccess
                            daySummary.totalSales > 0 -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier
                                .width(36.dp)
                                .fillMaxHeight()
                                .clickable {
                                    selectedDaySummary = daySummary
                                }
                        ) {
                            // Amount text above bar if present
                            if (daySummary.totalSales > 0) {
                                Text(
                                    text = "Q${daySummary.totalSales.toInt()}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 8.5.sp,
                                        fontWeight = if (isSelected || daySummary.isToday) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) barColor else MaterialTheme.colorScheme.onSurface
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                            }

                            // Single Vertical Bar
                            Box(
                                modifier = Modifier
                                    .width(if (isSelected) 28.dp else 22.dp)
                                    .fillMaxHeight(animatedRatio * 0.78f)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                barColor.copy(alpha = 0.7f),
                                                barColor
                                            )
                                        )
                                    )
                                    .then(
                                        if (isSelected) Modifier.border(
                                            2.dp,
                                            MaterialTheme.colorScheme.onSurface,
                                            RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                        ) else Modifier
                                    )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Day Number Label
                            Text(
                                text = "${daySummary.dayOfMonth}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = if (daySummary.isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (daySummary.isToday) EmeraldSuccess else MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }

            // Legend / Day Detail Card
            selectedDaySummary?.let { selDay ->
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selDay.isToday) EmeraldSuccess.copy(alpha = 0.12f)
                        else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (selDay.isToday) Icons.Default.Today else Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = if (selDay.isToday) EmeraldSuccess else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Día ${selDay.dayOfMonth} - ${selDay.dateString} ${if (selDay.isToday) " (HOY)" else ""}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Transacciones registradas: ${selDay.transactionCount} pedidos",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = formatQuetzales(selDay.totalSales),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (selDay.totalSales > 0) EmeraldSuccess else MaterialTheme.colorScheme.outline
                                )
                            )
                            if (totalMonthSales > 0 && selDay.totalSales > 0) {
                                Text(
                                    text = "${String.format("%.1f", (selDay.totalSales / totalMonthSales) * 100)}% del mes",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
