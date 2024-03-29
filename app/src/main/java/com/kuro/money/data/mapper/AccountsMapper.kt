package com.kuro.money.data.mapper

import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.CurrencyEntity
import com.kuro.money.domain.model.Accounts

fun Accounts.toAccountsEntity() = AccountEntity(
    name = name ?: "",
    icon = icon ?: "",
    uuid = uuid ?: "",
    balance = balance ?: 0.0,
    currencyEntity = currency?.toCurrencyEntity() ?: CurrencyEntity(
        code = "VND",
        symbol = "₫",
        name = "Việt Nam Đồng",
        icon = "ic_currency_vnd",
        id = 4
    )
)
