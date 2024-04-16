package com.kuro.money.presenter.transactions.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.constants.Constants
import com.kuro.money.data.AppCache
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.Wallet
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.AdvancedSearchAmount
import com.kuro.money.domain.model.AdvancedSearchCategory
import com.kuro.money.domain.model.AdvancedSearchTime
import com.kuro.money.domain.usecase.AccountsUseCase
import com.kuro.money.domain.usecase.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdvanceSearchViewModel @Inject constructor(
    private val accountsUseCase: AccountsUseCase,
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {
    private val _walletSelected = MutableStateFlow(Constants.ALL_WALLETS)
    val walletSelected = _walletSelected.asStateFlow()

    private val _allWallets = MutableStateFlow<Resource<List<AccountEntity>>>(Resource.Default)
    val allWallets = _allWallets.asStateFlow()

    private val _categorySelected = MutableStateFlow(AdvancedSearchCategory.ALL_CATEGORIES)
    val categorySelected = _categorySelected.asStateFlow()

    private val _timeSelected = MutableStateFlow<AdvancedSearchTime>(AdvancedSearchTime.All)
    val timeSelected = _timeSelected.asStateFlow()

    private val _amountSelected = MutableStateFlow<AdvancedSearchAmount>(AdvancedSearchAmount.All)
    val amountSelected = _amountSelected.asStateFlow()

    private val _noteSelected = MutableStateFlow("")
    val noteSelected = _noteSelected.asStateFlow()

    private val _withSelected = MutableStateFlow("")
    val withSelected = _withSelected.asStateFlow()

    private val _balance = MutableStateFlow(0.0)
    val balance = _balance.asStateFlow()

    private val _globalWallet = MutableStateFlow<AccountEntity?>(null)
    val globalWallet = _globalWallet.asStateFlow()

    fun getAllWalletsAndBalance() {
        viewModelScope.launch {
            AppCache.listRates.collectLatest { listRates ->
                accountsUseCase().collectLatest { data ->
                    _allWallets.value = data
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

    fun setWallet(walletName: String) {
        _walletSelected.value = walletName
    }

    fun setCategory(category: AdvancedSearchCategory) {
        _categorySelected.value = category
    }

    fun setTime(time: AdvancedSearchTime) {
        _timeSelected.value = time
    }

    fun setAmount(amount: AdvancedSearchAmount) {
        _amountSelected.value = amount
    }

    fun setNote(note: String) {
        _noteSelected.value = note
    }

    fun setWith(with: String) {
        _withSelected.value = with
    }

    fun querySearch() {

    }


}