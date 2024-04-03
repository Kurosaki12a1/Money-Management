package com.kuro.money.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
)
data class CategoryEntity(
    val name: String,
    val title: String,
    val icon: String,
    val type: String,
    val metadata: String,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
) {
    @Ignore var subCategories: List<SubCategoryEntity> = emptyList()
}

@Entity(
    tableName = "sub_categories",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("parentId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["parentId"])]
)
data class SubCategoryEntity(
    val parentId: Long,
    val name: String, // Primary Key
    val title: String,
    val icon: String,
    val type: String,
    val metadata: String,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
)

