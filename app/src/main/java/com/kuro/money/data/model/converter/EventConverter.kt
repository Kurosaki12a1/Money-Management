package com.kuro.money.data.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuro.money.data.model.EventEntity

class EventConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromListEvent(options: EventEntity?): String {
        if (options == null) {
            return ""
        }
        val type = object : TypeToken<EventEntity>() {}.type
        return gson.toJson(options, type)
    }

    @TypeConverter
    fun toListEvent(optionsString: String?): EventEntity? {
        if (optionsString.isNullOrEmpty()) {
            return null
        }
        val type = object : TypeToken<EventEntity>() {}.type
        return gson.fromJson(optionsString, type)
    }
}