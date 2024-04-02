package com.kuro.money.domain.usecase

import com.kuro.money.data.model.CurrencyEntity
import com.kuro.money.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrenciesUseCase @Inject constructor(private val repo: CurrencyRepository) {
    operator fun invoke(list: List<CurrencyEntity>) = repo.insert(list)

    operator fun invoke() = repo.getAllCurrencies()

    operator fun invoke(code: String) = repo.getCurrencyByCode(code)

    fun getListCurrenciesFromJSON() = repo.getListCurrenciesFromJSON()
}