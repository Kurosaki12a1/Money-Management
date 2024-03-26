package com.kuro.money.domain.repository

import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun insert(entity: TransactionEntity): Flow<Resource<Long>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: TransactionEntity): Flow<Resource<Int>>

    fun getListTransactions(): Flow<Resource<List<TransactionEntity>?>>

    fun deleteTransactionById(id: Long): Flow<Resource<Unit>>

}