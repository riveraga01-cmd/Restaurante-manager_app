package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.util.PermissionHelper
import com.example.data.entity.OrderEntity
import com.example.data.entity.OrderItemEntity
import com.example.ui.theme.BentoPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.util.PrinterConnectionType
import com.example.util.ThermalPrinterConfig
import com.example.util.ThermalPrinterManager
import kotlinx.coroutines.launch

@Composable
fun ThermalPrinterDialog(
    title: String = "Imprimir Ticket Térmico",
    order: OrderEntity,
    items: List<OrderItemEntity>,
    isKitchenComanda: Boolean = true,
    cashierName: String = "",
    amountReceived: Double = 0.0,
    change: Double = 0.0,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val printerManager = remember { ThermalPrinterManager(context) }

    var connectionType by remember { mutableStateOf(PrinterConnectionType.NETWORK_IP) }
    var ipAddress by remember { mutableStateOf("192.168.1.200") }
    var portText by remember { mutableStateOf("9100") }
    var selectedBtAddress by remember { mutableStateOf("") }
    var selectedBtName by remember { mutableStateOf("") }

    var hasBtPermission by remember {
        mutableStateOf(PermissionHelper.isBluetoothPermissionGranted(context))
    }

    var pairedDevices by remember(hasBtPermission) {
        mutableStateOf(if (hasBtPermission) printerManager.getPairedBluetoothDevices() else emptyList())
    }

    val btPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val granted = results.values.all { it } || PermissionHelper.isBluetoothPermissionGranted(context)
        hasBtPermission = granted
        if (granted) {
            pairedDevices = printerManager.getPairedBluetoothDevices()
        }
    }

    var isPrinting by remember { mutableStateOf(false) }
    var printResultMsg by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }

    val previewText = remember(order, items, isKitchenComanda, cashierName, amountReceived, change) {
        if (isKitchenComanda) {
            printerManager.generateKitchenTicketText(order, items)
        } else {
            printerManager.generateCashierTicketText(order, items, cashierName, amountReceived, change)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Print,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Connection Selector
                Text(
                    text = "Método de Conexión de Impresora:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = connectionType == PrinterConnectionType.NETWORK_IP,
                        onClick = { connectionType = PrinterConnectionType.NETWORK_IP },
                        label = { Text("Red Local (IP/Ethernet)") },
                        leadingIcon = {
                            Icon(Icons.Default.Router, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        modifier = Modifier.weight(1f)
                    )

                    FilterChip(
                        selected = connectionType == PrinterConnectionType.BLUETOOTH,
                        onClick = { connectionType = PrinterConnectionType.BLUETOOTH },
                        label = { Text("Bluetooth") },
                        leadingIcon = {
                            Icon(Icons.Default.Bluetooth, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                if (connectionType == PrinterConnectionType.NETWORK_IP) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = ipAddress,
                            onValueChange = { ipAddress = it },
                            label = { Text("IP Impresora") },
                            placeholder = { Text("192.168.1.200") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(0.65f)
                        )
                        OutlinedTextField(
                            value = portText,
                            onValueChange = { portText = it },
                            label = { Text("Puerto") },
                            placeholder = { Text("9100") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(0.35f)
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Dispositivos Bluetooth Emparejados:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (!hasBtPermission) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Permiso de Bluetooth Requerido",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color(0xFF92400E)
                                    )
                                    Text(
                                        text = "Para detectar y conectar impresoras térmicas ESC/POS, conceda el permiso de Dispositivos Cercanos / Bluetooth.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF78350F)
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = {
                                                btPermissionLauncher.launch(PermissionHelper.getBluetoothRequiredPermissions())
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Activar Bluetooth", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                        OutlinedButton(
                                            onClick = { PermissionHelper.openAppSettings(context) },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Ajustes", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        } else if (pairedDevices.isEmpty()) {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "No se encontraron impresoras Bluetooth emparejadas en este dispositivo.",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        } else {
                            pairedDevices.forEach { (name, mac) ->
                                val isSelected = selectedBtAddress == mac
                                OutlinedButton(
                                    onClick = {
                                        selectedBtAddress = mac
                                        selectedBtName = name
                                    },
                                    border = BorderStroke(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) BentoPrimary else MaterialTheme.colorScheme.outline
                                    ),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else Color.Transparent
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = name, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                        Text(text = mac, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }

                // Status Message Box
                if (printResultMsg != null) {
                    Surface(
                        color = if (isError) MaterialTheme.colorScheme.errorContainer else EmeraldSuccess.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isError) Icons.Default.Warning else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (isError) MaterialTheme.colorScheme.error else EmeraldSuccess
                            )
                            Text(
                                text = printResultMsg ?: "",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = if (isError) MaterialTheme.colorScheme.onErrorContainer else EmeraldSuccess
                                )
                            )
                        }
                    }
                }

                // Ticket Preview Card
                Text(
                    text = "Vista Previa de Comanda (ESC/POS 80mm):",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Surface(
                    color = Color(0xFFFFFFF0), // Paper light yellow tint
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = previewText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = !isPrinting,
                onClick = {
                    scope.launch {
                        isPrinting = true
                        printResultMsg = null
                        isError = false

                        val config = ThermalPrinterConfig(
                            connectionType = connectionType,
                            bluetoothAddress = selectedBtAddress,
                            bluetoothName = selectedBtName,
                            ipAddress = ipAddress.trim(),
                            port = portText.toIntOrNull() ?: 9100
                        )

                        val bytes = if (isKitchenComanda) {
                            printerManager.buildEscPosBytesForKitchen(order, items)
                        } else {
                            printerManager.buildEscPosBytesForReceipt(order, items, cashierName, amountReceived, change)
                        }

                        val result = printerManager.printBytes(config, bytes)

                        isPrinting = false
                        if (result.isSuccess) {
                            printResultMsg = result.getOrNull() ?: "Impreso correctamente."
                            isError = false
                        } else {
                            val err = result.exceptionOrNull()?.message ?: "Error desconocido"
                            printResultMsg = "No se pudo conectar a la impresora (${err}). Sincronización simulada completada."
                            isError = true
                        }
                    }
                }
            ) {
                if (isPrinting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviando...")
                } else {
                    Icon(imageVector = Icons.Default.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Enviar a Impresora")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
