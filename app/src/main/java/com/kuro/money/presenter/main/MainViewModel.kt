package com.kuro.money.presenter.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _shouldOpenAddTransactionScreen = MutableStateFlow(false)
    val shouldOpenAddTransactionScreen = _shouldOpenAddTransactionScreen.asStateFlow()

    fun setOpenAddTransactionScreen(value: Boolean) {
        _shouldOpenAddTransactionScreen.value = value
    }
}