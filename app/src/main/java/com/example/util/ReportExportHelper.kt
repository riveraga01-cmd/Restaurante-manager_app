package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.entity.DetailedSalesReportData
import com.example.data.entity.InvoiceEntity
import com.example.data.entity.OrderItemEntity
import com.example.data.entity.SaleEntity
import com.example.ui.components.formatQuetzales
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReportExportHelper {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val fileDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())

    fun buildTextSummary(report: DetailedSalesReportData): String {
        val sb = StringBuilder()
        sb.appendLine("=================================")
        sb.appendLine("    MANAGERPRO BY RIVERA POS     ")
        sb.appendLine("   REPORTE DE VENTAS DETALLADO   ")
        sb.appendLine("=================================")
        sb.appendLine("Período: ${report.periodName.uppercase()}")
        sb.appendLine("Fecha de Generación: ${dateFormat.format(Date())}")
        sb.appendLine("---------------------------------")
        sb.appendLine("MÉTRICAS PRINCIPALES:")
        sb.appendLine("• Total Ingresos: ${formatQuetzales(report.totalRevenue)}")
        sb.appendLine("• Total Pedidos: ${report.orderCount}")
        sb.appendLine("• Ticket Promedio: ${formatQuetzales(report.averageTicket)}")
        sb.appendLine("• Productos Vendidos: ${report.totalProductsSold}")
        sb.appendLine("• Hora de Mayor Venta: ${report.peakSalesHour}")
        sb.appendLine("• Mesero Top: ${report.topSellingWaiter?.let { "${it.first} (${formatQuetzales(it.second)})" } ?: "N/A"}")
        sb.appendLine("• Mesa Top: ${report.topConsumingTable?.let { "${it.first} (${formatQuetzales(it.second)})" } ?: "N/A"}")
        sb.appendLine("• Producto Más Vendido: ${report.mostSoldProduct?.let { "${it.first} (${it.second} ud)" } ?: "N/A"}")
        sb.appendLine("• Producto Menos Vendido: ${report.leastSoldProduct?.let { "${it.first} (${it.second} ud)" } ?: "N/A"}")
        sb.appendLine("---------------------------------")

        report.itemsByCategory.forEach { (category, items) ->
            if (items.isNotEmpty()) {
                sb.appendLine("\nCATEGORY: ${category.uppercase()} (${items.sumOf { it.quantitySold }} uds)")
                sb.appendLine("---------------------------------")
                items.forEach { item ->
                    sb.appendLine("${item.productName}")
                    sb.appendLine("  ${item.quantitySold} x ${formatQuetzales(item.unitPrice)} = ${formatQuetzales(item.totalRevenue)} (${String.format(Locale.US, "%.1f", item.participationPercentage)}%)")
                }
            }
        }
        sb.appendLine("=================================")
        sb.appendLine("Sistema POS ManagerPro by Rivera")
        return sb.toString()
    }

    // 1. COMPARTIR POR WHATSAPP
    fun shareViaWhatsApp(context: Context, report: DetailedSalesReportData) {
        val message = StringBuilder()
        message.appendLine("📊 *REPORTE DE VENTAS - MANAGERPRO BY RIVERA* 📊")
        message.appendLine("🗓️ *Período:* ${report.periodName}")
        message.appendLine("📅 *Generado:* ${dateFormat.format(Date())}")
        message.appendLine("")
        message.appendLine("💰 *Total Ingresos:* ${formatQuetzales(report.totalRevenue)}")
        message.appendLine("📝 *Número de Pedidos:* ${report.orderCount}")
        message.appendLine("🎟️ *Ticket Promedio:* ${formatQuetzales(report.averageTicket)}")
        message.appendLine("📦 *Total Productos Vendidos:* ${report.totalProductsSold}")
        message.appendLine("⏰ *Hora Pico:* ${report.peakSalesHour}")
        message.appendLine("👤 *Mesero que más vendió:* ${report.topSellingWaiter?.let { "${it.first} (${formatQuetzales(it.second)})" } ?: "N/A"}")
        message.appendLine("🪑 *Mesa que más consumió:* ${report.topConsumingTable?.let { "${it.first} (${formatQuetzales(it.second)})" } ?: "N/A"}")
        message.appendLine("🏆 *Producto Más Vendido:* ${report.mostSoldProduct?.let { "${it.first} (${it.second} uds)" } ?: "N/A"}")
        message.appendLine("🔻 *Producto Menos Vendido:* ${report.leastSoldProduct?.let { "${it.first} (${it.second} uds)" } ?: "N/A"}")
        message.appendLine("")
        message.appendLine("📋 *DESGLOSE POR CATEGORÍAS:*")

        report.itemsByCategory.forEach { (cat, items) ->
            if (items.isNotEmpty()) {
                message.appendLine("")
                message.appendLine("🔹 *${cat.uppercase()}*")
                items.take(5).forEach { item ->
                    message.appendLine("• ${item.productName}: ${item.quantitySold} uds - ${formatQuetzales(item.totalRevenue)}")
                }
                if (items.size > 5) {
                    message.appendLine("  ...y ${items.size - 5} productos más.")
                }
            }
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message.toString())
            type = "text/plain"
            setPackage("com.whatsapp")
        }

        try {
            context.startActivity(sendIntent)
        } catch (e: Exception) {
            // Fallback if WhatsApp direct intent fails
            val genericIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message.toString())
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(genericIntent, "Compartir Reporte de Ventas"))
        }
    }

    // 2. EXPORTAR A EXCEL (.CSV)
    fun exportToCsvAndShare(context: Context, report: DetailedSalesReportData) {
        try {
            val csvBuilder = StringBuilder()
            csvBuilder.appendLine("MANAGERPRO BY RIVERA POS - REPORTE DE VENTAS DETALLADO")
            csvBuilder.appendLine("Periodo,${report.periodName}")
            csvBuilder.appendLine("Fecha Generacion,${dateFormat.format(Date())}")
            csvBuilder.appendLine("Total Ingresos,${report.totalRevenue}")
            csvBuilder.appendLine("Numero de Pedidos,${report.orderCount}")
            csvBuilder.appendLine("Ticket Promedio,${report.averageTicket}")
            csvBuilder.appendLine("Total Productos Vendidos,${report.totalProductsSold}")
            csvBuilder.appendLine("Hora Pico,${report.peakSalesHour}")
            csvBuilder.appendLine("Mesero Top,${report.topSellingWaiter?.first ?: "N/A"},${report.topSellingWaiter?.second ?: 0.0}")
            csvBuilder.appendLine("Mesa Top,${report.topConsumingTable?.first ?: "N/A"},${report.topConsumingTable?.second ?: 0.0}")
            csvBuilder.appendLine("Producto Mas Vendido,${report.mostSoldProduct?.first ?: "N/A"},${report.mostSoldProduct?.second ?: 0}")
            csvBuilder.appendLine("Producto Menos Vendido,${report.leastSoldProduct?.first ?: "N/A"},${report.leastSoldProduct?.second ?: 0}")
            csvBuilder.appendLine()
            csvBuilder.appendLine("Categoria,Producto,Precio Unitario (Q),Cantidad Vendida,Total Vendido (Q),Porcentaje Participacion (%)")

            report.itemsByCategory.forEach { (category, items) ->
                items.forEach { item ->
                    csvBuilder.appendLine(
                        "\"$category\",\"${item.productName.replace("\"", "\"\"")}\",${item.unitPrice},${item.quantitySold},${item.totalRevenue},${String.format(Locale.US, "%.2f", item.participationPercentage)}"
                    )
                }
            }

            val fileName = "Reporte_Ventas_${fileDateFormat.format(Date())}.csv"
            val file = File(context.cacheDir, fileName)
            file.writeText(csvBuilder.toString(), Charsets.UTF_8)

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_SUBJECT, "Reporte de Ventas - ${report.periodName}")
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Exportar Reporte Excel / CSV"))
        } catch (e: Exception) {
            Toast.makeText(context, "Error al exportar CSV: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // 3. EXPORTAR A PDF / IMPRIMIR VÍA PRINT MANAGER
    fun printOrExportPdf(context: Context, report: DetailedSalesReportData) {
        val htmlContent = buildHtmlReport(report)
        val webView = WebView(context)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                if (printManager != null) {
                    val printAdapter: PrintDocumentAdapter = webView.createPrintDocumentAdapter("Reporte_Ventas_${report.periodName}")
                    val builder = PrintAttributes.Builder()
                    builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    printManager.print("Reporte de Ventas ManagerPro", printAdapter, builder.build())
                } else {
                    Toast.makeText(context, "Servicio de impresión no disponible", Toast.LENGTH_SHORT).show()
                }
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    private fun buildHtmlReport(report: DetailedSalesReportData): String {
        val sb = StringBuilder()
        sb.append("<!DOCTYPE html><html><head><style>")
        sb.append("body { font-family: Arial, sans-serif; margin: 20px; color: #333; }")
        sb.append("h1 { color: #1E3A8A; font-size: 22px; text-align: center; margin-bottom: 4px; }")
        sb.append("h2 { color: #4B5563; font-size: 14px; text-align: center; margin-top: 0; margin-bottom: 20px; }")
        sb.append(".summary-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 20px; background: #F3F4F6; padding: 15px; border-radius: 8px; }")
        sb.append(".metric { font-size: 13px; margin-bottom: 6px; }")
        sb.append(".metric label { font-weight: bold; color: #1F2937; }")
        sb.append("table { width: 100%; border-collapse: collapse; margin-top: 10px; margin-bottom: 25px; }")
        sb.append("th { background-color: #2563EB; color: white; padding: 8px; font-size: 12px; text-align: left; }")
        sb.append("td { border-bottom: 1px solid #E5E7EB; padding: 8px; font-size: 12px; }")
        sb.append(".cat-header { background-color: #EFF6FF; font-weight: bold; font-size: 14px; padding: 6px; border-left: 4px solid #2563EB; margin-top: 15px; }")
        sb.append(".text-right { text-align: right; }")
        sb.append("</style></head><body>")

        sb.append("<h1>MANAGERPRO BY RIVERA</h1>")
        sb.append("<h2>REPORTE DE VENTAS Y MÉRTRICAS (${report.periodName.uppercase()})</h2>")

        sb.append("<div class='summary-grid'>")
        sb.append("<div class='metric'><label>Total Ingresos:</label> ${formatQuetzales(report.totalRevenue)}</div>")
        sb.append("<div class='metric'><label>Número de Pedidos:</label> ${report.orderCount}</div>")
        sb.append("<div class='metric'><label>Ticket Promedio:</label> ${formatQuetzales(report.averageTicket)}</div>")
        sb.append("<div class='metric'><label>Productos Vendidos:</label> ${report.totalProductsSold} unidades</div>")
        sb.append("<div class='metric'><label>Hora de Mayor Venta:</label> ${report.peakSalesHour}</div>")
        sb.append("<div class='metric'><label>Mesero Top:</label> ${report.topSellingWaiter?.let { "${it.first} (${formatQuetzales(it.second)})" } ?: "N/A"}</div>")
        sb.append("<div class='metric'><label>Mesa Top:</label> ${report.topConsumingTable?.let { "${it.first} (${formatQuetzales(it.second)})" } ?: "N/A"}</div>")
        sb.append("<div class='metric'><label>Producto Más Vendido:</label> ${report.mostSoldProduct?.let { "${it.first} (${it.second} uds)" } ?: "N/A"}</div>")
        sb.append("<div class='metric'><label>Producto Menos Vendido:</label> ${report.leastSoldProduct?.let { "${it.first} (${it.second} uds)" } ?: "N/A"}</div>")
        sb.append("</div>")

        report.itemsByCategory.forEach { (cat, items) ->
            if (items.isNotEmpty()) {
                sb.append("<div class='cat-header'>Categoría: ${cat.uppercase()}</div>")
                sb.append("<table><thead><tr><th>Producto</th><th class='text-right'>P. Unitario</th><th class='text-right'>Cant. Vendida</th><th class='text-right'>Total Vendido</th><th class='text-right'>Participación</th></tr></thead><tbody>")
                items.forEach { item ->
                    sb.append("<tr>")
                    sb.append("<td>${item.productName}</td>")
                    sb.append("<td class='text-right'>${formatQuetzales(item.unitPrice)}</td>")
                    sb.append("<td class='text-right'>${item.quantitySold}</td>")
                    sb.append("<td class='text-right'><b>${formatQuetzales(item.totalRevenue)}</b></td>")
                    sb.append("<td class='text-right'>${String.format(Locale.US, "%.1f", item.participationPercentage)}%</td>")
                    sb.append("</tr>")
                }
                sb.append("</tbody></table>")
            }
        }

        sb.append("<p style='text-align:center; font-size:11px; color:#9CA3AF; margin-top:30px;'>Generado automáticamente por ManagerPro by Rivera POS</p>")
        sb.append("</body></html>")
        return sb.toString()
    }

    // ==========================================
    // --- INVOICE (FACTURACIÓN) EXPORT HELPERS ---
    // ==========================================

    fun buildInvoiceFormattedText(invoice: InvoiceEntity, items: List<OrderItemEntity>): String {
        val sb = StringBuilder()
        val cur = invoice.currencySymbol
        val dateOnly = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(invoice.timestamp))
        val timeOnly = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(invoice.timestamp))

        sb.appendLine("=================================")
        if (invoice.showLogo && invoice.logoUri.isNotBlank()) {
            sb.appendLine("        [ LOGOTIPO RESTAURANTE ]        ")
        }
        if (invoice.showRestaurantName && invoice.restaurantName.isNotBlank()) {
            sb.appendLine("       ${invoice.restaurantName.uppercase()}       ")
        }
        if (invoice.showAddress && invoice.restaurantAddress.isNotBlank()) {
            sb.appendLine("  ${invoice.restaurantAddress}  ")
        }
        if (invoice.showPhone && invoice.restaurantPhone.isNotBlank()) {
            sb.appendLine("  Tel: ${invoice.restaurantPhone}  ")
        }
        if (invoice.showEmail && invoice.restaurantEmail.isNotBlank()) {
            sb.appendLine("  Email: ${invoice.restaurantEmail}  ")
        }
        sb.appendLine("=================================")

        if (invoice.showInvoiceNumber) {
            sb.appendLine("FACTURA: ${invoice.invoiceNumber}")
        }
        if (invoice.showOrderNumber) {
            sb.appendLine("Comanda: #${invoice.orderNumber}")
        }
        if (invoice.showDate) {
            sb.appendLine("Fecha: $dateOnly")
        }
        if (invoice.showTime) {
            sb.appendLine("Hora: $timeOnly")
        }
        if (invoice.showCashierName) {
            sb.appendLine("Atendido por: ${invoice.cashierName}")
        }
        if (invoice.showPaymentMethod) {
            sb.appendLine("Método de Pago: ${invoice.paymentMethod}")
        }

        if (invoice.showCustomerType || invoice.showCustomerPhone) {
            sb.appendLine("---------------------------------")
            sb.appendLine("CLIENTE:")
            if (invoice.showCustomerType) {
                val custNameDisplay = when (invoice.customerType) {
                    "Consumidor Final" -> "Consumidor Final"
                    "Clientes Varios" -> "Clientes Varios"
                    else -> invoice.customerName.ifBlank { "Cliente Registrado" }
                }
                sb.appendLine("Cliente: $custNameDisplay")
            }
            if (invoice.showCustomerPhone && invoice.customerPhone.isNotBlank()) {
                sb.appendLine("Teléfono: ${invoice.customerPhone}")
            }
        }

        sb.appendLine("---------------------------------")
        if (invoice.showQuantity || invoice.showProductName || invoice.showUnitPrice || invoice.showSubtotal) {
            sb.appendLine("DETALLE DE CONSUMO:")
            sb.appendLine("---------------------------------")
            items.forEach { item ->
                val line = StringBuilder()
                if (invoice.showQuantity) line.append("${item.quantity}x ")
                if (invoice.showProductName) line.append("${item.productName} ")
                if (invoice.showUnitPrice) line.append("($cur${String.format(Locale.US, "%.2f", item.unitPrice)}) ")
                if (invoice.showSubtotal) line.append("-> $cur${String.format(Locale.US, "%.2f", item.subtotal)}")
                sb.appendLine(line.toString().trim())
            }
            sb.appendLine("---------------------------------")
        }

        if (invoice.showSubtotal) {
            sb.appendLine("Subtotal: $cur${String.format(Locale.US, "%.2f", invoice.subtotal)}")
        }
        if (invoice.discount > 0) {
            sb.appendLine("Descuento: -$cur${String.format(Locale.US, "%.2f", invoice.discount)}")
        }
        if (invoice.showTotal) {
            sb.appendLine("TOTAL A PAGAR: $cur${String.format(Locale.US, "%.2f", invoice.totalAmount)}")
        }

        if (invoice.showFooterMessage && invoice.footerMessage.isNotBlank()) {
            sb.appendLine("=================================")
            sb.appendLine("  ${invoice.footerMessage}  ")
        }
        if (invoice.showPrintTimestamp) {
            sb.appendLine("Impreso: ${dateFormat.format(Date())}")
        }
        sb.appendLine("=================================")
        return sb.toString()
    }

    fun shareInvoiceViaWhatsApp(context: Context, invoice: InvoiceEntity, items: List<OrderItemEntity>) {
        val text = buildInvoiceFormattedText(invoice, items)
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            setPackage("com.whatsapp")
        }
        try {
            context.startActivity(sendIntent)
        } catch (e: Exception) {
            val chooser = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(chooser, "Compartir Factura ${invoice.invoiceNumber}"))
        }
    }

    fun sendInvoiceViaEmail(context: Context, invoice: InvoiceEntity, items: List<OrderItemEntity>) {
        val text = buildInvoiceFormattedText(invoice, items)
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, "Factura ${invoice.invoiceNumber} - ${invoice.restaurantName}")
            putExtra(Intent.EXTRA_TEXT, text)
        }
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Enviar Factura por Correo"))
        } catch (e: Exception) {
            Toast.makeText(context, "No hay aplicación de correo configurada", Toast.LENGTH_SHORT).show()
        }
    }

    fun printOrExportInvoicePdf(context: Context, invoice: InvoiceEntity, items: List<OrderItemEntity>) {
        val htmlContent = buildHtmlInvoice(invoice, items)
        val webView = WebView(context)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                if (printManager != null) {
                    val printAdapter = webView.createPrintDocumentAdapter("Factura_${invoice.invoiceNumber}")
                    val builder = PrintAttributes.Builder()
                    builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    printManager.print("Factura_${invoice.invoiceNumber}", printAdapter, builder.build())
                } else {
                    Toast.makeText(context, "Servicio de impresión no disponible", Toast.LENGTH_SHORT).show()
                }
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    private fun buildHtmlInvoice(invoice: InvoiceEntity, items: List<OrderItemEntity>): String {
        val sb = StringBuilder()
        val cur = invoice.currencySymbol
        val dateOnly = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(invoice.timestamp))
        val timeOnly = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(invoice.timestamp))

        sb.append("<!DOCTYPE html><html><head><style>")
        sb.append("body { font-family: 'Helvetica Neue', Arial, sans-serif; margin: 20px; color: #1F2937; }")
        sb.append(".header { text-align: center; border-bottom: 2px solid #2563EB; padding-bottom: 10px; margin-bottom: 15px; }")
        sb.append(".header h1 { color: #1E3A8A; margin: 0; font-size: 22px; }")
        sb.append(".header p { margin: 2px 0; color: #4B5563; font-size: 13px; }")
        sb.append(".info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 15px; background: #F8FAFC; padding: 12px; border-radius: 8px; border: 1px solid #E2E8F0; }")
        sb.append(".info-block h3 { margin: 0 0 6px 0; color: #2563EB; font-size: 13px; text-transform: uppercase; }")
        sb.append(".info-block p { margin: 2px 0; font-size: 12px; }")
        sb.append("table { width: 100%; border-collapse: collapse; margin-top: 10px; margin-bottom: 15px; }")
        sb.append("th { background-color: #1E3A8A; color: white; padding: 8px; font-size: 12px; text-align: left; }")
        sb.append("td { border-bottom: 1px solid #E2E8F0; padding: 8px; font-size: 12px; }")
        sb.append(".totals { width: 280px; margin-left: auto; margin-top: 10px; }")
        sb.append(".totals table { margin: 0; }")
        sb.append(".totals td { padding: 4px 8px; border: none; }")
        sb.append(".totals .grand-total { font-size: 15px; font-weight: bold; color: #047857; border-top: 2px solid #10B981; }")
        sb.append(".footer { text-align: center; margin-top: 30px; padding-top: 10px; border-top: 1px dashed #CBD5E1; color: #64748B; font-size: 12px; }")
        sb.append(".text-right { text-align: right; }")
        sb.append("</style></head><body>")

        sb.append("<div class='header'>")
        if (invoice.showLogo && invoice.logoUri.isNotBlank()) {
            sb.append("<p style='font-size: 20px; font-weight: bold;'>🧾</p>")
        }
        if (invoice.showRestaurantName) sb.append("<h1>${invoice.restaurantName}</h1>")
        val headerInfo = mutableListOf<String>()
        if (invoice.showAddress && invoice.restaurantAddress.isNotBlank()) headerInfo.add(invoice.restaurantAddress)
        if (invoice.showPhone && invoice.restaurantPhone.isNotBlank()) headerInfo.add("Tel: ${invoice.restaurantPhone}")
        if (headerInfo.isNotEmpty()) sb.append("<p>${headerInfo.joinToString(" • ")}</p>")
        if (invoice.showEmail && invoice.restaurantEmail.isNotBlank()) sb.append("<p>Email: ${invoice.restaurantEmail}</p>")
        sb.append("</div>")

        sb.append("<div class='info-grid'>")
        sb.append("<div class='info-block'>")
        sb.append("<h3>Datos de Facturación</h3>")
        if (invoice.showInvoiceNumber) sb.append("<p><b>Factura No:</b> ${invoice.invoiceNumber}</p>")
        if (invoice.showOrderNumber) sb.append("<p><b>Comanda No:</b> #${invoice.orderNumber}</p>")
        if (invoice.showDate) sb.append("<p><b>Fecha:</b> $dateOnly</p>")
        if (invoice.showTime) sb.append("<p><b>Hora:</b> $timeOnly</p>")
        if (invoice.showCashierName) sb.append("<p><b>Cajero:</b> ${invoice.cashierName}</p>")
        if (invoice.showPaymentMethod) sb.append("<p><b>Método Pago:</b> ${invoice.paymentMethod}</p>")
        sb.append("</div>")

        if (invoice.showCustomerType || invoice.showCustomerPhone) {
            sb.append("<div class='info-block'>")
            sb.append("<h3>Datos del Cliente</h3>")
            if (invoice.showCustomerType) {
                val custNameDisplay = when (invoice.customerType) {
                    "Consumidor Final" -> "Consumidor Final"
                    "Clientes Varios" -> "Clientes Varios"
                    else -> invoice.customerName.ifBlank { "Cliente Registrado" }
                }
                sb.append("<p><b>Cliente:</b> $custNameDisplay</p>")
            }
            if (invoice.showCustomerPhone && invoice.customerPhone.isNotBlank()) {
                sb.append("<p><b>Teléfono:</b> ${invoice.customerPhone}</p>")
            }
            sb.append("</div>")
        }
        sb.append("</div>")

        sb.append("<table><thead><tr>")
        if (invoice.showQuantity) sb.append("<th>Cant.</th>")
        if (invoice.showProductName) sb.append("<th>Producto</th>")
        if (invoice.showUnitPrice) sb.append("<th class='text-right'>P. Unitario</th>")
        if (invoice.showSubtotal) sb.append("<th class='text-right'>Subtotal</th>")
        sb.append("</tr></thead><tbody>")

        items.forEach { item ->
            sb.append("<tr>")
            if (invoice.showQuantity) sb.append("<td>${item.quantity}</td>")
            if (invoice.showProductName) sb.append("<td>${item.productName}</td>")
            if (invoice.showUnitPrice) sb.append("<td class='text-right'>$cur${String.format(Locale.US, "%.2f", item.unitPrice)}</td>")
            if (invoice.showSubtotal) sb.append("<td class='text-right'>$cur${String.format(Locale.US, "%.2f", item.subtotal)}</td>")
            sb.append("</tr>")
        }
        sb.append("</tbody></table>")

        sb.append("<div class='totals'><table>")
        if (invoice.showSubtotal) sb.append("<tr><td>Subtotal:</td><td class='text-right'>$cur${String.format(Locale.US, "%.2f", invoice.subtotal)}</td></tr>")
        if (invoice.discount > 0) sb.append("<tr><td>Descuento:</td><td class='text-right'>-$cur${String.format(Locale.US, "%.2f", invoice.discount)}</td></tr>")
        if (invoice.showTotal) sb.append("<tr class='grand-total'><td>TOTAL A PAGAR:</td><td class='text-right'>$cur${String.format(Locale.US, "%.2f", invoice.totalAmount)}</td></tr>")
        sb.append("</table></div>")

        if (invoice.showFooterMessage && invoice.footerMessage.isNotBlank()) {
            sb.append("<div class='footer'>")
            sb.append("<p><b>${invoice.footerMessage}</b></p>")
            if (invoice.showPrintTimestamp) sb.append("<p>Impreso el: ${dateFormat.format(Date())}</p>")
            sb.append("</div>")
        }
        sb.append("</body></html>")
        return sb.toString()
    }

    // ==========================================
    // --- SHIFT SUMMARY (CORTE DE CAJA) PDF ---
    // ==========================================

    fun printOrExportShiftPdf(
        context: Context,
        cashierName: String,
        branchName: String,
        initialFund: Double,
        sales: List<SaleEntity>,
        totalCash: Double,
        totalCard: Double,
        totalTransfer: Double,
        grandTotal: Double,
        cashCounted: Double? = null,
        difference: Double? = null
    ) {
        val htmlContent = buildHtmlShiftSummary(
            cashierName = cashierName,
            branchName = branchName,
            initialFund = initialFund,
            sales = sales,
            totalCash = totalCash,
            totalCard = totalCard,
            totalTransfer = totalTransfer,
            grandTotal = grandTotal,
            cashCounted = cashCounted,
            difference = difference
        )

        val webView = WebView(context)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                if (printManager != null) {
                    val printAdapter: PrintDocumentAdapter = webView.createPrintDocumentAdapter("Resumen_Turno_Caja_${fileDateFormat.format(Date())}")
                    val builder = PrintAttributes.Builder()
                    builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    printManager.print("Resumen de Turno de Caja", printAdapter, builder.build())
                } else {
                    Toast.makeText(context, "Servicio de impresión no disponible", Toast.LENGTH_SHORT).show()
                }
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    fun shareShiftSummaryText(
        context: Context,
        cashierName: String,
        branchName: String,
        initialFund: Double,
        sales: List<SaleEntity>,
        totalCash: Double,
        totalCard: Double,
        totalTransfer: Double,
        grandTotal: Double,
        cashCounted: Double? = null,
        difference: Double? = null
    ) {
        val sb = StringBuilder()
        val nowFormatted = dateFormat.format(Date())
        sb.appendLine("==========================================")
        sb.appendLine("    RESTAURANTE RIVERA - RESUMEN DE TURNO ")
        sb.appendLine("==========================================")
        sb.appendLine("Sucursal: $branchName")
        sb.appendLine("Cajero(a): $cashierName")
        sb.appendLine("Fecha y Hora: $nowFormatted")
        sb.appendLine("Total Transacciones: ${sales.size}")
        sb.appendLine("------------------------------------------")
        sb.appendLine("TOTALES POR MÉTODO DE PAGO:")
        sb.appendLine("• Efectivo: ${formatQuetzales(totalCash)}")
        sb.appendLine("• Tarjeta: ${formatQuetzales(totalCard)}")
        sb.appendLine("• Transferencia: ${formatQuetzales(totalTransfer)}")
        sb.appendLine("------------------------------------------")
        sb.appendLine("VENTAS TOTALES DEL TURNO: ${formatQuetzales(grandTotal)}")
        sb.appendLine("Fondo Inicial de Caja: ${formatQuetzales(initialFund)}")
        sb.appendLine("Efectivo Esperado en Caja: ${formatQuetzales(initialFund + totalCash)}")
        if (cashCounted != null) {
            sb.appendLine("Efectivo Contado Real: ${formatQuetzales(cashCounted)}")
        }
        if (difference != null) {
            val diffPrefix = if (difference >= 0) "+ " else "- "
            sb.appendLine("Diferencia / Arqueo: $diffPrefix${formatQuetzales(Math.abs(difference))}")
        }
        sb.appendLine("------------------------------------------")
        sb.appendLine("DETALLE DE TRANSACCIONES:")
        if (sales.isEmpty()) {
            sb.appendLine("(Sin ventas registradas en este turno)")
        } else {
            val timeSdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            sales.forEachIndexed { idx, s ->
                val tStr = timeSdf.format(Date(s.timestamp))
                sb.appendLine("${idx + 1}. [${s.orderNumber}] ${s.paymentMethod} - ${formatQuetzales(s.total)} ($tStr)")
            }
        }
        sb.appendLine("==========================================")
        sb.appendLine("Generado automáticamente por POS Restaurante Rivera")

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, sb.toString())
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Compartir Resumen de Turno"))
    }

    private fun buildHtmlShiftSummary(
        cashierName: String,
        branchName: String,
        initialFund: Double,
        sales: List<SaleEntity>,
        totalCash: Double,
        totalCard: Double,
        totalTransfer: Double,
        grandTotal: Double,
        cashCounted: Double?,
        difference: Double?
    ): String {
        val nowFormatted = dateFormat.format(Date())
        val timeSdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        val sb = StringBuilder()
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><style>")
        sb.append("body { font-family: Arial, sans-serif; margin: 25px; color: #1F2937; background: #FFF; }")
        sb.append(".header { text-align: center; border-bottom: 2px solid #2563EB; padding-bottom: 12px; margin-bottom: 20px; }")
        sb.append("h1 { color: #1E3A8A; font-size: 24px; margin: 0 0 4px 0; }")
        sb.append("h2 { color: #4B5563; font-size: 15px; margin: 0; font-weight: normal; }")
        sb.append(".info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; background: #F8FAFC; border: 1px solid #E2E8F0; padding: 14px; border-radius: 8px; margin-bottom: 20px; }")
        sb.append(".info-item { font-size: 13px; }")
        sb.append(".info-item b { color: #0F172A; }")
        sb.append(".cards-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; margin-bottom: 20px; }")
        sb.append(".card { background: #EFF6FF; border: 1px solid #BFDBFE; border-radius: 8px; padding: 10px; text-align: center; }")
        sb.append(".card-title { font-size: 11px; color: #1E40AF; text-transform: uppercase; font-weight: bold; }")
        sb.append(".card-val { font-size: 18px; font-weight: bold; color: #1E3A8A; margin-top: 4px; }")
        sb.append("table { width: 100%; border-collapse: collapse; margin-top: 10px; font-size: 12px; }")
        sb.append("th { background-color: #2563EB; color: white; padding: 8px 10px; text-align: left; }")
        sb.append("td { border-bottom: 1px solid #E2E8F0; padding: 8px 10px; }")
        sb.append(".text-right { text-align: right; }")
        sb.append(".badge { display: inline-block; padding: 2px 8px; border-radius: 4px; font-weight: bold; font-size: 11px; }")
        sb.append(".badge-cash { background: #DCFCE7; color: #166534; }")
        sb.append(".badge-card { background: #E0E7FF; color: #3730A3; }")
        sb.append(".badge-transfer { background: #FEF3C7; color: #92400E; }")
        sb.append(".totals-box { background: #F1F5F9; border-radius: 8px; padding: 14px; margin-top: 20px; }")
        sb.append(".total-row { display: flex; justify-content: space-between; font-size: 14px; margin-bottom: 6px; }")
        sb.append(".grand-row { display: flex; justify-content: space-between; font-size: 18px; font-weight: bold; color: #1E3A8A; border-top: 2px solid #CBD5E1; padding-top: 8px; margin-top: 8px; }")
        sb.append(".footer { text-align: center; font-size: 11px; color: #94A3B8; margin-top: 30px; border-top: 1px dashed #CBD5E1; padding-top: 10px; }")
        sb.append("</style></head><body>")

        sb.append("<div class='header'>")
        sb.append("<h1>RESTAURANTE RIVERA</h1>")
        sb.append("<h2>RESUMEN Y AUDITORÍA DE TURNO DE CAJA</h2>")
        sb.append("</div>")

        sb.append("<div class='info-grid'>")
        sb.append("<div class='info-item'><b>Sucursal:</b> $branchName</div>")
        sb.append("<div class='info-item'><b>Cajero(a) Responsable:</b> $cashierName</div>")
        sb.append("<div class='info-item'><b>Fecha de Emisión:</b> $nowFormatted</div>")
        sb.append("<div class='info-item'><b>Total Transacciones:</b> ${sales.size} ventas</div>")
        sb.append("</div>")

        sb.append("<div class='cards-row'>")
        sb.append("<div class='card'><div class='card-title'>Efectivo</div><div class='card-val'>${formatQuetzales(totalCash)}</div></div>")
        sb.append("<div class='card'><div class='card-title'>Tarjeta</div><div class='card-val'>${formatQuetzales(totalCard)}</div></div>")
        sb.append("<div class='card'><div class='card-title'>Transferencia</div><div class='card-val'>${formatQuetzales(totalTransfer)}</div></div>")
        sb.append("<div class='card' style='background:#DBEAFE;'><div class='card-title'>Total Ventas</div><div class='card-val' style='color:#1D4ED8;'>${formatQuetzales(grandTotal)}</div></div>")
        sb.append("</div>")

        sb.append("<h3 style='margin-bottom:6px; color:#1E3A8A;'>Detalle de Transacciones del Turno</h3>")
        sb.append("<table><thead><tr><th>#</th><th>Comanda / Folio</th><th>Hora</th><th>Método de Pago</th><th>Cajero</th><th class='text-right'>Total</th></tr></thead><tbody>")

        if (sales.isEmpty()) {
            sb.append("<tr><td colspan='6' style='text-align:center; padding:16px; color:#64748B;'>No se registraron ventas en este turno.</td></tr>")
        } else {
            sales.forEachIndexed { i, s ->
                val tStr = timeSdf.format(Date(s.timestamp))
                val badgeClass = when (s.paymentMethod.lowercase()) {
                    "efectivo" -> "badge-cash"
                    "tarjeta" -> "badge-card"
                    else -> "badge-transfer"
                }
                sb.append("<tr>")
                sb.append("<td>${i + 1}</td>")
                sb.append("<td><b>${s.orderNumber}</b></td>")
                sb.append("<td>$tStr</td>")
                sb.append("<td><span class='badge $badgeClass'>${s.paymentMethod}</span></td>")
                sb.append("<td>${s.cashierName}</td>")
                sb.append("<td class='text-right'><b>${formatQuetzales(s.total)}</b></td>")
                sb.append("</tr>")
            }
        }
        sb.append("</tbody></table>")

        sb.append("<div class='totals-box'>")
        sb.append("<div class='total-row'><span>Fondo Inicial de Caja:</span><b>${formatQuetzales(initialFund)}</b></div>")
        sb.append("<div class='total-row'><span>Ventas en Efectivo del Turno:</span><b>${formatQuetzales(totalCash)}</b></div>")
        sb.append("<div class='total-row'><span>Efectivo Total Esperado en Caja:</span><b>${formatQuetzales(initialFund + totalCash)}</b></div>")
        if (cashCounted != null) {
            sb.append("<div class='total-row'><span>Efectivo Contado Físico:</span><b>${formatQuetzales(cashCounted)}</b></div>")
        }
        if (difference != null) {
            val diffColor = if (difference >= 0) "#166534" else "#DC2626"
            sb.append("<div class='total-row' style='color:$diffColor;'><span>Diferencia / Arqueo de Caja:</span><b>${if (difference >= 0) "+" else ""}${formatQuetzales(difference)}</b></div>")
        }
        sb.append("<div class='grand-row'><span>GRAN TOTAL VENTAS TURNO:</span><span>${formatQuetzales(grandTotal)}</span></div>")
        sb.append("</div>")

        sb.append("<div class='footer'>")
        sb.append("<p>Reporte generado conforme a la normativa interna de control financiero de Restaurante Rivera.</p>")
        sb.append("<p>Firma Cajero(a): _______________________ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Firma Gerente / Auditor: _______________________</p>")
        sb.append("</div>")

        sb.append("</body></html>")
        return sb.toString()
    }
}
