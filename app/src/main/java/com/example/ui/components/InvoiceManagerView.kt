package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.InvoiceEntity
import com.example.data.entity.OrderItemEntity
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.RestaurantViewModel
import com.example.util.HapticHelper
import com.example.util.ReportExportHelper
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun InvoiceManagerView(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val invoices by viewModel.allInvoices.collectAsState()
    val settings by viewModel.systemSettings.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Historial de Facturas, 1: Configuración Fiscal
    var searchQuery by remember { mutableStateOf("") }
    var selectedInvoiceForPreview by remember { mutableStateOf<InvoiceEntity?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val filteredInvoices = remember(invoices, searchQuery) {
        if (searchQuery.isBlank()) invoices
        else invoices.filter {
            it.invoiceNumber.contains(searchQuery, ignoreCase = true) ||
            it.orderNumber.contains(searchQuery, ignoreCase = true) ||
            it.customerName.contains(searchQuery, ignoreCase = true) ||
            it.customerNit.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Module Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "MÓDULO DE FACTURACIÓN PROFESIONAL",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "Generación automática de correlativos, datos fiscales, reimpresión y exportación PDF/WhatsApp",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Success Notification
        successMessage?.let { msg ->
            Surface(
                color = EmeraldSuccess.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = msg, style = MaterialTheme.typography.bodyMedium, color = EmeraldSuccess, modifier = Modifier.weight(1f))
                    IconButton(onClick = { successMessage = null }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            }
        }

        // Module Tabs
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Historial de Facturas (${invoices.size})", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) },
                modifier = Modifier.testTag("tab_historial_facturas")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Configuración Fiscal y Térmica", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Tune, contentDescription = null) },
                modifier = Modifier.testTag("tab_config_facturacion")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) {
            // HISTORIAL DE FACTURAS
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar por No. Factura, Comanda, Cliente o NIT...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_buscar_facturas")
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredInvoices.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No se encontraron facturas registradas", style = MaterialTheme.typography.titleMedium)
                        Text("Las facturas se generan automáticamente al realizar un cobro en Caja.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredInvoices, key = { it.id }) { invoice ->
                        InvoiceHistoryCard(
                            invoice = invoice,
                            onViewDetail = { selectedInvoiceForPreview = invoice }
                        )
                    }
                }
            }
        } else {
            // CONFIGURACIÓN FISCAL & IMPRESORA TÉRMICA (EXCLUSIVO GERENTE)
            BillingSettingsPanel(
                viewModel = viewModel,
                onSaved = { msg ->
                    HapticHelper.triggerSuccessVibration(context)
                    successMessage = msg
                }
            )
        }
    }

    // Preview and Action Dialog for selected invoice
    selectedInvoiceForPreview?.let { invoice ->
        InvoiceDetailDialog(
            invoice = invoice,
            viewModel = viewModel,
            onDismiss = { selectedInvoiceForPreview = null }
        )
    }
}

