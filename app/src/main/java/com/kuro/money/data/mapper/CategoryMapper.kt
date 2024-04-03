package com.kuro.money.data.mapper

import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.model.SubCategoryEntity
import com.kuro.money.domain.model.Category
import com.kuro.money.domain.model.SubCategory

fun Category.toCategoryEntity(): CategoryEntity = CategoryEntity(
    id = id ?: 0L,
    name = name ?: "",
    title = title ?: "",
    icon = icon ?: "",
    type = type ?: "",
    metadata = metadata ?: ""
).also { it.subCategories = subCategories?.map { it.toSubCategoryEntity() } ?: listOf() }

fun SubCategory.toSubCategoryEntity(): SubCategoryEntity = SubCategoryEntity(
    id = id ?: 0L,
    parentId = parentId ?: 0L,
    name = name ?: "",
    title = title ?: "",
    icon = icon ?: "",
    type = type ?: "",
    metadata = metadata ?: ""
)