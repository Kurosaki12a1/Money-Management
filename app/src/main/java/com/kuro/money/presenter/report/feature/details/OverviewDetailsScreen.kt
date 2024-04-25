package com.kuro.money.presenter.report.feature.details

import android.util.SparseArray
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.util.forEach
import androidx.navigation.NavController
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.GroupBar
import com.kuro.customimagevector.EmptyBox
import com.kuro.money.R
import com.kuro.money.constants.Constants
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.TimeRange
import com.kuro.money.extension.detectHorizontalWithDelay
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.presenter.utils.CrossSlide
import com.kuro.money.presenter.utils.getBalance
import com.kuro.money.presenter.utils.getWeekBoundariesOfMonth
import com.kuro.money.presenter.utils.monthToString
import com.kuro.money.presenter.utils.popBackStackWithLifeCycle
import com.kuro.money.presenter.utils.quarterToString
import com.kuro.money.presenter.utils.string
import com.kuro.money.presenter.utils.weekToString
import com.kuro.money.ui.theme.Gray
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import kotlin.math.abs

@Composable
fun OverviewDetailsScreen(
    navController: NavController,
    reportDetailsViewModel: ReportDetailsViewModel
) {
    BackHandler { navController.popBackStackWithLifeCycle() }
    val listState = rememberLazyListState()

    // List Of Tabs
    val tabs = reportDetailsViewModel.tabs.collectAsState().value
    // Option Time Range
    val timeOptions = reportDetailsViewModel.timeRange.collectAsState().value
    // Selected Tabs (generate Time Range for query data)
    val selectedTime = reportDetailsViewModel.selectedTime.collectAsState().value
    // Tab Selected
    val prevIndexSelected = remember { mutableIntStateOf(17) }
    val indexSelected = reportDetailsViewModel.indexSelected.collectAsState().value
    val transactions = remember { mutableStateListOf<TransactionEntity>() }
    val enabledTimeRangeOption = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        reportDetailsViewModel.transactions.collectLatest {
            if (it is Resource.Success) {
                transactions.clear()
                transactions.addAll(it.value)
            }
        }
    }

    LaunchedEffect(Unit) {
        /**
         * 0 - DAY
         * 1 - WEEK
         * 2 - MONTH
         * 3 - QUARTER
         * 4 - YEAR
         * 5 - ALL
         */
        reportDetailsViewModel.setTimeRange(2)
        /*
            Current time always last list
         */
        reportDetailsViewModel.setCurrentTimeRange(17)
    }

    LaunchedEffect(Unit) {
        listState.scrollToItem(indexSelected)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back",
                modifier = Modifier.noRippleClickable { navController.popBackStackWithLifeCycle() })
            Text(
                text = stringResource(id = R.string.overview),
                color = Color.Black,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.weight(1f)
            )
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Calendar",
                    modifier = Modifier.noRippleClickable {
                        enabledTimeRangeOption.value = true
                    }
                )
                DropDownMenuTimeRange(
                    isVisible = enabledTimeRangeOption.value,
                    onDismiss = { enabledTimeRangeOption.value = false }) {
                    reportDetailsViewModel.setTimeRange(it)
                }
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            state = listState,
            verticalAlignment = Alignment.CenterVertically
        ) {
            itemsIndexed(tabs, key = { _, item -> item }) { index, item ->
                TabItem(
                    tabName = item,
                    isSelected = index == indexSelected
                ) {
                    reportDetailsViewModel.setCurrentTimeRange(index)
                    reportDetailsViewModel.setIndexSelected(index)
                }
            }
        }
        CrossSlide(
            currentState = prevIndexSelected.intValue,
            targetState = indexSelected,
            modifier = Modifier
                .fillMaxSize()
                .detectHorizontalWithDelay(onSwipeLeft = {
                    if (indexSelected != prevIndexSelected.intValue) {
                        prevIndexSelected.intValue = indexSelected
                    }
                    if (indexSelected < 17) {
                        reportDetailsViewModel.setIndexSelected(indexSelected + 1)
                        reportDetailsViewModel.setCurrentTimeRange(indexSelected + 1)
                    }
                }, onSwipeRight = {
                    if (indexSelected != prevIndexSelected.intValue) {
                        prevIndexSelected.intValue = indexSelected
                    }
                    if (indexSelected > 0) {
                        reportDetailsViewModel.setIndexSelected(indexSelected - 1)
                        reportDetailsViewModel.setCurrentTimeRange(indexSelected - 1)
                    }
                }),
            orderedContent = (0..18).toList()
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                OverviewChart(transactions, selectedTime)
            }
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.OverviewChart(
    transactions: List<TransactionEntity>,
    timeRange: TimeRange?
) {
    if (transactions.isEmpty() || timeRange == null) {
        EmptyResultScreen()
        return
    }
    val width = this.maxWidth
    val groupBarData = mutableListOf<GroupBar>()
    var maxValue = 0.0
    when (timeRange) {
        // DAY
        is TimeRange.DAY -> {
            val income = transactions.filter { it.category.type == Constants.INCOME }
                .sumOf { getBalance(it) }
            val expense = transactions.filter { it.category.type == Constants.EXPENSE }
                .sumOf { getBalance(it) }
            val incomeData = BarData(point = Point(0f, income.toFloat()), label = "Income")
            val expenseData = BarData(point = Point(0f, expense.toFloat()), label = "Expense")
            maxValue = maxOf(income, abs(expense))
            val barData = mutableListOf<BarData>()
            barData.add(incomeData)
            barData.add(expenseData)
            groupBarData.add(GroupBar(timeRange.localDate.string(), barData))
        }
        // WEEK
        is TimeRange.WEEK -> {
            val listDate = List(7) { timeRange.startWeek.minusDays(it.toLong()) }
            listDate.forEachIndexed { index, localDate ->
                val income = transactions
                    .filter { it.category.type == Constants.INCOME }
                    .filter { it.displayDate.dayOfMonth == localDate.dayOfMonth }
                    .sumOf { getBalance(it) }
                val expense = transactions
                    .filter { it.category.type == Constants.EXPENSE }
                    .filter { it.displayDate.dayOfMonth == localDate.dayOfMonth }
                    .sumOf { getBalance(it) }
                val incomeData =
                    BarData(point = Point(index.toFloat(), income.toFloat()), label = "Income")
                val expenseData =
                    BarData(point = Point(index.toFloat(), expense.toFloat()), label = "Expense")
                val barData = mutableListOf<BarData>()
                barData.add(incomeData)
                barData.add(expenseData)
                groupBarData.add(GroupBar(localDate.string(), barData))
            }
        }

        is TimeRange.MONTH -> {
            val weeksOfMonth =
                getWeekBoundariesOfMonth(timeRange.startMonth.year, timeRange.startMonth.monthValue)
            weeksOfMonth.onEachIndexed { index, weekDay ->
                val income = transactions
                    .filter { it.category.type == Constants.INCOME }
                    .filter { it.displayDate.dayOfMonth >= weekDay.value.first.dayOfMonth }
                    .filter { it.displayDate.dayOfMonth <= weekDay.value.second.dayOfMonth }
                    .sumOf { getBalance(it) }
                val expense = transactions
                    .filter { it.category.type == Constants.EXPENSE }
                    .filter { it.displayDate.dayOfMonth >= weekDay.value.first.dayOfMonth }
                    .filter { it.displayDate.dayOfMonth <= weekDay.value.second.dayOfMonth }
                    .sumOf { getBalance(it) }
                val incomeData = BarData(
                    point = Point(index.toFloat(), income.toFloat()),
                    label = "Income"
                )
                val expenseData = BarData(
                    point = Point(index.toFloat(), expense.toFloat()),
                    label = "Expense"
                )
                (if (maxValue == 0.0) {
                    maxOf(income, expense)
                } else {
                    maxOf(maxOf(income, expense), maxValue)
                }).also { maxValue = it }
                val barData = mutableListOf<BarData>()
                barData.add(incomeData)
                barData.add(expenseData)
                groupBarData.add(
                    GroupBar(
                        weekToString(weekDay.value.first, weekDay.value.second),
                        barData
                    )
                )
            }
        }

        is TimeRange.QUARTER -> {
            val monthOfQuarter = SparseArray<Pair<LocalDate, LocalDate>>(3)
            monthOfQuarter.put(
                0, Pair(
                    timeRange.startQuarter.withDayOfMonth(1),
                    timeRange.startQuarter.plusMonths(1).minusDays(1)
                )
            )
            monthOfQuarter.put(
                1, Pair(
                    timeRange.startQuarter.plusMonths(1).withDayOfMonth(1),
                    timeRange.startQuarter.plusMonths(2).minusDays(1)
                )
            )
            monthOfQuarter.put(
                0, Pair(
                    timeRange.startQuarter.plusMonths(2).withDayOfMonth(1),
                    timeRange.endQuarter
                )
            )
            monthOfQuarter.forEach { key, monthQuarter ->
                val income = transactions
                    .filter { it.category.type == Constants.INCOME }
                    .filter { it.displayDate >= monthQuarter.first }
                    .filter { it.displayDate <= monthQuarter.second }
                    .sumOf { getBalance(it) }
                val expense = transactions
                    .filter { it.category.type == Constants.EXPENSE }
                    .filter { it.displayDate >= monthQuarter.first }
                    .filter { it.displayDate <= monthQuarter.second }
                    .sumOf { getBalance(it) }
                val incomeData = BarData(
                    point = Point(key.toFloat(), income.toFloat()),
                    label = "Income"
                )
                val expenseData = BarData(
                    point = Point(key.toFloat(), expense.toFloat()),
                    label = "Expense"
                )
                (if (maxValue == 0.0) {
                    maxOf(income, expense)
                } else {
                    maxOf(maxOf(income, expense), maxValue)
                }).also { maxValue = it }
                val barData = mutableListOf<BarData>()
                barData.add(incomeData)
                barData.add(expenseData)
                groupBarData.add(
                    GroupBar(
                        monthToString(monthQuarter.first),
                        barData
                    )
                )
            }
        }

        is TimeRange.YEAR -> {
            val quarterOfYear = SparseArray<Pair<LocalDate, LocalDate>>()
            val currentDate = LocalDate.now()
            if (timeRange.startYear.year == currentDate.year) {
                var index = 0
                while (timeRange.startYear.withMonth((index + 1) * 3).month > currentDate.month) {
                    quarterOfYear.put(
                        index, Pair(
                            timeRange.startYear.withMonth(index * 3 + 1).withDayOfMonth(1),
                            timeRange.startYear.withMonth((index + 1) * 3).withDayOfMonth(31)
                        )
                    )
                    index++
                }
                val nextQuarter = index + 1
                quarterOfYear.put(
                    nextQuarter, Pair(
                        timeRange.startYear.withMonth(nextQuarter * 3 + 1).withDayOfMonth(1),
                        currentDate
                    )
                )
            } else {
                for (index in 0..3) {
                    quarterOfYear.put(
                        index, Pair(
                            timeRange.startYear.withMonth(index * 3 + 1).withDayOfMonth(1),
                            timeRange.startYear.withMonth((index + 1) * 3).withDayOfMonth(31)
                        )
                    )
                }
            }
            quarterOfYear.forEach { key, quarterYear ->
                val income = transactions
                    .filter { it.category.type == Constants.INCOME }
                    .filter { it.displayDate >= quarterYear.first }
                    .filter { it.displayDate <= quarterYear.second }
                    .sumOf { getBalance(it) }
                val expense = transactions
                    .filter { it.category.type == Constants.EXPENSE }
                    .filter { it.displayDate >= quarterYear.first }
                    .filter { it.displayDate <= quarterYear.second }
                    .sumOf { getBalance(it) }
                val incomeData = BarData(
                    point = Point(key.toFloat(), income.toFloat()),
                    label = "Income"
                )
                val expenseData = BarData(
                    point = Point(key.toFloat(), expense.toFloat()),
                    label = "Expense"
                )
                (if (maxValue == 0.0) {
                    maxOf(income, expense)
                } else {
                    maxOf(maxOf(income, expense), maxValue)
                }).also { maxValue = it }
                val barData = mutableListOf<BarData>()
                barData.add(incomeData)
                barData.add(expenseData)
                groupBarData.add(
                    GroupBar(
                        quarterToString(quarterYear.first),
                        barData
                    )
                )
            }
        }

        else -> {
            // ALL OPTIONS
            // TODO
        }
    }
}


