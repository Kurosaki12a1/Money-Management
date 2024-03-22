package com.kuro.money.di

import android.app.Application
import android.content.Context
import com.kuro.money.data.data_source.local.preferences.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    fun provideAppPreferences(context: Context) = AppPreferences(context)
}