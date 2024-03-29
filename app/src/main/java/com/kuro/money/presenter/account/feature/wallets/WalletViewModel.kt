package com.kuro.money.presenter.account.feature.wallets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.CurrencyEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.ScreenSelection
import com.kuro.money.domain.usecase.AccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletUseCase: AccountsUseCase
) : ViewModel() {
    private val _getWallets = MutableStateFlow<Resource<List<AccountEntity>?>>(Resource.Default)
    val getWallets = _getWallets.asStateFlow()

    private val _iconSelected = MutableStateFlow("icon")
    val iconSelected = _iconSelected.asStateFlow()

    private val _currencySelected = MutableStateFlow<CurrencyEntity?>(null)
    val currencySelected = _currencySelected.asStateFlow()

    init {
        getWallets()
    }

    fun clearData() {
        _iconSelected.value = "icon"
        _currencySelected.value = null
    }

    fun setIcon(value: String) {
        _iconSelected.value = value
    }

    fun setCurrency(entity: CurrencyEntity?) {
        _currencySelected.value = entity
    }

    fun getWallets() {
        viewModelScope.launch {
            walletUseCase().collectLatest {
                _getWallets.value = it
            }
        }
    }
}