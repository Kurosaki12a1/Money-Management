package com.kuro.money.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.model.SubCategoryEntity

@Dao
interface SubCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(subCategory: SubCategoryEntity): Long

    @Query("SELECT COUNT(*) FROM sub_categories Where name =:name")
    fun countByName(name: String): Int

    @Query("SELECT * FROM sub_categories")
    fun loadAll(): List<SubCategoryEntity>
}