package com.kuro.money.presenter.account.feature.wallets

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
class WalletViewModel @Inject constructor(
    private val walletUseCase: AccountsUseCase,
) : ViewModel() {
    private val _getWallets = MutableStateFlow<Resource<List<AccountEntity>?>>(Resource.Default)
    val getWallets = _getWallets.asStateFlow()

    private val _deleteWallet = MutableStateFlow<Resource<Int>>(Resource.Default)
    val deleteWallet = _deleteWallet.asStateFlow()

    fun getWallets() {
        viewModelScope.launch {
            walletUseCase().collectLatest {
                _getWallets.value = it
            }
        }
    }

    fun deleteWalletById(id: Long) {
        viewModelScope.launch {
            walletUseCase.delete(id).collectLatest {
                _deleteWallet.value = it
            }
        }
    }

}