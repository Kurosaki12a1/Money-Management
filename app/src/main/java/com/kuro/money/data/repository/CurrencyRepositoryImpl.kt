package com.kuro.money.data.repository

import android.content.Context
import com.kuro.money.data.data_source.local.AppDatabase
import com.kuro.money.data.mapper.toCurrency
import com.kuro.money.data.model.Currency
import com.kuro.money.data.utils.FileUtils
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.Currencies
import com.kuro.money.domain.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val context: Context,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CurrencyRepository {
    override fun insert(list: List<Currency>): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            appDatabase.currencyDao().insert(list)
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun getAllCurrencies(): Flow<Resource<List<Currency>>> = flow {
        emit(Resource.Loading)
        try {
            val data = appDatabase.currencyDao().getAllCurrencies()
            emit(Resource.success(data))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun getListCurrenciesFromJSON(): Flow<Resource<List<Currency>>> = flow {
        emit(Resource.Loading)
        try {
            val data = FileUtils.loadJsonFromAsset<Currencies>(context, "currency.json")
            val result = mutableListOf<Currency>()
            data?.data?.forEach { result.add(it.toCurrency()) }
            emit(Resource.success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun getCurrencyByCode(code: String): Flow<Resource<Currency?>> = flow {
        emit(Resource.Loading)
        try {
            val data = appDatabase.currencyDao().getCurrencyByCode(code.uppercase())
            emit(Resource.success(data))
        } catch (e : Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

}