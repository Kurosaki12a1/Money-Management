package com.kuro.money.domain.usecase

import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.Wallet
import com.kuro.money.domain.repository.AccountsRepository
import javax.inject.Inject

class AccountsUseCase @Inject constructor(private val repo: AccountsRepository) {
    operator fun invoke(wallet: Wallet) = repo.insertAccount(wallet)

    operator fun invoke(listWallet: List<Wallet>) = repo.insertAccounts(listWallet)

    fun update(wallet: Wallet) = repo.updateAccount(wallet)

    operator fun invoke() = repo.getAllAccounts()

    operator fun invoke(jsonName: String) = repo.readFileFromJson(jsonName)

    operator fun invoke(id: Long) = repo.getAccountById(id)

    operator fun invoke(count : Int) = repo.getAccounts(count)

    fun delete(id : Long) = repo.deleteAccountById(id)
}