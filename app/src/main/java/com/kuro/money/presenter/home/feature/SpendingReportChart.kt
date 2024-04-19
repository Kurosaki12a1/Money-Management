package com.kuro.money.presenter.home.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisConfig
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.kuro.money.R
import com.kuro.money.constants.Constants
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.presenter.home.RecentTransactionViewModel
import com.kuro.money.presenter.home.TypeReport
import com.kuro.money.presenter.utils.getBalance
import kotlinx.coroutines.flow.collectLatest
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters


@Composable
fun BoxWithConstraintsScope.SpendingReportChart(
    recentTransactionViewModel: RecentTransactionViewModel
) {
    val typeReport = recentTransactionViewModel.tabSelected.collectAsState().value
    val listTransaction = remember { mutableStateListOf<TransactionEntity>() }
    LaunchedEffect(Unit) {
        recentTransactionViewModel.reportTransaction.collectLatest {
            if (it is Resource.Success) {
                listTransaction.clear()
                listTransaction.addAll(it.value)
            }
        }
    }

    val width = this.maxWidth
    val spendingValues: Pair<Double, Double>
    if (typeReport == TypeReport.WEEK) {
        val startLastWeek =
            LocalDate.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val endLastWeek =
            LocalDate.now().minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
        val startThisWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val endThisWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
        spendingValues = Pair(
            listTransaction
                .filter { it.category.type == Constants.EXPENSE }
                .filter { startLastWeek <= it.displayDate && it.displayDate <= endLastWeek }
                .sumOf { getBalance(it) },
            listTransaction
                .filter { it.category.type == Constants.EXPENSE }
                .filter { startThisWeek <= it.displayDate && it.displayDate <= endThisWeek }
                .sumOf { getBalance(it) }
        )
    } else {
        val lastMonth = LocalDate.now().minusMonths(1).month
        val thisMonth = LocalDate.now().month
        spendingValues = Pair(
            listTransaction
                .filter { it.category.type == Constants.EXPENSE }
                .filter { it.displayDate.month == lastMonth }
                .sumOf { getBalance(it) },
            listTransaction
                .filter { it.category.type == Constants.EXPENSE }
                .filter { it.displayDate.month == thisMonth }
                .sumOf { getBalance(it) },
        )
    }
    val chartData = listOf(
        BarData(
            point = Point(0f, spendingValues.first.toFloat()),
            color = Color.Blue,
            label = if (typeReport == TypeReport.WEEK)
                stringResource(id = R.string.last_week) else stringResource(id = R.string.last_month),
            dataCategoryOptions = DataCategoryOptions()
        ),
        BarData(
            point = Point(1f, spendingValues.second.toFloat()),
            color = Color.Red,
            label = if (typeReport == TypeReport.WEEK)
                stringResource(id = R.string.this_week) else stringResource(id = R.string.this_month_ext),
            dataCategoryOptions = DataCategoryOptions()
        )
    )


    val xAxisData = AxisData.Builder()
        .steps(chartData.size - 1)
        .startDrawPadding(width / 6)
        .axisLabelColor(Color.Black.copy(0.5f))
        .axisLabelFontSize(MaterialTheme.typography.body1.fontSize)
        .shouldDrawAxisLineTillEnd(true)
        .labelData { index -> chartData[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(1)
        .axisConfig(
            AxisConfig(
                shouldEllipsizeAxisLabel = true,
                minTextWidthToEllipsize = width / 6
            )
        )
        .shouldDrawAxisLine(false)
        .labelData { index ->
            (index * maxOf(spendingValues.first, spendingValues.second)).toString()
        }
        .build()
    Spacer(modifier = Modifier.height(10.dp))
    BarChart(
        modifier = Modifier
            .wrapContentWidth()
            .height(200.dp)
            .background(Color.White)
            .padding(top = 5.dp),
        barChartData = BarChartData(
            barChartType = BarChartType.VERTICAL,
            chartData = chartData,
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            barStyle = BarStyle(
                barWidth = width / 6,
                paddingBetweenBars = width / 6
            )
        )
    )
}