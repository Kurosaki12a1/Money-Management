package com.kuro.money.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyEntity(
    val code: String,
    val symbol: String,
    val icon: String,
    val name: String,
    @PrimaryKey var id: Long,
)