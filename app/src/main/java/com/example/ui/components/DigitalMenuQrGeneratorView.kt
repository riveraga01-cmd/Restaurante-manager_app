package com.example.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.entity.MenuItemEntity
import com.example.data.entity.TableEntity
import com.example.ui.theme.BentoPrimary
import com.example.ui.theme.CardBorderColor
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.RestaurantViewModel
import com.example.util.QRCodeDisplay
import com.example.util.QRCodeHelper
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigitalMenuQrGeneratorView(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val systemSettings by viewModel.systemSettings.collectAsState()
    val allMenuItems by viewModel.allMenuItems.collectAsState()
    val allTables by viewModel.allTables.collectAsState()

    var selectedQrType by remember { mutableStateOf(0) } // 0: Menú Digital Web, 1: Pedidos WhatsApp, 2: QR por Mesa
    val defaultWebMenuUrl = "https://riveraga01-cmd.github.io/Restaurante-manager_app/"
    var targetUrl by remember(systemSettings.website) { 
        val site = systemSettings.website.trim()
        val formatted = when {
            site.isBlank() || site == "www.restauranterivera.com" || site == "https://restauranterivera.com/menu-digital" -> defaultWebMenuUrl
            site.startsWith("http://") || site.startsWith("https://") -> site
            else -> "https://$site"
        }
        mutableStateOf(formatted)
    }
    var whatsappNumber by remember(systemSettings.whatsapp) { mutableStateOf(systemSettings.whatsapp) }
    var whatsappWelcomeMsg by remember { 
        mutableStateOf("¡Hola! Deseo realizar un pedido de Restaurante Rivera:\n- Mesa / Para Llevar:\n- Detalle del pedido:\n- Dirección de entrega (si aplica):") 
    }

    var selectedTableForQr by remember { mutableStateOf<TableEntity?>(null) }
    var customBannerNote by remember { mutableStateOf("¡Escanea para ver nuestro Menú Digital en tu móvil!") }

    // Computed QR Content
    val currentQrContent = remember(selectedQrType, targetUrl, whatsappNumber, whatsappWelcomeMsg, selectedTableForQr) {
        when (selectedQrType) {
            0 -> targetUrl
            1 -> {
                val cleanPhone = whatsappNumber.replace(Regex("[^0-9]"), "")
                "https://wa.me/$cleanPhone?text=${java.net.URLEncoder.encode(whatsappWelcomeMsg, "UTF-8")}"
            }
            2 -> {
                val tableName = selectedTableForQr?.tableNumber ?: "Mesa 1"
                val baseUrl = targetUrl.trimEnd('/')
                "$baseUrl?mesa=${java.net.URLEncoder.encode(tableName, "UTF-8")}&branch=${java.net.URLEncoder.encode(systemSettings.branchName, "UTF-8")}"
            }
            else -> targetUrl
        }
    }

    var showPrintDialog by remember { mutableStateOf(false) }
    var showWebSimulator by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.QrCode2,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Generador de QR para Menú Digital & WhatsApp",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Genere códigos QR listos para imprimir y colocar en mesas o compartir en redes sociales.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    TabRow(
                        selectedTabIndex = selectedQrType,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Tab(
                            selected = selectedQrType == 0,
                            onClick = { selectedQrType = 0 },
                            text = { Text("🌐 Menú Web", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedQrType == 1,
                            onClick = { selectedQrType = 1 },
                            text = { Text("💬 WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedQrType == 2,
                            onClick = { selectedQrType = 2 },
                            text = { Text("🪑 Por Mesa", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }
        }

        // Configuration form based on selected tab
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    when (selectedQrType) {
                        0 -> {
                            Text("Configuración de Enlace Web:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                            OutlinedTextField(
                                value = targetUrl,
                                onValueChange = { targetUrl = it },
                                label = { Text("URL o Enlace del Menú Digital") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth().testTag("input_qr_menu_url")
                            )
                        }
                        1 -> {
                            Text("Configuración de Pedidos por WhatsApp:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                            OutlinedTextField(
                                value = whatsappNumber,
                                onValueChange = { whatsappNumber = it },
                                label = { Text("Número de WhatsApp del Restaurante") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth().testTag("input_qr_whatsapp_phone")
                            )

                            OutlinedTextField(
                                value = whatsappWelcomeMsg,
                                onValueChange = { whatsappWelcomeMsg = it },
                                label = { Text("Mensaje Predeterminado al Iniciar Chat") },
                                minLines = 3,
                                maxLines = 5,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().testTag("input_qr_whatsapp_msg")
                            )
                        }
                        2 -> {
                            Text("Asignación de Mesa para el QR:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                            OutlinedTextField(
                                value = targetUrl,
                                onValueChange = { targetUrl = it },
                                label = { Text("URL Base del Menú Digital") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Text("Seleccione la Mesa:", style = MaterialTheme.typography.bodySmall)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                (allTables.takeIf { it.isNotEmpty() } ?: listOf(
                                    TableEntity(id = 1, tableNumber = "Mesa 1"),
                                    TableEntity(id = 2, tableNumber = "Mesa 2"),
                                    TableEntity(id = 3, tableNumber = "Mesa 3"),
                                    TableEntity(id = 4, tableNumber = "Mesa 4"),
                                    TableEntity(id = 5, tableNumber = "Mesa 5")
                                )).take(6).forEach { table ->
                                    val isSelected = (selectedTableForQr?.tableNumber ?: "Mesa 1") == table.tableNumber
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedTableForQr = table },
                                        label = { Text(table.tableNumber) }
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = customBannerNote,
                        onValueChange = { customBannerNote = it },
                        label = { Text("Encabezado o Lema para Imprimir") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Visual Stand / Preview Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "VISTA PREVIA DEL SOPORTE DE MESA",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = systemSettings.restaurantName.ifBlank { "Restaurante Rivera" },
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = customBannerNote,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (selectedQrType == 2) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BentoPrimary
                        ) {
                            Text(
                                text = "🪑 ${selectedTableForQr?.tableNumber ?: "Mesa 1"}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }

                    // QR Display Box
                    QRCodeDisplay(
                        content = currentQrContent,
                        size = 200.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Text(
                        text = currentQrContent,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 2,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                QRCodeHelper.copyToClipboard(context, "QR Link", currentQrContent)
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).testTag("btn_copiar_qr_link")
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copiar Enlace")
                        }

                        Button(
                            onClick = {
                                QRCodeHelper.shareQrImageOrText(
                                    context = context,
                                    title = "Compartir Menú QR",
                                    textToShare = "${systemSettings.restaurantName} - Menú Digital:\n$currentQrContent"
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.weight(1f).testTag("btn_compartir_qr")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Compartir")
                        }
                    }

                    FilledTonalButton(
                        onClick = {
                            showWebSimulator = true
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("btn_abrir_simulador_web")
                    ) {
                        Icon(Icons.Default.PhoneAndroid, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("📱 Probar Menú Web del Cliente (Simulador)")
                    }

                    if (selectedQrType == 1) {
                        FilledTonalButton(
                            onClick = {
                                QRCodeHelper.openWhatsAppMessage(context, whatsappNumber, whatsappWelcomeMsg)
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("btn_probar_whatsapp")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Probar Chat de WhatsApp Directo")
                        }
                    }
                }
            }
        }

        // Summary of Menu Items included in Menu
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Platillos Disponibles en el Menú (${allMenuItems.filter { it.isAvailable }.size})",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Actualizado en Vivo",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldSuccess
                        )
                    }

                    allMenuItems.take(5).forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "• ${item.name}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Q${String.format(Locale.US, "%.2f", item.price)}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                    if (allMenuItems.size > 5) {
                        Text(
                            text = "... y ${allMenuItems.size - 5} platillos más en el catálogo interactivo.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    if (showWebSimulator) {
        val currentTableNum = selectedTableForQr?.tableNumber ?: "Mesa 1"
        WebMenuSimulatorDialog(
            initialTable = currentTableNum,
            onDismiss = { showWebSimulator = false }
        )
    }
}
