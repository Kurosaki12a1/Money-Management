package com.kuro.money.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kuro.money.data.model.ExchangeRateEntity
import com.kuro.money.data.model.UpdateTimeInfo

@Dao
interface ExchangeRatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExchangeRates(entity : ExchangeRateEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateExchangeRates(entity : ExchangeRateEntity): Int

    @Query("SELECT * FROM exchange_rate WHERE baseCode=:currency")
    fun getExchangeRatesByCurrency(currency: String): ExchangeRateEntity?

    @Query("SELECT timeLastUpdateUnix, timeNextUpdateUnix FROM exchange_rate WHERE baseCode=:currency")
    fun getLastUpdateTimeAndNextUpdateTime(currency: String) : UpdateTimeInfo?
}