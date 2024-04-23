package co.yml.charts.common.utils

import java.text.DecimalFormatSymbols


class DecimalFormatter(
    symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()
) {
    // Get the thousands separator from DecimalFormatSymbols
    private val thousandsSeparator = symbols.groupingSeparator

    // Get the decimal separator from DecimalFormatSymbols
    private val decimalSeparator = symbols.decimalSeparator


    /**
     * Format a number string for display with thousands separators and decimal separators.
     *
     * @param input Input string to format.
     * @return Formatted numeric string.
     */
    fun formatForVisual(input: String): String {
        val isNegative = input.startsWith("-")
        val positiveInput = if (isNegative) input.substring(1) else input
        val split = positiveInput.split(decimalSeparator)
        //Format the integer part with thousands separator
        val intPartFormatted = split[0]
            .reversed()
            .chunked(3) // Divide the integer part into groups of 3 digits
            .joinToString(separator = thousandsSeparator.toString()) // Join groups using thousands separators
            .reversed()

        val intPart = if (isNegative) "-$intPartFormatted" else intPartFormatted

        // Get the decimal part if there is one
        val fractionPart = split.getOrNull(1)

        // Returns the formatted string, combining the integer and decimal parts if any
        return if (fractionPart == null) intPart else intPart + decimalSeparator + fractionPart
    }
}