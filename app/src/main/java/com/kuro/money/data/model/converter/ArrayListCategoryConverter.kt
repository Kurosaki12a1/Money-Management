package com.kuro.money.data.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.model.SubCategoryEntity

class ArrayListCategoryConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromListCategory(options: List<SubCategoryEntity>?): String {
        if (options == null) {
            return ""
        }
        val type = object : TypeToken<List<SubCategoryEntity>>() {}.type
        return gson.toJson(options, type)
    }

    @TypeConverter
    fun toListCategory(optionsString: String?): List<SubCategoryEntity> {
        if (optionsString.isNullOrEmpty()) {
            return arrayListOf()
        }
        val type = object : TypeToken<List<SubCategoryEntity>>() {}.type
        return gson.fromJson(optionsString, type)
    }
}