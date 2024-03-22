package com.kuro.money.data.mapper

import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.model.SubCategoryEntity
import com.kuro.money.domain.model.Category
import com.kuro.money.domain.model.SubCategory

fun Category.toCategoryEntity(): CategoryEntity = CategoryEntity(
    this.name ?: "",
    this.title ?: "",
    this.icon ?: "",
    this.type ?: "",
    this.metadata ?: "",
    this.subCategories?.map { it.toSubCategoryEntity() }?: listOf()
)

fun SubCategory.toSubCategoryEntity() : SubCategoryEntity = SubCategoryEntity(
    this.name ?: "",
    this.title ?: "",
    this.icon ?: "",
    this.type ?: "",
    this.metadata ?: ""
)