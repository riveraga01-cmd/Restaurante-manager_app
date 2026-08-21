package com.example.util

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
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

    // --- TRANSMISSION LOGIC ---

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
