package com.kuro.money.data.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuro.money.data.model.SubCategoryEntity
import com.kuro.money.domain.model.SelectedCategory

class SelectedCategoryConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromSubCategory(options: SelectedCategory?): String {
        if (options == null) {
            return ""
        }
        val type = object : TypeToken<SelectedCategory>() {}.type
        return gson.toJson(options, type)
    }

    @TypeConverter
    fun toSubCategory(optionsString: String?): SelectedCategory {
        val type = object : TypeToken<SelectedCategory>() {}.type
        return gson.fromJson(optionsString, type)
    }
}