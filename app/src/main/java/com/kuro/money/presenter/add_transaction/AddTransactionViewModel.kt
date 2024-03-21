package com.kuro.money.presenter.add_transaction

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddTransactionViewModel : ViewModel() {
    private val _amount = MutableStateFlow(0.0)
    val amount = _amount.asStateFlow()

    fun setAmount(amount: Double) {
        _amount.value = amount
    }

}