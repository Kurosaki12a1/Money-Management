package com.kuro.money.presenter.add_transaction

import androidx.lifecycle.ViewModel
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.domain.model.ScreenSelection
import com.kuro.money.domain.model.SelectedCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddTransactionViewModel : ViewModel() {
    private val _enableChildScreen = MutableStateFlow(ScreenSelection.ADD_TRANSACTION_SCREEN)
    val enableChildScreen = _enableChildScreen.asStateFlow()

    private val _selectedCategory = MutableStateFlow(SelectedCategory("", ""))
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _wallet = MutableStateFlow<AccountEntity?>(null)
    val wallet = _wallet.asStateFlow()

    private val _nameOfPeople = MutableStateFlow<String?>(null)
    val nameOfPeople = _nameOfPeople.asStateFlow()

    private val _note = MutableStateFlow("")
    val note = _note.asStateFlow()

    private val _amount = MutableStateFlow(0.0)
    val amount = _amount.asStateFlow()

    fun setAmount(amount: Double) {
        _amount.value = amount
    }

    fun clearData() {
        _amount.value = 0.0
        _note.value = ""
        _selectedCategory.value = SelectedCategory("", "")
        _wallet.value = null
    }

    fun setEnableCategoryScreen(value: Boolean) {
        if (value) {
            _enableChildScreen.value = ScreenSelection.SELECT_CATEGORY_SCREEN
        } else {
            _enableChildScreen.value = ScreenSelection.ADD_TRANSACTION_SCREEN
        }
    }

    fun setEnableWalletScreen(value : Boolean) {
        if (value) {
            _enableChildScreen.value = ScreenSelection.WALLET_SCREEN
        } else {
            _enableChildScreen.value = ScreenSelection.ADD_TRANSACTION_SCREEN
        }
    }

    fun setSelectedCategory(value: SelectedCategory) {
        _selectedCategory.value = value
    }

    fun setNote(note: String) {
        _note.value = note
    }

    fun setNamePeople(name : String) {
        _nameOfPeople.value = name
    }

    fun setWallet(entity: AccountEntity) {
        _wallet.value = entity
    }

    fun setEnableNoteScreen(value: Boolean) {
        if (value) {
            _enableChildScreen.value = ScreenSelection.NOTE_SCREEN
        } else {
            _enableChildScreen.value = ScreenSelection.ADD_TRANSACTION_SCREEN
        }
    }

    fun setEnableSelectPeopleScreen(value: Boolean) {
        if (value) {
            _enableChildScreen.value = ScreenSelection.WITH_SCREEN
        } else {
            _enableChildScreen.value = ScreenSelection.ADD_TRANSACTION_SCREEN
        }
    }

}