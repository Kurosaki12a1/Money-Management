package com.kuro.money.domain.usecase

import com.kuro.money.domain.repository.PreferencesRepository
import javax.inject.Inject

class PreferencesUseCase @Inject constructor(private val repo: PreferencesRepository) {

    fun isOnBoardingDone() = repo.isOnBoardingDone()

    fun isFirstTimeOpenApp() = repo.isFirstTimeOpenApp()

    fun getDefaultCurrency() = repo.getDefaultCurrency()

    fun setOnBoardingDone(value: Boolean) = repo.setOnBoardingDone(value)

    fun setFirstTimeOpenApp(value: Boolean) = repo.setFirstTimeOpen(value)

    fun setDefaultCurrency(value : String) = repo.setDefaultCurrency(value)
}