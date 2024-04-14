package com.kuro.money.data.model

import androidx.room.Entity
import androidx.room.Ignore
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
    var parentId: Long = 0L,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
) {
    @Ignore var subCategories : List<CategoryEntity> = emptyList()
}
