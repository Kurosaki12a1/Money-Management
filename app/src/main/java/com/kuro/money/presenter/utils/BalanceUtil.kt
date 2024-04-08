package com.kuro.money.presenter.utils

import com.kuro.money.data.AppCache
import com.kuro.money.data.model.CurrencyEntity

fun calculateBalanceBetweenTransaction(
    currency: CurrencyEntity,
    balance: Double,
    otherCurrency: CurrencyEntity,
    otherBalance: Double,
    isPlus: Boolean
): Double {
    // Rate  = currency / default currency
    var rates1 = AppCache.listRates.value[AppCache.defaultCurrency.value]?.find {
        it.currencyCode.lowercase() == currency.code.lowercase()
    }?.rate
    rates1 = if (rates1 != null) 1 / rates1 else 1.0
    // Rate = otherCurrency / Default Currency
    var rates2 = AppCache.listRates.value[AppCache.defaultCurrency.value]?.find {
        it.currencyCode.lowercase() == otherCurrency.code.lowercase()
    }?.rate
    rates2 = if (rates2 != null) 1 / rates2 else 1.0
    return if (isPlus) rates1 * balance + rates2 * otherBalance else rates1 * balance - rates2 * otherBalance
}