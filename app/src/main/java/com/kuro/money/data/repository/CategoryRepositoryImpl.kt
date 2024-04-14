package com.kuro.money.data.repository

import android.content.Context
import com.kuro.money.data.data_source.local.AppDatabase
import com.kuro.money.data.mapper.toCategoryEntity
import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.utils.FileUtils
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.Category
import com.kuro.money.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val context: Context,
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CategoryRepository {
    override fun insert(entity: CategoryEntity): Flow<Resource<Long>> = flow {
        emit(Resource.Loading)
        try {
            val data = appDatabase.categoryDao().insert(entity)
            emit(Resource.success(data))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun insert(list: List<CategoryEntity>): Flow<Resource<Long>> = flow {
        emit(Resource.Loading)
        try {
            var id = 0L // default for no insertion or insert empty list?
            list.forEach {
                // Means no same name
                if (appDatabase.categoryDao().countByName(it.name) == 0) {
                    id = appDatabase.categoryDao().insert(it)
                }
            }
            // Emit last id from list
            emit(Resource.success(id))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun isNameCategoryExist(name: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        try {
            val isNameExist = appDatabase.categoryDao().countByName(name)
            emit(Resource.success(isNameExist > 0))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun getAllCategories(): Flow<Resource<List<CategoryEntity>>> = flow {
        emit(Resource.Loading)
        try {
            val data = appDatabase.categoryDao().loadAll()
            emit(Resource.success(data))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun readFileFromJson(jsonName: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        try {
            val data = FileUtils.loadJsonFromAsset<List<Category>>(context, jsonName)
            data?.forEach { category ->
                appDatabase.categoryDao().insert(category.toCategoryEntity())
                category.subCategories?.forEach { subCategory ->
                    appDatabase.categoryDao().insert(subCategory.toCategoryEntity())
                }
            }
            emit(Resource.success(true))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)


}