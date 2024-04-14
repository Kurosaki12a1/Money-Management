package com.kuro.money.domain.usecase

import com.kuro.money.data.model.Transaction
import com.kuro.money.domain.repository.TransactionRepository
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
}