package com.kuro.money.presenter.account.feature.wallets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.CurrencyEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.usecase.AccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletUseCase: AccountsUseCase,
) : ViewModel() {
    private val _getWallets = MutableStateFlow<Resource<List<AccountEntity>?>>(Resource.Default)
    val getWallets = _getWallets.asStateFlow()

    private val _getDetailWallet = MutableStateFlow<Resource<AccountEntity?>>(Resource.Default)
    val getWalletDetails = _getDetailWallet.asStateFlow()

    fun getWallets() {
        viewModelScope.launch {
            walletUseCase().collectLatest {
                _getWallets.value = it
            }
        }
    }

    fun clearData() {
        _getDetailWallet.value = Resource.Default
    }

    fun getWalletById(id : Long) {
        viewModelScope.launch {
            walletUseCase(id).collectLatest {
                _getDetailWallet.value = it
            }
        }
    }
}