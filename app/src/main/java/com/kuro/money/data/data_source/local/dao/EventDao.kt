package com.kuro.money.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kuro.money.data.model.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: EventEntity): Long

    @Query("SELECT * FROM event")
    fun getAllEvents(): List<EventEntity>
}