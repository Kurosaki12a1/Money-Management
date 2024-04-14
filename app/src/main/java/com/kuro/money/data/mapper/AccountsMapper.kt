package com.kuro.money.data.mapper

import com.kuro.money.data.model.Wallet
import com.kuro.money.domain.model.Accounts

fun Accounts.toWallet() = Wallet(
    name = name ?: "",
    icon = icon ?: "",
    uuid = uuid ?: "",
    balance = balance ?: 0.0,
    currencyId = currencyId ?: 4L // 4 is code VND
)
