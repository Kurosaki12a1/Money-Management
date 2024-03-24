package com.kuro.money.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rate")
data class ExchangeRateEntity (
    @PrimaryKey(autoGenerate = false)
    val currencyCode : String,
    val rate: Double
)