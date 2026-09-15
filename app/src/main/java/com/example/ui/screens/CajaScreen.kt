package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.data.entity.OrderEntity
import com.example.data.entity.OrderItemEntity
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.ModuleTopBar
import com.example.ui.components.NuevosPedidosDigitalesAlert
import com.example.ui.components.ThermalPrinterDialog
import com.example.ui.components.UserSwitchDialog
import com.example.ui.components.formatQuetzales
import com.example.ui.theme.*
import com.example.ui.viewmodel.CashierTab
import com.example.ui.viewmodel.RestaurantViewModel
import com.example.util.ReportExportHelper
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CajaScreen(
    viewModel: RestaurantViewModel,
    onBackToInicio: () -> Unit
) {
    val cashierTab by viewModel.cashierTab.collectAsState()
    val cashierOrders by viewModel.cashierOrders.collectAsState()
    val allSales by viewModel.allSales.collectAsState()
    val cashierName by viewModel.cashierName.collectAsState()
    val cashierUsers by viewModel.cashierUsers.collectAsState()
    val selectedPaymentOrder by viewModel.selectedPaymentOrder.collectAsState()
    val context = LocalContext.current

    var showUserSwitchDialog by remember { mutableStateOf(false) }
    var ticketToPrint by remember { mutableStateOf<Triple<OrderEntity, List<OrderItemEntity>, Triple<String, Double, Double>>?>(null) }

    androidx.activity.compose.BackHandler(enabled = true) {
        if (selectedPaymentOrder != null) {
            viewModel.setSelectedPaymentOrder(null)
        } else if (ticketToPrint != null) {
            ticketToPrint = null
        } else if (showUserSwitchDialog) {
            showUserSwitchDialog = false
        } else if (cashierTab != CashierTab.POR_COBRAR) {
            viewModel.setCashierTab(CashierTab.POR_COBRAR)
        } else {
            onBackToInicio()
        }
    }

    val totalSalesToday = allSales.sumOf { it.total }
    val cashSalesToday = allSales.filter { it.paymentMethod == "Efectivo" }.sumOf { it.total }
    val cardSalesToday = allSales.filter { it.paymentMethod == "Tarjeta" }.sumOf { it.total }
    val transferSalesToday = allSales.filter { it.paymentMethod == "Transferencia" }.sumOf { it.total }

    Scaffold(
        topBar = {
            ModuleTopBar(
                title = "Módulo Caja",
                subtitle = "Cajero: $cashierName • Por cobrar: ${cashierOrders.size} pedidos",
                onBackClick = onBackToInicio,
                actions = {
                    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
                    IconButton(onClick = { viewModel.toggleDarkTheme() }) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Cambiar Tema",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showUserSwitchDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.SwitchAccount,
                            contentDescription = "Cambiar Cajero Activo",
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
            // Sub Tabs
            val pendingWebOrdersList by viewModel.pendingWebOrders.collectAsState()

            SecondaryTabRow(
                selectedTabIndex = cashierTab.ordinal,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = cashierTab == CashierTab.POR_COBRAR,
                    onClick = { viewModel.setCashierTab(CashierTab.POR_COBRAR) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("💵 Por Cobrar (${cashierOrders.size})")
                            if (pendingWebOrdersList.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = StatusCocinaBadge,
                                    modifier = Modifier.size(8.dp)
                                ) {}
                            }
                        }
                    }
                )
                Tab(
                    selected = cashierTab == CashierTab.VENTAS_DIA,
                    onClick = { viewModel.setCashierTab(CashierTab.VENTAS_DIA) },
                    text = { Text("📊 Ventas del Día") }
                )
                Tab(
                    selected = cashierTab == CashierTab.CIERRE_TURNO,
                    onClick = { viewModel.setCashierTab(CashierTab.CIERRE_TURNO) },
                    text = { Text("📑 Cierre de Turno") }
                )
            }

            // Real-time alert for Web/QR/WhatsApp Digital Orders
            NuevosPedidosDigitalesAlert(
                viewModel = viewModel,
                assignedRole = cashierName
            )

            when (cashierTab) {
                CashierTab.POR_COBRAR -> {
                    if (cashierOrders.isEmpty()) {
                        EmptyStateCard(
                            icon = Icons.Default.CheckCircle,
                            title = "No hay pedidos pendientes de cobro",
                            message = "Los pedidos marcados como 'Finalizados' por la cocina aparecerán aquí automáticamente."
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(cashierOrders, key = { it.id }) { order ->
                                CashierOrderCard(
                                    order = order,
                                    viewModel = viewModel,
                                    onChargeClick = { viewModel.setSelectedPaymentOrder(order) },
                                    onPrintTicketClick = { items ->
                                        ticketToPrint = Triple(order, items, Triple(cashierName, 0.0, 0.0))
                                    }
                                )
                            }
                        }
                    }
                }

                CashierTab.VENTAS_DIA -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Summary Card
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "VENTAS TOTALES DEL DÍA",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = formatQuetzales(totalSalesToday),
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("💵 Efectivo: ${formatQuetzales(cashSalesToday)}", style = MaterialTheme.typography.bodyMedium)
                                    Text("💳 Tarjeta: ${formatQuetzales(cardSalesToday)}", style = MaterialTheme.typography.bodyMedium)
                                }
                                Text("📲 Transf: ${formatQuetzales(transferSalesToday)}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Action buttons row for Shift PDF and Share
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    ReportExportHelper.printOrExportShiftPdf(
                                        context = context,
                                        cashierName = cashierName,
                                        branchName = "Restaurante Rivera",
                                        initialFund = 500.0,
                                        sales = allSales,
                                        totalCash = cashSalesToday,
                                        totalCard = cardSalesToday,
                                        totalTransfer = transferSalesToday,
                                        grandTotal = totalSalesToday
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_exportar_pdf_turno")
                            ) {
                                Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("PDF Resumen Turno")
                            }

                            FilledTonalButton(
                                onClick = {
                                    ReportExportHelper.shareShiftSummaryText(
                                        context = context,
                                        cashierName = cashierName,
                                        branchName = "Restaurante Rivera",
                                        initialFund = 500.0,
                                        sales = allSales,
                                        totalCash = cashSalesToday,
                                        totalCard = cardSalesToday,
                                        totalTransfer = transferSalesToday,
                                        grandTotal = totalSalesToday
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_compartir_resumen_turno")
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Compartir")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Historial de Transacciones de Hoy (${allSales.size}):",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (allSales.isEmpty()) {
                            EmptyStateCard(
                                icon = Icons.Default.Receipt,
                                title = "Aún no se registran ventas hoy",
                                message = "Cobre pedidos desde la pestaña 'Por Cobrar'."
                            )
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(allSales, key = { it.id }) { sale ->
                                    val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(sale.timestamp))
                                    Card(
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(14.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    text = "${sale.orderNumber} • ${sale.paymentMethod}",
                                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                                )
                                                Text(
                                                    text = "Cajero: ${sale.cashierName} • Hora: $timeStr",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            Text(
                                                text = formatQuetzales(sale.total),
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = EmeraldSuccess
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                CashierTab.CIERRE_TURNO -> {
                    val coroutineScope = rememberCoroutineScope()
                    var initialCashInput by remember { mutableStateOf("500.00") }
                    var actualCashInput by remember { mutableStateOf("") }
                    var showPinModalForCorte by remember { mutableStateOf(false) }
                    var pinInputForCorte by remember { mutableStateOf("") }
                    var pinErrorForCorte by remember { mutableStateOf<String?>(null) }
                    var isVerifyingPin by remember { mutableStateOf(false) }
                    var showCloseConfirmationDialog by remember { mutableStateOf<com.example.data.entity.DailyCloseEntity?>(null) }
                    var isProcessingClose by remember { mutableStateOf(false) }

                    val initialCashVal = initialCashInput.toDoubleOrNull() ?: 0.0
                    val actualCashVal = actualCashInput.toDoubleOrNull() ?: 0.0
                    val expectedCashTotal = initialCashVal + cashSalesToday
                    val discrepancyVal = actualCashVal - expectedCashTotal

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("card_cierre_de_caja")
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "📑 CORTE DE CAJA DIARIO",
                                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = "Cajero: $cashierName • Fecha: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        Surface(
                                            shape = CircleShape,
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        ) {
                                            Text(
                                                text = "${allSales.size} Ventas",
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                                ),
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))

                                    // Breakdown Table
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("💵 Ventas en Efectivo:", style = MaterialTheme.typography.bodyMedium)
                                        Text(formatQuetzales(cashSalesToday), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("💳 Ventas con Tarjeta (POS):", style = MaterialTheme.typography.bodyMedium)
                                        Text(formatQuetzales(cardSalesToday), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("🏦 Ventas por Transferencia:", style = MaterialTheme.typography.bodyMedium)
                                        Text(formatQuetzales(transferSalesToday), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("GRAN TOTAL INGRESOS:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                        Text(
                                            formatQuetzales(totalSalesToday),
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = EmeraldSuccess
                                            )
                                        )
                                    }

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))

                                    // Input Fields for Cash drawer count
                                    Text(
                                        text = "AUDITORÍA Y CONTEO DE EFECTIVO:",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = initialCashInput,
                                        onValueChange = { initialCashInput = it },
                                        label = { Text("Fondo Inicial de Caja (Q)") },
                                        leadingIcon = { Icon(Icons.Default.MonetizationOn, contentDescription = null) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("input_fondo_inicial")
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = actualCashInput,
                                        onValueChange = { actualCashInput = it },
                                        label = { Text("Efectivo Contado Real en Caja (Q)") },
                                        leadingIcon = { Icon(Icons.Default.PointOfSale, contentDescription = null) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        placeholder = { Text("Ingrese el dinero físico contado en gaveta") },
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("input_efectivo_contado")
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Discrepancy Alert Box
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = when {
                                            actualCashInput.isBlank() -> MaterialTheme.colorScheme.surfaceVariant
                                            discrepancyVal == 0.0 -> EmeraldSuccess.copy(alpha = 0.15f)
                                            discrepancyVal > 0 -> MaterialTheme.colorScheme.primaryContainer
                                            else -> MaterialTheme.colorScheme.errorContainer
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text("Efectivo Esperado (Fondo + Ventas Cash):", style = MaterialTheme.typography.bodySmall)
                                                Text(
                                                    formatQuetzales(expectedCashTotal),
                                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                                )
                                            }

                                            if (actualCashInput.isNotBlank()) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = when {
                                                            discrepancyVal == 0.0 -> "Diferencia: Caja Cuadrada Perfecta"
                                                            discrepancyVal > 0 -> "Diferencia: Sobrante en Caja (+)"
                                                            else -> "Diferencia: Faltante en Caja (-)"
                                                        },
                                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                                    )
                                                    Text(
                                                        text = formatQuetzales(discrepancyVal),
                                                        style = MaterialTheme.typography.titleSmall.copy(
                                                            fontWeight = FontWeight.ExtraBold,
                                                            color = when {
                                                                discrepancyVal >= 0 -> EmeraldSuccess
                                                                else -> MaterialTheme.colorScheme.error
                                                            }
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(18.dp))

                                    Button(
                                        onClick = {
                                            pinInputForCorte = ""
                                            pinErrorForCorte = null
                                            showPinModalForCorte = true
                                        },
                                        enabled = !isProcessingClose && actualCashInput.isNotBlank(),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("btn_procesar_corte_caja")
                                    ) {
                                        Icon(Icons.Default.LockOpen, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(if (isProcessingClose) "Procesando Cierre..." else "Generar Corte de Caja Diario")
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedButton(
                                        onClick = {
                                            ReportExportHelper.printOrExportShiftPdf(
                                                context = context,
                                                cashierName = cashierName,
                                                branchName = "Restaurante Rivera",
                                                initialFund = initialCashVal,
                                                sales = allSales,
                                                totalCash = cashSalesToday,
                                                totalCard = cardSalesToday,
                                                totalTransfer = transferSalesToday,
                                                grandTotal = totalSalesToday,
                                                cashCounted = if (actualCashInput.isNotBlank()) actualCashVal else null,
                                                difference = if (actualCashInput.isNotBlank()) discrepancyVal else null
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("btn_imprimir_resumen_cierre")
                                    ) {
                                        Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Imprimir / Exportar PDF de Turno")
                                    }
                                }
                            }
                        }
                    }

                    // Modal de Confirmación con PIN de Gerente
                    if (showPinModalForCorte) {
                        AlertDialog(
                            onDismissRequest = {
                                if (!isVerifyingPin) {
                                    showPinModalForCorte = false
                                    pinInputForCorte = ""
                                    pinErrorForCorte = null
                                }
                            },
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Autorización de Cierre de Turno", fontWeight = FontWeight.Bold)
                                }
                            },
                            text = {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "Ingrese PIN de Gerente / Administrador para confirmar el Cierre de Turno:",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    OutlinedTextField(
                                        value = pinInputForCorte,
                                        onValueChange = {
                                            pinInputForCorte = it
                                            pinErrorForCorte = null
                                        },
                                        label = { Text("PIN de Gerente") },
                                        visualTransformation = PasswordVisualTransformation(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                        singleLine = true,
                                        isError = pinErrorForCorte != null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("input_pin_corte_caja")
                                    )
                                    if (pinErrorForCorte != null) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.ErrorOutline,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = pinErrorForCorte ?: "",
                                                color = MaterialTheme.colorScheme.error,
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                            )
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        if (pinInputForCorte.isBlank()) {
                                            pinErrorForCorte = "PIN no válido. No se realizó el corte"
                                            return@Button
                                        }
                                        isVerifyingPin = true
                                        coroutineScope.launch {
                                            val isValid = viewModel.validateManagerPin(pinInputForCorte)
                                            isVerifyingPin = false
                                            if (isValid) {
                                                showPinModalForCorte = false
                                                pinInputForCorte = ""
                                                pinErrorForCorte = null
                                                isProcessingClose = true
                                                viewModel.recordCorteDeCaja(
                                                    cashierName = cashierName,
                                                    initialCash = initialCashVal,
                                                    actualCashCounted = actualCashVal,
                                                    cardSales = cardSalesToday,
                                                    transferSales = transferSalesToday,
                                                    totalSales = totalSalesToday,
                                                    onComplete = { dailyClose ->
                                                        isProcessingClose = false
                                                        showCloseConfirmationDialog = dailyClose
                                                    }
                                                )
                                            } else {
                                                pinErrorForCorte = "PIN no válido. No se realizó el corte"
                                                viewModel.logFailedPinAttempt(
                                                    user = cashierName,
                                                    role = "CAJA",
                                                    actionContext = "Corte de Caja Diario"
                                                )
                                            }
                                        }
                                    },
                                    enabled = !isVerifyingPin && pinInputForCorte.isNotBlank(),
                                    modifier = Modifier.testTag("btn_confirmar_pin_corte")
                                ) {
                                    Text(if (isVerifyingPin) "Verificando..." else "Confirmar Cierre")
                                }
                            },
                            dismissButton = {
                                OutlinedButton(
                                    onClick = {
                                        showPinModalForCorte = false
                                        pinInputForCorte = ""
                                        pinErrorForCorte = null
                                    },
                                    enabled = !isVerifyingPin
                                ) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }

                    // Confirmation Dialog for Corte de Caja & Reset de Turno
                    showCloseConfirmationDialog?.let { dailyClose ->
                        AlertDialog(
                            onDismissRequest = {
                                viewModel.resetShiftAfterCorte {
                                    initialCashInput = ""
                                    actualCashInput = ""
                                    showCloseConfirmationDialog = null
                                }
                            },
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = EmeraldSuccess,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Corte de Caja Exitoso", fontWeight = FontWeight.Bold)
                                }
                            },
                            text = {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text("El corte de caja se ha procesado y guardado en el historial de reportes del Gerente. Al presionar Aceptar, el turno se reiniciará en blanco.")
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text("Folio: ${dailyClose.folio}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                            Text("Cajero: ${dailyClose.closedBy}", style = MaterialTheme.typography.bodySmall)
                                            Text("Total Procesado: ${formatQuetzales(dailyClose.totalSales)}", style = MaterialTheme.typography.bodySmall)
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        viewModel.resetShiftAfterCorte {
                                            initialCashInput = ""
                                            actualCashInput = ""
                                            showCloseConfirmationDialog = null
                                        }
                                    },
                                    modifier = Modifier.testTag("btn_cerrar_confirmacion_corte")
                                ) {
                                    Text("Aceptar")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Payment Processing Modal
    selectedPaymentOrder?.let { order ->
        PaymentProcessingDialog(
            order = order,
            viewModel = viewModel,
            onDismiss = { viewModel.setSelectedPaymentOrder(null) },
            onConfirmPayment = {
                com.example.util.HapticHelper.triggerSuccessVibration(context)
                viewModel.processOrderPayment(order.id) { }
            },
            onPrintTicket = { data ->
                ticketToPrint = data
            }
        )
    }

    ticketToPrint?.let { (order, items, meta) ->
        ThermalPrinterDialog(
            title = "Imprimir Ticket de Venta",
            order = order,
            items = items,
            isKitchenComanda = false,
            cashierName = meta.first,
            amountReceived = meta.second,
            change = meta.third,
            onDismiss = { ticketToPrint = null }
        )
    }

    if (showUserSwitchDialog) {
        UserSwitchDialog(
            title = "Seleccionar Cajero",
            role = "CAJA",
            users = cashierUsers,
            activeUserName = cashierName,
            onSelectUser = { user ->
                viewModel.setCurrentUser(user)
                viewModel.setCashierName(user.name)
            },
            onAddNewUser = { name, role ->
                viewModel.saveUser(0, name, role, "")
            },
            onDismiss = { showUserSwitchDialog = false }
        )
    }
}

@Composable
fun CashierOrderCard(
    order: OrderEntity,
    viewModel: RestaurantViewModel,
    onChargeClick: () -> Unit,
    onPrintTicketClick: (List<OrderItemEntity>) -> Unit = {}
) {
    val items by viewModel.getOrderItemsFlow(order.id).collectAsState(initial = emptyList())

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = order.orderNumber,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${order.tableNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFD1FAE5)
                ) {
                    Text(
                        text = "LISTO PARA COBRAR",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldSuccess,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "Mesero: ${order.waiterName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${item.quantity}x ${item.productName}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = formatQuetzales(item.subtotal),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total a Cobrar:", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = formatQuetzales(order.totalAmount),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { onPrintTicketClick(items) },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Print, contentDescription = "Ticket", modifier = Modifier.size(18.dp))
                    }

                    Button(
                        onClick = onChargeClick,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        modifier = Modifier.testTag("btn_cobrar_pedido")
                    ) {
                        Icon(Icons.Default.Payments, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Cobrar Pedido 💵")
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentProcessingDialog(
    order: OrderEntity,
    viewModel: RestaurantViewModel,
    onDismiss: () -> Unit,
    onConfirmPayment: () -> Unit,
    onPrintTicket: (Triple<OrderEntity, List<OrderItemEntity>, Triple<String, Double, Double>>) -> Unit = {}
) {
    val items by viewModel.getOrderItemsFlow(order.id).collectAsState(initial = emptyList())
    val paymentMethod by viewModel.paymentMethod.collectAsState()
    val cashierName by viewModel.cashierName.collectAsState()
    val cashTendered by viewModel.cashTendered.collectAsState()

    var customerType by remember { mutableStateOf("Consumidor Final") }
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var generatedInvoice by remember { mutableStateOf<com.example.data.entity.InvoiceEntity?>(null) }

    val totalAmount = order.totalAmount
    val cashVal = cashTendered.toDoubleOrNull() ?: 0.0
    val changeAmount = if (cashVal >= totalAmount) cashVal - totalAmount else 0.0

    if (generatedInvoice != null) {
        // Invoice Generated Confirmation Dialog
        com.example.ui.components.InvoiceDetailDialog(
            invoice = generatedInvoice!!,
            viewModel = viewModel,
            onDismiss = {
                generatedInvoice = null
                onConfirmPayment()
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Cobrar y Facturar ${order.orderNumber} (${order.tableNumber})", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "Tipo de Cliente (Opcional):",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Consumidor Final", "Clientes Varios", "Cliente Registrado").forEach { type ->
                            FilterChip(
                                selected = customerType == type,
                                onClick = {
                                    customerType = type
                                    if (type == "Consumidor Final" || type == "Clientes Varios") {
                                        customerName = ""
                                    }
                                },
                                label = {
                                    Text(
                                        text = type,
                                        maxLines = 1,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    if (customerType == "Cliente Registrado") {
                        val isCustomerNameValid = customerName.trim().length >= 3
                        val customerPhoneDigits = customerPhone.filter { it.isDigit() }
                        val isCustomerPhoneValid = customerPhoneDigits.length in 8..15

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = customerName,
                                onValueChange = { customerName = it },
                                label = { Text("Nombre del Cliente") },
                                placeholder = { Text("Mínimo 3 letras") },
                                singleLine = true,
                                isError = customerName.isNotBlank() && !isCustomerNameValid,
                                trailingIcon = {
                                    if (isCustomerNameValid) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Válido", tint = EmeraldSuccess)
                                    }
                                },
                                supportingText = {
                                    if (customerName.isNotBlank() && !isCustomerNameValid) {
                                        Text("⚠️ Mínimo 3 letras", color = MaterialTheme.colorScheme.error)
                                    } else if (isCustomerNameValid) {
                                        Text("✓ Verificado", color = EmeraldSuccess)
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_nombre_cliente_factura")
                            )

                            OutlinedTextField(
                                value = customerPhone,
                                onValueChange = { customerPhone = it },
                                label = { Text("Teléfono") },
                                placeholder = { Text("8 a 15 dígitos") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                isError = customerPhone.isNotBlank() && !isCustomerPhoneValid,
                                trailingIcon = {
                                    if (isCustomerPhoneValid) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Válido", tint = EmeraldSuccess)
                                    }
                                },
                                supportingText = {
                                    if (customerPhone.isNotBlank() && !isCustomerPhoneValid) {
                                        Text("⚠️ 8-15 dígitos", color = MaterialTheme.colorScheme.error)
                                    } else if (isCustomerPhoneValid) {
                                        Text("✓ ${customerPhoneDigits.length} dígitos", color = EmeraldSuccess)
                                    }
                                },
                                modifier = Modifier
                                    .weight(0.9f)
                                    .testTag("input_telefono_cliente_factura")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Desglose del pedido:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${item.quantity}x ${item.productName}", style = MaterialTheme.typography.bodySmall)
                            Text(formatQuetzales(item.subtotal), style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("TOTAL A PAGAR:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text(
                            formatQuetzales(totalAmount),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Método de Pago:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        listOf("Efectivo", "Tarjeta", "Transferencia").forEach { method ->
                            FilterChip(
                                selected = paymentMethod == method,
                                onClick = { viewModel.setPaymentMethod(method) },
                                label = { Text(method) }
                            )
                        }
                    }

                    if (paymentMethod == "Efectivo") {
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = cashTendered,
                            onValueChange = { viewModel.setCashTendered(it) },
                            label = { Text("Monto Recibido en Efectivo (Q)") },
                            placeholder = { Text("Ej. 200.00") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_monto_recibido_caja")
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        if (cashVal >= totalAmount) {
                            Surface(
                                color = Color(0xFFD1FAE5),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFF10B981)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "CAMBIO A ENTREGAR:",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF065F46)
                                        )
                                    )
                                    Text(
                                        text = formatQuetzales(changeAmount),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF047857)
                                        )
                                    )
                                }
                            }
                        } else if (cashTendered.isNotBlank()) {
                            Surface(
                                color = Color(0xFFFEF2F2),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFEF4444)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Monto insuficiente (Faltan ${formatQuetzales(totalAmount - cashVal)}):",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF991B1B)
                                        )
                                    )
                                    Text(
                                        text = "Q0.00",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF991B1B)
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        // For Tarjeta or Transferencia, no cash change is required
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Pago Electrónico ($paymentMethod) • Cambio:",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Q0.00",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cashierName,
                        onValueChange = { viewModel.setCashierName(it) },
                        label = { Text("Nombre Cajero(a)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Generate official invoice first
                        viewModel.createAndSaveInvoice(
                            order = order,
                            items = items,
                            customerType = customerType,
                            customerName = if (customerType == "Cliente Registrado") customerName else "",
                            customerPhone = if (customerType == "Cliente Registrado") customerPhone else "",
                            discount = 0.0
                        ) { inv ->
                            generatedInvoice = inv
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                    enabled = paymentMethod != "Efectivo" || cashVal >= totalAmount,
                    modifier = Modifier.testTag("btn_confirmar_pago")
                ) {
                    Text("Cobrar, Facturar y Completar 🧾")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }
}
