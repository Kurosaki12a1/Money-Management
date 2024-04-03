package com.kuro.money.presenter.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.string(pattern: String = "dd/MM/yyyy"): String {
    if (this == LocalDate.now()) return "Today"
    return DateTimeFormatter.ofPattern(pattern).format(this)
}

fun Double.string(): String {
    val decimalFormat = DecimalFormat("#,###.##")
    return decimalFormat.format(this)
}

fun String.double(): Double {
    return if (this.isEmpty()) 0.0
    else this.toDouble()
}

fun String.format(): String {
    if (this.isEmpty()) return ""
    val regex = "([/*+-])|\\s+".toRegex()
    val parts = this.split(regex).filter { it.isNotBlank() }
    val operators = regex.findAll(this).map { it.value.trim() }.toList()

    val decimalFormatSymbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ','
    }
    val decimalFormat = DecimalFormat("#,###.##", decimalFormatSymbols)
    val formattedParts = parts.map { part ->
        if (part.contains('.') || part.contains(',')) {
            decimalFormat.format(part.toDouble())
        } else {
            decimalFormat.format(part.toLong())
        }
    }

    var result = formattedParts[0]
    operators.forEachIndexed { index, operator ->
        result += " $operator ${formattedParts[index + 1]}"
    }
    return result
}