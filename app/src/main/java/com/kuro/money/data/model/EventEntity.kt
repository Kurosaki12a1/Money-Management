package com.kuro.money.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.kuro.money.data.model.converter.LocalDateConverter
import java.time.LocalDate

@Entity(tableName = "event")
@TypeConverters(
    LocalDateConverter::class,
)
data class Event(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val icon: String,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val currencyId: Long,
    val walletId: Long
)

data class EventEntity(
    @Embedded val event: Event,

    @Relation(
        parentColumn = "currencyId",
        entityColumn = "id"
    )
    val currency: Currency,

    @Relation(
        parentColumn = "walletId",
        entityColumn = "id"
    )
    val wallet: Wallet
) {
    @delegate:Ignore
    val name by lazy { event.name }

    @delegate:Ignore
    val icon by lazy { event.icon }

    @delegate:Ignore
    val startDate by lazy { event.startDate }

    @delegate:Ignore
    val endDate by lazy { event.endDate }

    @delegate:Ignore
    val id by lazy { event.id }
}
