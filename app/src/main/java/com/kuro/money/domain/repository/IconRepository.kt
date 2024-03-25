package com.kuro.money.domain.repository

import kotlinx.coroutines.flow.Flow

interface IconRepository {
    fun getListIconFromJSON() : Flow<List<String>>
}