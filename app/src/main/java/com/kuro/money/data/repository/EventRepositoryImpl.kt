package com.kuro.money.data.repository

import com.kuro.money.data.data_source.local.AppDatabase
import com.kuro.money.data.model.Event
import com.kuro.money.data.model.EventEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.repository.EventRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : EventRepository {
    override fun insert(event: Event): Flow<Resource<Long>> = flow {
        emit(Resource.Loading)
        try {
            val id = appDatabase.eventDao().insert(event)
            emit(Resource.success(id))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)

    override fun getAllEvents(): Flow<Resource<List<EventEntity>?>> = flow {
        emit(Resource.Loading)
        try {
            val listData = appDatabase.eventDao().getAllEvents()
            emit(Resource.success(listData))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.failure(e, e.message))
        }
    }.flowOn(dispatcher)
}