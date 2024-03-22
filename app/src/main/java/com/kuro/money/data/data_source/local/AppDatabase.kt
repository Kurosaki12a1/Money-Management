package com.kuro.money.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuro.money.data.data_source.local.dao.CategoryDao
import com.kuro.money.data.model.CategoryEntity

@Database(entities = [CategoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "Money.db"
    }

    abstract fun categoryDao(): CategoryDao

}