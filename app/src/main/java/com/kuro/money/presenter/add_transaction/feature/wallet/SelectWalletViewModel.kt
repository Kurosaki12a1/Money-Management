package com.kuro.money.presenter.add_transaction.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.usecase.AccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectWalletViewModel @Inject constructor(
    private val accountsUseCase: AccountsUseCase
) : ViewModel() {
    private val _getAccountsUseCase = MutableStateFlow<Resource<List<AccountEntity>>>(Resource.Default)
    val getAccountUseCase = _getAccountsUseCase.asStateFlow()

    init {
        getAllWallets()
    }

    private fun getAllWallets(){
        viewModelScope.launch {
            accountsUseCase().collectLatest {
                _getAccountsUseCase.value = it
            }
        }
    }
}