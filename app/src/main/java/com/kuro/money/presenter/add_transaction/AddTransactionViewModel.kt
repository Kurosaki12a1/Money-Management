package com.kuro.money.presenter.add_transaction

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.constants.Constants
import com.kuro.money.data.AppCache
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.model.Currency
import com.kuro.money.data.model.Event
import com.kuro.money.data.model.Transaction
import com.kuro.money.data.model.Wallet
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.usecase.AccountsUseCase
import com.kuro.money.domain.usecase.CurrenciesUseCase
import com.kuro.money.domain.usecase.PreferencesUseCase
import com.kuro.money.domain.usecase.TransactionUseCase
import com.kuro.money.presenter.utils.calculateWalletAfterTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase,
    private val accountsUseCase: AccountsUseCase,
    private val currenciesUseCase: CurrenciesUseCase,
    private val preferencesUseCase: PreferencesUseCase,
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow<CategoryEntity?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _wallet = MutableStateFlow<AccountEntity?>(null)
    val wallet = _wallet.asStateFlow()

    private val _nameOfPeople = MutableStateFlow<String?>(null)
    val nameOfPeople = _nameOfPeople.asStateFlow()

    private val _eventSelected = MutableStateFlow<Event?>(null)
    val eventSelected = _eventSelected.asStateFlow()

    private val _dateTransaction = MutableStateFlow<LocalDate>(LocalDate.now())
    val dateTransaction = _dateTransaction.asStateFlow()

    private val _dateRemind = MutableStateFlow<LocalDate?>(null)
    val dateRemind = _dateRemind.asStateFlow()

    private val _note = MutableStateFlow("")
    val note = _note.asStateFlow()

    private val _uriSelected = MutableStateFlow<Uri?>(null)
    val uriSelected = _uriSelected.asStateFlow()

    private val _currencySelected = MutableStateFlow<Currency?>(null)
    val currencySelected = _currencySelected.asStateFlow()

    private val _amount = MutableStateFlow<String?>(null)
    val amount = _amount.asStateFlow()

    private val _insertTransaction = MutableStateFlow<Resource<Long>>(Resource.Default)
    val insertTransaction = _insertTransaction.asStateFlow()

    private val _bitmapFlow = MutableStateFlow<Bitmap?>(null)
    val bitmapFlow = _bitmapFlow.asStateFlow()

    init {
        initCurrencySelected()
    }

    fun setBitmap(bitmap: Bitmap?) {
        _bitmapFlow.value = bitmap
    }

    fun setDateTransaction(value: LocalDate) {
        _dateTransaction.value = value
    }

    fun setDateRemind(value: LocalDate) {
        _dateRemind.value = value
    }

    fun setAmount(amount: String?) {
        _amount.value = amount
    }

    fun setUriSelected(value: Uri?) {
        _uriSelected.value = value
    }

    private fun initCurrencySelected() {
        viewModelScope.launch {
            AppCache.defaultCurrencyEntity.collectLatest {
                _currencySelected.value = it
            }
        }
    }

    fun setCurrencySelected(value: Currency) {
        _currencySelected.value = value
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun setDefaultCurrency() {
        if (currencySelected.value == null) return
        val code = currencySelected.value?.code?.uppercase() ?: "VND"
        viewModelScope.launch {
            preferencesUseCase.setDefaultCurrency(code).flatMapLatest {
                AppCache.updateDefaultCurrency(code)
                currenciesUseCase(code)
            }.collectLatest { currency ->
                if (currency is Resource.Success && currency.value != null) {
                    AppCache.updateDefaultCurrencyEntity(currency.value)
                }
            }
        }
    }

    fun submitData() {
        viewModelScope.launch {
            val transactionEntity = Transaction(
                currencyId = currencySelected.value!!.id,
                amount = amount.value?.toDouble() ?: 0.0,
                createdDate = LocalDate.now(),
                displayDate = dateTransaction.value,
                categoryId = selectedCategory.value!!.id,
                note = note.value,
                walletId = wallet.value!!.id,
                people = nameOfPeople.value,
                eventId = eventSelected.value?.id,
                remindDate = dateRemind.value,
                image = uriSelected.value,
                isExcludedReport = false // TODO
            )
            transactionUseCase(transactionEntity).collectLatest {
                _insertTransaction.value = it
                if (it is Resource.Success && _wallet.value != null) {
                    accountsUseCase.update(
                        Wallet(
                            id = _wallet.value!!.id,
                            name = _wallet.value!!.name,
                            currencyId = _wallet.value!!.currency.id,
                            icon = _wallet.value!!.icon,
                            uuid = _wallet.value!!.uuid,
                            balance = calculateWalletAfterTransaction(
                                _wallet.value!!.currency,
                                _wallet.value!!.balance,
                                _currencySelected.value!!,
                                _amount.value?.toDouble() ?: 0.0,
                                _selectedCategory.value!!.type == Constants.INCOME
                            )
                        )
                    ).collectLatest {

                    }
                }
            }
        }
    }


    fun setEventSelected(value: Event?) {
        _eventSelected.value = value
    }

    fun setSelectedCategory(value: CategoryEntity?) {
        _selectedCategory.value = value
    }

    fun setNote(note: String) {
        _note.value = note
    }

    fun setNamePeople(name: String) {
        _nameOfPeople.value = name
    }

    fun setWallet(entity: AccountEntity) {
        _wallet.value = entity
    }

    override fun onCleared() {
        _bitmapFlow.value = null
        viewModelScope.launch {
            bitmapFlow.collect { it?.recycle() }
        }
        super.onCleared()
    }


}