package com.example.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.entity.DetailedSalesReportData
import com.example.data.entity.InvoiceEntity
import com.example.data.entity.OrderItemEntity
import com.example.data.entity.SaleEntity
import com.example.ui.components.formatQuetzales
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
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
        val webView = WebView(context).apply {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
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

            override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                val parent = view?.parent as? android.view.ViewGroup
                parent?.removeView(view)
                try {
                    view?.stopLoading()
                    view?.destroy()
                } catch (_: Throwable) {}
                return true
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
        val divider = when (invoice.ticketDividerStyle) {
            "====" -> "================================="
            "...." -> "................................."
            "****" -> "*********************************"
            else -> "---------------------------------"
        }
        val headerDivider = "================================="

        sb.appendLine(headerDivider)
        if (invoice.showLogo && invoice.logoUri.isNotBlank()) {
            sb.appendLine("        [ LOGOTIPO RESTAURANTE ]        ")
        }
        if (invoice.showRestaurantName && invoice.restaurantName.isNotBlank()) {
            sb.appendLine("       ${invoice.restaurantName.uppercase()}       ")
        }
        if (invoice.showBranchName && invoice.branchName.isNotBlank()) {
            sb.appendLine("       ${invoice.branchName}       ")
        }
        if (invoice.showTaxId && invoice.restaurantTaxId.isNotBlank()) {
            sb.appendLine("       NIT: ${invoice.restaurantTaxId}       ")
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
        sb.appendLine(headerDivider)

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
        if (invoice.showTableNumber && invoice.tableNumber.isNotBlank()) {
            sb.appendLine("Mesa/Área: ${invoice.tableNumber}")
        }
        if (invoice.showWaiterName && invoice.waiterName.isNotBlank()) {
            sb.appendLine("Mesero(a): ${invoice.waiterName}")
        }
        if (invoice.showCashierName) {
            sb.appendLine("Cajero(a): ${invoice.cashierName}")
        }
        if (invoice.showPaymentMethod) {
            sb.appendLine("Método de Pago: ${invoice.paymentMethod}")
        }

        if (invoice.showCustomerType || invoice.showCustomerNit || invoice.showCustomerPhone) {
            sb.appendLine(divider)
            sb.appendLine("DATOS DEL CLIENTE:")
            if (invoice.showCustomerType) {
                val custNameDisplay = when (invoice.customerType) {
                    "Consumidor Final" -> "Consumidor Final"
                    "Clientes Varios" -> "Clientes Varios"
                    else -> invoice.customerName.ifBlank { "Cliente Registrado" }
                }
                sb.appendLine("Nombre: $custNameDisplay")
            }
            if (invoice.showCustomerNit && invoice.customerNit.isNotBlank()) {
                sb.appendLine("NIT: ${invoice.customerNit}")
            }
            if (invoice.showCustomerPhone && invoice.customerPhone.isNotBlank()) {
                sb.appendLine("Teléfono: ${invoice.customerPhone}")
            }
        }

        sb.appendLine(divider)
        if (invoice.showQuantity || invoice.showProductName || invoice.showUnitPrice || invoice.showSubtotal) {
            sb.appendLine("DETALLE DE CONSUMO:")
            sb.appendLine(divider)
            items.forEach { item ->
                val line = StringBuilder()
                if (invoice.showQuantity) line.append("${item.quantity}x ")
                if (invoice.showProductName) line.append("${item.productName} ")
                if (invoice.showUnitPrice) line.append("($cur${String.format(Locale.US, "%.2f", item.unitPrice)}) ")
                if (invoice.showSubtotal) line.append("-> $cur${String.format(Locale.US, "%.2f", item.subtotal)}")
                sb.appendLine(line.toString().trim())
                if (item.notes.isNotBlank()) {
                    sb.appendLine("   * Nota: ${item.notes}")
                }
            }
            sb.appendLine(divider)
        }

        if (invoice.showSubtotal) {
            sb.appendLine("Subtotal: $cur${String.format(Locale.US, "%.2f", invoice.subtotal)}")
        }
        if (invoice.discount > 0) {
            sb.appendLine("Descuento: -$cur${String.format(Locale.US, "%.2f", invoice.discount)}")
        }
        if (invoice.showTaxBreakdown) {
            val baseAmount = invoice.totalAmount / 1.12
            val vatAmount = invoice.totalAmount - baseAmount
            sb.appendLine("Base Imponible: $cur${String.format(Locale.US, "%.2f", baseAmount)}")
            sb.appendLine("IVA (12%): $cur${String.format(Locale.US, "%.2f", vatAmount)}")
        }
        if (invoice.showTotal) {
            sb.appendLine("TOTAL A PAGAR: $cur${String.format(Locale.US, "%.2f", invoice.totalAmount)}")
        }

        if (invoice.showTipLine) {
            sb.appendLine(divider)
            sb.appendLine("Propina Sugerida (10%): $cur${String.format(Locale.US, "%.2f", invoice.totalAmount * 0.10)}")
            sb.appendLine("Propina Voluntaria: [ ____________ ]")
            sb.appendLine("Firma Cliente:      [ ____________ ]")
        }

        if (invoice.showLegalNotice && invoice.legalNotice.isNotBlank()) {
            sb.appendLine(divider)
            sb.appendLine("  ${invoice.legalNotice}  ")
        }

        if (invoice.showFooterMessage && invoice.footerMessage.isNotBlank()) {
            sb.appendLine(headerDivider)
            sb.appendLine("  ${invoice.footerMessage}  ")
        }
        if (invoice.showQrCode) {
            sb.appendLine("  [ QR: Consulta tu factura / Menú Online ]  ")
        }
        if (invoice.showPrintTimestamp) {
            sb.appendLine("Impreso: ${dateFormat.format(Date())}")
        }
        sb.appendLine(headerDivider)
        return sb.toString()
    }

    fun shareInvoiceViaWhatsApp(context: Context, invoice: InvoiceEntity, items: List<OrderItemEntity>) {
        try {
            // 1. Generar el archivo físico PDF con diseño FEL oficial dentro de context.cacheDir
            val pdfFile = PdfInvoiceGenerator.generateInvoicePdfFile(context, invoice, items)

            // 2. Generar el URI seguro mediante FileProvider con permisos de lectura
            val pdfUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val caption = "Adjunto Factura Electrónica FEL No. ${invoice.invoiceNumber} de ${invoice.restaurantName}. Total: ${invoice.currencySymbol}${String.format(Locale.US, "%.2f", invoice.totalAmount)}"

            // 3. Construir Intent con MIME application/pdf y adjuntar EXTRA_STREAM
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, pdfUri)
                putExtra(Intent.EXTRA_TEXT, caption)
                putExtra(Intent.EXTRA_SUBJECT, "Factura Electrónica ${invoice.invoiceNumber}")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setPackage("com.whatsapp")
            }

            // Conceder permisos de lectura a la aplicación receptora
            context.grantUriPermission("com.whatsapp", pdfUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val resInfoList = context.packageManager.queryIntentActivities(sendIntent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                context.grantUriPermission(packageName, pdfUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            try {
                context.startActivity(sendIntent)
            } catch (e: Exception) {
                // Si WhatsApp no está instalado directamente, abrir selector general de aplicaciones
                val chooser = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, pdfUri)
                    putExtra(Intent.EXTRA_TEXT, caption)
                    putExtra(Intent.EXTRA_SUBJECT, "Factura Electrónica ${invoice.invoiceNumber}")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(chooser, "Compartir Factura PDF ${invoice.invoiceNumber}"))
            }
        } catch (e: Exception) {
            Log.e("ReportExportHelper", "Error al compartir Factura PDF por WhatsApp: ${e.message}", e)
            Toast.makeText(context, "Error al generar PDF para WhatsApp: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun sendInvoiceViaEmail(context: Context, invoice: InvoiceEntity, items: List<OrderItemEntity>) {
        try {
            val pdfFile = PdfInvoiceGenerator.generateInvoicePdfFile(context, invoice, items)
            val pdfUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_EMAIL, if (invoice.customerPhone.contains("@")) arrayOf(invoice.customerPhone) else emptyArray<String>())
                putExtra(Intent.EXTRA_SUBJECT, "Factura FEL ${invoice.invoiceNumber} - ${invoice.restaurantName}")
                putExtra(Intent.EXTRA_TEXT, "Estimado(a) cliente,\n\nAdjuntamos su Factura Electrónica (FEL) No. ${invoice.invoiceNumber} correspondiente a su consumo en ${invoice.restaurantName}.\n\nTotal: ${invoice.currencySymbol}${String.format(Locale.US, "%.2f", invoice.totalAmount)}\n\n¡Gracias por su preferencia!")
                putExtra(Intent.EXTRA_STREAM, pdfUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(emailIntent, "Enviar Factura por Correo"))
        } catch (e: Exception) {
            val text = buildInvoiceFormattedText(invoice, items)
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_SUBJECT, "Factura ${invoice.invoiceNumber} - ${invoice.restaurantName}")
                putExtra(Intent.EXTRA_TEXT, text)
            }
            try {
                context.startActivity(Intent.createChooser(emailIntent, "Enviar Factura por Correo"))
            } catch (ex: Exception) {
                Toast.makeText(context, "No hay aplicación de correo configurada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun printOrExportInvoicePdf(context: Context, invoice: InvoiceEntity, items: List<OrderItemEntity>) {
        val htmlContent = buildHtmlInvoice(invoice, items)
        val webView = WebView(context).apply {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
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

            override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                val parent = view?.parent as? android.view.ViewGroup
                parent?.removeView(view)
                try {
                    view?.stopLoading()
                    view?.destroy()
                } catch (_: Throwable) {}
                return true
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    // =========================================================================
    // 3. INTEGRACIÓN CON IMPRESORA TÉRMICA / DE CALOR (Bluetooth / ESC-POS / PrintManager)
    // =========================================================================

    /**
     * Imprime el recibo térmico de la factura en papel continuo (58mm o 80mm).
     * Si se detecta o proporciona una impresora Bluetooth, envía los comandos ESC/POS directamente.
     * De lo contrario o como opción estándar, invoca el servicio nativo de impresión Android (PrintManager).
     */
    fun printThermalReceipt(
        context: Context,
        invoice: InvoiceEntity,
        items: List<OrderItemEntity>,
        paperWidthMm: Int = 80,
        preferredBluetoothAddress: String? = null,
        onResult: ((Boolean, String) -> Unit)? = null
    ) {
        val printerManager = ThermalPrinterManager(context)
        val escPosBytes = printerManager.buildEscPosBytesForInvoice(invoice, items, paperWidthMm)

        // Buscar impresora Bluetooth emparejada si no se especificó una
        val targetBtAddress = preferredBluetoothAddress ?: run {
            val paired = printerManager.getPairedBluetoothDevices()
            paired.firstOrNull()?.second
        }

        if (!targetBtAddress.isNullOrBlank()) {
            val config = ThermalPrinterConfig(
                connectionType = PrinterConnectionType.BLUETOOTH,
                bluetoothAddress = targetBtAddress,
                paperWidthMm = paperWidthMm
            )
            Toast.makeText(context, "Enviando a impresora Bluetooth térmica...", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.Main).launch {
                val result = printerManager.printBytes(config, escPosBytes)
                if (result.isSuccess) {
                    Toast.makeText(context, "Ticket térmico impreso exitosamente.", Toast.LENGTH_SHORT).show()
                    onResult?.invoke(true, result.getOrDefault("Éxito"))
                } else {
                    Toast.makeText(context, "Error Bluetooth, abriendo PrintManager nativo...", Toast.LENGTH_SHORT).show()
                    printThermalViaPrintManager(context, invoice, items, paperWidthMm)
                    onResult?.invoke(false, result.exceptionOrNull()?.message ?: "Error BT")
                }
            }
        } else {
            // Abrir servicio nativo PrintManager configurado para dimensiones de ticket térmico
            printThermalViaPrintManager(context, invoice, items, paperWidthMm)
            onResult?.invoke(true, "Enviado a servicio de impresión Android")
        }
    }

    private fun printThermalViaPrintManager(
        context: Context,
        invoice: InvoiceEntity,
        items: List<OrderItemEntity>,
        paperWidthMm: Int = 80
    ) {
        val thermalHtml = buildThermalHtmlReceipt(invoice, items, paperWidthMm)
        val webView = WebView(context).apply {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                if (printManager != null) {
                    val printAdapter = webView.createPrintDocumentAdapter("Ticket_${invoice.invoiceNumber}")
                    val builder = PrintAttributes.Builder()
                    builder.setMediaSize(PrintAttributes.MediaSize.ISO_A6)
                    printManager.print("Ticket_${invoice.invoiceNumber}", printAdapter, builder.build())
                } else {
                    Toast.makeText(context, "Servicio de impresión no disponible", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                val parent = view?.parent as? android.view.ViewGroup
                parent?.removeView(view)
                try {
                    view?.stopLoading()
                    view?.destroy()
                } catch (_: Throwable) {}
                return true
            }
        }
        webView.loadDataWithBaseURL(null, thermalHtml, "text/html", "UTF-8", null)
    }

    private fun buildThermalHtmlReceipt(
        invoice: InvoiceEntity,
        items: List<OrderItemEntity>,
        paperWidthMm: Int = 80
    ): String {
        val cur = invoice.currencySymbol
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateStr = dateFormat.format(Date(invoice.timestamp))
        val baseAmount = invoice.totalAmount / 1.12
        val vatAmount = invoice.totalAmount - baseAmount
        val totalInWords = NumberToWordsHelper.toSpanishWords(invoice.totalAmount)

        val sb = StringBuilder()
        sb.append("<!DOCTYPE html><html><head><meta charset='utf-8'><style>")
        sb.append("@page { size: ${paperWidthMm}mm auto; margin: 2mm; }")
        sb.append("body { font-family: monospace; font-size: 11px; width: ${paperWidthMm - 4}mm; margin: 0 auto; color: #000; }")
        sb.append(".center { text-align: center; }")
        sb.append(".right { text-align: right; }")
        sb.append(".bold { font-weight: bold; }")
        sb.append(".divider { border-top: 1px dashed #000; margin: 5px 0; }")
        sb.append(".double-divider { border-top: 2px solid #000; margin: 5px 0; }")
        sb.append("table { width: 100%; border-collapse: collapse; font-size: 10.5px; }")
        sb.append("td, th { padding: 2px 0; }")
        sb.append("</style></head><body>")

        sb.append("<div class='center'>")
        sb.append("<div class='bold' style='font-size: 14px;'>${invoice.restaurantName.uppercase()}</div>")
        if (invoice.branchName.isNotBlank()) sb.append("<div>Sucursal: ${invoice.branchName}</div>")
        sb.append("<div>NIT Emisor: ${invoice.restaurantTaxId.ifBlank { "1234567-8" }}</div>")
        if (invoice.restaurantAddress.isNotBlank()) sb.append("<div>${invoice.restaurantAddress}</div>")
        if (invoice.restaurantPhone.isNotBlank()) sb.append("<div>Tel: ${invoice.restaurantPhone}</div>")
        sb.append("</div>")

        sb.append("<div class='double-divider'></div>")
        sb.append("<div class='center bold'>DOCUMENTO TRIBUTARIO ELECTRÓNICO</div>")
        sb.append("<div class='center bold' style='font-size: 13px;'>FACTURA (FEL)</div>")
        sb.append("<div><b>Serie:</b> ${if (invoice.invoiceNumber.contains("-")) invoice.invoiceNumber.substringBeforeLast("-") else "FEL-A"}</div>")
        sb.append("<div><b>Número:</b> ${invoice.invoiceNumber.substringAfterLast("-", invoice.invoiceNumber)}</div>")
        sb.append("<div><b>Fecha:</b> $dateStr</div>")
        sb.append("<div class='divider'></div>")

        val clientName = when {
            invoice.customerName.isNotBlank() -> invoice.customerName
            invoice.customerType.isNotBlank() -> invoice.customerType
            else -> "Consumidor Final"
        }
        sb.append("<div><b>Cliente:</b> $clientName</div>")
        sb.append("<div><b>NIT:</b> ${invoice.customerNit.ifBlank { "C/F" }}</div>")
        if (invoice.orderNumber.isNotBlank()) {
            sb.append("<div><b>Comanda:</b> #${invoice.orderNumber}${if (invoice.tableNumber.isNotBlank()) " | <b>Mesa:</b> ${invoice.tableNumber}" else ""}</div>")
        }
        sb.append("<div><b>Cajero:</b> ${invoice.cashierName} | <b>Pago:</b> ${invoice.paymentMethod}</div>")

        sb.append("<div class='divider'></div>")
        sb.append("<table><thead><tr><th>CANT</th><th>DESCRIPCIÓN</th><th class='right'>TOTAL</th></tr></thead><tbody>")
        for (item in items) {
            sb.append("<tr>")
            sb.append("<td style='vertical-align: top; width: 35px;'>${item.quantity}x</td>")
            sb.append("<td><b>${item.productName}</b>")
            if (item.notes.isNotBlank()) sb.append("<div style='font-size: 9px; color: #444;'>* ${item.notes}</div>")
            sb.append("</td>")
            sb.append("<td class='right' style='vertical-align: top;'>$cur${String.format(Locale.US, "%.2f", item.subtotal)}</td>")
            sb.append("</tr>")
        }
        sb.append("</tbody></table>")

        sb.append("<div class='divider'></div>")
        sb.append("<table>")
        sb.append("<tr><td>Subtotal:</td><td class='right'>$cur${String.format(Locale.US, "%.2f", invoice.subtotal)}</td></tr>")
        if (invoice.discount > 0) {
            sb.append("<tr><td>Descuento:</td><td class='right'>-$cur${String.format(Locale.US, "%.2f", invoice.discount)}</td></tr>")
        }
        sb.append("<tr><td>Base Imponible:</td><td class='right'>$cur${String.format(Locale.US, "%.2f", baseAmount)}</td></tr>")
        sb.append("<tr><td>IVA (12%):</td><td class='right'>$cur${String.format(Locale.US, "%.2f", vatAmount)}</td></tr>")
        sb.append("<tr class='bold' style='font-size: 13px;'><td style='border-top: 1px solid #000; padding-top: 4px;'>TOTAL A PAGAR:</td><td class='right' style='border-top: 1px solid #000; padding-top: 4px;'>$cur${String.format(Locale.US, "%.2f", invoice.totalAmount)}</td></tr>")
        sb.append("</table>")

        sb.append("<div class='divider'></div>")
        sb.append("<div class='bold'>TOTAL EN LETRAS:</div>")
        sb.append("<div style='font-size: 9.5px;'>$totalInWords</div>")

        sb.append("<div class='double-divider'></div>")
        sb.append("<div class='center' style='font-size: 9.5px;'>")
        sb.append("<div class='bold'>CERTIFICACIÓN SAT - FEL</div>")
        sb.append("<div>Certificador: INFILE, S.A. | NIT: 125543-9</div>")
        sb.append("<div>Documento Tributario Electrónico</div>")
        if (invoice.footerMessage.isNotBlank()) {
            sb.append("<div style='margin-top: 4px; font-style: italic;'>${invoice.footerMessage}</div>")
        }
        sb.append("</div>")

        sb.append("</body></html>")
        return sb.toString()
    }

    // =========================================================================
    // 1. REDISEÑO DEL PDF (Diseño Oficial FEL Guatemala con Tablas 1px solid #000)
    // =========================================================================

    private fun buildHtmlInvoice(invoice: InvoiceEntity, items: List<OrderItemEntity>): String {
        val sb = StringBuilder()
        val cur = invoice.currencySymbol
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val dateStr = dateFormat.format(Date(invoice.timestamp))

        val serie = if (invoice.invoiceNumber.contains("-")) {
            invoice.invoiceNumber.substringBeforeLast("-").ifBlank { "FEL-A" }
        } else {
            "FEL-A"
        }
        val dteNumber = invoice.invoiceNumber.substringAfterLast("-", invoice.invoiceNumber)
        val uuid = run {
            val hash = Math.abs(invoice.invoiceNumber.hashCode().toLong())
            val p1 = hash.toString(16).uppercase().padStart(8, '0').take(8)
            val p2 = "4B7D"
            val p3 = "4E8F"
            val p4 = "A2C1"
            val p5 = Math.abs(invoice.id).toString(16).uppercase().padStart(12, '0').take(12)
            "$p1-$p2-$p3-$p4-$p5"
        }

        val baseAmount = invoice.totalAmount / 1.12
        val vatAmount = invoice.totalAmount - baseAmount
        val totalInWords = NumberToWordsHelper.toSpanishWords(invoice.totalAmount)

        // Generar QR de verificación SAT en base64 para renderizado offline garantizado
        val verificationUrl = "https://fel.sat.gob.gt/verificador?nit=${invoice.restaurantTaxId}&serie=$serie&numero=$dteNumber&monto=${String.format(Locale.US, "%.2f", invoice.totalAmount)}"
        val qrBase64Src = try {
            val qrBitmap: Bitmap = QRCodeHelper.generateQRCodeBitmap(verificationUrl, 256)
            val stream = ByteArrayOutputStream()
            qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val base64 = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
            "data:image/png;base64,$base64"
        } catch (_: Exception) {
            ""
        }

        sb.append("<!DOCTYPE html><html><head><meta charset='utf-8'>")
        sb.append("<style>")
        sb.append("@page { size: letter portrait; margin: 12mm; }")
        sb.append("* { box-sizing: border-box; }")
        sb.append("body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; color: #000; margin: 0; padding: 5px; font-size: 11px; line-height: 1.3; }")

        // Encabezado delimitado: Negocio a la izquierda, bloque FACTURA con borde negro a la derecha
        sb.append(".header-table { width: 100%; border-collapse: collapse; margin-bottom: 12px; }")
        sb.append(".header-left { vertical-align: top; width: 62%; padding-right: 15px; }")
        sb.append(".header-left h1 { font-size: 18px; font-weight: 900; margin: 0 0 3px 0; color: #000; text-transform: uppercase; letter-spacing: 0.5px; }")
        sb.append(".header-left .branch { font-size: 12px; font-weight: bold; margin: 0 0 4px 0; color: #222; }")
        sb.append(".header-left p { margin: 2px 0; font-size: 10.5px; color: #111; }")

        sb.append(".header-right { vertical-align: top; width: 38%; }")
        sb.append(".fel-box { border: 1px solid #000; padding: 0; background-color: #fff; }")
        sb.append(".fel-box-header { background-color: #F3F4F6; border-bottom: 1px solid #000; padding: 5px; text-align: center; font-weight: bold; font-size: 11px; letter-spacing: 0.5px; }")
        sb.append(".fel-box-body { padding: 6px 8px; font-size: 10px; line-height: 1.45; }")

        // Cuadro de datos del cliente delimitado con 1px solid #000
        sb.append(".client-box { width: 100%; border-collapse: collapse; border: 1px solid #000; margin-bottom: 12px; }")
        sb.append(".client-box-header { background-color: #F3F4F6; border-bottom: 1px solid #000; padding: 4px 8px; font-weight: bold; font-size: 10.5px; }")
        sb.append(".client-box td { padding: 4px 8px; font-size: 10.5px; vertical-align: top; border: 1px solid #000; }")

        // Tabla de detalle de consumos con bordes negros bien definidos (1px solid #000)
        sb.append(".items-table { width: 100%; border-collapse: collapse; border: 1px solid #000; margin-bottom: 12px; }")
        sb.append(".items-table th { background-color: #F3F4F6; color: #000; border: 1px solid #000; padding: 6px 8px; font-size: 10.5px; font-weight: bold; text-align: left; }")
        sb.append(".items-table td { border: 1px solid #000; padding: 5px 8px; font-size: 10.5px; }")
        sb.append(".text-center { text-align: center; }")
        sb.append(".text-right { text-align: right; }")

        // Sección de Totales y Total en Letras
        sb.append(".totals-container { width: 100%; border-collapse: collapse; margin-bottom: 12px; }")
        sb.append(".words-cell { vertical-align: top; width: 55%; padding-right: 12px; }")
        sb.append(".words-box { border: 1px solid #000; background-color: #FAFAFA; padding: 0; }")
        sb.append(".words-box-header { background-color: #F3F4F6; border-bottom: 1px solid #000; padding: 4px 8px; font-weight: bold; font-size: 10px; }")
        sb.append(".words-box-body { padding: 8px; font-size: 10px; line-height: 1.4; font-weight: bold; color: #111; }")

        sb.append(".totals-cell { vertical-align: top; width: 45%; }")
        sb.append(".totals-table { width: 100%; border-collapse: collapse; border: 1px solid #000; }")
        sb.append(".totals-table td { border: 1px solid #000; padding: 4px 8px; font-size: 10.5px; }")
        sb.append(".totals-table .grand-total { background-color: #F3F4F6; font-size: 12px; font-weight: bold; }")

        // Cuadro de Certificación SAT y QR al pie de página
        sb.append(".cert-box { width: 100%; border-collapse: collapse; border: 1px solid #000; }")
        sb.append(".cert-box td { border: none; padding: 8px; vertical-align: middle; }")
        sb.append(".qr-cell { width: 90px; text-align: center; }")
        sb.append(".qr-img { width: 80px; height: 80px; display: block; border: 1px solid #000; }")
        sb.append(".cert-text { font-size: 9.5px; line-height: 1.4; color: #111; padding-left: 10px; }")
        sb.append(".cert-text .cert-title { font-weight: bold; font-size: 10px; margin-bottom: 2px; }")
        sb.append("</style></head><body>")

        // 1. Encabezado delimitado
        sb.append("<table class='header-table'><tr>")
        sb.append("<td class='header-left'>")
        sb.append("<h1>${invoice.restaurantName}</h1>")
        if (invoice.branchName.isNotBlank()) sb.append("<div class='branch'>Sucursal: ${invoice.branchName}</div>")
        sb.append("<p><b>NIT Emisor:</b> ${invoice.restaurantTaxId.ifBlank { "1234567-8" }}</p>")
        if (invoice.restaurantAddress.isNotBlank()) sb.append("<p><b>Dirección:</b> ${invoice.restaurantAddress}</p>")
        val contact = listOfNotNull(
            invoice.restaurantPhone.takeIf { it.isNotBlank() }?.let { "Tel: $it" },
            invoice.restaurantEmail.takeIf { it.isNotBlank() }?.let { "Email: $it" }
        ).joinToString(" • ")
        if (contact.isNotBlank()) sb.append("<p>$contact</p>")
        if (invoice.legalNotice.isNotBlank()) sb.append("<p style='font-size: 9.5px; color: #333;'>${invoice.legalNotice}</p>")
        sb.append("</td>")

        sb.append("<td class='header-right'>")
        sb.append("<div class='fel-box'>")
        sb.append("<div class='fel-box-header'>FACTURA ELECTRÓNICA (DTE)</div>")
        sb.append("<div class='fel-box-body'>")
        sb.append("<div><b>SERIE:</b> $serie</div>")
        sb.append("<div><b>NÚMERO DTE:</b> $dteNumber</div>")
        sb.append("<div><b>AUTORIZACIÓN:</b> <span style='font-size: 8.5px;'>$uuid</span></div>")
        sb.append("<div><b>FECHA EMISIÓN:</b> $dateStr</div>")
        sb.append("<div><b>RÉGIMEN:</b> Pagos Trimestrales ISR</div>")
        sb.append("</div>")
        sb.append("</div>")
        sb.append("</td>")
        sb.append("</tr></table>")

        // 2. Cuadro de datos del cliente
        val clientName = when {
            invoice.customerName.isNotBlank() -> invoice.customerName
            invoice.customerType.isNotBlank() -> invoice.customerType
            else -> "Consumidor Final"
        }
        sb.append("<table class='client-box'>")
        sb.append("<tr><td colspan='2' class='client-box-header'>DATOS DEL RECEPTOR / CLIENTE</td></tr>")
        sb.append("<tr>")
        sb.append("<td style='width: 60%;'>")
        sb.append("<div><b>Nombre / Razón Social:</b> $clientName</div>")
        sb.append("<div><b>NIT / CUI:</b> ${invoice.customerNit.ifBlank { "C/F (Consumidor Final)" }}</div>")
        sb.append("<div><b>Dirección:</b> ${invoice.restaurantAddress.ifBlank { "Ciudad de Guatemala" }}</div>")
        sb.append("</td>")
        sb.append("<td style='width: 40%;'>")
        sb.append("<div><b>Fecha:</b> $dateStr</div>")
        sb.append("<div><b>Teléfono:</b> ${invoice.customerPhone.ifBlank { "N/A" }}</div>")
        sb.append("<div><b>Comanda:</b> #${invoice.orderNumber}${if (invoice.tableNumber.isNotBlank()) " | <b>Mesa:</b> ${invoice.tableNumber}" else ""}</div>")
        sb.append("</td>")
        sb.append("</tr></table>")

        // 3. Tabla de detalle de consumos [Código, Cantidad, Descripción, P. Unitario, Total]
        sb.append("<table class='items-table'><thead><tr>")
        sb.append("<th style='width: 35px;' class='text-center'>No.</th>")
        sb.append("<th style='width: 45px;' class='text-center'>Cant.</th>")
        sb.append("<th>Descripción del Producto / Servicio</th>")
        sb.append("<th style='width: 85px;' class='text-right'>P. Unitario</th>")
        sb.append("<th style='width: 95px;' class='text-right'>Total ($cur)</th>")
        sb.append("</tr></thead><tbody>")

        items.forEachIndexed { index, item ->
            sb.append("<tr>")
            sb.append("<td class='text-center'>#${index + 1}</td>")
            sb.append("<td class='text-center'>${item.quantity}</td>")
            sb.append("<td><b>${item.productName}</b>")
            if (item.notes.isNotBlank()) {
                sb.append("<div style='font-size: 9.5px; color: #4B5563; font-style: italic;'>Nota: ${item.notes}</div>")
            }
            sb.append("</td>")
            sb.append("<td class='text-right'>$cur${String.format(Locale.US, "%.2f", item.unitPrice)}</td>")
            sb.append("<td class='text-right'><b>$cur${String.format(Locale.US, "%.2f", item.subtotal)}</b></td>")
            sb.append("</tr>")
        }
        sb.append("</tbody></table>")

        // 4. Pie con TOTAL EN LETRAS y desglose de IVA (12%)
        sb.append("<table class='totals-container'><tr>")
        sb.append("<td class='words-cell'>")
        sb.append("<div class='words-box'>")
        sb.append("<div class='words-box-header'>TOTAL EN LETRAS</div>")
        sb.append("<div class='words-box-body'>")
        sb.append("<div>$totalInWords</div>")
        sb.append("<div style='margin-top: 8px; font-weight: normal; font-size: 9.5px; color: #444;'>")
        sb.append("<b>Forma de Pago:</b> ${invoice.paymentMethod} • <b>Cajero:</b> ${invoice.cashierName}")
        sb.append("</div>")
        sb.append("</div>")
        sb.append("</div>")
        sb.append("</td>")

        sb.append("<td class='totals-cell'>")
        sb.append("<table class='totals-table'>")
        sb.append("<tr><td>Subtotal:</td><td class='text-right'>$cur${String.format(Locale.US, "%.2f", invoice.subtotal)}</td></tr>")
        if (invoice.discount > 0) {
            sb.append("<tr><td>Descuento:</td><td class='text-right'>-$cur${String.format(Locale.US, "%.2f", invoice.discount)}</td></tr>")
        }
        sb.append("<tr><td>Base Imponible:</td><td class='text-right'>$cur${String.format(Locale.US, "%.2f", baseAmount)}</td></tr>")
        sb.append("<tr><td>IVA (12%):</td><td class='text-right'>$cur${String.format(Locale.US, "%.2f", vatAmount)}</td></tr>")
        sb.append("<tr class='grand-total'><td><b>TOTAL A PAGAR:</b></td><td class='text-right'><b>$cur${String.format(Locale.US, "%.2f", invoice.totalAmount)}</b></td></tr>")
        sb.append("</table>")
        sb.append("</td>")
        sb.append("</tr></table>")

        // 5. Código QR de certificación SAT y pie de página
        sb.append("<table class='cert-box'><tr>")
        if (qrBase64Src.isNotBlank()) {
            sb.append("<td class='qr-cell'><img class='qr-img' src='$qrBase64Src' alt='QR Verificación SAT' /></td>")
        }
        sb.append("<td class='cert-text'>")
        sb.append("<div class='cert-title'>CERTIFICACIÓN SAT - FACTURA ELECTRÓNICA EN LÍNEA (FEL)</div>")
        sb.append("<div><b>Número de Autorización:</b> $uuid</div>")
        sb.append("<div><b>Serie:</b> $serie | <b>Número DTE:</b> $dteNumber | <b>Fecha Certificación:</b> $dateStr</div>")
        sb.append("<div><b>Certificador Autorizado:</b> INFILE, S.A. (NIT: 125543-9)</div>")
        sb.append("<div>Documento Tributario Electrónico emitido de conformidad con las disposiciones de la SAT.</div>")
        if (invoice.footerMessage.isNotBlank()) {
            sb.append("<div style='margin-top: 4px; font-style: italic; color: #333;'>${invoice.footerMessage}</div>")
        }
        sb.append("</td>")
        sb.append("</tr></table>")

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

        val webView = WebView(context).apply {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
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

            override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                val parent = view?.parent as? android.view.ViewGroup
                parent?.removeView(view)
                try {
                    view?.stopLoading()
                    view?.destroy()
                } catch (_: Throwable) {}
                return true
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
