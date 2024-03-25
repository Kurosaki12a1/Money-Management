package com.kuro.money.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.kuro.money.data.data_source.local.AppDatabase
import com.kuro.money.data.data_source.local.dao.AccountsDao
import com.kuro.money.data.data_source.local.dao.CategoryDao
import com.kuro.money.data.data_source.local.dao.CurrencyDao
import com.kuro.money.data.data_source.local.dao.EventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application, AppDatabase::class.java, AppDatabase.DB_NAME
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao = appDatabase.categoryDao()

    @Provides
    fun provideAccountsDao(appDatabase: AppDatabase): AccountsDao = appDatabase.accountsDao()

    @Provides
    fun provideCurrencyDao(appDatabase: AppDatabase) : CurrencyDao = appDatabase.currencyDao()

    @Provides
    fun provideEventDao(appDatabase : AppDatabase) : EventDao = appDatabase.eventDao()
}