package com.kuro.money.presenter.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.mapper.toExchangeRateEntity
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.ScreenSelection
import com.kuro.money.domain.usecase.AccountsUseCase
import com.kuro.money.domain.usecase.CategoryUseCase
import com.kuro.money.domain.usecase.CurrenciesUseCase
import com.kuro.money.domain.usecase.ExchangeRatesUseCase
import com.kuro.money.domain.usecase.PreferencesUseCase
import com.kuro.money.domain.usecase.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val categoryUseCase: CategoryUseCase,
    private val accountUseCase: AccountsUseCase,
    private val currenciesUseCase: CurrenciesUseCase,
    private val preferencesUseCase: PreferencesUseCase,
    private val exchangeRatesUseCase: ExchangeRatesUseCase,
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {
    private val _navigateScreenTo = MutableStateFlow(ScreenSelection.MAIN_SCREEN)
    val navigateScreenTo = _navigateScreenTo.asStateFlow()

    private val _getListTransaction = MutableStateFlow<Resource<List<TransactionEntity>?>>(Resource.Default)
    val getListTransaction = _getListTransaction.asStateFlow()

    init {
        getAndInsertCategoriesFromJson()
        getAndInsertExchangeRatesFromInternet()
        getListTransaction()
    }

    fun getListTransaction() {
        viewModelScope.launch {
            transactionUseCase().collectLatest {
                _getListTransaction.value = it
            }
        }
    }

    fun setOpenAddTransactionScreen(value: Boolean) {
        _navigateScreenTo.value =
            if (value) ScreenSelection.ADD_TRANSACTION_SCREEN else ScreenSelection.MAIN_SCREEN
    }

    private fun getAndInsertExchangeRatesFromInternet() {
        viewModelScope.launch {
            exchangeRatesUseCase.getExchangeRatesResponse("USD").flatMapLatest {
                when (it) {
                    is Resource.Success -> {
                        exchangeRatesUseCase(it.value.toExchangeRateEntity())
                    }
                    else -> flowOf(it)
                }
            }.collectLatest {
            }
        }
    }

    private fun getAndInsertCategoriesFromJson() {
        viewModelScope.launch {
            preferencesUseCase.isFirstTimeOpenApp().first().let { isFirstTimeOpen ->
                if (isFirstTimeOpen) {
                    val insertCategory = categoryUseCase("categories.json").flatMapLatest {
                        when (it) {
                            is Resource.Success -> {
                                if (it.value.isNullOrEmpty()) flowOf(Resource.failure(Exception("No data from Json")))
                                else categoryUseCase(it.value)
                            }

                            else -> flowOf(it) as Flow<Resource<Long>>
                        }
                    }
                    val insertAccount = accountUseCase("accounts.json").flatMapLatest {
                        when (it) {
                            is Resource.Success -> {
                                if (it.value.isNullOrEmpty()) flowOf(Resource.failure(Exception("No data from JSON")))
                                else accountUseCase(it.value)
                            }

                            else -> flowOf(it) as Flow<Resource<Long>>
                        }
                    }
                    val insertCurrencies =
                        currenciesUseCase.getListCurrenciesFromJSON().flatMapLatest {
                            when (it) {
                                is Resource.Success -> {
                                    if (it.value.isEmpty()) flowOf(Resource.failure(Exception("No data from JSON")))
                                    else currenciesUseCase(it.value)
                                }

                                else -> flowOf(it)
                            }
                        }
                    insertCategory
                        .zip(insertAccount) { _, _ -> }
                        .zip(insertCurrencies) { _, _ -> }
                        .collectLatest {
                            preferencesUseCase.setFirstTimeOpenApp(false).collectLatest { }
                        }
                }
            }
        }
    }
}