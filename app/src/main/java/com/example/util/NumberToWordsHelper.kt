package com.example.util

import java.util.Locale

/**
 * Helper para convertir montos numéricos a texto en español para Facturas FEL de Guatemala.
 * Ejemplo: 125.50 -> "CIENTO VEINTICINCO QUETZALES CON 50/100"
 */
object NumberToWordsHelper {

    private val UNIDADES = arrayOf(
        "", "UN", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE"
    )

    private val ESPECIALES = arrayOf(
        "DIEZ", "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE",
        "DIECISEIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE"
    )

    private val VEINTES = arrayOf(
        "VEINTE", "VEINTIUN", "VEINTIDOS", "VEINTITRES", "VEINTICUATRO",
        "VEINTICINCO", "VEINTISEIS", "VEINTISIETE", "VEINTIOCHO", "VEINTINUEVE"
    )

    private val DECENAS = arrayOf(
        "", "", "", "TREINTA", "CUARENTA", "CINCUENTA",
        "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"
    )

    private val CENTENAS = arrayOf(
        "", "CIENTO", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS",
        "QUINIENTOS", "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"
    )

    fun toSpanishWords(
        amount: Double,
        currencySingular: String = "QUETZAL",
        currencyPlural: String = "QUETZALES"
    ): String {
        val safeAmount = if (amount < 0) -amount else amount
        val integerPart = safeAmount.toLong()
        val cents = Math.round((safeAmount - integerPart) * 100.0).toInt().coerceIn(0, 99)
        val centsStr = String.format(Locale.US, "%02d/100", cents)

        val words = if (integerPart == 0L) {
            "CERO"
        } else {
            convertLongToWords(integerPart).trim()
        }

        val currency = if (integerPart == 1L) currencySingular else currencyPlural
        return "$words $currency CON $centsStr"
    }

    private fun convertLongToWords(n: Long): String {
        if (n == 0L) return ""

        if (n in 1..999) {
            return convertGroupOfThree(n.toInt())
        }

        if (n in 1000..999999) {
            val thousands = (n / 1000).toInt()
            val remainder = (n % 1000).toInt()
            val thousandsStr = if (thousands == 1) "UN MIL" else "${convertGroupOfThree(thousands)} MIL"
            val remainderStr = if (remainder > 0) " ${convertGroupOfThree(remainder)}" else ""
            return "$thousandsStr$remainderStr"
        }

        if (n in 1000000..999999999) {
            val millions = (n / 1000000).toInt()
            val remainder = n % 1000000
            val millionsStr = if (millions == 1) "UN MILLON" else "${convertGroupOfThree(millions)} MILLONES"
            val remainderStr = if (remainder > 0) " ${convertLongToWords(remainder)}" else ""
            return "$millionsStr$remainderStr"
        }

        return n.toString()
    }

    private fun convertGroupOfThree(num: Int): String {
        if (num == 0) return ""
        if (num == 100) return "CIEN"

        val sb = StringBuilder()
        val c = num / 100
        val d = (num % 100) / 10
        val u = num % 10
        val du = num % 100

        if (c > 0) {
            sb.append(CENTENAS[c]).append(" ")
        }

        when {
            du in 1..9 -> {
                sb.append(UNIDADES[du])
            }
            du in 10..19 -> {
                sb.append(ESPECIALES[du - 10])
            }
            du in 20..29 -> {
                sb.append(VEINTES[du - 20])
            }
            du >= 30 -> {
                sb.append(DECENAS[d])
                if (u > 0) {
                    sb.append(" Y ").append(UNIDADES[u])
                }
            }
        }

        return sb.toString().trim()
    }
}
