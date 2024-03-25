package com.kuro.money.data.model

import androidx.room.Entity

@Entity(tableName = "exchange_rate")
data class ExchangeRateEntity(
    val result: String,
    val documentation: String,
    val termsOfUse: String,
    val timeLastUpdateUnix: Long,
    val timeLastUpdateUtc: String,
    val timeNextUpdateUnix: Long,
    val timeNextUpdateUtc: String,
    val baseCode: String,
    val conversionRates: List<ConversionRates>
)

data class ConversionRates(
    val currencyCode: String, // To currency converter
    val baseCode: String, // Main Currency
    val rate: Double
)