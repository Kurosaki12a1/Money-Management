package com.kuro.money.di

import com.kuro.money.domain.repository.AccountsRepository
import com.kuro.money.domain.repository.CategoryRepository
import com.kuro.money.domain.repository.PreferencesRepository
import com.kuro.money.domain.usecase.AccountsUseCase
import com.kuro.money.domain.usecase.CategoryUseCase
import com.kuro.money.domain.usecase.PreferencesUseCase
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
    fun providePreferencesUseCase(repo : PreferencesRepository) = PreferencesUseCase(repo)

    @Provides
    @Singleton
    fun provideAccountUseCase(repo : AccountsRepository) = AccountsUseCase(repo)
}