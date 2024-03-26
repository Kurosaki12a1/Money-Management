package com.kuro.money.data.data_source.remote

import com.kuro.money.domain.model.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExchangeRatesAPI {

    @GET("latest/{currency}")
    suspend fun getExchangeRate(@Path("currency") currency: String ): ExchangeRateResponse
}