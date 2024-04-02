package com.kuro.money.domain.repository

import com.kuro.money.data.model.CurrencyEntity
import com.kuro.money.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun insert(list: List<CurrencyEntity>): Flow<Resource<Unit>>

    fun getAllCurrencies(): Flow<Resource<List<CurrencyEntity>>>

    fun getListCurrenciesFromJSON() : Flow<Resource<List<CurrencyEntity>>>

    fun getCurrencyByCode(code : String) : Flow<Resource<CurrencyEntity?>>

}