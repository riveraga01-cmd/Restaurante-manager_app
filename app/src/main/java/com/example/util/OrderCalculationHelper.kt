package com.example.util

import com.example.data.entity.MenuItemEntity
import com.example.data.entity.OrderEntity
import com.example.data.entity.OrderItemEntity
import java.util.Locale

/**
 * Result model containing the recalculated breakdown of an order.
 */
data class OrderCalculationResult(
    val subtotal: Double,
    val tipPercentage: Double = 10.0,
    val tipAmount: Double = 0.0,
    val taxPercentage: Double = 0.0,
    val taxAmount: Double = 0.0,
    val discountAmount: Double = 0.0,
    val totalWithTip: Double = 0.0,
    val grandTotal: Double = 0.0
) {
    fun formatSubtotal(currency: String = "Q"): String = "$currency${String.format(Locale.US, "%.2f", subtotal)}"
    fun formatTip(currency: String = "Q"): String = "$currency${String.format(Locale.US, "%.2f", tipAmount)}"
    fun formatTax(currency: String = "Q"): String = "$currency${String.format(Locale.US, "%.2f", taxAmount)}"
    fun formatDiscount(currency: String = "Q"): String = "$currency${String.format(Locale.US, "%.2f", discountAmount)}"
    fun formatGrandTotal(currency: String = "Q"): String = "$currency${String.format(Locale.US, "%.2f", grandTotal)}"
}

/**
 * Reusable utility for calculating and recalculating order subtotals, tips, taxes, discounts and totals.
 * Usable when adding new products, modifying quantities, or consolidating existing orders.
 */
object OrderCalculationHelper {

    /**
     * Calculates the subtotal for a single item line.
     */
    fun calculateItemSubtotal(unitPrice: Double, quantity: Int): Double {
        return (unitPrice * quantity.coerceAtLeast(1)).coerceAtLeast(0.0)
    }

    /**
     * Recalculates subtotal, tip (propina), tax, discount and grand total from a list of OrderItemEntity.
     *
     * @param items List of order item entities.
     * @param tipPercent Tip percentage (defaults to 10.0%).
     * @param fixedTip Optional fixed tip amount (overrides percentage if provided).
     * @param taxPercent Optional tax percentage.
     * @param discount Optional discount amount.
     * @return Complete breakdown in [OrderCalculationResult].
     */
    fun calculateOrderTotals(
        items: List<OrderItemEntity>,
        tipPercent: Double = 10.0,
        fixedTip: Double? = null,
        taxPercent: Double = 0.0,
        discount: Double = 0.0
    ): OrderCalculationResult {
        val subtotal = items.sumOf { item ->
            if (item.subtotal > 0) item.subtotal else calculateItemSubtotal(item.unitPrice, item.quantity)
        }
        val tip = fixedTip ?: (subtotal * (tipPercent.coerceAtLeast(0.0) / 100.0))
        val tax = subtotal * (taxPercent.coerceAtLeast(0.0) / 100.0)
        val grandTotal = (subtotal + tip + tax - discount).coerceAtLeast(0.0)
        val totalWithTip = (subtotal + tip).coerceAtLeast(0.0)

        return OrderCalculationResult(
            subtotal = subtotal,
            tipPercentage = tipPercent,
            tipAmount = tip,
            taxPercentage = taxPercent,
            taxAmount = tax,
            discountAmount = discount,
            totalWithTip = totalWithTip,
            grandTotal = grandTotal
        )
    }

    /**
     * Calculates totals directly from pairs of MenuItemEntity and (Quantity, Notes).
     */
    fun calculateFromMenuPairs(
        items: List<Pair<MenuItemEntity, Pair<Int, String>>>,
        tipPercent: Double = 10.0,
        fixedTip: Double? = null,
        discount: Double = 0.0
    ): OrderCalculationResult {
        val subtotal = items.sumOf { (menuItem, pair) ->
            calculateItemSubtotal(menuItem.price, pair.first)
        }
        val tip = fixedTip ?: (subtotal * (tipPercent.coerceAtLeast(0.0) / 100.0))
        val grandTotal = (subtotal + tip - discount).coerceAtLeast(0.0)

        return OrderCalculationResult(
            subtotal = subtotal,
            tipPercentage = tipPercent,
            tipAmount = tip,
            discountAmount = discount,
            totalWithTip = subtotal + tip,
            grandTotal = grandTotal
        )
    }

    /**
     * Recalculates an existing OrderEntity with an updated or appended set of order items,
     * maintaining independent kitchenStatus for previously cooked vs newly added items.
     */
    fun recalculateExistingOrder(
        existingOrder: OrderEntity,
        existingItems: List<OrderItemEntity>,
        newItems: List<OrderItemEntity>,
        tipPercent: Double = 10.0,
        additionalNotes: String? = null
    ): Pair<OrderEntity, List<OrderItemEntity>> {
        // Keep existing items with their current kitchenStatus
        val combinedItems = mutableListOf<OrderItemEntity>()
        combinedItems.addAll(existingItems)

        for (newItem in newItems) {
            // Only merge if existing item has the EXACT same notes and is STILL in 'PENDIENTE' status (not yet cooked)
            val existingPendingIndex = combinedItems.indexOfFirst {
                it.menuItemId == newItem.menuItemId &&
                it.notes.trim() == newItem.notes.trim() &&
                it.unitPrice == newItem.unitPrice &&
                it.kitchenStatus in listOf("PENDIENTE", "NUEVO")
            }

            if (existingPendingIndex >= 0) {
                val found = combinedItems[existingPendingIndex]
                val updatedQty = found.quantity + newItem.quantity
                val updatedSubtotal = calculateItemSubtotal(found.unitPrice, updatedQty)
                combinedItems[existingPendingIndex] = found.copy(
                    quantity = updatedQty,
                    subtotal = updatedSubtotal,
                    kitchenStatus = "PENDIENTE"
                )
            } else {
                // Add as a new item line with kitchenStatus = "PENDIENTE" so kitchen only receives this new dish
                combinedItems.add(
                    newItem.copy(
                        id = 0L, // new row ID
                        orderId = existingOrder.id,
                        kitchenStatus = "PENDIENTE",
                        createdAt = System.currentTimeMillis()
                    )
                )
            }
        }

        val calculation = calculateOrderTotals(combinedItems, tipPercent = tipPercent)

        val combinedNotes = when {
            additionalNotes.isNullOrBlank() -> existingOrder.generalNotes
            existingOrder.generalNotes.isNullOrBlank() -> additionalNotes
            existingOrder.generalNotes.contains(additionalNotes) -> existingOrder.generalNotes
            else -> "${existingOrder.generalNotes} | $additionalNotes"
        }

        val updatedOrder = existingOrder.copy(
            totalAmount = calculation.subtotal, // Subtotal stored in OrderEntity
            status = "PENDIENTE", // Set to PENDIENTE so kitchen receives the new items
            completedAt = null,
            generalNotes = combinedNotes
        )

        return Pair(updatedOrder, combinedItems)
    }
}
