package com.kuro.money.data.mapper

import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.domain.model.Category


fun Category.toCategoryEntity(): CategoryEntity = CategoryEntity(
    id = id ?: 0L,
    parentId = parentId ?: 0L,
    name = name ?: "",
    title = title ?: "",
    icon = icon ?: "",
    type = type ?: "",
    metadata = metadata ?: ""
)