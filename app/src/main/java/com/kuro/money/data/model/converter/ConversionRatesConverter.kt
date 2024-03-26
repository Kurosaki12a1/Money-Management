package com.kuro.money.data.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuro.money.data.model.ConversionRates

class ConversionRatesConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromListConversionRates(options: List<ConversionRates>?): String {
        if (options == null) {
            return ""
        }
        val type = object : TypeToken<List<ConversionRates>>() {}.type
        return gson.toJson(options, type)
    }

    @TypeConverter
    fun toListConversionRates(optionsString: String?): List<ConversionRates>? {
        if (optionsString.isNullOrEmpty()) {
            return null
        }
        val type = object : TypeToken<List<ConversionRates>>() {}.type
        return gson.fromJson(optionsString, type)
    }
}