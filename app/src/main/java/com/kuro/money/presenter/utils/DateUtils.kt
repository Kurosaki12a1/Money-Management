package com.kuro.money.presenter.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

fun getWeeksOfMonth(year: Int, month: Int): Map<Int, List<LocalDate>> {
    // Set the start date as the first day of the given month and year
    val dateStart = LocalDate.of(year, month, 1)
    // Determine the last date of the month
    val dateEnd = dateStart.withDayOfMonth(dateStart.lengthOfMonth())
    // Initialize a mutable map to store weeks and their dates
    val weeks = mutableMapOf<Int, MutableList<LocalDate>>()

    // Iterate over each day from start to end of the month
    var currentDate = dateStart
    while (currentDate.isBefore(dateEnd) || currentDate.isEqual(dateEnd)) {
        // Get the week number of the current year for the current date
        val weekOfYear = currentDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())
        // Add the current date to the corresponding week in the map
        weeks.getOrPut(weekOfYear) { mutableListOf() }.add(currentDate)
        // Move to the next day
        currentDate = currentDate.plusDays(1)
    }

    // Adjust the map keys to start from 1 and include only weeks of the current month
    return weeks.entries.sortedBy { it.key }.mapIndexed { index, entry ->
        (index + 1) to entry.value
    }.toMap()
}

// Function to get the start and end dates of each week for a given month and year
fun getWeekBoundariesOfMonth(year: Int, month: Int): Map<Int, Pair<LocalDate, LocalDate>> {
    // Set the start date as the first day of the given month and year
    val dateStart = LocalDate.of(year, month, 1)
    // Determine the last date of the month
    val monthEnd = dateStart.withDayOfMonth(dateStart.lengthOfMonth())
    // Get current Date
    val currentDate = LocalDate.now()
    // Determine the end date
    val dateEnd = if(currentDate.isAfter(monthEnd)) monthEnd else currentDate
    // Initialize a mutable map to store weeks and their start/end dates
    val weeks = mutableMapOf<Int, Pair<LocalDate, LocalDate>>()

    // Variable to track the start of the current week
    var currentWeekStart = dateStart.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    if (currentWeekStart.monthValue < month) currentWeekStart = dateStart
    while (currentWeekStart.isBefore(dateEnd) || currentWeekStart.isEqual(dateEnd)) {
        var currentWeekEnd = currentWeekStart.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))  // Tìm Chủ Nhật của tuần
        if (currentWeekEnd.isAfter(dateEnd)) {
            currentWeekEnd = dateEnd
        }

        val weekOfYear = currentWeekStart.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())
        weeks[weekOfYear] = Pair(currentWeekStart, currentWeekEnd)

        if (currentWeekEnd.isEqual(dateEnd)) {
            break
        }

        currentWeekStart = currentWeekEnd.plusDays(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        if (currentWeekStart.monthValue != month) {
            break
        }
    }

    // Adjust the map keys to start from 1 and include only weeks of the current month
    return weeks.entries.sortedBy { it.key }.mapIndexed { index, entry ->
        // Remap the week numbers to start from 1
        (index + 1) to entry.value
    }.toMap()
}

fun weekToString(startWeek: LocalDate, endWeek: LocalDate): String {
    val formater = DateTimeFormatter.ofPattern("dd/MM")
    return formater.format(startWeek) + " - " + formater.format(endWeek)
}

fun monthToString(date : LocalDate) : String {
    val formater = DateTimeFormatter.ofPattern("MM/yyyy")
    return formater.format(date)
}

fun quarterToString(date : LocalDate) : String {
    val quarter = (date.monthValue - 1) / 3  + 1
    return "Q$quarter ${date.year}"
}