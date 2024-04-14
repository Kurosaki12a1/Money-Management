package com.kuro.money.presenter.account.feature.wallets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.model.Currency
import com.kuro.money.data.model.Wallet
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.usecase.AccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditWalletViewModel @Inject constructor(
    private val walletUseCase: AccountsUseCase,
) : ViewModel() {
    private val _iconSelected = MutableStateFlow("icon")
    val iconSelected = _iconSelected.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _balance = MutableStateFlow<Long?>(null)
    val balance = _balance.asStateFlow()

    private val _currencySelected = MutableStateFlow<Currency?>(null)
    val currencySelected = _currencySelected.asStateFlow()

    private val _updateWallet = MutableStateFlow<Resource<Int>>(Resource.Default)
    val updateWallet = _updateWallet.asStateFlow()

    private val _deleteWallet = MutableStateFlow<Resource<Int>>(Resource.Default)
    val deleteWallet = _deleteWallet.asStateFlow()

    private val _id = MutableStateFlow(0L)
    val id = _id.asStateFlow()

    private val _uuid = MutableStateFlow("")
    val uuid = _uuid.asStateFlow()

    fun getWalletById(id: Long) {
        viewModelScope.launch {
            walletUseCase(id).collectLatest {
                if (it is Resource.Success) {
                    _uuid.value = it.value.uuid
                    _id.value = it.value.id
                    _iconSelected.value = it.value.icon
                    _name.value = it.value.name
                    _balance.value = it.value.balance.toLong()
                    _currencySelected.value = it.value.currency
                }
            }
        }
    }

    fun clearData() {
        _updateWallet.value = Resource.Default
        _deleteWallet.value = Resource.Default
    }

    fun delete() {
        viewModelScope.launch {
            walletUseCase.delete(id.value).collectLatest {
                _deleteWallet.value = it
            }
        }
    }

    fun setBalance(value: Long) {
        _balance.value = value
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setIcon(value: String) {
        _iconSelected.value = value
    }

    fun setCurrency(entity: Currency?) {
        _currencySelected.value = entity
    }

    fun save() {
        if (name.value.isEmpty() || balance.value == null || currencySelected.value == null) return
        val entity = Wallet(
            name.value,
            currencySelected.value!!.id,
            iconSelected.value,
            uuid.value,
            balance.value!!.toDouble(),
            id.value
        )
        viewModelScope.launch {
            walletUseCase.update(entity).collectLatest {
                _updateWallet.value = it
            }
        }
    }
}