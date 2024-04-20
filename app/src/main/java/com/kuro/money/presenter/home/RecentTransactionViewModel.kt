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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class RecentTransactionViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {

    private val _allTransactions =
        MutableStateFlow<Resource<List<TransactionEntity>?>>(Resource.Default)
    val allTransactions = _allTransactions.asStateFlow()

    private val _reportTransaction =
        MutableStateFlow<Resource<List<TransactionEntity>>>(Resource.Default)
    val reportTransaction = _reportTransaction.asStateFlow()


    private val _tabSelected = MutableStateFlow(TypeReport.WEEK)
    val tabSelected = _tabSelected.asStateFlow()

    fun setTabSelected(typeReport: TypeReport) {
        if (typeReport == _tabSelected.value) return
        _tabSelected.value = typeReport
        getTransactionsBetweenDate()
    }


    fun getAllTransactions() {
        viewModelScope.launch {
            transactionUseCase().collectLatest {
                _allTransactions.value = it
            }
        }
    }

    fun getTransactionsBetweenDate() {
        if (_tabSelected.value == TypeReport.WEEK) {
            // Last week from Monday
            val startDate = LocalDate.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            // End week at sunday
            val endDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

            viewModelScope.launch {
                transactionUseCase(startDate, endDate).collectLatest {
                    _reportTransaction.value = it
                }
            }
        } else {
            // Start Day of month last month
            val startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1)
            // End of month
            val endDate = LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1)

            viewModelScope.launch {
                transactionUseCase(startDate, endDate).collectLatest {
                    _reportTransaction.value = it
                }
            }
        }
    }
}