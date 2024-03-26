package com.kuro.money.data.repository

import com.kuro.money.data.data_source.local.AppDatabase
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TransactionRepository {
    override fun insert(entity: TransactionEntity): Flow<Resource<Long>> = flow {
        emit(Resource.Loading)
        try {
            val id = appDatabase.transactionDao().insert(entity)
            emit(Resource.success(id))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun update(entity: TransactionEntity): Flow<Resource<Int>> = flow {
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

    override fun deleteTransactionById(id: Long): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            appDatabase.transactionDao().deleteTransactionById(id)
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)
}