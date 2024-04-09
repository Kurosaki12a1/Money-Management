package com.kuro.money.presenter.add_transaction

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.constants.Constants
import com.kuro.money.data.AppCache
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.CurrencyEntity
import com.kuro.money.data.model.EventEntity
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.SelectedCategory
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
    private val _selectedCategory = MutableStateFlow(SelectedCategory())
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _wallet = MutableStateFlow<AccountEntity?>(null)
    val wallet = _wallet.asStateFlow()

    private val _nameOfPeople = MutableStateFlow<String?>(null)
    val nameOfPeople = _nameOfPeople.asStateFlow()

    private val _eventSelected = MutableStateFlow<EventEntity?>(null)
    val eventSelected = _eventSelected.asStateFlow()

    private val _dateTransaction = MutableStateFlow<LocalDate>(LocalDate.now())
    val dateTransaction = _dateTransaction.asStateFlow()

    private val _dateRemind = MutableStateFlow<LocalDate?>(null)
    val dateRemind = _dateRemind.asStateFlow()

    private val _note = MutableStateFlow("")
    val note = _note.asStateFlow()

    private val _uriSelected = MutableStateFlow<Uri?>(null)
    val uriSelected = _uriSelected.asStateFlow()

    private val _currencySelected = MutableStateFlow<CurrencyEntity?>(null)
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

    fun setCurrencySelected(value: CurrencyEntity) {
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
            val transactionEntity = TransactionEntity(
                currency = currencySelected.value!!,
                amount = amount.value?.toDouble() ?: 0.0,
                createdDate = LocalDate.now(),
                displayDate = dateTransaction.value,
                category = selectedCategory.value,
                note = note.value,
                wallet = wallet.value!!,
                people = nameOfPeople.value,
                event = eventSelected.value,
                remindDate = dateRemind.value,
                image = uriSelected.value,
                isExcludedReport = false // TODO
            )
            transactionUseCase(transactionEntity).collectLatest {
                _insertTransaction.value = it
                if (it is Resource.Success && _wallet.value != null) {
                    accountsUseCase.update(
                        AccountEntity(
                            id = _wallet.value!!.id,
                            name = _wallet.value!!.name,
                            currencyEntity = _wallet.value!!.currencyEntity,
                            icon = _wallet.value!!.icon,
                            uuid = _wallet.value!!.uuid,
                            balance = calculateWalletAfterTransaction(
                                _wallet.value!!.currencyEntity,
                                _wallet.value!!.balance,
                                _currencySelected.value!!,
                                _amount.value?.toDouble() ?: 0.0,
                                _selectedCategory.value.type == Constants.INCOME
                            )
                        )
                    ).collectLatest {

                    }
                }
            }
        }
    }


    fun setEventSelected(value: EventEntity) {
        _eventSelected.value = value
    }

    fun setSelectedCategory(value: SelectedCategory) {
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