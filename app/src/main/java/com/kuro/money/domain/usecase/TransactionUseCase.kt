package com.kuro.money.domain.usecase

import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionUseCase @Inject constructor(
    private val repo: TransactionRepository
) {
    operator fun invoke(entity: TransactionEntity) = repo.insert(entity)
    fun update(entity: TransactionEntity) = repo.update(entity)

    fun getTransactionById(id: Long) = repo.getTransactionById(id)

    operator fun invoke(id: Long) = repo.deleteTransactionById(id)
    operator fun invoke() = repo.getListTransactions()
}