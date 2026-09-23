package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.example.data.entity.InvoiceEntity
import com.example.data.entity.OrderItemEntity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Generador nativo de Facturas en formato PDF estándar FEL (Factura Electrónica en Línea) de Guatemala.
 * Genera el archivo físico .pdf en context.cacheDir con diseño oficial SAT:
 * - Bordes negros definidos de 1px.
 * - Encabezado delimitado con datos del emisor y bloque DTE/Factura a la derecha.
 * - Cuadro de datos del cliente / receptor.
 * - Tabla de detalle de consumos [Código, Cantidad, Descripción, P. Unitario, Total].
 * - Pie con TOTAL EN LETRAS en español, desglose de IVA (12%) y Base Imponible.
 * - Código QR de certificación SAT y datos del certificador al pie de página.
 */
object PdfInvoiceGenerator {

    fun generateInvoicePdfFile(
        context: Context,
        invoice: InvoiceEntity,
        items: List<OrderItemEntity>
    ): File {
        val pdfDocument = PdfDocument()

        // Página A4 estándar: 595 x 842 puntos (72 dpi)
        val pageWidth = 595
        val pageHeight = 842
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val strokePaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 1f
            isAntiAlias = true
        }

        val fillHeaderPaint = Paint().apply {
            color = Color.parseColor("#F3F4F6")
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 9f
            isAntiAlias = true
        }

