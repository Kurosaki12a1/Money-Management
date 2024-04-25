package com.kuro.money.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.kuro.money.data.data_source.local.AppDatabase
import com.kuro.money.data.model.Transaction
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.AdvancedSearchAmount
import com.kuro.money.domain.model.AdvancedSearchCategory
import com.kuro.money.domain.model.AdvancedSearchTime
import com.kuro.money.domain.model.TimeRange
import com.kuro.money.domain.model.buildAmountCondition
import com.kuro.money.domain.model.buildCategoryCondition
import com.kuro.money.domain.model.buildNoteCondition
import com.kuro.money.domain.model.buildTimeCondition
import com.kuro.money.domain.model.buildTimeRange
import com.kuro.money.domain.model.buildWalletCondition
import com.kuro.money.domain.model.buildWithCondition
import com.kuro.money.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TransactionRepository {
    override fun insert(entity: Transaction): Flow<Resource<Long>> = flow {
        emit(Resource.Loading)
        try {
            val id = appDatabase.transactionDao().insert(entity)
            emit(Resource.success(id))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun update(entity: Transaction): Flow<Resource<Int>> = flow {
        emit(Resource.Loading)
        try {
            val rowEdited = appDatabase.transactionDao().update(entity)
            emit(Resource.success(rowEdited))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun getListTransactions(): Flow<Resource<List<TransactionEntity>?>> = flow {
        emit(Resource.Loading)
        try {
            val listData = appDatabase.transactionDao().getListTransactions()
            emit(Resource.success(listData))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun deleteTransactionById(id: Long): Flow<Resource<Int>> = flow {
        emit(Resource.Loading)
        try {
            val rowEffected = appDatabase.transactionDao().deleteTransactionById(id)
            emit(Resource.success(rowEffected))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun getTransactionById(id: Long): Flow<Resource<TransactionEntity>> = flow {
        emit(Resource.Loading)
        try {
            val data = appDatabase.transactionDao().getTransactionById(id)
            emit(Resource.success(data))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun getTransactionByMonthYear(monthYear: String) = flow {
        emit(Resource.Loading)
        try {
            val listData = appDatabase.transactionDao().getTransactionsByDate(monthYear)
            emit(Resource.Success(listData))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun queryTransaction(
        amount: AdvancedSearchAmount,
        walletName: String,
        time: AdvancedSearchTime,
        note: String,
        category: AdvancedSearchCategory,
        with: String
    ): Flow<Resource<List<TransactionEntity>>> = flow {
        emit(Resource.Loading)
        val conditions = listOf(
            buildWalletCondition(walletName),
            buildNoteCondition(note),
            buildWithCondition(with),
            buildTimeCondition(time),
            buildAmountCondition(amount),
            buildCategoryCondition(category)
        ).filter { it.isNotEmpty() }
        val query = buildString {
            append("SELECT t.* FROM transactions t JOIN accounts w ON w.id = t.walletId JOIN categories c ON c.id = t.categoryId ")
            if (conditions.isNotEmpty()) {
                append(" WHERE ")
                append(conditions.joinToString(" AND "))
            }
        }
        try {
            val data = appDatabase.transactionDao().queryTransactions(SimpleSQLiteQuery(query))
            emit(Resource.success(data))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun getTransactionsBetweenDates(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<Resource<List<TransactionEntity>>>  = flow {
        emit(Resource.Loading)
        try {
            val data = appDatabase.transactionDao().getTransactionsBetweenDates(startDate, endDate)
            emit(Resource.success(data))
        } catch (e : Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun queryTransactionWithTimeRange(timeRange: TimeRange): Flow<Resource<List<TransactionEntity>>> = flow {
        emit(Resource.Loading)
        val condition = buildTimeRange(timeRange)
        val query = buildString {
            append("SELECT t.* FROM transactions t ")
            if (condition.isNotEmpty()) {
                append(" WHERE ")
                append(condition)
            }
        }
        try {
            val data = appDatabase.transactionDao().queryTransactions(SimpleSQLiteQuery(query))
            emit(Resource.success(data))
        } catch (e: Exception){
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)
}