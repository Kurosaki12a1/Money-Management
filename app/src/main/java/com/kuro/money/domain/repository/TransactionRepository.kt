package com.kuro.money.domain.repository

import com.kuro.money.data.model.Transaction
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.AdvancedSearchAmount
import com.kuro.money.domain.model.AdvancedSearchCategory
import com.kuro.money.domain.model.AdvancedSearchTime
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TransactionRepository {
    fun insert(entity: Transaction): Flow<Resource<Long>>

    fun update(entity: Transaction): Flow<Resource<Int>>

    fun getListTransactions(): Flow<Resource<List<TransactionEntity>?>>

    fun deleteTransactionById(id: Long): Flow<Resource<Int>>

    fun getTransactionById(id: Long): Flow<Resource<TransactionEntity>>

    fun getTransactionByMonthYear(monthYear: String): Flow<Resource<List<TransactionEntity>>>

    fun queryTransaction(
        amount : AdvancedSearchAmount,
        walletName : String,
        time : AdvancedSearchTime,
        note: String,
        category: AdvancedSearchCategory,
        with : String
    ) : Flow<Resource<List<TransactionEntity>>>


    fun getTransactionsBetweenDates(
        startDate : LocalDate,
        endDate: LocalDate
    ) : Flow<Resource<List<TransactionEntity>>>

}