package com.kuro.money.di

import com.kuro.money.domain.repository.AccountsRepository
import com.kuro.money.domain.repository.CategoryRepository
import com.kuro.money.domain.repository.CurrencyRepository
import com.kuro.money.domain.repository.ExchangeRatesRepository
import com.kuro.money.domain.repository.IconRepository
import com.kuro.money.domain.repository.PreferencesRepository
import com.kuro.money.domain.repository.TransactionRepository
import com.kuro.money.domain.usecase.AccountsUseCase
import com.kuro.money.domain.usecase.CategoryUseCase
import com.kuro.money.domain.usecase.CurrenciesUseCase
import com.kuro.money.domain.usecase.ExchangeRatesUseCase
import com.kuro.money.domain.usecase.GetIconUseCase
import com.kuro.money.domain.usecase.PreferencesUseCase
import com.kuro.money.domain.usecase.TransactionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideCategoryUseCase(repo: CategoryRepository) = CategoryUseCase(repo)

    @Provides
    @Singleton
    fun providePreferencesUseCase(repo: PreferencesRepository) = PreferencesUseCase(repo)

    @Provides
    @Singleton
    fun provideAccountUseCase(repo: AccountsRepository) = AccountsUseCase(repo)

    @Provides
    @Singleton
    fun provideGetIconUseCase(repo: IconRepository) = GetIconUseCase(repo)

    @Provides
    @Singleton
    fun provideCurrencyUseCase(repo : CurrencyRepository) = CurrenciesUseCase(repo)

    @Provides
    @Singleton
    fun provideExchangesRateUseCase(repo : ExchangeRatesRepository) = ExchangeRatesUseCase(repo)

    @Provides
    @Singleton
    fun provideTransactionUseCase(repo: TransactionRepository) = TransactionUseCase(repo)
}