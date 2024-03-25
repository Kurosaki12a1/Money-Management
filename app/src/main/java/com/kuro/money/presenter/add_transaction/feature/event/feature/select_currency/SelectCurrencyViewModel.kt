package com.kuro.money.presenter.add_transaction.feature.event.feature.select_currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.model.CurrencyEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.usecase.CurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCurrencyViewModel @Inject constructor(
    private val currenciesUseCase: CurrenciesUseCase
) : ViewModel() {
    private val _getListCurrencies =
        MutableStateFlow<Resource<List<CurrencyEntity>>>(Resource.Default)
    val getListCurrencies = _getListCurrencies.asStateFlow()

    init {
        getListCurrencies()
    }

    private fun getListCurrencies() {
        viewModelScope.launch {
            currenciesUseCase().collectLatest {
                _getListCurrencies.value = it
            }
        }
    }
}