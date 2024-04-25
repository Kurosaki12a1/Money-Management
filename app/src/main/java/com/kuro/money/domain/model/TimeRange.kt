package com.kuro.money.domain.model

import java.time.LocalDate

sealed class TimeRange {
    data class DAY(val localDate: LocalDate) : TimeRange()
    data class WEEK(val startWeek: LocalDate, val endWeek: LocalDate) : TimeRange()
    data class MONTH(val startMonth: LocalDate, val endMonth: LocalDate) : TimeRange()
    data class QUARTER(val startQuarter: LocalDate, val endQuarter: LocalDate) : TimeRange()
    data class YEAR(val startYear: LocalDate, val endYear: LocalDate) : TimeRange()
    data object ALL : TimeRange()

    companion object {
        fun string(value: TimeRange): String = when (value) {
            is DAY -> "Day"
            is WEEK -> "Week"
            is MONTH -> "Month"
            is QUARTER -> "Quarter"
            is YEAR -> "Year"
            else -> "All"
        }

        // This is default
        fun getList() = listOf("Day", "Week", "Month", "Quarter", "Year", "All")
    }
}

fun buildTimeRange(range: TimeRange): String {
    return when (range) {
        is TimeRange.ALL -> ""
        is TimeRange.DAY -> "  t.displayDate = '${range.localDate}' "
        is TimeRange.WEEK -> "  t.displayDate BETWEEN '${range.startWeek}' AND '${range.endWeek}' "
        is TimeRange.MONTH -> "  t.displayDate BETWEEN '${range.startMonth}' AND '${range.endMonth}' "
        is TimeRange.YEAR -> "  t.displayDate BETWEEN ${range.startYear}' AND '${range.endYear}' "
        is TimeRange.QUARTER -> "  t.displayDate BETWEEN '${range.startQuarter}' AND '${range.endQuarter}' "
    }
}