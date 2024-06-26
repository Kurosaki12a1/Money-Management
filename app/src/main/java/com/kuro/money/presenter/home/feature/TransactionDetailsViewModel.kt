package com.kuro.money.presenter.home.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.usecase.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailsViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase
) : ViewModel()  {
    private val _deleteTransaction = MutableStateFlow<Resource<Int>>(Resource.Default)
    val deleteTransaction = _deleteTransaction.asStateFlow()

    private val _detailTransaction = MutableStateFlow<Resource<TransactionEntity>>(Resource.Default)
    val detailTransaction = _detailTransaction.asStateFlow()

    fun deleteTransaction(id : Long) {
        viewModelScope.launch {
            transactionUseCase(id).collectLatest {
                _deleteTransaction.value = it
            }
        }
    }

    fun getTransactionById(id : Long) {
        viewModelScope.launch {
            transactionUseCase.getTransactionById(id).collectLatest {
                _detailTransaction.value = it
            }
        }
    }

    fun setDefault() {
        _deleteTransaction.value = Resource.Default
    }
}