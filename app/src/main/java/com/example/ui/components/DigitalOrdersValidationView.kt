package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.entity.WebOrderEntity
import com.example.data.entity.WebOrderItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.RestaurantViewModel
import kotlinx.coroutines.delay
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

fun parseWebOrderItems(jsonStr: String): List<WebOrderItem> {
    val list = mutableListOf<WebOrderItem>()
    if (jsonStr.isBlank()) return list
    try {
        val array = JSONArray(jsonStr)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val menuItemId = obj.optLong("id", obj.optLong("menuItemId", (i + 1).toLong()))
            val productName = obj.optString("name", obj.optString("productName", "Producto"))
            val unitPrice = obj.optDouble("price", obj.optDouble("unitPrice", 0.0))
            val quantity = obj.optInt("quantity", 1)
            val subtotal = obj.optDouble("subtotal", unitPrice * quantity)
            val notes = obj.optString("notes", "")
            list.add(
                WebOrderItem(
                    id = (i + 1).toLong(),
                    menuItemId = menuItemId,
                    productName = productName,
                    unitPrice = unitPrice,
                    quantity = quantity,
                    subtotal = subtotal,
                    notes = notes
                )
            )
        }
    } catch (e: Exception) {
        android.util.Log.e("DigitalOrders", "Error parsing web order items: ${e.message}")
    }
    return list
}

fun formatElapsedMinutes(createdAt: Long): String {
    val diff = (System.currentTimeMillis() - createdAt) / 60000
    return when {
        diff < 1 -> "Hace un momento"
        diff == 1L -> "Hace 1 minuto"
        diff < 60 -> "Hace $diff min"
        else -> "${diff / 60}h ${diff % 60}m"
    }
}

/**
 * Alerta en tiempo real de "Nuevos Pedidos Digitales" para las pantallas de Mesero y Caja.
 * Incluye un banner pulsante con conteo y la lista desplegable de comandas pendientes de validación.
 */
@Composable
fun NuevosPedidosDigitalesAlert(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier,
    assignedRole: String = "Mesero",
    initiallyExpanded: Boolean = true
) {
    val pendingOrders by viewModel.pendingWebOrders.collectAsState()
    var isExpanded by remember { mutableStateOf(initiallyExpanded) }
    var orderToReject by remember { mutableStateOf<WebOrderEntity?>(null) }
    var orderApprovedFeedback by remember { mutableStateOf<String?>(null) }

    if (pendingOrders.isEmpty()) return

    // Pulse animation for alert header
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alertAlpha by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alertAlpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("alerta_nuevos_pedidos_digitales"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = StatusCocinaContainer.copy(alpha = 0.95f)
        ),
        border = BorderStroke(2.dp, StatusCocinaBadge.copy(alpha = alertAlpha))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = StatusCocinaBadge,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "Alerta",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Nuevos Pedidos Digitales",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = StatusCocinaBadge
                            ) {
                                Text(
                                    text = "${pendingOrders.size} PENDIENTE${if (pendingOrders.size > 1) "S" else ""}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "Requiere aprobación antes de enviar a Cocina",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.testTag("btn_toggle_pedidos_digitales")
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Ocultar" else "Mostrar",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Divider(color = StatusCocinaBadge.copy(alpha = 0.3f), thickness = 1.dp)

                    pendingOrders.forEach { order ->
                        DigitalOrderValidationCard(
                            order = order,
                            onApprove = {
                                viewModel.approveWebOrder(
                                    webOrderId = order.id,
                                    assignedWaiter = assignedRole,
                                    onSuccess = { posId ->
                                        orderApprovedFeedback = "Pedido #${order.webOrderId.ifBlank { "WEB-${order.id}" }} aprobado y enviado a Cocina (Comanda POS #$posId)."
                                    }
                                )
                            },
                            onReject = {
                                orderToReject = order
                            }
                        )
                    }
                }
            }
        }
    }

    // Modal de confirmación de Rechazo
    if (orderToReject != null) {
        val targetOrder = orderToReject!!
        AlertDialog(
            onDismissRequest = { orderToReject = null },
            icon = {
                Icon(
                    Icons.Default.Cancel,
                    contentDescription = null,
                    tint = StatusCanceladoBadge,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "¿Rechazar Pedido Digital?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "El pedido #${targetOrder.webOrderId.ifBlank { "WEB-${targetOrder.id}" }} proveniente de '${targetOrder.origin}' (${targetOrder.tableNumber}) será cancelado y eliminado de la cola."
                    )
                    Text(
                        text = "Total a descartar: ${formatQuetzales(targetOrder.totalAmount)}",
                        fontWeight = FontWeight.Bold,
                        color = StatusCanceladoBadge
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.rejectWebOrder(targetOrder.id)
                        orderToReject = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusCanceladoBadge),
                    modifier = Modifier.testTag("btn_confirmar_rechazo_pedido")
                ) {
                    Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("RECHAZAR PEDIDO")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { orderToReject = null }) {
                    Text("Volver")
                }
            }
        )
    }

    // Modal / Snackbar de feedback al aprobar
    if (orderApprovedFeedback != null) {
        AlertDialog(
            onDismissRequest = { orderApprovedFeedback = null },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = EmeraldSuccess,
                    modifier = Modifier.size(40.dp)
                )
            },
            title = {
                Text("¡Pedido en Cocina!", fontWeight = FontWeight.Bold, color = EmeraldSuccess)
            },
            text = {
                Text(orderApprovedFeedback ?: "")
            },
            confirmButton = {
                Button(
                    onClick = { orderApprovedFeedback = null },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess)
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}