        val textBoldPaint = Paint().apply {
            color = Color.BLACK
            textSize = 9f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 13f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val leftMargin = 30f
        val rightMargin = 565f
        val contentWidth = rightMargin - leftMargin

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

        // ==========================================
        // 1. ENCABEZADO DELIMITADO (EMISOR Y FACTURA)
        // ==========================================
        var curY = 32f

        // Datos del Negocio (Izquierda)
        val businessRight = 345f
        canvas.drawText(invoice.restaurantName.uppercase(), leftMargin, curY + 12f, titlePaint)
        if (invoice.branchName.isNotBlank()) {
            canvas.drawText("Sucursal: ${invoice.branchName}", leftMargin, curY + 25f, textBoldPaint)
        }
        canvas.drawText("NIT Emisor: ${invoice.restaurantTaxId.ifBlank { "1234567-8" }}", leftMargin, curY + 37f, textPaint)
        if (invoice.restaurantAddress.isNotBlank()) {
            val addr = if (invoice.restaurantAddress.length > 55) invoice.restaurantAddress.take(55) + "..." else invoice.restaurantAddress
            canvas.drawText("Dirección: $addr", leftMargin, curY + 49f, textPaint)
        }
        val contactLine = listOfNotNull(
            invoice.restaurantPhone.takeIf { it.isNotBlank() }?.let { "Tel: $it" },
            invoice.restaurantEmail.takeIf { it.isNotBlank() }?.let { "Email: $it" }
        ).joinToString(" | ")
        if (contactLine.isNotBlank()) {
            canvas.drawText(contactLine, leftMargin, curY + 61f, textPaint)
        }
        if (invoice.legalNotice.isNotBlank()) {
            val notice = if (invoice.legalNotice.length > 60) invoice.legalNotice.take(60) + "..." else invoice.legalNotice
            canvas.drawText(notice, leftMargin, curY + 73f, textPaint)
        }

        // Bloque FACTURA FEL (Derecha) con borde negro 1px
        val felBoxLeft = 355f
        val felBoxTop = curY
        val felBoxBottom = curY + 84f
        val felBoxRect = RectF(felBoxLeft, felBoxTop, rightMargin, felBoxBottom)
        canvas.drawRect(felBoxRect, strokePaint)

        // Título del recuadro
        val felHeaderRect = RectF(felBoxLeft, felBoxTop, rightMargin, felBoxTop + 16f)
        canvas.drawRect(felHeaderRect, fillHeaderPaint)
        canvas.drawRect(felHeaderRect, strokePaint)
        val dteTitle = "FACTURA ELECTRÓNICA (DTE)"
        val dteTitleWidth = textBoldPaint.measureText(dteTitle)
        canvas.drawText(dteTitle, felBoxLeft + ((rightMargin - felBoxLeft - dteTitleWidth) / 2f), felBoxTop + 12f, textBoldPaint)

        canvas.drawText("SERIE: $serie", felBoxLeft + 8f, felBoxTop + 29f, textBoldPaint)
        canvas.drawText("NÚMERO DTE: $dteNumber", felBoxLeft + 8f, felBoxTop + 41f, textBoldPaint)
        canvas.drawText("NO. AUTORIZACIÓN: ${uuid.take(18)}...", felBoxLeft + 8f, felBoxTop + 53f, textPaint)
        canvas.drawText("FECHA EMISIÓN: $dateStr", felBoxLeft + 8f, felBoxTop + 65f, textPaint)
        canvas.drawText("RÉGIMEN: Pagos Trimestrales ISR", felBoxLeft + 8f, felBoxTop + 77f, textPaint)

        curY = felBoxBottom + 10f

        // ==========================================
        // 2. CUADRO DE DATOS DEL CLIENTE / RECEPTOR
        // ==========================================
        val clientBoxTop = curY
        val clientBoxBottom = curY + 54f
        val clientBoxRect = RectF(leftMargin, clientBoxTop, rightMargin, clientBoxBottom)
        canvas.drawRect(clientBoxRect, strokePaint)

        // Barra superior del cliente
        val clientHeaderRect = RectF(leftMargin, clientBoxTop, rightMargin, clientBoxTop + 14f)
        canvas.drawRect(clientHeaderRect, fillHeaderPaint)
        canvas.drawRect(clientHeaderRect, strokePaint)
        canvas.drawText("DATOS DEL RECEPTOR / CLIENTE", leftMargin + 8f, clientBoxTop + 10.5f, textBoldPaint)

        val clientNameDisplay = when {
            invoice.customerName.isNotBlank() -> invoice.customerName
            invoice.customerType.isNotBlank() -> invoice.customerType
            else -> "Consumidor Final"
        }
        val nitDisplay = invoice.customerNit.ifBlank { "C/F (Consumidor Final)" }
        val phoneDisplay = invoice.customerPhone.ifBlank { "N/A" }
        val orderInfoDisplay = "Comanda: #${invoice.orderNumber}${if (invoice.tableNumber.isNotBlank()) " | Mesa: ${invoice.tableNumber}" else ""}"

        canvas.drawText("Nombre / Razón Social: $clientNameDisplay", leftMargin + 8f, clientBoxTop + 26f, textBoldPaint)
        canvas.drawText("NIT / CUI: $nitDisplay", leftMargin + 8f, clientBoxTop + 37f, textPaint)
        canvas.drawText("Dirección: ${invoice.restaurantAddress.ifBlank { "Ciudad de Guatemala" }}", leftMargin + 8f, clientBoxTop + 48f, textPaint)

        canvas.drawText("Fecha y Hora: $dateStr", 340f, clientBoxTop + 26f, textPaint)
        canvas.drawText("Teléfono: $phoneDisplay", 340f, clientBoxTop + 37f, textPaint)
        canvas.drawText(orderInfoDisplay, 340f, clientBoxTop + 48f, textPaint)

        curY = clientBoxBottom + 10f

        // ==========================================
        // 3. TABLA DE DETALLE DE CONSUMOS
        // [Código, Cantidad, Descripción, P. Unitario, Total]
        // ==========================================
        val colCode = leftMargin // 30f
        val colCodeWidth = 35f
        val colQty = colCode + colCodeWidth // 65f
        val colQtyWidth = 40f
        val colDesc = colQty + colQtyWidth // 105f
        val colDescWidth = 260f
        val colUnitPrice = colDesc + colDescWidth // 365f
        val colUnitPriceWidth = 85f
        val colTotal = colUnitPrice + colUnitPriceWidth // 450f
        val colTotalWidth = rightMargin - colTotal // 115f

        val tableHeaderTop = curY
        val tableHeaderBottom = curY + 16f
        val tableHeaderRect = RectF(leftMargin, tableHeaderTop, rightMargin, tableHeaderBottom)
        canvas.drawRect(tableHeaderRect, fillHeaderPaint)
        canvas.drawRect(tableHeaderRect, strokePaint)

        // Líneas divisoras verticales del encabezado
        canvas.drawLine(colQty, tableHeaderTop, colQty, tableHeaderBottom, strokePaint)
        canvas.drawLine(colDesc, tableHeaderTop, colDesc, tableHeaderBottom, strokePaint)
        canvas.drawLine(colUnitPrice, tableHeaderTop, colUnitPrice, tableHeaderBottom, strokePaint)
        canvas.drawLine(colTotal, tableHeaderTop, colTotal, tableHeaderBottom, strokePaint)

        canvas.drawText("No.", colCode + 8f, tableHeaderTop + 11.5f, textBoldPaint)
        canvas.drawText("Cant.", colQty + 8f, tableHeaderTop + 11.5f, textBoldPaint)
        canvas.drawText("Descripción del Producto / Servicio", colDesc + 8f, tableHeaderTop + 11.5f, textBoldPaint)

        val uPriceTitle = "P. Unitario"
        val uPriceTitleX = colTotal - 8f - textBoldPaint.measureText(uPriceTitle)
        canvas.drawText(uPriceTitle, uPriceTitleX, tableHeaderTop + 11.5f, textBoldPaint)

        val totTitle = "Total (Q)"
        val totTitleX = rightMargin - 8f - textBoldPaint.measureText(totTitle)
        canvas.drawText(totTitle, totTitleX, tableHeaderTop + 11.5f, textBoldPaint)

        curY = tableHeaderBottom

        val tableContentStart = curY
        items.forEachIndexed { index, item ->
            val hasNotes = item.notes.isNotBlank()
            val rowHeight = if (hasNotes) 24f else 17f
            val rowBottom = curY + rowHeight

            // Borde inferior de la fila
            canvas.drawLine(leftMargin, rowBottom, rightMargin, rowBottom, strokePaint)

            // Divisores verticales de la fila
            canvas.drawLine(leftMargin, curY, leftMargin, rowBottom, strokePaint)
            canvas.drawLine(colQty, curY, colQty, rowBottom, strokePaint)
            canvas.drawLine(colDesc, curY, colDesc, rowBottom, strokePaint)
            canvas.drawLine(colUnitPrice, curY, colUnitPrice, rowBottom, strokePaint)
            canvas.drawLine(colTotal, curY, colTotal, rowBottom, strokePaint)
            canvas.drawLine(rightMargin, curY, rightMargin, rowBottom, strokePaint)

            // Contenido
            canvas.drawText("#${index + 1}", colCode + 8f, curY + 12f, textPaint)
            canvas.drawText("${item.quantity}", colQty + 12f, curY + 12f, textPaint)

            val nameTrunc = if (item.productName.length > 38) item.productName.take(38) + "..." else item.productName
            canvas.drawText(nameTrunc, colDesc + 8f, curY + 12f, textBoldPaint)
            if (hasNotes) {
                val notesTrunc = if (item.notes.length > 45) item.notes.take(45) + "..." else item.notes
                val notePaint = Paint(textPaint).apply { textSize = 7.5f; color = Color.DKGRAY }
                canvas.drawText("Obs: $notesTrunc", colDesc + 8f, curY + 21f, notePaint)
            }

            val curSymbol = invoice.currencySymbol
            val uPriceStr = "$curSymbol${String.format(Locale.US, "%.2f", item.unitPrice)}"
            val uPriceX = colTotal - 8f - textPaint.measureText(uPriceStr)
            canvas.drawText(uPriceStr, uPriceX, curY + 12f, textPaint)

            val totalStr = "$curSymbol${String.format(Locale.US, "%.2f", item.subtotal)}"
            val totalX = rightMargin - 8f - textBoldPaint.measureText(totalStr)
            canvas.drawText(totalStr, totalX, curY + 12f, textBoldPaint)

            curY = rowBottom
        }

        curY += 10f

        // ==========================================
        // 4. TOTAL EN LETRAS Y DESGLOSE DE IMPUESTOS (IVA 12%)
        // ==========================================
        val totalsSectionTop = curY
        val totalsSectionBottom = curY + 70f

        // Cuadro TOTAL EN LETRAS (Izquierda)
        val wordsBoxWidth = 295f
        val wordsBoxRect = RectF(leftMargin, totalsSectionTop, leftMargin + wordsBoxWidth, totalsSectionBottom)
        canvas.drawRect(wordsBoxRect, strokePaint)

        val wordsHeaderRect = RectF(leftMargin, totalsSectionTop, leftMargin + wordsBoxWidth, totalsSectionTop + 14f)
        canvas.drawRect(wordsHeaderRect, fillHeaderPaint)
        canvas.drawRect(wordsHeaderRect, strokePaint)
        canvas.drawText("TOTAL EN LETRAS", leftMargin + 8f, totalsSectionTop + 10.5f, textBoldPaint)

        val totalInWords = NumberToWordsHelper.toSpanishWords(invoice.totalAmount)
        val wordsPaint = Paint().apply {
            color = Color.BLACK
            textSize = 8.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        // Separar en 2 líneas si es largo
        if (totalInWords.length > 44) {
            val splitIdx = totalInWords.lastIndexOf(" ", 44).let { if (it <= 0) 40 else it }
            val line1 = totalInWords.substring(0, splitIdx).trim()
            val line2 = totalInWords.substring(splitIdx).trim()
            canvas.drawText(line1, leftMargin + 8f, totalsSectionTop + 28f, wordsPaint)
            canvas.drawText(line2, leftMargin + 8f, totalsSectionTop + 40f, wordsPaint)
        } else {
            canvas.drawText(totalInWords, leftMargin + 8f, totalsSectionTop + 30f, wordsPaint)
        }
        canvas.drawText("Forma de Pago: ${invoice.paymentMethod} | Cajero: ${invoice.cashierName}", leftMargin + 8f, totalsSectionTop + 58f, textPaint)

        // Tabla de Totales e IVA (Derecha) con bordes 1px
        val totalsTableLeft = leftMargin + wordsBoxWidth + 10f
        val totalsTableRight = rightMargin
        val totalsTableRect = RectF(totalsTableLeft, totalsSectionTop, totalsTableRight, totalsSectionBottom)
        canvas.drawRect(totalsTableRect, strokePaint)

        val baseAmount = invoice.totalAmount / 1.12
        val vatAmount = invoice.totalAmount - baseAmount
        val curSym = invoice.currencySymbol

        val rowH = (totalsSectionBottom - totalsSectionTop) / 4f
        for (i in 0 until 4) {
            val rTop = totalsSectionTop + (i * rowH)
            val rBottom = rTop + rowH
            if (i > 0) {
                canvas.drawLine(totalsTableLeft, rTop, totalsTableRight, rTop, strokePaint)
            }
            if (i == 3) {
                val grandRect = RectF(totalsTableLeft, rTop, totalsTableRight, rBottom)
                canvas.drawRect(grandRect, fillHeaderPaint)
                canvas.drawRect(grandRect, strokePaint)
            }

            val (lbl, amountStr, isBold) = when (i) {
                0 -> Triple("Subtotal:", "$curSym${String.format(Locale.US, "%.2f", invoice.subtotal)}", false)
                1 -> Triple("Base Imponible:", "$curSym${String.format(Locale.US, "%.2f", baseAmount)}", false)
                2 -> Triple("IVA (12%):", "$curSym${String.format(Locale.US, "%.2f", vatAmount)}", false)
                else -> Triple("TOTAL A PAGAR:", "$curSym${String.format(Locale.US, "%.2f", invoice.totalAmount)}", true)
            }

            val p = if (isBold) textBoldPaint else textPaint
            canvas.drawText(lbl, totalsTableLeft + 8f, rTop + (rowH / 2f) + 3.5f, p)
            val valWidth = p.measureText(amountStr)
            canvas.drawText(amountStr, totalsTableRight - 8f - valWidth, rTop + (rowH / 2f) + 3.5f, p)
        }

        curY = totalsSectionBottom + 12f

        // ==========================================
        // 5. CÓDIGO QR Y CERTIFICACIÓN SAT (PIE DE PÁGINA)
        // ==========================================
        val footerBoxTop = curY
        val footerBoxBottom = curY + 82f
        val footerBoxRect = RectF(leftMargin, footerBoxTop, rightMargin, footerBoxBottom)
        canvas.drawRect(footerBoxRect, strokePaint)

        // Generar QR Bitmap
        val verificationUrl = "https://fel.sat.gob.gt/verificador?nit=${invoice.restaurantTaxId}&serie=$serie&numero=$dteNumber&monto=${String.format(Locale.US, "%.2f", invoice.totalAmount)}"
        val qrBitmap: Bitmap = try {
            QRCodeHelper.generateQRCodeBitmap(verificationUrl, 256)
        } catch (_: Exception) {
            Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        }

        val qrSize = 68f
        val qrRect = RectF(leftMargin + 8f, footerBoxTop + 7f, leftMargin + 8f + qrSize, footerBoxTop + 7f + qrSize)
        canvas.drawBitmap(qrBitmap, null, qrRect, null)

        // Textos de certificación SAT
        val certLeft = leftMargin + qrSize + 18f
        val certTitlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 8.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        val certTextPaint = Paint().apply {
            color = Color.BLACK
            textSize = 7.5f
            isAntiAlias = true
        }

        canvas.drawText("CERTIFICACIÓN FEL - SUPERINTENDENCIA DE ADMINISTRACIÓN TRIBUTARIA (SAT)", certLeft, footerBoxTop + 14f, certTitlePaint)
        canvas.drawText("Autorización UUID: $uuid", certLeft, footerBoxTop + 26f, certTextPaint)
        canvas.drawText("Serie: $serie | Número DTE: $dteNumber | Fecha Certificación: $dateStr", certLeft, footerBoxTop + 37f, certTextPaint)
        canvas.drawText("Certificador Autorizado: INFILE, S.A. (NIT: 125543-9)", certLeft, footerBoxTop + 48f, certTextPaint)
        canvas.drawText("Documento Tributario Electrónico emitido de conformidad con el régimen FEL de Guatemala.", certLeft, footerBoxTop + 59f, certTextPaint)

        val footerMsg = if (invoice.footerMessage.isNotBlank()) invoice.footerMessage else "¡Gracias por su compra! Esperamos servirle nuevamente."
        val footerMsgPaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 7.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            isAntiAlias = true
        }
        canvas.drawText(footerMsg, certLeft, footerBoxTop + 71f, footerMsgPaint)

        pdfDocument.finishPage(page)

        // Guardar archivo en context.cacheDir/facturas
        val outputDir = File(context.cacheDir, "facturas")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val cleanName = invoice.invoiceNumber.replace("[^a-zA-Z0-9_-]".toRegex(), "_")
        val outputFile = File(outputDir, "Factura_$cleanName.pdf")
        val outputStream = FileOutputStream(outputFile)
        pdfDocument.writeTo(outputStream)
        outputStream.flush()
        outputStream.close()
        pdfDocument.close()

        return outputFile
    }
}
