package com.kuro.money.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.Wallet

@Dao
interface AccountsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWallet(accounts: Wallet): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWallet(accounts: Wallet): Int

    @Transaction
    @Query("SELECT * FROM accounts")
    fun getAllWallets(): List<AccountEntity>

    @Transaction
    @Query("SELECT * FROM accounts WHERE id=:id")
    fun getWalletById(id: Long): AccountEntity

    @Transaction
    @Query("SELECT * FROM accounts ORDER BY id LIMIT :count")
    fun getWallets(count : Int) : List<AccountEntity>

    @Query("DELETE FROM accounts WHERE id=:id")
    fun deleteWalletById(id: Long) : Int
}