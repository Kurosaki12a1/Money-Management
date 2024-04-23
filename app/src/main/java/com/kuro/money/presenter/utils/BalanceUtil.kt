package com.kuro.money.presenter.utils

import com.kuro.money.constants.Constants
import com.kuro.money.data.AppCache
import com.kuro.money.data.model.Currency
import com.kuro.money.data.model.TransactionEntity

fun calculateBalanceBetweenTransaction(
    currency: Currency,
    balance: Double,
    otherCurrency: Currency,
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

fun calculateWalletAfterTransaction(
    walletCurrency: Currency,
    walletBalance: Double,
    transactionCurrency: Currency,
    transactionBalance: Double,
    isPlus: Boolean // Salary = true
): Double {
    // Rate  = currency / default currency
    var rates1 = AppCache.listRates.value[AppCache.defaultCurrency.value]?.find {
        it.currencyCode.lowercase() == walletCurrency.code.lowercase()
    }?.rate
    rates1 = if (rates1 != null) 1 / rates1 else 1.0
    // Rate = otherCurrency / Default Currency
    var rates2 = AppCache.listRates.value[AppCache.defaultCurrency.value]?.find {
        it.currencyCode.lowercase() == transactionCurrency.code.lowercase()
    }?.rate
    rates2 = if (rates2 != null) 1 / rates2 else 1.0
    return if (isPlus) walletBalance + rates2 / rates1 * transactionBalance else walletBalance - rates2 / rates1 * transactionBalance
}

fun getBalance(transactionEntity: TransactionEntity): Double {
    val rates = AppCache.listRates.value[AppCache.defaultCurrency.value]?.find { rate ->
        rate.currencyCode.lowercase() == transactionEntity.currency.code.lowercase()
    }?.rate
    return if (rates != null) 1 / rates * transactionEntity.amount else transactionEntity.amount
}

fun getBalanceFromList(list: List<TransactionEntity>?): Double {
    if (list.isNullOrEmpty()) return 0.0
    return list.filter { it.category.type == Constants.INCOME }
        .sumOf { getBalance(it) } - list.filter { it.category.type == Constants.EXPENSE }
        .sumOf { getBalance(it) }
}

fun getIncomeFromList(list : List<TransactionEntity>?) : Double {
    if (list.isNullOrEmpty()) return 0.0
    return list.filter { it.category.type == Constants.INCOME }
        .sumOf { getBalance(it) }
}

fun getExpenseFromList(list : List<TransactionEntity>?) : Double {
    if (list.isNullOrEmpty()) return 0.0
    return list.filter { it.category.type == Constants.EXPENSE }
        .sumOf { getBalance(it) }
}