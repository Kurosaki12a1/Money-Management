package com.kuro.money.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuro.money.data.data_source.local.dao.AccountsDao
import com.kuro.money.data.data_source.local.dao.CategoryDao
import com.kuro.money.data.data_source.local.dao.CurrencyDao
import com.kuro.money.data.data_source.local.dao.EventDao
import com.kuro.money.data.data_source.local.dao.ExchangeRatesDao
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.model.CurrencyEntity
import com.kuro.money.data.model.EventEntity
import com.kuro.money.data.model.ExchangeRateEntity

@Database(
    entities = [CategoryEntity::class, AccountEntity::class, EventEntity::class, CurrencyEntity::class, ExchangeRateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "Money.db"
    }

    abstract fun categoryDao(): CategoryDao

    abstract fun accountsDao(): AccountsDao

    abstract fun eventDao(): EventDao

    abstract fun currencyDao(): CurrencyDao

    abstract fun exchangeRatesDao(): ExchangeRatesDao

}