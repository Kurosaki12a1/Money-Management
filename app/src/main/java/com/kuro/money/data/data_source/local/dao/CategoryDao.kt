package com.kuro.money.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuro.money.data.model.CategoryEntity

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: CategoryEntity) : Long

    @Query("SELECT COUNT(*) FROM categories Where name =:name")
    fun countByName(name: String) : Int

    @Query("SELECT * FROM categories")
    fun loadAll(): List<CategoryEntity>
}