package com.kuro.money.domain.usecase

import com.kuro.money.data.model.Transaction
import com.kuro.money.domain.model.AdvancedSearchAmount
import com.kuro.money.domain.model.AdvancedSearchCategory
import com.kuro.money.domain.model.AdvancedSearchTime
import com.kuro.money.domain.model.TimeRange
import com.kuro.money.domain.repository.TransactionRepository
import java.time.LocalDate
import javax.inject.Inject

class TransactionUseCase @Inject constructor(
    private val repo: TransactionRepository
) {
    operator fun invoke(entity: Transaction) = repo.insert(entity)
    fun update(entity: Transaction) = repo.update(entity)

    fun getTransactionById(id: Long) = repo.getTransactionById(id)

    operator fun invoke(id: Long) = repo.deleteTransactionById(id)
    operator fun invoke() = repo.getListTransactions()

    fun getTransactionByMonth(monthYear: String) = repo.getTransactionByMonthYear(monthYear)

    operator fun invoke(
        amount: AdvancedSearchAmount,
        walletName: String,
        time: AdvancedSearchTime,
        note: String,
        category: AdvancedSearchCategory,
        with: String
    ) = repo.queryTransaction(amount, walletName, time, note, category, with)

    operator fun invoke(startDate: LocalDate, endDate: LocalDate) =
        repo.getTransactionsBetweenDates(startDate, endDate)

    operator fun invoke(timeRange: TimeRange) = repo.queryTransactionWithTimeRange(timeRange)
}