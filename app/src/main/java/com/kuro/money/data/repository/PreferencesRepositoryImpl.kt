package com.kuro.money.data.repository

import com.kuro.money.data.data_source.local.preferences.AppPreferences
import com.kuro.money.domain.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val appPreferences: AppPreferences,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PreferencesRepository {
    override fun isOnBoardingDone(): Flow<Boolean> = flow {
        emit(appPreferences.isOnBoardingDone)
    }.flowOn(dispatcher)

    override fun isFirstTimeOpenApp(): Flow<Boolean> = flow {
        emit(appPreferences.isFirstTimeOpenApp)
    }.flowOn(dispatcher)

    override fun setFirstTimeOpen(value: Boolean): Flow<Boolean> = flow {
        appPreferences.isFirstTimeOpenApp = value
        emit(appPreferences.isFirstTimeOpenApp)
    }.flowOn(dispatcher)

    override fun setOnBoardingDone(value: Boolean): Flow<Boolean> = flow {
        appPreferences.isOnBoardingDone = value
        emit(appPreferences.isOnBoardingDone)
    }.flowOn(dispatcher)

    override fun getDefaultCurrency(): Flow<String> = flow {
        emit(appPreferences.defaultCurrency)
    }.flowOn(dispatcher)

    override fun setDefaultCurrency(value : String): Flow<String> = flow {
        appPreferences.defaultCurrency = value
        emit(appPreferences.defaultCurrency)
    }.flowOn(dispatcher)


}