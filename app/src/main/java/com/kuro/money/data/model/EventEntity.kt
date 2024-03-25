package com.kuro.money.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kuro.money.data.model.converter.AccountConverter
import com.kuro.money.data.model.converter.CurrencyConverter
import com.kuro.money.data.model.converter.EventConverter
import com.kuro.money.data.model.converter.LocalDateConverter
import java.time.LocalDate

@Entity(tableName = "event")
@TypeConverters(
    CurrencyConverter::class,
    EventConverter::class,
    LocalDateConverter::class,
    AccountConverter::class
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val icon: String,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val currency: CurrencyEntity,
    val wallet: AccountEntity
)