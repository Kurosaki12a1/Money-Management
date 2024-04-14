package com.kuro.money.domain.repository

import com.kuro.money.data.model.Transaction
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun insert(entity: Transaction): Flow<Resource<Long>>

    fun update(entity: Transaction): Flow<Resource<Int>>

    fun getListTransactions(): Flow<Resource<List<TransactionEntity>?>>

    fun deleteTransactionById(id: Long): Flow<Resource<Int>>

    fun getTransactionById(id: Long): Flow<Resource<TransactionEntity>>

    fun getTransactionByMonthYear(monthYear: String): Flow<Resource<List<TransactionEntity>>>

}