package com.kuro.money.domain.repository

import com.kuro.money.data.model.Currency
import com.kuro.money.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun insert(list: List<Currency>): Flow<Resource<Unit>>

    fun getAllCurrencies(): Flow<Resource<List<Currency>>>

    fun getListCurrenciesFromJSON() : Flow<Resource<List<Currency>>>

    fun getCurrencyByCode(code : String) : Flow<Resource<Currency?>>

}