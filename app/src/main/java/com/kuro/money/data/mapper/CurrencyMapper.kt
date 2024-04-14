package com.kuro.money.data.mapper

import com.kuro.money.data.model.Currency
import com.kuro.money.domain.model.CurrencyData

fun CurrencyData.toCurrency() = Currency(
    id = id ?: 0L,
    code = code ?: "",
    name = name ?: "",
    symbol = symbol ?: "",
    icon = "ic_currency_" + code?.lowercase()
)