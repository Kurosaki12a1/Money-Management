package com.kuro.money.domain.usecase

import com.kuro.money.data.model.EventEntity
import com.kuro.money.domain.repository.EventRepository
import javax.inject.Inject

class EventUseCase @Inject constructor(private val repo: EventRepository) {
    operator fun invoke(event : EventEntity) = repo.insert(event)

    operator fun invoke() = repo.getAllEvents()
}