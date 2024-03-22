package com.kuro.money.di

import com.kuro.money.domain.repository.CategoryRepository
import com.kuro.money.domain.usecase.CategoryUseCase
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
}