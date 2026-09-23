package com.example.ui.components

import android.content.Context
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

    // 0: QR General / A Domicilio, 1: QR Individual por Mesa
    var selectedQrType by remember { mutableStateOf(0) }
    val baseWebMenuUrl = remember(systemSettings.website) {
        val raw = systemSettings.website.trim().let {
            if (it.isBlank() || it.contains("Restaurante-manager_app") || it == "www.restauranterivera.com") {
                "https://riveraga01-cmd.github.io/Restaurante/"
            } else {
                it
            }
        }
        if (raw.endsWith("/")) raw else "$raw/"
    }

    var selectedTableForQr by remember { mutableStateOf<TableEntity?>(null) }
    var manualTableNum by remember { mutableStateOf("1") }
    var customBannerNote by remember { mutableStateOf("¡Escanea para ver nuestro Menú Digital en tu móvil!") }

    // Helper to compute table number string
    val currentTableNum = remember(selectedTableForQr, manualTableNum) {
        val fromSelection = selectedTableForQr?.tableNumber?.filter { it.isDigit() }
        if (!fromSelection.isNullOrBlank()) {
            fromSelection
        } else if (manualTableNum.isNotBlank()) {
            manualTableNum.filter { it.isDigit() }.ifBlank { "1" }
        } else {
            "1"
        }
    }

    // Computed QR Content URL strictly according to requirements:
    // a) QR General / A Domicilio: https://riveraga01-cmd.github.io/Restaurante-manager_app/?tipo=domicilio
    // b) QR Individual por Mesa: https://riveraga01-cmd.github.io/Restaurante-manager_app/?tipo=mesa&num=X
    val currentQrContent = remember(selectedQrType, currentTableNum, baseWebMenuUrl) {
        when (selectedQrType) {
            0 -> "${baseWebMenuUrl}?tipo=domicilio"
            1 -> "${baseWebMenuUrl}?tipo=mesa&num=${currentTableNum}"
            else -> "${baseWebMenuUrl}?tipo=domicilio"
        }
    }

    var showWebSimulator by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Header & Selection of QR Type
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
                                text = "Generador de Códigos QR para Menú Web",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Genere códigos QR listos para colocar en mesas o compartir en redes sociales.",
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
                            onClick = { 
                                selectedQrType = 0 
                                customBannerNote = "¡Pide a Domicilio o Para Llevar escaneando este QR!"
                            },
                            text = { 
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.DeliveryDining, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("🛵 General / A Domicilio", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        )
                        Tab(
                            selected = selectedQrType == 1,
                            onClick = { 
                                selectedQrType = 1 
                                customBannerNote = "¡Ordena directamente desde tu mesa escaneando este QR!"
                            },
                            text = { 
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.TableBar, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("🪑 Individual por Mesa", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
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
                            Text("Configuración de QR General / A Domicilio:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                            Text(
                                text = "Este código QR dirige a la carta digital con el flujo de entrega a domicilio activado, solicitando nombre, teléfono, ubicación GPS y dirección.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            OutlinedTextField(
                                value = currentQrContent,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Enlace Generado (Automático)") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth().testTag("input_qr_domicilio_url")
                            )
                        }
                        1 -> {
                            Text("Asignación de Mesa para el QR:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                            Text(
                                text = "Seleccione o escriba el número de mesa. El QR fijará la mesa y ocultará los datos de entrega innecesarios.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Quick table selector chips
                            val tablesToDisplay = if (allTables.isNotEmpty()) {
                                allTables
                            } else {
                                listOf(
                                    TableEntity(id = 1, tableNumber = "Mesa 1"),
                                    TableEntity(id = 2, tableNumber = "Mesa 2"),
                                    TableEntity(id = 3, tableNumber = "Mesa 3"),
                                    TableEntity(id = 4, tableNumber = "Mesa 4"),
                                    TableEntity(id = 5, tableNumber = "Mesa 5"),
                                    TableEntity(id = 6, tableNumber = "Mesa 6"),
                                    TableEntity(id = 7, tableNumber = "Mesa 7"),
                                    TableEntity(id = 8, tableNumber = "Mesa 8")
                                )
                            }

                            Text("Mesas del Restaurante:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(tablesToDisplay) { table ->
                                    val tNum = table.tableNumber.filter { it.isDigit() }.ifBlank { table.tableNumber }
                                    val isSelected = currentTableNum == tNum
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { 
                                            selectedTableForQr = table 
                                            manualTableNum = tNum
                                        },
                                        label = { Text("Mesa $tNum", fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                        leadingIcon = if (isSelected) {
                                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                        } else null
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = manualTableNum,
                                    onValueChange = { input ->
                                        val digitsOnly = input.filter { it.isDigit() }
                                        manualTableNum = digitsOnly
                                        selectedTableForQr = null
                                    },
                                    label = { Text("Número de Mesa") },
                                    placeholder = { Text("1") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(12.dp),
                                    leadingIcon = { Icon(Icons.Default.TableRestaurant, contentDescription = null) },
                                    modifier = Modifier.weight(1f).testTag("input_qr_table_num")
                                )
                            }

                            OutlinedTextField(
                                value = currentQrContent,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Enlace Generado por Mesa") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth().testTag("input_qr_mesa_url")
                            )
                        }
                    }

                    OutlinedTextField(
                        value = customBannerNote,
                        onValueChange = { customBannerNote = it },
                        label = { Text("Lema o Instrucción para el Cliente") },
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
                            text = if (selectedQrType == 1) "TARJETA DE MESA (STAND QR)" else "QR PROMOCIONAL A DOMICILIO",
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

                    if (selectedQrType == 1) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BentoPrimary
                        ) {
                            Text(
                                text = "🪑 Mesa $currentTableNum",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EmeraldSuccess
                        ) {
                            Text(
                                text = "🛵 A Domicilio y Para Llevar",
                                style = MaterialTheme.typography.titleSmall.copy(
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

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentQrContent,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                QRCodeHelper.copyToClipboard(context, "Enlace Menú QR", currentQrContent)
                                Toast.makeText(context, "Enlace copiado al portapapeles", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).testTag("btn_copiar_qr_link")
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copiar")
                        }

                        Button(
                            onClick = {
                                val shareTitle = if (selectedQrType == 1) "Menú QR Mesa $currentTableNum" else "Menú QR A Domicilio"
                                QRCodeHelper.shareQrImageOrText(
                                    context = context,
                                    title = shareTitle,
                                    textToShare = "${systemSettings.restaurantName} - $shareTitle:\n$currentQrContent"
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
                        Text(if (selectedQrType == 1) "📱 Probar Menú Mesa $currentTableNum" else "📱 Probar Menú A Domicilio")
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
                            text = "Sincronizado",
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
        val simTable = if (selectedQrType == 1) "Mesa $currentTableNum" else "A Domicilio"
        WebMenuSimulatorDialog(
            initialTable = simTable,
            customBaseUrl = baseWebMenuUrl,
            onDismiss = { showWebSimulator = false }
        )
    }
}
