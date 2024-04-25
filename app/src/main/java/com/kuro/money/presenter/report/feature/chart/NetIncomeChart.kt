package com.kuro.money.presenter.report.feature.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.GroupBarChart
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarPlotData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.GroupBar
import co.yml.charts.ui.barchart.models.GroupBarChartData
import com.kuro.money.constants.Constants
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.presenter.utils.formatLargeNumber
import com.kuro.money.presenter.utils.getBalance
import com.kuro.money.presenter.utils.getWeekBoundariesOfMonth
import com.kuro.money.presenter.utils.weekToString

@Composable
fun BoxWithConstraintsScope.NetIncomeChart(
    listTransaction: List<TransactionEntity>
) {
    if (listTransaction.isEmpty()) return
    val year = listTransaction[0].displayDate.year
    val month = listTransaction[0].displayDate.monthValue
    val weeksOfMonth = getWeekBoundariesOfMonth(year, month)
    val groupBarData = mutableListOf<GroupBar>()
    val widthBar = this.maxWidth / 10f
    var maxValue = 0.0
    weeksOfMonth.onEachIndexed { index, weekDay ->
        val income = listTransaction
            .filter { it.category.type == Constants.INCOME }
            .filter { it.displayDate.dayOfMonth >= weekDay.value.first.dayOfMonth }
            .filter { it.displayDate.dayOfMonth <= weekDay.value.second.dayOfMonth }
            .sumOf { getBalance(it) }
        val expense = listTransaction
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
        groupBarData.add(GroupBar(weekToString(weekDay.value.first, weekDay.value.second), barData))
    }

    val groupBarPlotData = BarPlotData(
        groupBarList = groupBarData,
        barColorPaletteList = listOf(Color.Blue, Color.Red),
        barStyle = BarStyle(barWidth = widthBar, paddingBetweenBars = widthBar)
    )


    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .startDrawPadding(20.dp)
        .steps(groupBarData.size - 1)
        .axisLabelAngle(-30f)
        .labelAndAxisLinePadding(30.dp)
        .axisLabelFontSize(8.sp)
        .labelData { index -> groupBarData[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(10)
        .labelAndAxisLinePadding(30.dp)
        .axisLabelFontSize(10.sp)
        .topPadding(30.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * maxValue / 10).formatLargeNumber() }
        .build()

    val groupBarChartData = GroupBarChartData(
        barPlotData = groupBarPlotData,
        xAxisData = xAxisData,
        yAxisData = yAxisData
    )

    GroupBarChart(
        modifier = Modifier
            .background(Color.White)
            .height(300.dp),
        groupBarChartData = groupBarChartData
    )

}