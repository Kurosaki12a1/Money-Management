package com.kuro.money.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kuro.money.data.model.Event
import com.kuro.money.data.model.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: Event): Long

    @Transaction
    @Query("SELECT * FROM event")
    fun getAllEvents(): List<EventEntity>
}