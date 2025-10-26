package com.example.roseledger.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Formats a Double value as a acurrency string with comma and two decimal places.
 * Example : 12345.6 -> ₱12,345.67
 *
 * @param amount The numerical amount to format.
 * @return A formatted currency string.
 */
fun formatAmountWithCommas(amount: Double): String {
    val philippines = Locale("en", "PH")
    val format = NumberFormat.getCurrencyInstance(philippines)
    return format.format(amount)
}