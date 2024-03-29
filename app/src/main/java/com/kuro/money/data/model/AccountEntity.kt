package com.kuro.money.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kuro.money.data.model.converter.ArrayListCategoryConverter
import com.kuro.money.data.model.converter.CurrencyConverter

@Entity(tableName = "accounts")
@TypeConverters(CurrencyConverter::class)
data class AccountEntity(
    val name: String,
    val currencyEntity: CurrencyEntity,
    val icon: String,
    val uuid: String,
    val balance: Double,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L
)