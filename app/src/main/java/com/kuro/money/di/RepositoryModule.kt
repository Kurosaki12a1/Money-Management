package com.kuro.money.di

import android.content.Context
import com.kuro.money.data.data_source.local.AppDatabase
import com.kuro.money.data.data_source.local.preferences.AppPreferences
import com.kuro.money.data.data_source.remote.ExchangeRatesAPI
import com.kuro.money.data.repository.AccountsRepositoryImpl
import com.kuro.money.data.repository.CategoryRepositoryImpl
import com.kuro.money.data.repository.CurrencyRepositoryImpl
import com.kuro.money.data.repository.EventRepositoryImpl
import com.kuro.money.data.repository.ExchangeRatesRepositoryImpl
import com.kuro.money.data.repository.IconRepositoryImpl
import com.kuro.money.data.repository.PreferencesRepositoryImpl
import com.kuro.money.data.repository.TransactionRepositoryImpl
import com.kuro.money.domain.repository.AccountsRepository
import com.kuro.money.domain.repository.CategoryRepository
import com.kuro.money.domain.repository.CurrencyRepository
import com.kuro.money.domain.repository.EventRepository
import com.kuro.money.domain.repository.ExchangeRatesRepository
import com.kuro.money.domain.repository.IconRepository
import com.kuro.money.domain.repository.PreferencesRepository
import com.kuro.money.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideCategoryRepository(context: Context, appDatabase: AppDatabase): CategoryRepository =
        CategoryRepositoryImpl(context, appDatabase)

    @Singleton
    @Provides
    fun providePreferencesRepository(appPreferences: AppPreferences): PreferencesRepository =
        PreferencesRepositoryImpl(appPreferences)

    @Singleton
    @Provides
    fun provideAccountsRepository(context: Context, appDatabase: AppDatabase): AccountsRepository =
        AccountsRepositoryImpl(context, appDatabase)

    @Singleton
    @Provides
    fun provideEventRepository(appDatabase: AppDatabase): EventRepository =
        EventRepositoryImpl(appDatabase)

    @Singleton
    @Provides
    fun provideIconRepository(context: Context): IconRepository = IconRepositoryImpl(context)

    @Singleton
    @Provides
    fun provideCurrencyRepository(context: Context, appDatabase: AppDatabase): CurrencyRepository =
        CurrencyRepositoryImpl(context, appDatabase)

    @Singleton
    @Provides
    fun provideExchangeRateRepository(
        appDatabase: AppDatabase,
        exchangeRatesAPI: ExchangeRatesAPI
    ): ExchangeRatesRepository = ExchangeRatesRepositoryImpl(appDatabase, exchangeRatesAPI)

    @Singleton
    @Provides
    fun provideTransactionRepository(
        appDatabase: AppDatabase
    ) : TransactionRepository = TransactionRepositoryImpl(appDatabase)
}