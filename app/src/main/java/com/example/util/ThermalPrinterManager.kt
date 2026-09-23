package com.example.util

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import com.example.data.entity.InvoiceEntity
import com.example.data.entity.OrderEntity
import com.example.data.entity.OrderItemEntity
import com.example.ui.components.formatQuetzales
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*

enum class PrinterConnectionType {
    BLUETOOTH, NETWORK_IP
}

data class ThermalPrinterConfig(
    val connectionType: PrinterConnectionType = PrinterConnectionType.NETWORK_IP,
    val bluetoothAddress: String = "",
    val bluetoothName: String = "",
    val ipAddress: String = "192.168.1.200",
    val port: Int = 9100,
    val paperWidthMm: Int = 80 // 58 or 80
)

class ThermalPrinterManager(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    @SuppressLint("MissingPermission")
    fun getPairedBluetoothDevices(): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        try {
            bluetoothAdapter?.bondedDevices?.forEach { device ->
                list.add(Pair(device.name ?: "Impresora BT", device.address))
            }
        } catch (e: Exception) {
            Log.e("ThermalPrinter", "Error reading paired bluetooth devices: ${e.message}")
        }
        return list
    }

    // --- ESC/POS COMMAND CONSTANTS ---
    companion object {
        val ESC_INIT = byteArrayOf(0x1B, 0x40) // Initialize printer
        val ALIGN_LEFT = byteArrayOf(0x1B, 0x61, 0x00)
        val ALIGN_CENTER = byteArrayOf(0x1B, 0x61, 0x01)
        val ALIGN_RIGHT = byteArrayOf(0x1B, 0x61, 0x02)
        val BOLD_ON = byteArrayOf(0x1B, 0x45, 0x01)
        val BOLD_OFF = byteArrayOf(0x1B, 0x45, 0x00)
        val SIZE_NORMAL = byteArrayOf(0x1D, 0x21, 0x00)
        val SIZE_LARGE = byteArrayOf(0x1D, 0x21, 0x11) // Double height & width
        val SIZE_MEDIUM = byteArrayOf(0x1D, 0x21, 0x01) // Double height
        val FEED_PAPER = byteArrayOf(0x1B, 0x64, 0x03) // Feed 3 lines
        val PAPER_CUT = byteArrayOf(0x1D, 0x56, 0x42, 0x00) // Full cut
        val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    // --- TICKET GENERATORS ---

    fun generateKitchenTicketText(
        order: OrderEntity,
        items: List<OrderItemEntity>,
        stationName: String = "COCINA"
    ): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateStr = dateFormat.format(Date(order.createdAt))
        val width = 32 // 58mm default width line char count

        val sb = StringBuilder()
        sb.appendLine("================================")
        sb.appendLine("       COMANDA DE $stationName     ")
        sb.appendLine("================================")
        sb.appendLine("Pedido #${order.orderNumber}")
        sb.appendLine("Mesa: ${order.tableNumber}")
        sb.appendLine("Mesero: ${order.waiterName}")
        sb.appendLine("Fecha: $dateStr")
        sb.appendLine("--------------------------------")
        sb.appendLine("CANT  DESCRIPCION")
        sb.appendLine("--------------------------------")
        for (item in items) {
            sb.appendLine("${item.quantity.toString().padStart(3)}x  ${item.productName}")
            if (item.notes.isNotBlank()) {
                sb.appendLine("      * NOTA: ${item.notes}")
            }
        }
        if (!order.generalNotes.isNullOrBlank()) {
            sb.appendLine("--------------------------------")
            sb.appendLine("NOTAS GENERALES: ${order.generalNotes}")
        }
        sb.appendLine("================================")
        sb.appendLine("\n\n")
        return sb.toString()
    }

    fun generateCashierTicketText(
        order: OrderEntity,
        items: List<OrderItemEntity>,
        cashierName: String,
        amountReceived: Double = 0.0,
        change: Double = 0.0
    ): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateStr = dateFormat.format(Date(order.paidAt ?: System.currentTimeMillis()))

        val sb = StringBuilder()
        sb.appendLine("================================")
        sb.appendLine("   MANAGERPRO BY RIVERA POS     ")
        sb.appendLine("    Restaurante & Cafeteria    ")
        sb.appendLine("================================")
        sb.appendLine("Ticket de Venta #${order.orderNumber}")
        sb.appendLine("Mesa: ${order.tableNumber}")
        sb.appendLine("Mesero: ${order.waiterName}")
        sb.appendLine("Cajero: $cashierName")
        sb.appendLine("Fecha: $dateStr")
        sb.appendLine("--------------------------------")
        sb.appendLine("CANT PRODUCTO         SUBTOTAL")
        sb.appendLine("--------------------------------")
        for (item in items) {
            val nameTrunc = if (item.productName.length > 15) item.productName.take(15) else item.productName.padEnd(15)
            val qtyStr = "${item.quantity}x".padEnd(4)
            val priceStr = formatQuetzales(item.subtotal).padStart(10)
            sb.appendLine("$qtyStr$nameTrunc$priceStr")
        }
        sb.appendLine("--------------------------------")
        sb.appendLine("TOTAL:          ${formatQuetzales(order.totalAmount).padStart(16)}")
        sb.appendLine("Forma de Pago:  ${order.paymentMethod ?: "Efectivo"}")
        if (amountReceived > 0) {
            sb.appendLine("Efectivo:       ${formatQuetzales(amountReceived).padStart(16)}")
            sb.appendLine("Cambio:         ${formatQuetzales(change).padStart(16)}")
        }
        sb.appendLine("================================")
        sb.appendLine("   ¡Gracias por su preferencia! ")
        sb.appendLine("================================")
        sb.appendLine("\n\n")
        return sb.toString()
    }

    // --- ESC/POS BYTE BUILDERS ---

    fun buildEscPosBytesForKitchen(
        order: OrderEntity,
        items: List<OrderItemEntity>,
        stationName: String = "COCINA"
    ): ByteArray {
        val baos = java.io.ByteArrayOutputStream()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        baos.write(ESC_INIT)
        baos.write(ALIGN_CENTER)
        baos.write(BOLD_ON)
        baos.write(SIZE_LARGE)
        baos.write("COMANDA $stationName\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(SIZE_MEDIUM)
        baos.write("ORDEN #${order.orderNumber}\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(BOLD_OFF)
        baos.write(SIZE_NORMAL)
        baos.write("--------------------------------\n".toByteArray(Charsets.ISO_8859_1))

        baos.write(ALIGN_LEFT)
        baos.write("Mesa: ${order.tableNumber}\n".toByteArray(Charsets.ISO_8859_1))
        baos.write("Mesero: ${order.waiterName}\n".toByteArray(Charsets.ISO_8859_1))
        baos.write("Hora: ${dateFormat.format(Date(order.createdAt))}\n".toByteArray(Charsets.ISO_8859_1))
        baos.write("--------------------------------\n".toByteArray(Charsets.ISO_8859_1))

        baos.write(BOLD_ON)
        baos.write("CANT  DESCRIPCION\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(BOLD_OFF)

        for (item in items) {
            baos.write(BOLD_ON)
            baos.write("${item.quantity}x  ${item.productName}\n".toByteArray(Charsets.ISO_8859_1))
            baos.write(BOLD_OFF)
            if (item.notes.isNotBlank()) {
                baos.write("   >> NOTA: ${item.notes}\n".toByteArray(Charsets.ISO_8859_1))
            }
        }

        if (!order.generalNotes.isNullOrBlank()) {
            baos.write("--------------------------------\n".toByteArray(Charsets.ISO_8859_1))
            baos.write(BOLD_ON)
            baos.write("OBSERVACIONES: ${order.generalNotes}\n".toByteArray(Charsets.ISO_8859_1))
            baos.write(BOLD_OFF)
        }

        baos.write(ALIGN_CENTER)
        baos.write("==============================\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(FEED_PAPER)
        baos.write(PAPER_CUT)

        return baos.toByteArray()
    }

    fun buildEscPosBytesForReceipt(
        order: OrderEntity,
        items: List<OrderItemEntity>,
        cashierName: String,
        amountReceived: Double = 0.0,
        change: Double = 0.0
    ): ByteArray {
        val baos = java.io.ByteArrayOutputStream()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        baos.write(ESC_INIT)
        baos.write(ALIGN_CENTER)
        baos.write(BOLD_ON)
        baos.write(SIZE_LARGE)
        baos.write("MANAGERPRO BY RIVERA\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(SIZE_NORMAL)
        baos.write("Restaurante & Sistema POS\n".toByteArray(Charsets.ISO_8859_1))
        baos.write("TICKET DE VENTA #${order.orderNumber}\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(BOLD_OFF)
        baos.write("--------------------------------\n".toByteArray(Charsets.ISO_8859_1))

        baos.write(ALIGN_LEFT)
        baos.write("Fecha: ${dateFormat.format(Date(order.paidAt ?: System.currentTimeMillis()))}\n".toByteArray(Charsets.ISO_8859_1))
        baos.write("Mesa: ${order.tableNumber} | Mesero: ${order.waiterName}\n".toByteArray(Charsets.ISO_8859_1))
        baos.write("Cajero: $cashierName\n".toByteArray(Charsets.ISO_8859_1))
        baos.write("--------------------------------\n".toByteArray(Charsets.ISO_8859_1))

        baos.write(BOLD_ON)
        baos.write("CANT PRODUCTO         SUBTOTAL\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(BOLD_OFF)

        for (item in items) {
            val nameTrunc = if (item.productName.length > 15) item.productName.take(15) else item.productName.padEnd(15)
            val qtyStr = "${item.quantity}x".padEnd(4)
            val priceStr = formatQuetzales(item.subtotal).padStart(10)
            baos.write("$qtyStr$nameTrunc$priceStr\n".toByteArray(Charsets.ISO_8859_1))
        }

        baos.write("--------------------------------\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(ALIGN_RIGHT)
        baos.write(BOLD_ON)
        baos.write(SIZE_MEDIUM)
        baos.write("TOTAL: ${formatQuetzales(order.totalAmount)}\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(SIZE_NORMAL)
        baos.write("Pago con: ${order.paymentMethod ?: "Efectivo"}\n".toByteArray(Charsets.ISO_8859_1))
        if (amountReceived > 0) {
            baos.write("Recibido: ${formatQuetzales(amountReceived)}\n".toByteArray(Charsets.ISO_8859_1))
            baos.write("Cambio:   ${formatQuetzales(change)}\n".toByteArray(Charsets.ISO_8859_1))
        }
        baos.write(BOLD_OFF)

        baos.write(ALIGN_CENTER)
        baos.write("--------------------------------\n".toByteArray(Charsets.ISO_8859_1))
        baos.write("¡Gracias por su preferencia!\n".toByteArray(Charsets.ISO_8859_1))
        baos.write("ManagerPro by Rivera POS System\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(FEED_PAPER)
        baos.write(PAPER_CUT)

        return baos.toByteArray()
    }

    fun generateInvoiceTicketText(
        invoice: InvoiceEntity,
        items: List<OrderItemEntity>,
        paperWidthMm: Int = 80
    ): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateStr = dateFormat.format(Date(invoice.timestamp))
        val is58mm = paperWidthMm <= 58
        val width = if (is58mm) 32 else 48
        val divider = "-".repeat(width)
        val doubleDivider = "=".repeat(width)

        val serie = if (invoice.invoiceNumber.contains("-")) {
            invoice.invoiceNumber.substringBeforeLast("-").ifBlank { "FEL-A" }
        } else {
            "FEL-A"
        }
        val dteNum = invoice.invoiceNumber.substringAfterLast("-", invoice.invoiceNumber)
        val cur = invoice.currencySymbol

        val sb = StringBuilder()
        sb.appendLine(doubleDivider)
        sb.appendLine(centerText(invoice.restaurantName.uppercase(), width))
        if (invoice.branchName.isNotBlank()) {
            sb.appendLine(centerText("Sucursal: ${invoice.branchName}", width))
        }
        sb.appendLine(centerText("NIT Emisor: ${invoice.restaurantTaxId.ifBlank { "1234567-8" }}", width))
        if (invoice.restaurantAddress.isNotBlank()) {
            val addr = if (invoice.restaurantAddress.length > width) invoice.restaurantAddress.take(width - 3) + "..." else invoice.restaurantAddress
            sb.appendLine(centerText(addr, width))
        }
        if (invoice.restaurantPhone.isNotBlank()) {
            sb.appendLine(centerText("Tel: ${invoice.restaurantPhone}", width))
        }
        sb.appendLine(divider)
        sb.appendLine(centerText("DOCUMENTO TRIBUTARIO ELECTRÓNICO", width))
        sb.appendLine(centerText("FACTURA (FEL)", width))
        sb.appendLine("SERIE: $serie | NÚMERO: $dteNum")
        sb.appendLine("FECHA EMISIÓN: $dateStr")
        sb.appendLine("RÉGIMEN: Pagos Trimestrales ISR")
        sb.appendLine(divider)

        val clientName = when {
            invoice.customerName.isNotBlank() -> invoice.customerName
            invoice.customerType.isNotBlank() -> invoice.customerType
            else -> "Consumidor Final"
        }
        sb.appendLine("CLIENTE: $clientName")
        sb.appendLine("NIT: ${invoice.customerNit.ifBlank { "C/F" }}")
        if (invoice.orderNumber.isNotBlank()) {
            sb.appendLine("COMANDA: #${invoice.orderNumber}${if (invoice.tableNumber.isNotBlank()) " | MESA: ${invoice.tableNumber}" else ""}")
        }
        sb.appendLine("CAJERO: ${invoice.cashierName} | PAGO: ${invoice.paymentMethod}")
        sb.appendLine(divider)

        if (is58mm) {
            sb.appendLine("CANT DESCRIPCION        TOTAL")
            sb.appendLine(divider)
            for (item in items) {
                val qtyStr = "${item.quantity}x".padEnd(4)
                val priceStr = "$cur${String.format(Locale.US, "%.2f", item.subtotal)}".padStart(8)
                val descWidth = width - qtyStr.length - priceStr.length
                val descStr = if (item.productName.length > descWidth) item.productName.take(descWidth) else item.productName.padEnd(descWidth)
                sb.appendLine("$qtyStr$descStr$priceStr")
                if (item.notes.isNotBlank()) {
                    sb.appendLine("     * ${item.notes.take(width - 7)}")
                }
            }
        } else {
            sb.appendLine("CANT COD  DESCRIPCION            P.UNIT    TOTAL")
            sb.appendLine(divider)
            items.forEachIndexed { idx, item ->
                val qtyStr = "${item.quantity}x".padEnd(4)
                val codStr = "#${idx + 1}".padEnd(5)
                val uPriceStr = "$cur${String.format(Locale.US, "%.2f", item.unitPrice)}".padStart(8)
                val totStr = "$cur${String.format(Locale.US, "%.2f", item.subtotal)}".padStart(9)
                val descW = width - (qtyStr.length + codStr.length + uPriceStr.length + totStr.length)
                val descStr = if (item.productName.length > descW) item.productName.take(descW) else item.productName.padEnd(descW)
                sb.appendLine("$qtyStr$codStr$descStr$uPriceStr$totStr")
                if (item.notes.isNotBlank()) {
                    sb.appendLine("         * ${item.notes.take(width - 11)}")
                }
            }
        }

        sb.appendLine(divider)
        val baseAmount = invoice.totalAmount / 1.12
        val vatAmount = invoice.totalAmount - baseAmount

        sb.appendLine(formatLine("Subtotal:", "$cur${String.format(Locale.US, "%.2f", invoice.subtotal)}", width))
        if (invoice.discount > 0) {
            sb.appendLine(formatLine("Descuento:", "-$cur${String.format(Locale.US, "%.2f", invoice.discount)}", width))
        }
        sb.appendLine(formatLine("Base Imponible:", "$cur${String.format(Locale.US, "%.2f", baseAmount)}", width))
        sb.appendLine(formatLine("IVA (12%):", "$cur${String.format(Locale.US, "%.2f", vatAmount)}", width))
        sb.appendLine(doubleDivider)
        sb.appendLine(formatLine("TOTAL A PAGAR:", "$cur${String.format(Locale.US, "%.2f", invoice.totalAmount)}", width))
        sb.appendLine(doubleDivider)

        val totalWords = NumberToWordsHelper.toSpanishWords(invoice.totalAmount)
        sb.appendLine("TOTAL EN LETRAS:")
        sb.appendLine(totalWords)
        sb.appendLine(divider)

        sb.appendLine(centerText("CERTIFICACIÓN SAT", width))
        sb.appendLine(centerText("INFILE, S.A. NIT: 125543-9", width))
        sb.appendLine(centerText("Documento Tributario Electrónico", width))
        if (invoice.footerMessage.isNotBlank()) {
            sb.appendLine(divider)
            sb.appendLine(centerText(invoice.footerMessage, width))
        }
        sb.appendLine(doubleDivider)
        sb.appendLine("\n\n\n")
        return sb.toString()
    }

    fun buildEscPosBytesForInvoice(
        invoice: InvoiceEntity,
        items: List<OrderItemEntity>,
        paperWidthMm: Int = 80
    ): ByteArray {
        val baos = java.io.ByteArrayOutputStream()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateStr = dateFormat.format(Date(invoice.timestamp))
        val is58mm = paperWidthMm <= 58
        val width = if (is58mm) 32 else 48
        val divider = "-".repeat(width) + "\n"
        val doubleDivider = "=".repeat(width) + "\n"
        val cur = invoice.currencySymbol

        val serie = if (invoice.invoiceNumber.contains("-")) {
            invoice.invoiceNumber.substringBeforeLast("-").ifBlank { "FEL-A" }
        } else {
            "FEL-A"
        }
        val dteNum = invoice.invoiceNumber.substringAfterLast("-", invoice.invoiceNumber)

        baos.write(ESC_INIT)
        baos.write(ALIGN_CENTER)
        baos.write(BOLD_ON)
        baos.write(SIZE_LARGE)
        baos.write("${invoice.restaurantName}\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(BOLD_OFF)
        baos.write(SIZE_NORMAL)

        if (invoice.branchName.isNotBlank()) {
            baos.write("Sucursal: ${invoice.branchName}\n".toByteArray(Charsets.ISO_8859_1))
        }
        baos.write("NIT Emisor: ${invoice.restaurantTaxId.ifBlank { "1234567-8" }}\n".toByteArray(Charsets.ISO_8859_1))
        if (invoice.restaurantAddress.isNotBlank()) {
            val addr = if (invoice.restaurantAddress.length > width) invoice.restaurantAddress.take(width - 3) + "..." else invoice.restaurantAddress
            baos.write("$addr\n".toByteArray(Charsets.ISO_8859_1))
        }
        if (invoice.restaurantPhone.isNotBlank()) {
            baos.write("Tel: ${invoice.restaurantPhone}\n".toByteArray(Charsets.ISO_8859_1))
        }

        baos.write(divider.toByteArray(Charsets.ISO_8859_1))
        baos.write(BOLD_ON)
        baos.write("DOCUMENTO TRIBUTARIO ELECTRONICO\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(SIZE_MEDIUM)
        baos.write("FACTURA FEL\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(SIZE_NORMAL)
        baos.write("SERIE: $serie | NO: $dteNum\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(BOLD_OFF)
        baos.write("Fecha: $dateStr\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(divider.toByteArray(Charsets.ISO_8859_1))

        baos.write(ALIGN_LEFT)
        val clientName = when {
            invoice.customerName.isNotBlank() -> invoice.customerName
            invoice.customerType.isNotBlank() -> invoice.customerType
            else -> "Consumidor Final"
        }
        baos.write("Cliente: $clientName\n".toByteArray(Charsets.ISO_8859_1))
        baos.write("NIT: ${invoice.customerNit.ifBlank { "C/F" }}\n".toByteArray(Charsets.ISO_8859_1))
        if (invoice.orderNumber.isNotBlank()) {
            baos.write("Comanda: #${invoice.orderNumber}${if (invoice.tableNumber.isNotBlank()) " | Mesa: ${invoice.tableNumber}" else ""}\n".toByteArray(Charsets.ISO_8859_1))
        }
        baos.write("Cajero: ${invoice.cashierName} | Pago: ${invoice.paymentMethod}\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(divider.toByteArray(Charsets.ISO_8859_1))

        baos.write(BOLD_ON)
        if (is58mm) {
            baos.write("CANT DESCRIPCION        TOTAL\n".toByteArray(Charsets.ISO_8859_1))
        } else {
            baos.write("CANT COD  DESCRIPCION            P.UNIT    TOTAL\n".toByteArray(Charsets.ISO_8859_1))
        }
        baos.write(BOLD_OFF)
        baos.write(divider.toByteArray(Charsets.ISO_8859_1))

        if (is58mm) {
            for (item in items) {
                val qtyStr = "${item.quantity}x".padEnd(4)
                val priceStr = "$cur${String.format(Locale.US, "%.2f", item.subtotal)}".padStart(8)
                val descWidth = width - qtyStr.length - priceStr.length
                val descStr = if (item.productName.length > descWidth) item.productName.take(descWidth) else item.productName.padEnd(descWidth)
                baos.write("$qtyStr$descStr$priceStr\n".toByteArray(Charsets.ISO_8859_1))
                if (item.notes.isNotBlank()) {
                    baos.write("   * ${item.notes.take(width - 5)}\n".toByteArray(Charsets.ISO_8859_1))
                }
            }
        } else {
            items.forEachIndexed { idx, item ->
                val qtyStr = "${item.quantity}x".padEnd(4)
                val codStr = "#${idx + 1}".padEnd(5)
                val uPriceStr = "$cur${String.format(Locale.US, "%.2f", item.unitPrice)}".padStart(8)
                val totStr = "$cur${String.format(Locale.US, "%.2f", item.subtotal)}".padStart(9)
                val descW = width - (qtyStr.length + codStr.length + uPriceStr.length + totStr.length)
                val descStr = if (item.productName.length > descW) item.productName.take(descW) else item.productName.padEnd(descW)
                baos.write("$qtyStr$codStr$descStr$uPriceStr$totStr\n".toByteArray(Charsets.ISO_8859_1))
                if (item.notes.isNotBlank()) {
                    baos.write("     * ${item.notes.take(width - 7)}\n".toByteArray(Charsets.ISO_8859_1))
                }
            }
        }

        baos.write(divider.toByteArray(Charsets.ISO_8859_1))
        val baseAmount = invoice.totalAmount / 1.12
        val vatAmount = invoice.totalAmount - baseAmount

        baos.write((formatLine("Subtotal:", "$cur${String.format(Locale.US, "%.2f", invoice.subtotal)}", width) + "\n").toByteArray(Charsets.ISO_8859_1))
        if (invoice.discount > 0) {
            baos.write((formatLine("Descuento:", "-$cur${String.format(Locale.US, "%.2f", invoice.discount)}", width) + "\n").toByteArray(Charsets.ISO_8859_1))
        }
        baos.write((formatLine("Base Imponible:", "$cur${String.format(Locale.US, "%.2f", baseAmount)}", width) + "\n").toByteArray(Charsets.ISO_8859_1))
        baos.write((formatLine("IVA (12%):", "$cur${String.format(Locale.US, "%.2f", vatAmount)}", width) + "\n").toByteArray(Charsets.ISO_8859_1))
        baos.write(doubleDivider.toByteArray(Charsets.ISO_8859_1))

        baos.write(BOLD_ON)
        baos.write(SIZE_MEDIUM)
        baos.write((formatLine("TOTAL A PAGAR:", "$cur${String.format(Locale.US, "%.2f", invoice.totalAmount)}", width) + "\n").toByteArray(Charsets.ISO_8859_1))
        baos.write(SIZE_NORMAL)
        baos.write(BOLD_OFF)
        baos.write(doubleDivider.toByteArray(Charsets.ISO_8859_1))

        val totalWords = NumberToWordsHelper.toSpanishWords(invoice.totalAmount)
        baos.write("TOTAL EN LETRAS:\n".toByteArray(Charsets.ISO_8859_1))
        baos.write("$totalWords\n".toByteArray(Charsets.ISO_8859_1))
        baos.write(divider.toByteArray(Charsets.ISO_8859_1))

        baos.write(ALIGN_CENTER)
        baos.write("CERTIFICACION SAT - FEL\n".toByteArray(Charsets.ISO_8859_1))
        baos.write("INFILE S.A. NIT: 125543-9\n".toByteArray(Charsets.ISO_8859_1))
        baos.write("Documento Tributario Electronico\n".toByteArray(Charsets.ISO_8859_1))
        if (invoice.footerMessage.isNotBlank()) {
            baos.write("${invoice.footerMessage}\n".toByteArray(Charsets.ISO_8859_1))
        }
        baos.write(FEED_PAPER)
        baos.write(PAPER_CUT)

        return baos.toByteArray()
    }

    private fun centerText(text: String, width: Int): String {
        if (text.length >= width) return text.take(width)
        val leftPadding = (width - text.length) / 2
        return " ".repeat(leftPadding) + text
    }

    private fun formatLine(label: String, value: String, width: Int): String {
        val spaces = (width - label.length - value.length).coerceAtLeast(1)
        return label + " ".repeat(spaces) + value
    }

    // --- TRANSMISSION LOGIC ---

    @SuppressLint("MissingPermission")
    suspend fun printBytes(
        config: ThermalPrinterConfig,
        bytes: ByteArray
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            when (config.connectionType) {
                PrinterConnectionType.NETWORK_IP -> {
                    if (config.ipAddress.isBlank()) {
                        return@withContext Result.failure(IllegalArgumentException("Dirección IP de la impresora no configurada."))
                    }
                    val socket = Socket()
                    socket.connect(InetSocketAddress(config.ipAddress, config.port), 3000)
                    val outputStream: OutputStream = socket.getOutputStream()
                    outputStream.write(bytes)
                    outputStream.flush()
                    outputStream.close()
                    socket.close()
                    Result.success("Impresión enviada exitosamente a la IP ${config.ipAddress}:${config.port}")
                }
                PrinterConnectionType.BLUETOOTH -> {
                    if (config.bluetoothAddress.isBlank()) {
                        return@withContext Result.failure(IllegalArgumentException("Dispositivo Bluetooth no seleccionado."))
                    }
                    @SuppressLint("MissingPermission")
                    val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(config.bluetoothAddress)
                        ?: return@withContext Result.failure(IllegalStateException("No se encontró la impresora Bluetooth."))

                    @SuppressLint("MissingPermission")
                    val btSocket: BluetoothSocket = device!!.createRfcommSocketToServiceRecord(SPP_UUID)
                    bluetoothAdapter?.cancelDiscovery()
                    btSocket.connect()
                    val outputStream = btSocket.outputStream
                    outputStream.write(bytes)
                    outputStream.flush()
                    outputStream.close()
                    btSocket.close()
                    Result.success("Impresión enviada por Bluetooth a ${config.bluetoothName}")
                }
            }
        } catch (e: Exception) {
            Log.e("ThermalPrinter", "Error al enviar impresión: ${e.message}", e)
            Result.failure(e)
        }
    }
}
