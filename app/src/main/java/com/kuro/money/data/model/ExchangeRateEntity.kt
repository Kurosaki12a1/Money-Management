package com.kuro.money.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kuro.money.data.model.converter.ConversionRatesConverter

@Entity(tableName = "exchange_rate")
@TypeConverters(ConversionRatesConverter::class)
data class ExchangeRateEntity(
    val result: String,
    val documentation: String,
    val termsOfUse: String,
    val timeLastUpdateUnix: Long,
    val timeLastUpdateUtc: String,
    val timeNextUpdateUnix: Long,
    val timeNextUpdateUtc: String,
    @PrimaryKey(autoGenerate = false)
    val baseCode: String,
    val conversionRates: List<ConversionRates>
)

data class ConversionRates(
    val currencyCode: String, // To currency converter
    val baseCode: String, // Main Currency
    val rate: Double
)

data class UpdateTimeInfo(
    @ColumnInfo(name = "timeLastUpdateUnix") val timeLastUpdateUnix: Long,
    @ColumnInfo(name = "timeNextUpdateUnix") val timeNextUpdateUnix: Long
)