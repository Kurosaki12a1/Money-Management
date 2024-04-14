package com.kuro.money.domain.repository

import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.Wallet
import com.kuro.money.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AccountsRepository {
    fun insertAccount(wallet: Wallet): Flow<Resource<Long>>

    fun insertAccounts(listWallet: List<Wallet>): Flow<Resource<Long>>

    fun updateAccount(wallet: Wallet): Flow<Resource<Int>>

    fun getAllAccounts(): Flow<Resource<List<AccountEntity>>>

    fun readFileFromJson(jsonName: String): Flow<Resource<List<Wallet>?>>

    fun getAccountById(id : Long) : Flow<Resource<AccountEntity>>

    fun getAccounts(count : Int) : Flow<Resource<List<AccountEntity>>>

    fun deleteAccountById(id: Long) : Flow<Resource<Int>>
}