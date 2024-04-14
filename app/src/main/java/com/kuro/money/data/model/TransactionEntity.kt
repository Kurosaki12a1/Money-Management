package com.kuro.money.data.model

import android.net.Uri
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.kuro.money.data.model.converter.LocalDateConverter
import com.kuro.money.data.model.converter.UriConverter
import java.time.LocalDate

@Entity(tableName = "transactions")
@TypeConverters(
    LocalDateConverter::class,
    UriConverter::class,
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val currencyId: Long,
    val amount: Double,
    val createdDate: LocalDate,
    val displayDate: LocalDate,
    val categoryId: Long,
    val note: String?,
    val walletId: Long,
    val people: String? = "",
    val eventId: Long? = null,
    val remindDate: LocalDate? = null,
    val image: Uri? = null,
    val isExcludedReport: Boolean
)

data class TransactionEntity(

    @Embedded val transaction: Transaction,


    @Relation(
        parentColumn = "currencyId",
        entityColumn = "id"
    )
    val currency: Currency,


    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity,

    @Relation(
        parentColumn = "walletId",
        entityColumn = "id"
    )
    val wallet: Wallet,

    @Relation(
        parentColumn = "eventId",
        entityColumn = "id"
    )
    val event: Event? = null
) {
    @delegate:Ignore
    val amount by lazy { transaction.amount }

    @delegate:Ignore
    val note by lazy { transaction.note }

    @delegate:Ignore
    val createdDate by lazy { transaction.createdDate }

    @delegate:Ignore
    val displayDate by lazy { transaction.displayDate }

    @delegate:Ignore
    val id by lazy { transaction.id }

    @delegate:Ignore
    val people by lazy { transaction.people }

    @delegate:Ignore
    val remindDate by lazy { transaction.remindDate }

    @delegate:Ignore
    val image by lazy { transaction.image }

    @delegate:Ignore
    val isExcludedReport by lazy { transaction.isExcludedReport }
}