@Composable
private fun InvoiceHistoryCard(
    invoice: InvoiceEntity,
    onViewDetail: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_factura_${invoice.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = invoice.invoiceNumber,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Comanda #${invoice.orderNumber}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Cliente: ${invoice.customerName} • NIT: ${invoice.customerNit}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = "${dateFormat.format(Date(invoice.timestamp))} • Cobrado por: ${invoice.cashierName} (${invoice.paymentMethod})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatQuetzales(invoice.totalAmount),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = EmeraldSuccess)
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedButton(
                    onClick = onViewDetail,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_ver_factura_${invoice.id}")
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ver / Imprimir", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun InvoiceDetailDialog(
    invoice: InvoiceEntity,
    viewModel: RestaurantViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var orderItems by remember { mutableStateOf<List<OrderItemEntity>>(emptyList()) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    LaunchedEffect(invoice.orderId) {
        orderItems = viewModel.getOrderItems(invoice.orderId)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Receipt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Factura ${invoice.invoiceNumber}", fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 450.dp)
            ) {
                // Professional Invoice Digital Paper Preview
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFAFAFA),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Header Datos Restaurante
                        Text(
                            text = invoice.restaurantName.uppercase(),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        )
                        Text("Tel: ${invoice.restaurantPhone}", style = MaterialTheme.typography.bodySmall)
                        Text(invoice.restaurantAddress, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        Text("Correo: ${invoice.restaurantEmail}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Customer & Order Info
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("CLIENTE: ${if (invoice.customerName.isBlank()) invoice.customerType else invoice.customerName}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                if (invoice.customerPhone.isNotBlank()) {
                                    Text("Tel: ${invoice.customerPhone}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Comanda: #${invoice.orderNumber}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                Text(dateFormat.format(Date(invoice.timestamp)), style = MaterialTheme.typography.labelSmall)
                                Text("Pago: ${invoice.paymentMethod}", style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Line items table
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("CANT / PRODUCTO", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1f))
                            Text("SUBTOTAL", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        orderItems.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${item.quantity}x ${item.productName}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                                Text(formatQuetzales(item.subtotal), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Totals
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal:", style = MaterialTheme.typography.bodySmall)
                            Text(formatQuetzales(invoice.subtotal), style = MaterialTheme.typography.bodySmall)
                        }
                        if (invoice.discount > 0) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Descuento:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                                Text("-${formatQuetzales(invoice.discount)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("TOTAL A PAGAR:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                            Text(formatQuetzales(invoice.totalAmount), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = EmeraldSuccess))
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = invoice.footerMessage,
                            style = MaterialTheme.typography.labelSmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = {
                        ReportExportHelper.printOrExportInvoicePdf(context, invoice, orderItems)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_exportar_pdf_factura")
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("PDF / Imprimir", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = {
                        ReportExportHelper.shareInvoiceViaWhatsApp(context, invoice, orderItems)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_whatsapp_factura")
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WhatsApp", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = {
                        ReportExportHelper.sendInvoiceViaEmail(context, invoice, orderItems)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_email_factura")
                ) {
                    Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Correo", fontSize = 11.sp)
                }
            }
        }
    )
}

@Composable
private fun BillingSettingsPanel(
    viewModel: RestaurantViewModel,
    onSaved: (String) -> Unit
) {
    val settings by viewModel.systemSettings.collectAsState()

    var restaurantName by remember(settings) { mutableStateOf(settings.restaurantName) }
    var address by remember(settings) { mutableStateOf(settings.address) }
    var phone by remember(settings) { mutableStateOf(settings.phone) }
    var email by remember(settings) { mutableStateOf(settings.email) }
    var taxId by remember(settings) { mutableStateOf(settings.taxId) }
    var footerMessage by remember(settings) { mutableStateOf(settings.footerMessage) }
    var nextInvoiceNumberText by remember(settings) { mutableStateOf("${settings.nextInvoiceNumber}") }
    var taxPercentText by remember(settings) { mutableStateOf("${settings.taxPercent}") }
    var currencySymbol by remember(settings) { mutableStateOf(settings.currencySymbol) }
    var paperWidthMm by remember(settings) { mutableIntStateOf(settings.paperWidthMm) }

    var showLogo by remember(settings) { mutableStateOf(settings.showLogo) }
    var showRestaurantName by remember(settings) { mutableStateOf(settings.showRestaurantName) }
    var showAddress by remember(settings) { mutableStateOf(settings.showAddress) }
    var showPhone by remember(settings) { mutableStateOf(settings.showPhone) }
    var showEmail by remember(settings) { mutableStateOf(settings.showEmail) }
    var showInvoiceNumber by remember(settings) { mutableStateOf(settings.showInvoiceNumber) }
    var showOrderNumber by remember(settings) { mutableStateOf(settings.showOrderNumber) }
    var showDate by remember(settings) { mutableStateOf(settings.showDate) }
    var showTime by remember(settings) { mutableStateOf(settings.showTime) }
    var showCashierName by remember(settings) { mutableStateOf(settings.showCashierName) }
    var showPaymentMethod by remember(settings) { mutableStateOf(settings.showPaymentMethod) }
    var showCustomerType by remember(settings) { mutableStateOf(settings.showCustomerType) }
    var showCustomerPhone by remember(settings) { mutableStateOf(settings.showCustomerPhone) }
    var showQuantity by remember(settings) { mutableStateOf(settings.showQuantity) }
    var showProductName by remember(settings) { mutableStateOf(settings.showProductName) }
    var showUnitPrice by remember(settings) { mutableStateOf(settings.showUnitPrice) }
    var showSubtotal by remember(settings) { mutableStateOf(settings.showSubtotal) }
    var showTotal by remember(settings) { mutableStateOf(settings.showTotal) }
    var showFooterMessage by remember(settings) { mutableStateOf(settings.showFooterMessage) }
    var showPrintTimestamp by remember(settings) { mutableStateOf(settings.showPrintTimestamp) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "PARÁMETROS FISCALES Y DE IMPRESIÓN (EXCLUSIVO GERENCIA)",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        )
        Text(
            text = "Estos valores aparecerán impresos en cada factura oficial generada por el sistema.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = restaurantName,
                onValueChange = { restaurantName = it },
                label = { Text("Nombre del Restaurante") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_nombre_restaurante")
            )

            OutlinedTextField(
                value = taxId,
                onValueChange = { taxId = it },
                label = { Text("NIT o Identificación Fiscal") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_nit_restaurante")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Dirección Física del Establecimiento") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_direccion_restaurante")
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono de Contacto") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico Oficial") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = nextInvoiceNumberText,
                onValueChange = { nextInvoiceNumberText = it.filter { c -> c.isDigit() } },
                label = { Text("Siguiente No. Correlativo de Factura") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = taxPercentText,
                onValueChange = { taxPercentText = it },
                label = { Text("Porcentaje de Impuesto (% IVA)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = currencySymbol,
                onValueChange = { currencySymbol = it },
                label = { Text("Símbolo de Moneda") },
                singleLine = true,
                modifier = Modifier.weight(0.7f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = footerMessage,
            onValueChange = { footerMessage = it },
            label = { Text("Mensaje al Pie de Factura") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Formato de papel impresora térmica
        Text("Tamaño del Papel Térmico:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterChip(
                selected = paperWidthMm == 80,
                onClick = { paperWidthMm = 80 },
                label = { Text("80 mm (Estándar POS Amplio)") },
                leadingIcon = { if (paperWidthMm == 80) Icon(Icons.Default.Check, contentDescription = null) }
            )
            FilterChip(
                selected = paperWidthMm == 58,
                onClick = { paperWidthMm = 58 },
                label = { Text("58 mm (Portátil / Compacto)") },
                leadingIcon = { if (paperWidthMm == 58) Icon(Icons.Default.Check, contentDescription = null) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        // PERSONALIZAR FACTURA (CAMPOS A IMPRIMIR)
        Text(
            text = "PERSONALIZAR FACTURA (CAMPOS VISIBLES EN IMPRESIÓN)",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        )
        Text(
            text = "Active o desactive los elementos que se incluirán en el diseño impreso de la factura.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Datos del Restaurante:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Logotipo", showLogo) { showLogo = it }
                    SwitchOptionItem("Nombre Restaurante", showRestaurantName) { showRestaurantName = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Dirección", showAddress) { showAddress = it }
                    SwitchOptionItem("Teléfono", showPhone) { showPhone = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Correo Electrónico", showEmail) { showEmail = it }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                Text("Encabezado de Factura:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Número de Factura", showInvoiceNumber) { showInvoiceNumber = it }
                    SwitchOptionItem("Número de Comanda", showOrderNumber) { showOrderNumber = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Fecha", showDate) { showDate = it }
                    SwitchOptionItem("Hora", showTime) { showTime = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Nombre del Cajero", showCashierName) { showCashierName = it }
                    SwitchOptionItem("Forma de Pago", showPaymentMethod) { showPaymentMethod = it }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                Text("Datos del Cliente y Detalle:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Tipo/Nombre Cliente", showCustomerType) { showCustomerType = it }
                    SwitchOptionItem("Teléfono Cliente", showCustomerPhone) { showCustomerPhone = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Cantidad Producto", showQuantity) { showQuantity = it }
                    SwitchOptionItem("Nombre Producto", showProductName) { showProductName = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Precio Unitario", showUnitPrice) { showUnitPrice = it }
                    SwitchOptionItem("Subtotal", showSubtotal) { showSubtotal = it }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                Text("Pie de Factura:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Total a Pagar", showTotal) { showTotal = it }
                    SwitchOptionItem("Mensaje Final", showFooterMessage) { showFooterMessage = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Fecha/Hora Impresión", showPrintTimestamp) { showPrintTimestamp = it }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val nextNum = nextInvoiceNumberText.toLongOrNull() ?: 1001L
                val taxPct = taxPercentText.toDoubleOrNull() ?: 12.0
                viewModel.updateBillingSettings(
                    restaurantName = restaurantName,
                    address = address,
                    phone = phone,
                    email = email,
                    taxId = taxId,
                    footerMessage = footerMessage,
                    nextInvoiceNumber = nextNum,
                    taxPercent = taxPct,
                    currencySymbol = currencySymbol,
                    paperWidthMm = paperWidthMm,
                    showLogo = showLogo,
                    showRestaurantName = showRestaurantName,
                    showAddress = showAddress,
                    showPhone = showPhone,
                    showEmail = showEmail,
                    showInvoiceNumber = showInvoiceNumber,
                    showOrderNumber = showOrderNumber,
                    showDate = showDate,
                    showTime = showTime,
                    showCashierName = showCashierName,
                    showPaymentMethod = showPaymentMethod,
                    showCustomerType = showCustomerType,
                    showCustomerPhone = showCustomerPhone,
                    showQuantity = showQuantity,
                    showProductName = showProductName,
                    showUnitPrice = showUnitPrice,
                    showSubtotal = showSubtotal,
                    showTotal = showTotal,
                    showFooterMessage = showFooterMessage,
                    showPrintTimestamp = showPrintTimestamp
                ) {
                    onSaved("Configuración fiscal y personalización de plantilla actualizada con éxito.")
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("btn_guardar_config_facturacion")
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Guardar Parámetros de Facturación", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SwitchOptionItem(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
