package com.kuro.money.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kuro.money.data.model.TransactionEntity

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: TransactionEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: TransactionEntity): Int

    @Query("SELECT * FROM transactions")
    fun getListTransactions(): List<TransactionEntity>

    @Query("DELETE FROM transactions WHERE id=:id")
    fun deleteTransactionById(id: Long) : Int
}