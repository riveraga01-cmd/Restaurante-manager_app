package com.example.data.entity

data class SoldProductReportItem(
    val productName: String,
    val category: String,
    val unitPrice: Double,
    val quantitySold: Int,
    val totalRevenue: Double,
    val participationPercentage: Double
)

data class DetailedSalesReportData(
    val periodName: String = "Diario",
    val startDateMs: Long = 0L,
    val endDateMs: Long = 0L,
    val itemsByCategory: Map<String, List<SoldProductReportItem>> = emptyMap(),
    val totalProductsSold: Int = 0,
    val totalRevenue: Double = 0.0,
    val averageTicket: Double = 0.0,
    val orderCount: Int = 0,
    val mostSoldProduct: Pair<String, Int>? = null,
    val leastSoldProduct: Pair<String, Int>? = null,
    val peakSalesHour: String = "N/A",
    val topSellingWaiter: Pair<String, Double>? = null,
    val topConsumingTable: Pair<String, Double>? = null
)
