package com.kuro.money.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kuro.money.data.model.converter.AccountConverter
import com.kuro.money.data.model.converter.EventConverter
import com.kuro.money.data.model.converter.LocalDateConverter
import com.kuro.money.data.model.converter.UriConverter
import java.time.LocalDate

@Entity(tableName = "transactions")
@TypeConverters(
    LocalDateConverter::class,
    AccountConverter::class,
    EventConverter::class,
    UriConverter::class
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val amount: Double,
    val createdDate: LocalDate,
    val displayDate: LocalDate,
    val category: String,
    val note: String?,
    val wallet: AccountEntity,
    val people: String? = "",
    val event: EventEntity? = null,
    val remindDate: LocalDate? = null,
    val image: Uri? = null,
    val isExcludedReport: Boolean
)