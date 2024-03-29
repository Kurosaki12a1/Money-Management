package com.kuro.money.data.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuro.money.data.model.CurrencyEntity

class CurrencyConverter {
    private val gson = Gson()
    @TypeConverter
    fun fromCurrency(options: CurrencyEntity?): String {
        if (options == null) {
            return ""
        }
        val type = object : TypeToken<CurrencyEntity>() {}.type
        return gson.toJson(options, type)
    }

    @TypeConverter
    fun toCurrency(optionsString: String?): CurrencyEntity? {
        if (optionsString.isNullOrEmpty()) {
            return null
        }
        val type = object : TypeToken<CurrencyEntity>() {}.type
        return gson.fromJson(optionsString, type)
    }
}