package com.kuro.money.presenter.home

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
class RecentTransactionViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {

    private val _allTransactions = MutableStateFlow<Resource<List<TransactionEntity>?>>(Resource.Default)
    val allTransactions = _allTransactions.asStateFlow()

    fun getAllTransactions() {
        viewModelScope.launch {
            transactionUseCase().collectLatest {
                _allTransactions.value = it
            }
        }
    }
}