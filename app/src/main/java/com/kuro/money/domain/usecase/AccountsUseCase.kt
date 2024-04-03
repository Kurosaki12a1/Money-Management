package com.kuro.money.domain.usecase

import com.kuro.money.data.model.AccountEntity
import com.kuro.money.domain.repository.AccountsRepository
import javax.inject.Inject

class AccountsUseCase @Inject constructor(private val repo: AccountsRepository) {
    operator fun invoke(entity: AccountEntity) = repo.insertAccount(entity)

    operator fun invoke(list: List<AccountEntity>) = repo.insertAccounts(list)

    fun update(entity: AccountEntity) = repo.updateAccount(entity)

    operator fun invoke() = repo.getAllAccounts()

    operator fun invoke(jsonName: String) = repo.readFileFromJson(jsonName)

    operator fun invoke(id: Long) = repo.getAccountById(id)

    operator fun invoke(count : Int) = repo.getAccounts(count)

    fun delete(id : Long) = repo.deleteAccountById(id)
}