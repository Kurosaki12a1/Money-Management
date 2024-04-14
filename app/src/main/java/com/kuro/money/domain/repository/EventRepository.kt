package com.kuro.money.domain.repository

import com.kuro.money.data.model.Event
import com.kuro.money.data.model.EventEntity
import com.kuro.money.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun insert(event: Event): Flow<Resource<Long>>

    fun getAllEvents(): Flow<Resource<List<EventEntity>?>>
}