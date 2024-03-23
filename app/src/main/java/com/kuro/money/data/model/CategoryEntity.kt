package com.kuro.money.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kuro.money.data.model.converter.ArrayListCategoryConverter

@Entity(tableName = "categories")
@TypeConverters(ArrayListCategoryConverter::class)
data class CategoryEntity(
    val name: String,
    val title: String,
    val icon: String,
    val type: String,
    val metadata: String,
    val subCategories: List<SubCategoryEntity>,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L
)

data class SubCategoryEntity(
    val name: String, // Primary Key
    val title: String,
    val icon: String,
    val type: String,
    val metadata: String,
)