/**
 * Tarjeta individual para cada pedido digital pendiente de validación.
 * Muestra: Origen (Mesa X o WhatsApp), lista de productos, total, botón APROBAR y botón RECHAZAR.
 */
@Composable
fun DigitalOrderValidationCard(
    order: WebOrderEntity,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = remember(order.itemsJson) { parseWebOrderItems(order.itemsJson) }
    val isWhatsApp = order.origin.contains("WhatsApp", ignoreCase = true)
    val originDisplay = remember(order.origin, order.tableNumber) {
        when {
            isWhatsApp -> "WhatsApp ${order.customerPhone.ifBlank { "" }}".trim()
            order.tableNumber.isNotBlank() -> order.tableNumber
            else -> order.origin
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("card_pedido_digital_${order.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Top row: ID / Origin Badge / Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Origin Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isWhatsApp) Color(0xFF25D366).copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (isWhatsApp) Icons.Default.Chat else Icons.Default.TableRestaurant,
                                contentDescription = null,
                                tint = if (isWhatsApp) Color(0xFF1E7E34) else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Origen: $originDisplay",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (isWhatsApp) Color(0xFF1E7E34) else MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "#${order.webOrderId.ifBlank { "WEB-${order.id}" }}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = formatElapsedMinutes(order.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Customer info & Notes if present
            if (order.customerName.isNotBlank() || order.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (order.customerName.isNotBlank()) {
                        Text(
                            text = "Cliente: ${order.customerName}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (order.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Nota: \"${order.notes}\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(8.dp))

            // Products List
            Text(
                text = "Productos solicitados:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (items.isEmpty()) {
                Text(
                    text = "No se pudieron cargar los ítems detallados.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.size(22.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${item.quantity}x",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = item.productName,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (item.notes.isNotBlank()) {
                                        Text(
                                            text = "(${item.notes})",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            Text(
                                text = formatQuetzales(item.subtotal),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(10.dp))

            // Total and Action Buttons (APROBAR / RECHAZAR)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total a Pagar:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatQuetzales(order.totalAmount),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Botón RECHAZAR
                    OutlinedButton(
                        onClick = onReject,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = StatusCanceladoBadge
                        ),
                        border = BorderStroke(1.5.dp, StatusCanceladoBadge),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_rechazar_pedido_${order.id}")
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Rechazar",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "RECHAZAR",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    // Botón APROBAR
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldSuccess,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_aprobar_pedido_${order.id}")
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Aprobar",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "APROBAR",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
