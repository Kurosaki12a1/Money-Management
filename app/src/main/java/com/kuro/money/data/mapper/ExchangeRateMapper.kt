package com.kuro.money.data.mapper

import com.kuro.money.data.model.ConversionRates
import com.kuro.money.data.model.ExchangeRateEntity
import com.kuro.money.domain.model.ExchangeRateResponse

fun ExchangeRateResponse.toExchangeRateEntity(): ExchangeRateEntity {
    val listConversionRates = mutableListOf<ConversionRates>()
    conversionRates?.forEach { (code, rate) ->
        val entity = ConversionRates(baseCode ?: "", code, rate)
        listConversionRates.add(entity)
    }
    return ExchangeRateEntity(
        result = result ?: "",
        documentation = documentation ?: "",
        termsOfUse = termsOfUse ?: "",
        timeLastUpdateUnix = timeLastUpdateUnix ?: 0L,
        timeNextUpdateUtc = timeNextUpdateUtc ?: "",
        timeLastUpdateUtc = timeLastUpdateUtc ?: "",
        timeNextUpdateUnix = timeNextUpdateUnix ?: 0L,
        baseCode = baseCode ?: "",
        conversionRates = listConversionRates
    )
}