@Composable
private fun DropDownMenuTimeRange(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onClick: (Int) -> Unit
) {
    val listTimeRange = TimeRange.getList()
    DropdownMenu(expanded = isVisible, onDismissRequest = { onDismiss() }) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            listTimeRange.forEachIndexed { index, item ->
                DropdownMenuItem(onClick = {
                    onClick(index)
                    onDismiss()
                }) {
                    Text(
                        text = item,
                        color = Color.Black,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}


@Composable
private fun EmptyResultScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(imageVector = EmptyBox, contentDescription = "EmptyBox")
        Text(
            text = stringResource(id = R.string.no_data),
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
private fun TabItem(
    tabName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val widthText = remember { mutableStateOf(IntSize.Zero) }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .noRippleClickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = tabName,
            color = if (isSelected) Color.Black else Color.Black.copy(0.5f),
            style = MaterialTheme.typography.caption,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.onGloballyPositioned {
                widthText.value = it.size
            })
        if (isSelected) {
            Canvas(
                modifier = Modifier
                    .width(with(LocalDensity.current) { widthText.value.width.toDp() })
                    .height(1.dp)
            ) {
                drawLine(
                    color = Color.Black,
                    start = Offset.Zero,
                    end = Offset(x = size.width, 0f),
                    strokeWidth = 2.dp.toPx()
                )
            }
        }
    }
}