package com.kuro.money.data.repository

import android.content.Context
import com.kuro.money.data.data_source.local.AppDatabase
import com.kuro.money.data.mapper.toAccountsEntity
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.utils.FileUtils
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.Accounts
import com.kuro.money.domain.repository.AccountsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val context: Context,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AccountsRepository {
    override fun insertAccount(accountEntity: AccountEntity): Flow<Resource<Long>> = flow {
        emit(Resource.Loading)
        try {
            val data = appDatabase.accountsDao().insertWallet(accountEntity)
            emit(Resource.success(data))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun insertAccounts(list: List<AccountEntity>): Flow<Resource<Long>> = flow {
        emit(Resource.Loading)
        var id = 0L
        try {
            list.forEach {
                id = appDatabase.accountsDao().insertWallet(it)
            }
            emit(Resource.success(id))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun updateAccount(accountEntity: AccountEntity): Flow<Resource<Int>> = flow {
        emit(Resource.Loading)
        try {
            val data = appDatabase.accountsDao().updateWallet(accountEntity)
            emit(Resource.success(data))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun getAllAccounts(): Flow<Resource<List<AccountEntity>>> = flow {
        emit(Resource.Loading)
        try {
            val list = appDatabase.accountsDao().getAllWallets()
            emit(Resource.success(list))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun readFileFromJson(jsonName: String): Flow<Resource<List<AccountEntity>?>> = flow {
        emit(Resource.Loading)
        try {
            val data = FileUtils.loadJsonFromAsset<List<Accounts>>(context, jsonName)
            emit(Resource.success(data?.map { it.toAccountsEntity() }))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun getAccountById(id: Long): Flow<Resource<AccountEntity>> = flow {
        emit(Resource.Loading)
        try {
            val data = appDatabase.accountsDao().getWalletById(id)
            emit(Resource.success(data))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun deleteAccountById(id: Long): Flow<Resource<Int>> = flow {
        emit(Resource.Loading)
        try {
            val rowDelete = appDatabase.accountsDao().deleteWalletById(id)
            emit(Resource.success(rowDelete))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }

    override fun getAccounts(count: Int): Flow<Resource<List<AccountEntity>>> = flow {
        emit(Resource.Loading)
        try {
            val data = appDatabase.accountsDao().getWallets(count)
            emit(Resource.success(data))
        } catch (e : Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)
}