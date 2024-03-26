package com.kuro.money.domain.repository

import com.kuro.money.data.data_source.remote.ExchangeRatesAPI
import com.kuro.money.data.model.ExchangeRateEntity
import com.kuro.money.data.model.UpdateTimeInfo
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.ExchangeRateResponse
import kotlinx.coroutines.flow.Flow

interface ExchangeRatesRepository {
    suspend fun getExchangeRateOnline(currency: String): Flow<Resource<ExchangeRateResponse>>

    fun insertOrUpdateExchangeRate(exchangeRate: ExchangeRateEntity) : Flow<Resource<Unit>>

    fun getExchangeRateByCurrency(currency: String): Flow<Resource<ExchangeRateEntity?>>

    fun getLastUpdateTimeAndNextUpdateTime(currency: String) : Flow<UpdateTimeInfo?>
}