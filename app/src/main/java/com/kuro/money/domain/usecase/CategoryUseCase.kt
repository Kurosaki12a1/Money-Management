package com.kuro.money.domain.usecase

import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryUseCase @Inject constructor(private val repo : CategoryRepository) {
    operator fun invoke(entity : CategoryEntity) = repo.insert(entity)

    operator fun invoke(listEntity : List<CategoryEntity>) = repo.insert(listEntity)

    operator fun invoke() = repo.loadAll()

    operator fun invoke(jsonName : String) = repo.readFileFromJson(jsonName)
}