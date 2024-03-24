package com.kuro.money.data.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

class LocalDateConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromLocalDate(options: LocalDate?): String {
        if (options == null) {
            return ""
        }
        val type = object : TypeToken<LocalDate>() {}.type
        return gson.toJson(options, type)
    }

    @TypeConverter
    fun toLocalDate(optionsString: String?): LocalDate? {
        if (optionsString.isNullOrEmpty()) {
            return null
        }
        val type = object : TypeToken<LocalDate>() {}.type
        return gson.fromJson(optionsString, type)
    }
}