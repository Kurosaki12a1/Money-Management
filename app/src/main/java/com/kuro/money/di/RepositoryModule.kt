package com.kuro.money.di

import android.content.Context
import com.kuro.money.data.data_source.local.AppDatabase
import com.kuro.money.data.repository.CategoryRepositoryImpl
import com.kuro.money.domain.repository.CategoryRepository
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
}