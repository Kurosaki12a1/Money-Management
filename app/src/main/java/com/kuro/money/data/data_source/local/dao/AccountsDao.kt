package com.kuro.money.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kuro.money.data.model.AccountEntity

@Dao
interface AccountsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWallet(accounts: AccountEntity) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWallet(accounts: AccountEntity) : Int

    @Query("SELECT * FROM accounts")
    fun getAllWallets(): List<AccountEntity>
}