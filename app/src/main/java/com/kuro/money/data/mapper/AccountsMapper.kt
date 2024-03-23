package com.kuro.money.data.mapper

import com.kuro.money.data.model.AccountEntity
import com.kuro.money.domain.model.Accounts

fun Accounts.toAccountsEntity() = AccountEntity(
    name = name ?: "", icon = icon ?: "", uuid = uuid ?: "", balance = balance ?: 0.0
)