package com.kuro.money.presenter.account.feature.wallets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class AddWalletViewModel @Inject constructor(
    private val accountsUseCase: AccountsUseCase
) : ViewModel() {
    private val _iconSelected = MutableStateFlow("icon")
    val iconSelected = _iconSelected.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _balance = MutableStateFlow<Long?>(null)
    val balance = _balance.asStateFlow()

    private val _currencySelected = MutableStateFlow<CurrencyEntity?>(null)
    val currencySelected = _currencySelected.asStateFlow()

    private val _insertWallet = MutableStateFlow<Resource<Long>>(Resource.Default)
    val insertWallet = _insertWallet.asStateFlow()

    fun submit() {
        if (name.value.isEmpty() || balance.value == null || currencySelected.value == null) return
        val entity = AccountEntity(
            name.value,
            currencySelected.value!!,
            iconSelected.value,
            UUID.randomUUID().toString(),
            balance.value!!.toDouble()
        )
        viewModelScope.launch {
            accountsUseCase(entity).collectLatest {
                _insertWallet.value = it
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

    fun setCurrency(entity: CurrencyEntity?) {
        _currencySelected.value = entity
    }
}