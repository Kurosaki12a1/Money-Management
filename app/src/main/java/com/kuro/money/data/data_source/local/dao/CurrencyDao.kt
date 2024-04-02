package com.kuro.money.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuro.money.data.model.CurrencyEntity

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<CurrencyEntity>)

    @Query("SELECT * FROM currencies Where code=:code")
    fun getCurrencyByCode(code: String) : CurrencyEntity?

    @Query("SELECT * FROM currencies")
    fun getAllCurrencies() : List<CurrencyEntity>
}