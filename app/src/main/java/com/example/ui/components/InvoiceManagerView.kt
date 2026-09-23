package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Historial de Facturas, 1: Configuración Fiscal y Térmica
    var searchQuery by remember { mutableStateOf("") }
    var selectedInvoiceForPreview by remember { mutableStateOf<InvoiceEntity?>(null) }
    var invoiceToEdit by remember { mutableStateOf<InvoiceEntity?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val filteredInvoices = remember(invoices, searchQuery) {
        if (searchQuery.isBlank()) invoices
        else invoices.filter {
            it.invoiceNumber.contains(searchQuery, ignoreCase = true) ||
            it.orderNumber.contains(searchQuery, ignoreCase = true) ||
            it.customerName.contains(searchQuery, ignoreCase = true) ||
            it.customerNit.contains(searchQuery, ignoreCase = true) ||
            it.tableNumber.contains(searchQuery, ignoreCase = true) ||
            it.waiterName.contains(searchQuery, ignoreCase = true)
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
                    text = "Personalización completa de la factura final, correlativos fiscales, impresión térmica y exportación digital",
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
                placeholder = { Text("Buscar por No. Factura, Comanda, Cliente, NIT, Mesa o Mesero...") },
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
                            onViewDetail = { selectedInvoiceForPreview = invoice },
                            onEdit = { invoiceToEdit = invoice }
                        )
                    }
                }
            }
        } else {
            // CONFIGURACIÓN FISCAL & IMPRESORA TÉRMICA
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
            onDismiss = { selectedInvoiceForPreview = null },
            onEditRequest = {
                selectedInvoiceForPreview = null
                invoiceToEdit = invoice
            }
        )
    }

    // Direct Editor Dialog for an existing invoice
    invoiceToEdit?.let { invoice ->
        EditInvoiceDialog(
            invoice = invoice,
            viewModel = viewModel,
            onDismiss = { invoiceToEdit = null },
            onSaved = {
                invoiceToEdit = null
                successMessage = "Factura ${invoice.invoiceNumber} actualizada correctamente."
            }
        )
    }
}

