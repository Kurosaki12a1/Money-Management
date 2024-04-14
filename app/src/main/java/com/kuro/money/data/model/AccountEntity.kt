package com.kuro.money.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "accounts"
)
data class Wallet(
    val name: String,
    val currencyId: Long,
    val icon: String,
    val uuid: String,
    val balance: Double,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L
)

data class AccountEntity(

    @Embedded val account: Wallet,

    @Relation(
        parentColumn = "currencyId",
        entityColumn = "id"
    )
    val currency: Currency
) {
    @delegate:Ignore
    val name by lazy { account.name }

    @delegate:Ignore
    val icon by lazy { account.icon }

    @delegate:Ignore
    val uuid by lazy { account.uuid }

    @delegate:Ignore
    val balance by lazy { account.balance }

    @delegate:Ignore
    val id by lazy { account.id }
}