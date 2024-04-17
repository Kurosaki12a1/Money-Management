package com.kuro.money.presenter.transactions.feature.search_transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.usecase.TransactionUseCase
import com.kuro.money.presenter.utils.string
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchTransactionViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {
    private val _listSearchedTransaction =
        MutableStateFlow<Resource<List<TransactionEntity>?>>(Resource.Default)

    val listSearchTransaction = _listSearchedTransaction.asStateFlow()

    fun getListSearchTransaction(search: String) {
        if (search.isBlank()) {
            _listSearchedTransaction.value = Resource.success(emptyList())
            return
        }
        val isNumber = search[0].isDigit()
        viewModelScope.launch {
            transactionUseCase().collectLatest {
                if (it is Resource.Success) {
                    val data = it.value?.filter { entity ->
                        val amount = entity.amount.string()
                        // Search by amount
                        if (isNumber) {
                            amount.contains(search) && amount.startsWith(search)
                        } else {
                            // Search by category
                            entity.category.name.lowercase().contains(search.lowercase())
                                    || entity.note?.lowercase()?.contains(search.lowercase()) == true
                        }
                    }
                    _listSearchedTransaction.emit(Resource.success(data))
                } else {
                    _listSearchedTransaction.value = it
                }
            }
        }
    }
}