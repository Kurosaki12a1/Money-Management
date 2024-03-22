package com.kuro.money.domain.repository

import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun insert(entity: CategoryEntity): Flow<Resource<Long>>

    fun insert(list: List<CategoryEntity>): Flow<Resource<Long>>

    fun isNameExist(name: String): Flow<Resource<Boolean>>

    fun loadAll(): Flow<Resource<List<CategoryEntity>>>

    fun readFileFromJson(jsonName: String): Flow<Resource<List<CategoryEntity>?>>
}