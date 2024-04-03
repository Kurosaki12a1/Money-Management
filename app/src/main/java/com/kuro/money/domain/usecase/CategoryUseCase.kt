package com.kuro.money.domain.usecase

import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class CategoryUseCase @Inject constructor(private val repo: CategoryRepository) {
    operator fun invoke(entity: CategoryEntity) = repo.insert(entity)

    operator fun invoke(listEntity: List<CategoryEntity>) = repo.insert(listEntity)

    fun getAllCategories() = repo.getAllCategories()

    fun getAllSubCategories() = repo.getAllSubCategories()

    operator fun invoke(jsonName: String) = repo.readFileFromJson(jsonName)

    fun isNameExist(name: String) =
        combine(repo.isNameCategoryExist(name), repo.isNameSubCategoryExist(name)) { isCategoryExist, isSubCategoryExist ->
            when {
                isCategoryExist is Resource.Success && isCategoryExist.value -> Resource.Success(true)
                isSubCategoryExist is Resource.Success && isSubCategoryExist.value -> Resource.Success(true)
                isCategoryExist is Resource.Loading || isSubCategoryExist is Resource.Loading -> Resource.Loading
                isCategoryExist is Resource.Failure -> isCategoryExist
                isSubCategoryExist is Resource.Failure -> isSubCategoryExist
                else -> Resource.Success(false)
            }
        }
}