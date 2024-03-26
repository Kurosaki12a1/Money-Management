package com.kuro.money.domain.usecase

import com.kuro.money.data.model.ExchangeRateEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.ExchangeRateResponse
import com.kuro.money.domain.repository.ExchangeRatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ExchangeRatesUseCase @Inject constructor(
    private val repo: ExchangeRatesRepository
) {
    operator fun invoke(exchangeRateEntity: ExchangeRateEntity) =
        repo.insertOrUpdateExchangeRate(exchangeRateEntity)

    suspend fun getExchangeRatesResponse(currency: String): Flow<Resource<ExchangeRateResponse>> {
        val currentTimeInMillis = System.currentTimeMillis()
        return repo.getLastUpdateTimeAndNextUpdateTime(currency).flatMapLatest {
            if (it == null // No data
                || it.timeLastUpdateUnix * 1000 + TimeUnit.DAYS.toMillis(1) < currentTimeInMillis // No update 1 day ago
                || currentTimeInMillis - it.timeNextUpdateUnix * 1000 > TimeUnit.DAYS.toMillis(1)
            ) {
                repo.getExchangeRateOnline(currency)
            } else {
                flowOf(Resource.failure(Error("No need to update")))
            }
        }
    }

    operator fun invoke(currency: String) = repo.getExchangeRateByCurrency(currency)
}