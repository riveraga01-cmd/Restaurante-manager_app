package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.CategorySaleSummary

data class CategoryStyle(
    val primaryColor: Color,
    val secondaryColor: Color,
    val icon: ImageVector,
    val containerColor: Color
)

fun getCategoryStyle(category: String): CategoryStyle {
    return when (category.lowercase()) {
        "platillos" -> CategoryStyle(
            primaryColor = Color(0xFFEA580C),
            secondaryColor = Color(0xFFFB923C),
            icon = Icons.Default.Restaurant,
            containerColor = Color(0xFFFFEDD5)
        )
        "bebidas" -> CategoryStyle(
            primaryColor = Color(0xFF0284C7),
            secondaryColor = Color(0xFF38BDF8),
            icon = Icons.Default.LocalBar,
            containerColor = Color(0xFFE0F2FE)
        )
        "postres" -> CategoryStyle(
            primaryColor = Color(0xFF9333EA),
            secondaryColor = Color(0xFFC084FC),
            icon = Icons.Default.Cake,
            containerColor = Color(0xFFF3E8FF)
        )
        "entradas" -> CategoryStyle(
            primaryColor = Color(0xFF059669),
            secondaryColor = Color(0xFF34D399),
            icon = Icons.Default.Tapas,
            containerColor = Color(0xFFD1FAE5)
        )
        else -> CategoryStyle(
            primaryColor = Color(0xFFD97706),
            secondaryColor = Color(0xFFFBBF24),
            icon = Icons.Default.Fastfood,
            containerColor = Color(0xFFFEF3C7)
        )
    }
}

@Composable
fun CategorySalesBarChart(
    categorySales: List<CategorySaleSummary>,
    salesPeriod: String,
    modifier: Modifier = Modifier
) {
    val totalSales = categorySales.sumOf { it.totalAmount }
    val maxSale = categorySales.maxOfOrNull { it.totalAmount }?.coerceAtLeast(10.0) ?: 100.0
    val topCategory = categorySales.maxByOrNull { it.totalAmount }

    var selectedCategory by remember { mutableStateOf<CategorySaleSummary?>(null) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("chart_ventas_categoria")
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
                        text = "📊 Ventas Diarias por Categoría",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Gráfica de ventas acumuladas en Quetzales ($salesPeriod)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = formatQuetzales(totalSales),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Top Category Leader Highlight
            if (topCategory != null && topCategory.totalAmount > 0) {
                val style = getCategoryStyle(topCategory.category)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = style.containerColor,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = style.icon,
                            contentDescription = null,
                            tint = style.primaryColor,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Categoría Más Vendida: ${topCategory.category}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = style.primaryColor
                            )
                            Text(
                                text = "${formatQuetzales(topCategory.totalAmount)} (${if (totalSales > 0) String.format("%.1f", (topCategory.totalAmount / totalSales) * 100) else "0"}% del total)",
                                style = MaterialTheme.typography.bodySmall,
                                color = style.primaryColor
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = style.primaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // BAR CHART CANVAS & LAYOUT
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Y-axis gridlines & labels
                    val steps = 3
                    for (i in steps downTo 0) {
                        val levelValue = (maxSale / steps) * i
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
                                modifier = Modifier.width(36.dp)
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                thickness = 0.8.dp
                            )
                        }
                    }
                }

                // Bars Row
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 40.dp, end = 8.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Bottom
                ) {
                    categorySales.forEach { catSale ->
                        val isSelected = selectedCategory?.category == catSale.category
                        val style = getCategoryStyle(catSale.category)

                        val fillRatio = if (maxSale > 0) (catSale.totalAmount / maxSale).toFloat().coerceIn(0.04f, 1f) else 0.04f
                        val animatedHeightRatio by animateFloatAsState(
                            targetValue = fillRatio,
                            animationSpec = tween(durationMillis = 800),
                            label = "barHeight"
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable {
                                    selectedCategory = if (isSelected) null else catSale
                                }
                        ) {
                            // Amount label above bar
                            Text(
                                text = if (catSale.totalAmount > 0) "Q${catSale.totalAmount.toInt()}" else "Q0",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) style.primaryColor else MaterialTheme.colorScheme.onSurface
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Vertical Bar
                            Box(
                                modifier = Modifier
                                    .width(if (isSelected) 34.dp else 28.dp)
                                    .fillMaxHeight(animatedHeightRatio * 0.78f)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                style.secondaryColor,
                                                style.primaryColor
                                            )
                                        )
                                    )
                                    .then(
                                        if (isSelected) Modifier.border(
                                            2.dp,
                                            MaterialTheme.colorScheme.onSurface,
                                            RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                        ) else Modifier
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // X-Axis Category Labels & Percentages
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                categorySales.forEach { catSale ->
                    val style = getCategoryStyle(catSale.category)
                    val isSelected = selectedCategory?.category == catSale.category
                    val percentage = if (totalSales > 0) (catSale.totalAmount / totalSales) * 100 else 0.0

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                selectedCategory = if (isSelected) null else catSale
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = style.icon,
                                contentDescription = null,
                                tint = style.primaryColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = catSale.category,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = style.containerColor,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = "${String.format("%.1f", percentage)}%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = style.primaryColor
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Selected Category Interactive Card
            selectedCategory?.let { sel ->
                val style = getCategoryStyle(sel.category)
                val selPercentage = if (totalSales > 0) (sel.totalAmount / totalSales) * 100 else 0.0

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = style.containerColor),
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
                                imageVector = style.icon,
                                contentDescription = null,
                                tint = style.primaryColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = sel.category,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = style.primaryColor
                                    )
                                )
                                Text(
                                    text = "Ítems vendidos: ${sel.itemCount} unidades",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = style.primaryColor
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = formatQuetzales(sel.totalAmount),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = style.primaryColor
                                )
                            )
                            Text(
                                text = "${String.format("%.1f", selPercentage)}% del total",
                                style = MaterialTheme.typography.labelSmall,
                                color = style.primaryColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Progress Breakdown
            Text(
                text = "Desglose por Categoría:",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            categorySales.forEach { catSale ->
                val style = getCategoryStyle(catSale.category)
                val pct = if (totalSales > 0) (catSale.totalAmount / totalSales).toFloat() else 0f

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1.2f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(style.primaryColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = catSale.category,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    // Progress bar
                    Box(
                        modifier = Modifier
                            .weight(1.5f)
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(pct)
                                .clip(CircleShape)
                                .background(style.primaryColor)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = formatQuetzales(catSale.totalAmount),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
