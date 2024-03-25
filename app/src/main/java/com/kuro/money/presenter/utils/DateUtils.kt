package com.kuro.money.presenter.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.string(pattern: String = "dd/MM/yyyy"): String {
    if (this == LocalDate.now()) return "Today"
    return DateTimeFormatter.ofPattern(pattern).format(this)
}