package com.kuro.money.data.repository

import com.kuro.money.data.data_source.local.AppDatabase
import com.kuro.money.data.data_source.remote.ExchangeRatesAPI
import com.kuro.money.data.model.ExchangeRateEntity
import com.kuro.money.data.model.UpdateTimeInfo
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.ExchangeRateResponse
import com.kuro.money.domain.repository.ExchangeRatesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExchangeRatesRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val exchangeRatesAPI: ExchangeRatesAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExchangeRatesRepository {
    override suspend fun getExchangeRateOnline(currency: String): Flow<Resource<ExchangeRateResponse>> =
        flow {
            emit(Resource.Loading)
            try {
                val data = exchangeRatesAPI.getExchangeRate(currency)
                emit(Resource.success(data))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.failure(e, e.message))
            }
        }.flowOn(dispatcher)

    override fun insertOrUpdateExchangeRate(exchangeRate: ExchangeRateEntity): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            if (appDatabase.exchangeRatesDao().getExchangeRatesByCurrency(exchangeRate.baseCode) != null) {
                appDatabase.exchangeRatesDao().updateExchangeRates(exchangeRate)
            } else {
                appDatabase.exchangeRatesDao().insertExchangeRates(exchangeRate)
            }
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun getExchangeRateByCurrency(currency: String): Flow<Resource<ExchangeRateEntity?>> =
        flow {
            emit(Resource.Loading)
            try {
                val exchangeRates =
                    appDatabase.exchangeRatesDao().getExchangeRatesByCurrency(currency)
                emit(Resource.success(exchangeRates))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.failure(e, e.message))
            }
        }.flowOn(dispatcher)

    override fun getLastUpdateTimeAndNextUpdateTime(currency: String): Flow<UpdateTimeInfo?> =
        flow {
            val updateTimeInfo =
                appDatabase.exchangeRatesDao().getLastUpdateTimeAndNextUpdateTime(currency)
            emit(updateTimeInfo)
        }.flowOn(dispatcher)
}