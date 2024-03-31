package com.kuro.money.presenter.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.AppCache
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
class HomeViewModel @Inject constructor(
    private val accountsUseCase: AccountsUseCase
) : ViewModel() {

    private val _allWallets = MutableStateFlow<Resource<List<AccountEntity>>>(Resource.Default)
    val allWallets = _allWallets.asStateFlow()

    private val _balance = MutableStateFlow(0.0)
    val balance = _balance.asStateFlow()

    fun getBalance() {
        viewModelScope.launch {
            AppCache.listRates.collectLatest { listRates ->
                accountsUseCase().collectLatest { data ->
                    if (data is Resource.Success) {
                        _balance.value = 0.0
                        data.value.forEach { wallet ->
                            val rates = listRates[AppCache.defaultCurrency.value]?.find { rates ->
                                rates.currencyCode.lowercase() == wallet.currencyEntity.code.lowercase()
                            }?.rate
                            // rates is 1 unit of base code equal certain value of currency code
                            // So we must use 1/ rates.
                            _balance.value += if (rates != null) 1 / rates * wallet.balance else wallet.balance
                        }
                    }
                }
            }
        }
    }

    fun getAllWallets() {
        viewModelScope.launch {
            accountsUseCase().collectLatest {
                _allWallets.value = it
            }
        }
    }
}