@Composable
private fun InvoiceHistoryCard(
    invoice: InvoiceEntity,
    onViewDetail: () -> Unit,
    onEdit: () -> Unit
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
                    if (invoice.tableNumber.isNotBlank()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                        ) {
                            Text(
                                text = "Mesa: ${invoice.tableNumber}",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                val clientDisplay = if (invoice.customerNit.isNotBlank()) {
                    "${invoice.customerName.ifBlank { invoice.customerType }} • NIT: ${invoice.customerNit}"
                } else {
                    invoice.customerName.ifBlank { invoice.customerType }
                }

                Text(
                    text = "Cliente: $clientDisplay",
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

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp).testTag("btn_editar_factura_${invoice.id}")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar Factura", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    }

                    Button(
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
}

@Composable
fun InvoiceDetailDialog(
    invoice: InvoiceEntity,
    viewModel: RestaurantViewModel,
    onDismiss: () -> Unit,
    onEditRequest: () -> Unit = {}
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onEditRequest) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar contenido de factura", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
                    .verticalScroll(rememberScrollState())
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
                        if (invoice.showLogo && invoice.logoUri.isNotBlank()) {
                            if (invoice.logoUri == "ic_restaurant") {
                                Icon(
                                    Icons.Default.Restaurant,
                                    contentDescription = "Logo",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .padding(bottom = 6.dp)
                                )
                            } else {
                                AsyncImage(
                                    model = invoice.logoUri,
                                    contentDescription = "Logo Restaurante",
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .padding(bottom = 6.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                        if (invoice.showRestaurantName) {
                            Text(
                                text = invoice.restaurantName.uppercase(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                        if (invoice.showBranchName && invoice.branchName.isNotBlank()) {
                            Text("Sucursal: ${invoice.branchName}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                        }
                        if (invoice.showTaxId && invoice.restaurantTaxId.isNotBlank()) {
                            Text("NIT Emisor: ${invoice.restaurantTaxId}", style = MaterialTheme.typography.bodySmall)
                        }
                        if (invoice.showAddress && invoice.restaurantAddress.isNotBlank()) {
                            Text(invoice.restaurantAddress, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        }
                        if (invoice.showPhone && invoice.restaurantPhone.isNotBlank()) {
                            Text("Tel: ${invoice.restaurantPhone}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        }
                        if (invoice.showEmail && invoice.restaurantEmail.isNotBlank()) {
                            Text("Correo: ${invoice.restaurantEmail}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Customer & Order Info
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                if (invoice.showCustomerType) {
                                    Text("CLIENTE: ${if (invoice.customerName.isBlank()) invoice.customerType else invoice.customerName}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                }
                                if (invoice.showCustomerNit && invoice.customerNit.isNotBlank()) {
                                    Text("NIT: ${invoice.customerNit}", style = MaterialTheme.typography.bodySmall)
                                }
                                if (invoice.showCustomerPhone && invoice.customerPhone.isNotBlank()) {
                                    Text("Tel: ${invoice.customerPhone}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                if (invoice.showOrderNumber) {
                                    Text("Comanda: #${invoice.orderNumber}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                }
                                if (invoice.showTableNumber && invoice.tableNumber.isNotBlank()) {
                                    Text("Mesa: ${invoice.tableNumber}", style = MaterialTheme.typography.bodySmall)
                                }
                                if (invoice.showDate) {
                                    Text(dateFormat.format(Date(invoice.timestamp)), style = MaterialTheme.typography.labelSmall)
                                }
                                if (invoice.showCashierName) {
                                    Text("Cajero: ${invoice.cashierName}", style = MaterialTheme.typography.labelSmall)
                                }
                                if (invoice.showPaymentMethod) {
                                    Text("Pago: ${invoice.paymentMethod}", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Line items table
                        if (invoice.showQuantity || invoice.showProductName || invoice.showUnitPrice || invoice.showSubtotal) {
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
                        }

                        // Totals
                        if (invoice.showSubtotal) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Subtotal:", style = MaterialTheme.typography.bodySmall)
                                Text(formatQuetzales(invoice.subtotal), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        if (invoice.discount > 0) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Descuento:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                                Text("-${formatQuetzales(invoice.discount)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                            }
                        }
                        if (invoice.showTaxBreakdown) {
                            val base = invoice.totalAmount / 1.12
                            val vat = invoice.totalAmount - base
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Base Imponible:", style = MaterialTheme.typography.bodySmall)
                                Text(formatQuetzales(base), style = MaterialTheme.typography.bodySmall)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("IVA (12%):", style = MaterialTheme.typography.bodySmall)
                                Text(formatQuetzales(vat), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        if (invoice.showTotal) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("TOTAL A PAGAR:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                Text(formatQuetzales(invoice.totalAmount), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold, color = EmeraldSuccess))
                            }
                        }

                        if (invoice.showTipLine) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(6.dp)) {
                                    Text("Propina Sugerida (10%): ${formatQuetzales(invoice.totalAmount * 0.10)}", style = MaterialTheme.typography.labelSmall)
                                    Text("Firma Cliente: ________________________", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }

                        if (invoice.showLegalNotice && invoice.legalNotice.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = invoice.legalNotice,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }

                        if (invoice.showFooterMessage && invoice.footerMessage.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = invoice.footerMessage,
                                style = MaterialTheme.typography.labelSmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                        Text("PDF FEL", fontSize = 11.sp)
                    }

                    FilledTonalButton(
                        onClick = {
                            val paperWidth = if (invoice.paperWidthMm == 58) 58 else 80
                            ReportExportHelper.printThermalReceipt(
                                context = context,
                                invoice = invoice,
                                items = orderItems,
                                paperWidthMm = paperWidth
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_imprimir_termica_factura")
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Térmica", fontSize = 11.sp)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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

                // Botón destacado "Listo / Finalizado"
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_factura_listo_finalizado")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Listo / Finalizado", fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}

@Composable
fun EditInvoiceDialog(
    invoice: InvoiceEntity,
    viewModel: RestaurantViewModel,
    onDismiss: () -> Unit,
    onSaved: () -> Unit
) {
    var invoiceNumber by remember { mutableStateOf(invoice.invoiceNumber) }
    var customerName by remember { mutableStateOf(invoice.customerName) }
    var customerNit by remember { mutableStateOf(invoice.customerNit) }
    var customerPhone by remember { mutableStateOf(invoice.customerPhone) }
    var tableNumber by remember { mutableStateOf(invoice.tableNumber) }
    var waiterName by remember { mutableStateOf(invoice.waiterName) }
    var footerMessage by remember { mutableStateOf(invoice.footerMessage) }
    var legalNotice by remember { mutableStateOf(invoice.legalNotice) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.EditNote, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar Datos de la Factura Final", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    "Modifique la información que se mostrará en esta factura individual al reimprimir o exportar:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = invoiceNumber,
                    onValueChange = { invoiceNumber = it },
                    label = { Text("Número de Factura") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Nombre del Cliente") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = customerNit,
                        onValueChange = { customerNit = it },
                        label = { Text("NIT o C.F.") },
                        singleLine = true,
                        modifier = Modifier.weight(0.8f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it },
                        label = { Text("Teléfono Cliente") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = tableNumber,
                        onValueChange = { tableNumber = it },
                        label = { Text("Mesa / Área") },
                        singleLine = true,
                        modifier = Modifier.weight(0.8f)
                    )
                }

                OutlinedTextField(
                    value = waiterName,
                    onValueChange = { waiterName = it },
                    label = { Text("Mesero(a) Asignado") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = legalNotice,
                    onValueChange = { legalNotice = it },
                    label = { Text("Disposición Legal / Leyenda Fiscal") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = footerMessage,
                    onValueChange = { footerMessage = it },
                    label = { Text("Mensaje de Pie de Factura") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = invoice.copy(
                        invoiceNumber = invoiceNumber.trim(),
                        customerName = customerName.trim(),
                        customerNit = customerNit.trim(),
                        customerPhone = customerPhone.trim(),
                        tableNumber = tableNumber.trim(),
                        waiterName = waiterName.trim(),
                        legalNotice = legalNotice.trim(),
                        footerMessage = footerMessage.trim()
                    )
                    viewModel.updateInvoice(updated) {
                        onSaved()
                    }
                }
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
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

    // Datos del Establecimiento
    var restaurantName by remember(settings) { mutableStateOf(settings.restaurantName) }
    var branchName by remember(settings) { mutableStateOf(settings.branchName) }
    var address by remember(settings) { mutableStateOf(settings.address) }
    var phone by remember(settings) { mutableStateOf(settings.phone) }
    var email by remember(settings) { mutableStateOf(settings.email) }
    var taxId by remember(settings) { mutableStateOf(settings.taxId) }

    // Parámetros y Textos de Factura
    var ticketPrefix by remember(settings) { mutableStateOf(settings.ticketPrefix) }
    var nextInvoiceNumberText by remember(settings) { mutableStateOf("${settings.nextInvoiceNumber}") }
    var taxPercentText by remember(settings) { mutableStateOf("${settings.taxPercent}") }
    var currencySymbol by remember(settings) { mutableStateOf(settings.currencySymbol) }
    var paperWidthMm by remember(settings) { mutableIntStateOf(settings.paperWidthMm) }
    var ticketDividerStyle by remember(settings) { mutableStateOf(settings.ticketDividerStyle) }
    var legalNotice by remember(settings) { mutableStateOf(settings.legalNotice) }
    var footerMessage by remember(settings) { mutableStateOf(settings.footerMessage) }

    // Opciones Visibles / Switches
    var showLogo by remember(settings) { mutableStateOf(settings.showLogo) }
    var showRestaurantName by remember(settings) { mutableStateOf(settings.showRestaurantName) }
    var showBranchName by remember(settings) { mutableStateOf(settings.showBranchName) }
    var showTaxId by remember(settings) { mutableStateOf(settings.showTaxId) }
    var showAddress by remember(settings) { mutableStateOf(settings.showAddress) }
    var showPhone by remember(settings) { mutableStateOf(settings.showPhone) }
    var showEmail by remember(settings) { mutableStateOf(settings.showEmail) }

    var showInvoiceNumber by remember(settings) { mutableStateOf(settings.showInvoiceNumber) }
    var showOrderNumber by remember(settings) { mutableStateOf(settings.showOrderNumber) }
    var showDate by remember(settings) { mutableStateOf(settings.showDate) }
    var showTime by remember(settings) { mutableStateOf(settings.showTime) }
    var showCashierName by remember(settings) { mutableStateOf(settings.showCashierName) }
    var showWaiterName by remember(settings) { mutableStateOf(settings.showWaiterName) }
    var showTableNumber by remember(settings) { mutableStateOf(settings.showTableNumber) }
    var showPaymentMethod by remember(settings) { mutableStateOf(settings.showPaymentMethod) }

    var showCustomerType by remember(settings) { mutableStateOf(settings.showCustomerType) }
    var showCustomerNit by remember(settings) { mutableStateOf(settings.showCustomerNit) }
    var showCustomerPhone by remember(settings) { mutableStateOf(settings.showCustomerPhone) }

    var showQuantity by remember(settings) { mutableStateOf(settings.showQuantity) }
    var showProductName by remember(settings) { mutableStateOf(settings.showProductName) }
    var showUnitPrice by remember(settings) { mutableStateOf(settings.showUnitPrice) }
    var showSubtotal by remember(settings) { mutableStateOf(settings.showSubtotal) }

    var showTotal by remember(settings) { mutableStateOf(settings.showTotal) }
    var showTaxBreakdown by remember(settings) { mutableStateOf(settings.showTaxBreakdown) }
    var showTipLine by remember(settings) { mutableStateOf(settings.showTipLine) }
    var showLegalNotice by remember(settings) { mutableStateOf(settings.showLegalNotice) }
    var showFooterMessage by remember(settings) { mutableStateOf(settings.showFooterMessage) }
    var showQrCode by remember(settings) { mutableStateOf(settings.showQrCode) }
    var showPrintTimestamp by remember(settings) { mutableStateOf(settings.showPrintTimestamp) }

    var showLivePreview by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "CONFIGURACIÓN DE FACTURA FINAL Y DATOS FISCALES",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = "Personalice los textos, leyendas, divisores y qué campos exactos aparecerán en la factura.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedButton(
                onClick = { showLivePreview = !showLivePreview },
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(if (showLivePreview) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (showLivePreview) "Ocultar Vista Previa" else "Vista Previa en Vivo")
            }
        }

        // Live Ticket Preview Box
        if (showLivePreview) {
            Spacer(modifier = Modifier.height(14.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SIMULADOR DE TICKET TÉRMICO (${paperWidthMm}mm)",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        )
                        Surface(shape = RoundedCornerShape(4.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                            Text("En Tiempo Real", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD1D5DB)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            val divChar = when (ticketDividerStyle) {
                                "====" -> "========================================"
                                "...." -> "........................................"
                                "****" -> "****************************************"
                                else -> "----------------------------------------"
                            }

                            Text(divChar, fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color.Gray)
                            if (showLogo) {
                                if (settings.logoUri.isNotBlank() && settings.logoUri != "ic_restaurant") {
                                    AsyncImage(
                                        model = settings.logoUri,
                                        contentDescription = "Logo",
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .align(Alignment.CenterHorizontally),
                                        contentScale = ContentScale.Fit
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                } else {
                                    Text("[ LOGOTIPO DEL RESTAURANTE ]", modifier = Modifier.align(Alignment.CenterHorizontally), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                            if (showRestaurantName && restaurantName.isNotBlank()) {
                                Text(restaurantName.uppercase(), modifier = Modifier.align(Alignment.CenterHorizontally), fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            }
                            if (showBranchName && branchName.isNotBlank()) {
                                Text(branchName, modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 12.sp)
                            }
                            if (showTaxId && taxId.isNotBlank()) {
                                Text("NIT: $taxId", modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 12.sp)
                            }
                            if (showAddress && address.isNotBlank()) {
                                Text(address, modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 11.sp, color = Color.DarkGray)
                            }
                            if (showPhone && phone.isNotBlank()) {
                                Text("Tel: $phone", modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 11.sp, color = Color.DarkGray)
                            }
                            if (showEmail && email.isNotBlank()) {
                                Text("Email: $email", modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 11.sp, color = Color.DarkGray)
                            }
                            Text(divChar, fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color.Gray)

                            if (showInvoiceNumber) Text("FACTURA: ${ticketPrefix.ifBlank { "FAC-" }}001001", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            if (showOrderNumber) Text("Comanda: #104", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                            if (showDate) Text("Fecha: 27/08/2026", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                            if (showTime) Text("Hora: 14:30", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                            if (showTableNumber) Text("Mesa: Mesa 4 (Terraza)", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                            if (showWaiterName) Text("Mesero: Carlos Gómez", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                            if (showCashierName) Text("Cajero: Admin Principal", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                            if (showPaymentMethod) Text("Pago: Tarjeta de Crédito", fontFamily = FontFamily.Monospace, fontSize = 12.sp)

                            if (showCustomerType || showCustomerNit || showCustomerPhone) {
                                Text(divChar, fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color.Gray)
                                Text("DATOS DEL CLIENTE:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                if (showCustomerType) Text("Cliente: Juan Pérez", fontSize = 12.sp)
                                if (showCustomerNit) Text("NIT: 7483920-1", fontSize = 12.sp)
                                if (showCustomerPhone) Text("Tel: 5555-1234", fontSize = 12.sp)
                            }

                            if (showQuantity || showProductName || showUnitPrice || showSubtotal) {
                                Text(divChar, fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color.Gray)
                                Text("DETALLE DE CONSUMO:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("2x Hamburguesa Gourmet ($currencySymbol 45.00) -> $currencySymbol 90.00", fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                                Text("1x Bebida Refrescante ($currencySymbol 15.00) -> $currencySymbol 15.00", fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                            }

                            Text(divChar, fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color.Gray)
                            if (showSubtotal) Text("Subtotal: $currencySymbol 105.00", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                            if (showTaxBreakdown) {
                                Text("Base Imponible: $currencySymbol 93.75", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color.DarkGray)
                                Text("IVA (12%): $currencySymbol 11.25", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color.DarkGray)
                            }
                            if (showTotal) Text("TOTAL A PAGAR: $currencySymbol 105.00", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)

                            if (showTipLine) {
                                Text(divChar, fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color.Gray)
                                Text("Propina Sugerida (10%): $currencySymbol 10.50", fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                                Text("Firma: ________________________", fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                            }

                            if (showLegalNotice && legalNotice.isNotBlank()) {
                                Text(divChar, fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color.Gray)
                                Text(legalNotice, fontSize = 10.sp, color = Color.DarkGray, modifier = Modifier.align(Alignment.CenterHorizontally))
                            }

                            if (showFooterMessage && footerMessage.isNotBlank()) {
                                Text(divChar, fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color.Gray)
                                Text(footerMessage, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, fontSize = 11.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                            }

                            if (showQrCode) {
                                Text("[ QR: Consulta tu factura electrónica ]", modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 11.sp, color = Color(0xFF2563EB))
                            }

                            if (showPrintTimestamp) {
                                Text("Impreso: 27/08/2026 14:35", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
                            }
                            Text(divChar, fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECCIÓN 1: DATOS FISCALES DEL EMISOR
        Text("1. DATOS DEL ESTABLECIMIENTO / EMISOR", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = restaurantName,
                onValueChange = { restaurantName = it },
                label = { Text("Nombre Comercial del Restaurante") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_nombre_restaurante")
            )

            OutlinedTextField(
                value = branchName,
                onValueChange = { branchName = it },
                label = { Text("Nombre de Sucursal o Sede") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_sucursal_restaurante")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = taxId,
                onValueChange = { taxId = it },
                label = { Text("NIT o Identificación Tributaria") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_nit_restaurante")
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono de Contacto") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Dirección Física Oficial") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_direccion_restaurante")
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico para Facturación") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))

        // SECCIÓN 2: FORMATO, CORRELATIVOS Y DISPOSICIONES
        Text("2. CORRELATIVOS, FORMATO E IMPRESIÓN", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = ticketPrefix,
                onValueChange = { ticketPrefix = it },
                label = { Text("Prefijo de Factura (ej: FAC-, DTE-)") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = nextInvoiceNumberText,
                onValueChange = { nextInvoiceNumberText = it.filter { c -> c.isDigit() } },
                label = { Text("Siguiente No. Correlativo") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = taxPercentText,
                onValueChange = { taxPercentText = it },
                label = { Text("% IVA (Impuesto)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = currencySymbol,
                onValueChange = { currencySymbol = it },
                label = { Text("Símbolo Moneda") },
                singleLine = true,
                modifier = Modifier.weight(0.8f)
            )

            Column(modifier = Modifier.weight(1.2f)) {
                Text("Divisores de Ticket:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("----", "====", "....", "****").forEach { style ->
                        FilterChip(
                            selected = ticketDividerStyle == style,
                            onClick = { ticketDividerStyle = style },
                            label = { Text(style, fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text("Ancho de Impresora Térmica:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
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

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = legalNotice,
            onValueChange = { legalNotice = it },
            label = { Text("Aviso Legal / Régimen Fiscal (ej: Sujeto a pagos trimestrales ISR)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = footerMessage,
            onValueChange = { footerMessage = it },
            label = { Text("Mensaje de Agradecimiento al Pie de Factura") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))

        // SECCIÓN 3: CONTROL DE CAMPOS VISIBLES EN LA FACTURA FINAL
        Text(
            text = "3. CAMPOS VISIBLES EN LA FACTURA FINAL",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        )
        Text(
            text = "Active o desactive exactamente lo que verá el cliente en su ticket impreso o digital.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("🏢 Datos del Restaurante:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Logotipo", showLogo) { showLogo = it }
                    SwitchOptionItem("Nombre Restaurante", showRestaurantName) { showRestaurantName = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Sucursal / Sede", showBranchName) { showBranchName = it }
                    SwitchOptionItem("NIT Emisor", showTaxId) { showTaxId = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Dirección", showAddress) { showAddress = it }
                    SwitchOptionItem("Teléfono", showPhone) { showPhone = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Correo Electrónico", showEmail) { showEmail = it }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider()
                Spacer(modifier = Modifier.height(10.dp))

                Text("📋 Encabezado de la Transacción:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Número de Factura", showInvoiceNumber) { showInvoiceNumber = it }
                    SwitchOptionItem("Número de Comanda", showOrderNumber) { showOrderNumber = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Fecha de Emisión", showDate) { showDate = it }
                    SwitchOptionItem("Hora de Emisión", showTime) { showTime = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Mesa o Área", showTableNumber) { showTableNumber = it }
                    SwitchOptionItem("Nombre de Mesero", showWaiterName) { showWaiterName = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Nombre del Cajero", showCashierName) { showCashierName = it }
                    SwitchOptionItem("Forma de Pago", showPaymentMethod) { showPaymentMethod = it }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider()
                Spacer(modifier = Modifier.height(10.dp))

                Text("👤 Datos del Cliente:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Nombre / Tipo Cliente", showCustomerType) { showCustomerType = it }
                    SwitchOptionItem("NIT del Cliente", showCustomerNit) { showCustomerNit = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Teléfono del Cliente", showCustomerPhone) { showCustomerPhone = it }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider()
                Spacer(modifier = Modifier.height(10.dp))

                Text("🍽️ Detalle de Consumo:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Cantidad", showQuantity) { showQuantity = it }
                    SwitchOptionItem("Nombre del Producto", showProductName) { showProductName = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Precio Unitario", showUnitPrice) { showUnitPrice = it }
                    SwitchOptionItem("Subtotales de Línea", showSubtotal) { showSubtotal = it }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider()
                Spacer(modifier = Modifier.height(10.dp))

                Text("💰 Totales, Impuestos y Pie de Ticket:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Total a Pagar", showTotal) { showTotal = it }
                    SwitchOptionItem("Desglose Base + IVA (12%)", showTaxBreakdown) { showTaxBreakdown = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Línea Propina Sugerida (10%) / Firma", showTipLine) { showTipLine = it }
                    SwitchOptionItem("Aviso Régimen Fiscal", showLegalNotice) { showLegalNotice = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Mensaje de Despedida", showFooterMessage) { showFooterMessage = it }
                    SwitchOptionItem("Código QR / Verificación", showQrCode) { showQrCode = it }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SwitchOptionItem("Fecha/Hora de Impresión", showPrintTimestamp) { showPrintTimestamp = it }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val nextNum = nextInvoiceNumberText.toLongOrNull() ?: 1001L
                val taxPct = taxPercentText.toDoubleOrNull() ?: 12.0
                viewModel.updateBillingSettings(
                    restaurantName = restaurantName.trim(),
                    branchName = branchName.trim(),
                    address = address.trim(),
                    phone = phone.trim(),
                    email = email.trim(),
                    taxId = taxId.trim(),
                    footerMessage = footerMessage.trim(),
                    legalNotice = legalNotice.trim(),
                    nextInvoiceNumber = nextNum,
                    taxPercent = taxPct,
                    currencySymbol = currencySymbol.trim(),
                    paperWidthMm = paperWidthMm,
                    ticketPrefix = ticketPrefix.trim(),
                    ticketDividerStyle = ticketDividerStyle,
                    showLogo = showLogo,
                    showRestaurantName = showRestaurantName,
                    showBranchName = showBranchName,
                    showTaxId = showTaxId,
                    showAddress = showAddress,
                    showPhone = showPhone,
                    showEmail = showEmail,
                    showInvoiceNumber = showInvoiceNumber,
                    showOrderNumber = showOrderNumber,
                    showDate = showDate,
                    showTime = showTime,
                    showCashierName = showCashierName,
                    showWaiterName = showWaiterName,
                    showTableNumber = showTableNumber,
                    showPaymentMethod = showPaymentMethod,
                    showCustomerType = showCustomerType,
                    showCustomerNit = showCustomerNit,
                    showCustomerPhone = showCustomerPhone,
                    showQuantity = showQuantity,
                    showProductName = showProductName,
                    showUnitPrice = showUnitPrice,
                    showSubtotal = showSubtotal,
                    showTotal = showTotal,
                    showTaxBreakdown = showTaxBreakdown,
                    showTipLine = showTipLine,
                    showLegalNotice = showLegalNotice,
                    showFooterMessage = showFooterMessage,
                    showQrCode = showQrCode,
                    showPrintTimestamp = showPrintTimestamp
                ) {
                    onSaved("Configuración fiscal y personalización de factura guardada exitosamente.")
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_guardar_config_facturacion")
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Guardar Parámetros y Diseño de Factura", fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
        modifier = Modifier
            .width(170.dp)
            .padding(vertical = 3.dp, horizontal = 2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.weight(1f),
            maxLines = 2
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
