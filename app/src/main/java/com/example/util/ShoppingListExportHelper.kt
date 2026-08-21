package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.entity.InventoryItemEntity
import com.example.ui.components.formatQuetzales
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ShoppingListExportHelper {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val fileDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())

    fun buildTextSummary(items: List<InventoryItemEntity>): String {
        val sb = StringBuilder()
        sb.appendLine("=================================")
        sb.appendLine("    RESTAURANTE RIVERA POS      ")
        sb.appendLine("   LISTA AUTOMÁTICA DE COMPRAS   ")
        sb.appendLine("=================================")
        sb.appendLine("Fecha de Generación: ${dateFormat.format(Date())}")
        sb.appendLine("Total Insumos a Reabastecer: ${items.size}")
        sb.appendLine("---------------------------------")

        var totalEstimatedCost = 0.0
        items.forEachIndexed { index, item ->
            val suggestedQty = maxOf(0.0, item.idealStock - item.currentStock)
            val estimatedCost = suggestedQty * item.unitCost
            totalEstimatedCost += estimatedCost

            sb.appendLine("${index + 1}. ${item.productName.uppercase()}")
            sb.appendLine("   Stock Actual: ${item.currentStock} ${item.unit}")
            sb.appendLine("   Stock Mínimo: ${item.minStock} ${item.unit}")
            sb.appendLine("   Sugerido Comprar: ${String.format(Locale.US, "%.2f", suggestedQty)} ${item.unit}")
            sb.appendLine("   Proveedor: ${item.supplier}")
            if (item.unitCost > 0) {
                sb.appendLine("   Costo Est.: ${formatQuetzales(estimatedCost)}")
            }
            sb.appendLine("---------------------------------")
        }

        if (totalEstimatedCost > 0) {
            sb.appendLine("COSTO ESTIMADO TOTAL: ${formatQuetzales(totalEstimatedCost)}")
            sb.appendLine("=================================")
        }
        sb.appendLine("Generado por Sistema de Inventario Rivera")
        return sb.toString()
    }

    // 1. SHARE VIA WHATSAPP
    fun shareViaWhatsApp(context: Context, items: List<InventoryItemEntity>) {
        val message = StringBuilder()
        message.appendLine("🛒 *LISTA AUTOMÁTICA DE COMPRAS - RESTAURANTE RIVERA* 🛒")
        message.appendLine("📅 *Generado:* ${dateFormat.format(Date())}")
        message.appendLine("⚠️ *Insumos por debajo del stock mínimo:* ${items.size}")
        message.appendLine("")

        var totalEstimatedCost = 0.0
        items.forEach { item ->
            val suggestedQty = maxOf(0.0, item.idealStock - item.currentStock)
            val cost = suggestedQty * item.unitCost
            totalEstimatedCost += cost

            message.appendLine("• *${item.productName}*")
            message.appendLine("  ├ Stock Actual: ${item.currentStock} ${item.unit}")
            message.appendLine("  ├ Stock Mínimo: ${item.minStock} ${item.unit}")
            message.appendLine("  ├ 🚨 *Comprar:* *${String.format(Locale.US, "%.2f", suggestedQty)} ${item.unit}*")
            message.appendLine("  └ 🏢 Proveedor: ${item.supplier}")
            message.appendLine("")
        }

        if (totalEstimatedCost > 0) {
            message.appendLine("💰 *Costo Est. Total:* ${formatQuetzales(totalEstimatedCost)}")
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
            val genericIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message.toString())
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(genericIntent, "Compartir Lista de Compras"))
        }
    }

    // 2. EXPORT TO CSV / EXCEL
    fun exportToCsvAndShare(context: Context, items: List<InventoryItemEntity>) {
        try {
            val csvBuilder = StringBuilder()
            csvBuilder.appendLine("RESTAURANTE RIVERA - LISTA AUTOMATICA DE COMPRAS")
            csvBuilder.appendLine("Fecha Generacion,${dateFormat.format(Date())}")
            csvBuilder.appendLine("Total Insumos Reabastecer,${items.size}")
            csvBuilder.appendLine()
            csvBuilder.appendLine("Ingrediente,Stock Actual,Stock Minimo,Stock Ideal,Cantidad Sugerida,Unidad,Proveedor,Costo Unitario (Q),Costo Estimado (Q)")

            items.forEach { item ->
                val suggestedQty = maxOf(0.0, item.idealStock - item.currentStock)
                val estimatedCost = suggestedQty * item.unitCost
                csvBuilder.appendLine(
                    "\"${item.productName.replace("\"", "\"\"")}\",${item.currentStock},${item.minStock},${item.idealStock},${String.format(Locale.US, "%.2f", suggestedQty)},${item.unit},\"${item.supplier.replace("\"", "\"\"")}\",${item.unitCost},${String.format(Locale.US, "%.2f", estimatedCost)}"
                )
            }

            val fileName = "Lista_Compras_${fileDateFormat.format(Date())}.csv"
            val file = File(context.cacheDir, fileName)
            file.writeText(csvBuilder.toString(), Charsets.UTF_8)

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_SUBJECT, "Lista de Compras Restaurante Rivera")
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Exportar Lista de Compras (Excel/CSV)"))
        } catch (e: Exception) {
            Toast.makeText(context, "Error al exportar CSV: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // 3. PRINT OR EXPORT PDF
    fun printOrExportPdf(context: Context, items: List<InventoryItemEntity>) {
        val htmlContent = buildHtmlShoppingList(items)
        val webView = WebView(context)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                if (printManager != null) {
                    val printAdapter: PrintDocumentAdapter = webView.createPrintDocumentAdapter("Lista_Compras_Rivera")
                    val builder = PrintAttributes.Builder()
                    builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    printManager.print("Lista de Compras Rivera", printAdapter, builder.build())
                } else {
                    Toast.makeText(context, "Servicio de impresión no disponible", Toast.LENGTH_SHORT).show()
                }
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    private fun buildHtmlShoppingList(items: List<InventoryItemEntity>): String {
        val sb = StringBuilder()
        sb.append("<!DOCTYPE html><html><head><style>")
        sb.append("body { font-family: Arial, sans-serif; margin: 20px; color: #333; }")
        sb.append("h1 { color: #065F46; font-size: 22px; text-align: center; margin-bottom: 4px; }")
        sb.append("h2 { color: #4B5563; font-size: 14px; text-align: center; margin-top: 0; margin-bottom: 20px; }")
        sb.append("table { width: 100%; border-collapse: collapse; margin-top: 10px; margin-bottom: 25px; }")
        sb.append("th { background-color: #059669; color: white; padding: 8px; font-size: 12px; text-align: left; }")
        sb.append("td { border-bottom: 1px solid #E5E7EB; padding: 8px; font-size: 12px; }")
        sb.append(".badge-buy { background-color: #FEF2F2; color: #DC2626; font-weight: bold; padding: 3px 6px; border-radius: 4px; }")
        sb.append(".text-right { text-align: right; }")
        sb.append(".total-row { background-color: #ECFDF5; font-weight: bold; }")
        sb.append("</style></head><body>")

        sb.append("<h1>RESTAURANTE RIVERA</h1>")
        sb.append("<h2>LISTA AUTOMÁTICA DE COMPRAS - REABASTECIMIENTO DE INVENTARIO</h2>")
        sb.append("<p style='font-size:12px; color:#6B7280;'>Fecha de generación: ${dateFormat.format(Date())} | Total insumos: ${items.size}</p>")

        sb.append("<table><thead><tr><th>Ingrediente</th><th class='text-right'>Stock Actual</th><th class='text-right'>Stock Mín.</th><th class='text-right'>Stock Ideal</th><th class='text-right'>Cantidad a Comprar</th><th>Proveedor</th><th class='text-right'>Costo Est.</th></tr></thead><tbody>")

        var totalEst = 0.0
        items.forEach { item ->
            val suggestedQty = maxOf(0.0, item.idealStock - item.currentStock)
            val cost = suggestedQty * item.unitCost
            totalEst += cost

            sb.append("<tr>")
            sb.append("<td><b>${item.productName}</b></td>")
            sb.append("<td class='text-right'>${item.currentStock} ${item.unit}</td>")
            sb.append("<td class='text-right'>${item.minStock} ${item.unit}</td>")
            sb.append("<td class='text-right'>${item.idealStock} ${item.unit}</td>")
            sb.append("<td class='text-right'><span class='badge-buy'>${String.format(Locale.US, "%.2f", suggestedQty)} ${item.unit}</span></td>")
            sb.append("<td>${item.supplier}</td>")
            sb.append("<td class='text-right'>${if (cost > 0) formatQuetzales(cost) else "-"}</td>")
            sb.append("</tr>")
        }

        if (totalEst > 0) {
            sb.append("<tr class='total-row'>")
            sb.append("<td colspan='6' class='text-right'><b>COSTO TOTAL ESTIMADO DE REABASTECIMIENTO:</b></td>")
            sb.append("<td class='text-right'><b>${formatQuetzales(totalEst)}</b></td>")
            sb.append("</tr>")
        }

        sb.append("</tbody></table>")
        sb.append("<p style='text-align:center; font-size:11px; color:#9CA3AF; margin-top:30px;'>Sistema de Inventarios y Recetas - Restaurante Rivera</p>")
        sb.append("</body></html>")
        return sb.toString()
    }
}
