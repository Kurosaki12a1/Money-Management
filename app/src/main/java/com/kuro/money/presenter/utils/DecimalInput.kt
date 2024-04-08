package com.kuro.money.presenter.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormatSymbols

/**
 * Visual transformation for input fields that formats decimal numbers.
 * This transformation applies a visual formatting to numbers with decimal places
 * and thousands separators without changing the actual data being inputted.
 *
 * @property decimalFormatter An instance of DecimalFormatter used to format the numbers.
 */

class DecimalInputVisualTransformation(
    private val decimalFormatter: DecimalFormatter
) : VisualTransformation {

    /** Transforms the input text into a visually formatted decimal number.
    *
    * @param text The current text in the input field.
    * @return A TransformedText object containing the visually formatted string and offset mapping.
    */
    override fun filter(text: AnnotatedString): TransformedText {

        val inputText = text.text

        // Use the DecimalFormatter to format the string for visual presentation
        val formattedNumber = decimalFormatter.formatForVisual(inputText)

        // Create a new AnnotatedString with the formatted text while preserving any span styles
        // and paragraph styles from the original input text.
        val newText = AnnotatedString(
            text = formattedNumber,
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles
        )

        // Initialize an OffsetMapping to correctly map cursor positions
        // from the original input text to the transformed text and vice versa.
        val offsetMapping = FixedCursorOffsetMapping(
            input = inputText,
            formattedInput = formattedNumber,
            decimalFormatter = decimalFormatter
        )

        // Return the transformed text with its associated offset mapping.
        return TransformedText(newText, offsetMapping)
    }
}

private class FixedCursorOffsetMapping(
    private val input : String,
    private val formattedInput: String,
    private val decimalFormatter: DecimalFormatter
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        // Returns the position of the cursor after formatting
        // This code needs to be improved to handle it properly
        val originalSubString = input.substring(0, offset)
        val formattedSubString = decimalFormatter.formatForVisual(originalSubString)
        return formattedSubString.length
    }
    override fun transformedToOriginal(offset: Int): Int {
        // Returns the initial position of the cursor before formatting
        // This code needs logic to determine the corresponding initial position
        val formattedSubString = formattedInput.substring(0, offset)
        val cleanupSubString = decimalFormatter.cleanup(formattedSubString)
        return cleanupSubString.length
    }
}

class DecimalFormatter(
    symbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance()
) {
    // Get the thousands separator from DecimalFormatSymbols
    private val thousandsSeparator = symbols.groupingSeparator

    // Get the decimal separator from DecimalFormatSymbols
    private val decimalSeparator = symbols.decimalSeparator

    /**
     * The function removes non-numeric characters and retains the first decimal separator (if any).
     *
     * @param input Input string to clean.
     * @return Sanitized string containing only numbers and at most one decimal separator.
     */
    fun cleanup(input: String): String {
        // Check if string contains only non-numeric characters, returns empty string
        if (input.matches("\\D".toRegex())) return ""
        // If string contains only 0, return "0"
        if (input.matches("0+".toRegex())) return "0"

        val sb = StringBuilder()
        // Flag to check if there is a decimal separator
        var hasDecimalSep = false

        for (char in input) {
            if (char.isDigit()) {
                sb.append(char)
                continue
            }
            if (char == decimalSeparator && !hasDecimalSep && sb.isNotEmpty()) {
                sb.append(char)
                hasDecimalSep = true
            }
        }

        return sb.toString()
    }

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