package com.kuro.money.data.mapper

import com.kuro.money.data.model.CurrencyEntity
import com.kuro.money.domain.model.Currency

fun Currency.toCurrencyEntity() = CurrencyEntity(
    id = id ?: 0L,
    code = code ?: "",
    name = name ?: "",
    symbol = symbol ?: ""
)