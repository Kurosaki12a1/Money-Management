package com.kuro.money.presenter.report.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.TimeRange
import com.kuro.money.domain.usecase.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class ReportDetailsViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {

    private val _tabs = MutableStateFlow<List<String>>(emptyList())
    val tabs = _tabs.asStateFlow()

    /**
     * 0 - DAY
     * 1 - WEEK
     * 2 - MONTH
     * 3 - QUARTER
     * 4 - YEAR
     * 5 - ALL
     */
    private val _timeRange = MutableStateFlow(2)
    val timeRange = _timeRange.asStateFlow()

    private val _selectedTime = MutableStateFlow<TimeRange?>(null)
    val selectedTime = _selectedTime.asStateFlow()

    private val _indexSelected = MutableStateFlow(17)
    val indexSelected = _indexSelected.asStateFlow()

    private val _transactions =
        MutableStateFlow<Resource<List<TransactionEntity>>>(Resource.Default)
    val transactions = _transactions.asStateFlow()

    fun setIndexSelected(index: Int) {
        _indexSelected.value = index
    }

    fun setTimeRange(index: Int) {
        _timeRange.value = index
        _tabs.value = generateTabsName(index)
    }

    fun setCurrentTimeRange(tabIndex: Int) {
        val dateList = generateDateList(_timeRange.value)
        when (_timeRange.value) {
            0 -> _selectedTime.value = TimeRange.DAY(dateList[tabIndex])
            1 -> {
                val startOfWeek =
                    dateList[tabIndex].with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val endOfWeek = startOfWeek.plusDays(6)
                _selectedTime.value = TimeRange.WEEK(startOfWeek, endOfWeek)
            }

            2 -> {
                val startOfMonth = dateList[tabIndex].withDayOfMonth(1)
                val endOfMonth = startOfMonth.plusMonths(1).minusDays(1)
                _selectedTime.value = TimeRange.MONTH(startOfMonth, endOfMonth)
            }

            3 -> {
                val quarter = (dateList[tabIndex].monthValue - 1) / 3
                val startOfQuarter = dateList[tabIndex].withMonth(quarter * 3 + 1).withDayOfMonth(1)
                val endOfQuarter = startOfQuarter.plusMonths(3).minusDays(1)
                _selectedTime.value = TimeRange.QUARTER(startOfQuarter, endOfQuarter)
            }

            4 -> {
                val startOfYear = dateList[tabIndex].withMonth(1).withDayOfMonth(1)
                val endOfYear = dateList[tabIndex].withMonth(12).withDayOfMonth(31)
                _selectedTime.value = TimeRange.YEAR(startOfYear, endOfYear)
            }

            else -> {
                _selectedTime.value = TimeRange.ALL
            }
        }
        if (_selectedTime.value != null) {
            viewModelScope.launch {
                transactionUseCase(_selectedTime.value!!).collectLatest {
                    _transactions.value = it
                }
            }
        }
    }

    private fun generateDateList(timeRange: Int): List<LocalDate> {
        val today = LocalDate.now()
        return when (timeRange) {
            0 -> List(18) { today.minusDays(it.toLong()) }
            1 -> List(18) { today.minusWeeks(it.toLong()) }
            2 -> List(18) { today.minusMonths(it.toLong()) }
            3 -> List(18) { today.minusMonths(3L * it) }
            4 -> List(18) { today.minusYears(it.toLong()) }
            else -> listOf(LocalDate.now())
        }.reversed()
    }

    private fun generateTabsName(timeRange: Int): List<String> {
        val formatterDay = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val formatterMonth = DateTimeFormatter.ofPattern("MM/yyyy")
        val dateList = generateDateList(timeRange)

        return when (timeRange) {
            0 -> dateList.mapIndexed { index, date ->
                when (index) {
                    17 -> "TODAY"
                    16 -> "YESTERDAY"
                    else -> date.format(formatterDay)
                }
            }

            1 -> dateList.mapIndexed { index, date ->
                val startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val endOfWeek = startOfWeek.plusDays(6)
                when (index) {
                    17 -> "THIS WEEK"
                    16 -> "LAST WEEK"
                    else -> "${startOfWeek.format(formatterDay)} - ${endOfWeek.format(formatterDay)}"
                }
            }

            2 -> dateList.mapIndexed { index, date ->
                when (index) {
                    17 -> "THIS MONTH"
                    16 -> "LAST MONTH"
                    else -> date.format(formatterMonth)
                }
            }

            3 -> dateList.mapIndexed { index, date ->
                val quarter = (date.monthValue - 1) / 3 + 1
                "Q$quarter ${date.year}"
            }

            4 -> dateList.mapIndexed { index, date ->
                when (index) {
                    17 -> "THIS YEAR"
                    16 -> "LAST YEAR"
                    else -> date.format(formatterMonth)
                }
            }

            else -> emptyList()
        }
    }
}
