package com.kuro.money.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    fun isOnBoardingDone(): Flow<Boolean>

    fun isFirstTimeOpenApp(): Flow<Boolean>

    fun setOnBoardingDone(value: Boolean): Flow<Boolean>

    fun setFirstTimeOpen(value: Boolean): Flow<Boolean>
}