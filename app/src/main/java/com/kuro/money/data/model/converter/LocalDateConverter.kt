package com.kuro.money.data.model.converter

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun toDate(dateString: String?): LocalDate? {
        return dateString?.let {
            return formatter.parse(it, LocalDate::from)
        }
    }

    @TypeConverter
    fun fromDate(date: LocalDate?): String? {
        return date?.format(formatter)
    }
}