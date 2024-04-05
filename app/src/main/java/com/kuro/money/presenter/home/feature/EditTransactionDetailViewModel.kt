package com.kuro.money.presenter.home.feature

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.AppCache
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.CurrencyEntity
import com.kuro.money.data.model.EventEntity
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.SelectedCategory
import com.kuro.money.domain.usecase.CurrenciesUseCase
import com.kuro.money.domain.usecase.PreferencesUseCase
import com.kuro.money.domain.usecase.TransactionUseCase
import com.kuro.money.presenter.utils.string
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
class EditTransactionDetailViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase,
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

    private val _updateTransaction = MutableStateFlow<Resource<Int>>(Resource.Default)
    val updateTransaction = _updateTransaction.asStateFlow()

    private val _bitmapFlow = MutableStateFlow<Bitmap?>(null)
    val bitmapFlow = _bitmapFlow.asStateFlow()

    private val _id = MutableStateFlow(0L)

    init {
        initCurrencySelected()
    }

    fun getTransaction(id : Long) {
        viewModelScope.launch {
            transactionUseCase.getTransactionById(id).collectLatest {
                if (it is Resource.Success) {
                    setData(it.value)
                }
            }
        }
    }

    private fun setData(data: TransactionEntity) {
        _id.value = data.id
        _amount.value = data.amount.string()
        _currencySelected.value = data.currency
        _dateTransaction.value = data.displayDate
        _selectedCategory.value = data.category
        _note.value = data.note ?: ""
        _wallet.value = data.wallet
        _nameOfPeople.value = data.people
        _eventSelected.value = data.event
        _dateRemind.value = data.remindDate
        _uriSelected.value = data.image
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
                id = _id.value,
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
            transactionUseCase.update(transactionEntity).collectLatest {
                _updateTransaction.value = it
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