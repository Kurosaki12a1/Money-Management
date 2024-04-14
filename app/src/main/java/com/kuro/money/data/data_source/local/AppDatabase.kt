package com.kuro.money.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuro.money.data.data_source.local.dao.AccountsDao
import com.kuro.money.data.data_source.local.dao.CategoryDao
import com.kuro.money.data.data_source.local.dao.CurrencyDao
import com.kuro.money.data.data_source.local.dao.EventDao
import com.kuro.money.data.data_source.local.dao.ExchangeRatesDao
import com.kuro.money.data.data_source.local.dao.TransactionDao
import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.model.Currency
import com.kuro.money.data.model.Event
import com.kuro.money.data.model.ExchangeRateEntity
import com.kuro.money.data.model.Transaction
import com.kuro.money.data.model.Wallet

@Database(
    entities = [
        CategoryEntity::class,
        Wallet::class,
        Event::class,
        Currency::class,
        ExchangeRateEntity::class,
        Transaction::class
    ],
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

    abstract fun transactionDao(): TransactionDao
}