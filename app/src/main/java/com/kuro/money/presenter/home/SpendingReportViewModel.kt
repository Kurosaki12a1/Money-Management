package com.kuro.money.presenter.home

import androidx.lifecycle.ViewModel
import com.kuro.money.domain.usecase.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SpendingReportViewModel @Inject constructor(
    val transaction : TransactionUseCase
): ViewModel() {
    private val _tabSelected = MutableStateFlow(0)
    val tabSelected = _tabSelected.asStateFlow()

    fun setTabSelected(index : Int) {
        _tabSelected.value = index
    }
}