package com.example

import com.example.util.QRCodeHelper
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testExtractPairingCode_fromPlainText() {
    val input = "RIVERA-4819-B-481920"
    val result = QRCodeHelper.extractPairingCode(input)
    assertEquals("RIVERA-4819-B-481920", result)
  }

  @Test
  fun testExtractPairingCode_fromJson() {
    val json = """{"type":"DEVICE_PAIRING","code":"RIVERA-4819-B-481920","pin":"481920","role":"MESERO"}"""
    val result = QRCodeHelper.extractPairingCode(json)
    assertEquals("RIVERA-4819-B-481920", result)
  }

  @Test
  fun testExtractPin_fromRawPin() {
    val pin = "481920"
    assertEquals("481920", QRCodeHelper.extractPin(pin))
  }

  @Test
  fun testExtractPin_fromFullCode() {
    val code = "RIVERA-4819-B-481920"
    assertEquals("481920", QRCodeHelper.extractPin(code))
  }

  @Test
  fun testExtractPin_fromJson() {
    val json = """{"type":"DEVICE_PAIRING","code":"RIVERA-4819-B-481920","pin":"481920"}"""
    assertEquals("481920", QRCodeHelper.extractPin(json))
  }

  @Test
  fun testNumberToWords_amounts() {
    assertEquals("CERO QUETZALES CON 00/100", com.example.util.NumberToWordsHelper.toSpanishWords(0.0))
    assertEquals("UN QUETZAL CON 00/100", com.example.util.NumberToWordsHelper.toSpanishWords(1.0))
    assertEquals("VEINTIUN QUETZALES CON 50/100", com.example.util.NumberToWordsHelper.toSpanishWords(21.50))
    assertEquals("CIENTO VEINTICINCO QUETZALES CON 75/100", com.example.util.NumberToWordsHelper.toSpanishWords(125.75))
    assertEquals("UN MIL QUINIENTOS QUETZALES CON 00/100", com.example.util.NumberToWordsHelper.toSpanishWords(1500.00))
  }
}

