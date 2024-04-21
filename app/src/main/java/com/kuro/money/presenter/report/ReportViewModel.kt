package com.kuro.money.presenter.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.constants.Constants
import com.kuro.money.data.AppCache
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.model.Wallet
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.usecase.AccountsUseCase
import com.kuro.money.domain.usecase.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val accountsUseCase: AccountsUseCase,
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {

    private val _balance = MutableStateFlow(0.0)
    val balance = _balance.asStateFlow()

    private val _globalWallet = MutableStateFlow<AccountEntity?>(null)
    val globalWallet = _globalWallet.asStateFlow()

    private val _allWallets = MutableStateFlow<Resource<List<AccountEntity>>>(Resource.Default)
    val allWallets = _allWallets.asStateFlow()

    private val _selectedWallet = MutableStateFlow<AccountEntity?>(null)
    val selectedWallet = _selectedWallet.asStateFlow()

    private val _monthList = MutableStateFlow<List<String>>(emptyList())
    val monthList = _monthList.asStateFlow()

    private val _monthSelected =
        MutableStateFlow<String>(DateTimeFormatter.ofPattern("MM/yyyy").format(LocalDate.now()))
    val monthSelected = _monthSelected.asStateFlow()

    private val _transactionSelected =
        MutableStateFlow<Resource<List<TransactionEntity>>>(Resource.Default)
    val transactionSelected = _transactionSelected.asStateFlow()

    init {
        generateMonthList()
        setMonthSelected(LocalDate.now())
    }

    fun getAllWallets() {
        viewModelScope.launch {
            accountsUseCase().collectLatest {
                _allWallets.value = it
            }
        }
    }

    private fun getTransactionByMonth(monthYear: String) {
        val indexMonth = monthList.value.indexOf(monthYear)
        if (indexMonth == 0) {
            viewModelScope.launch {
                transactionUseCase.getTransactionByMonth(monthYear).collectLatest {
                    _transactionSelected.value = it
                }
            }
        } else {
            viewModelScope.launch {
                transactionUseCase
                    .getTransactionByMonth(monthList.value[indexMonth - 1]).zip(
                        transactionUseCase.getTransactionByMonth(monthYear)
                    ) { first, second ->
                        when {
                            first is Resource.Success && second is Resource.Success -> {
                                Resource.Success(first.value + second.value)
                            }

                            first is Resource.Success -> Resource.Success(first.value)
                            second is Resource.Success -> Resource.success(second.value)
                            first is Resource.Loading || second is Resource.Loading -> Resource.Loading
                            first is Resource.Failure || second is Resource.Failure -> Resource.Failure(
                                Exception(), "Failed to get transaction"
                            )

                            else -> Resource.Default
                        }
                    }.collectLatest {
                        _transactionSelected.value = it
                    }
            }
        }
    }

    fun setMonthSelected(monthYear: String) {
        if (_monthSelected.value == monthYear) return
        _monthSelected.value = monthYear
        getTransactionByMonth(monthYear)
    }

    private fun setMonthSelected(date: LocalDate) {
        _monthSelected.value = DateTimeFormatter.ofPattern("MM/yyyy").format(date)
        getTransactionByMonth(_monthSelected.value)
    }

    fun setSelectedWallet(wallet: AccountEntity) {
        _selectedWallet.value = wallet
    }

    fun getBalance() {
        viewModelScope.launch {
            AppCache.listRates.collectLatest { listRates ->
                accountsUseCase().collectLatest { data ->
                    if (data is Resource.Success) {
                        _balance.value = 0.0
                        data.value.forEach { wallet ->
                            val rates = listRates[AppCache.defaultCurrency.value]?.find { rates ->
                                rates.currencyCode.lowercase() == wallet.currency.code.lowercase()
                            }?.rate
                            // rates is 1 unit of base code equal certain value of currency code
                            // So we must use 1/ rates.
                            _balance.value += if (rates != null) 1 / rates * wallet.balance else wallet.balance
                        }
                        createGlobalWallet()
                    }
                }
            }
        }
    }


    private fun createGlobalWallet() {
        viewModelScope.launch {
            AppCache.defaultCurrencyEntity.collectLatest {
                if (it != null) {
                    val wallet = Wallet(
                        id = Constants.GLOBAL_WALLET_ID,
                        name = Constants.GLOBAL_WALLET_NAME,
                        icon = Constants.GLOBAL_WALLET_ICON,
                        uuid = Constants.GLOBAL_WALLET_UUID,
                        balance = _balance.value,
                        currencyId = it.id
                    )
                    _globalWallet.value =
                        AccountEntity(
                            account = wallet,
                            currency = it
                        )
                }
            }
        }
    }

    private fun generateMonthList() {
        val currentMonth = LocalDate.now()
        val monthList = List(18) { i ->
            currentMonth.minusMonths(i.toLong()).format(DateTimeFormatter.ofPattern("MM/yyyy"))
        }.reversed()

        _monthList.value = monthList
    }
}