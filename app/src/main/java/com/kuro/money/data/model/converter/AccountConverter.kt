package com.kuro.money.data.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.CurrencyEntity

class AccountConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromAccounts(options: AccountEntity?): String {
        if (options == null) {
            return ""
        }
        val type = object : TypeToken<AccountEntity>() {}.type
        return gson.toJson(options, type)
    }

    @TypeConverter
    fun toAccounts(optionsString: String?): AccountEntity? {
        if (optionsString.isNullOrEmpty()) {
            return null
        }
        val type = object : TypeToken<AccountEntity>() {}.type
        return gson.fromJson(optionsString, type)
    }
}