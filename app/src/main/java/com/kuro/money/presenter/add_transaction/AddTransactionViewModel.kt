package com.kuro.money.presenter.add_transaction

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.EventEntity
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.SelectedCategory
import com.kuro.money.domain.usecase.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow(SelectedCategory("", ""))
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _wallet = MutableStateFlow<AccountEntity?>(null)
    val wallet = _wallet.asStateFlow()

    private val _nameOfPeople = MutableStateFlow<String?>(null)
    val nameOfPeople = _nameOfPeople.asStateFlow()

    private val _eventSelected = MutableStateFlow<EventEntity?>(null)
    val eventSelected = _eventSelected.asStateFlow()

    private val _note = MutableStateFlow("")
    val note = _note.asStateFlow()

    private val _uriSelected = MutableStateFlow<Uri?>(null)
    val uriSelected = _uriSelected.asStateFlow()

    private val _amount = MutableStateFlow<Double?>(null)
    val amount = _amount.asStateFlow()

    private val _insertTransaction = MutableStateFlow<Resource<Long>>(Resource.Default)
    val insertTransaction = _insertTransaction.asStateFlow()

    fun setAmount(amount: Double?) {
        _amount.value = amount
    }

    fun setUriSelected(value: Uri?) {
        _uriSelected.value = value
    }

    fun submitData(displayDate: LocalDate, remindDate: LocalDate?) {
        viewModelScope.launch {
            val transactionEntity = TransactionEntity(
                amount = amount.value ?: 0.0,
                createdDate = LocalDate.now(),
                displayDate = displayDate,
                category = selectedCategory.value.name,
                note = note.value,
                wallet = wallet.value!!,
                people = nameOfPeople.value,
                event = eventSelected.value,
                remindDate = remindDate,
                image = uriSelected.value,
                isExcludedReport = false // TODO
            )
            transactionUseCase(transactionEntity).collectLatest {
                _insertTransaction.value = it
                println(it)
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


}