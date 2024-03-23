package com.kuro.money.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    val name: String,
    val icon: String,
    val uuid: String,
    val balance: Double,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L
)