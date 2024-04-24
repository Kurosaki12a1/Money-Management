@file:OptIn(ExperimentalMaterialApi::class)

package com.kuro.money.presenter.report.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.kuro.money.R
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.domain.model.LetterColor
import com.kuro.money.presenter.utils.getBalanceFromList
import com.kuro.money.presenter.utils.toImageBitmap

@Composable
fun BoxWithConstraintsScope.ExpenseChart(transactions: List<TransactionEntity>) {
    if (transactions.isEmpty()) return
    val categoryTransaction = transactions.groupBy { it.category.name }
    val listPieCharSlice = mutableListOf<PieChartData.Slice>()
    categoryTransaction.onEachIndexed { index, entry ->
        listPieCharSlice.add(
            PieChartData.Slice(
                entry.key,
                getBalanceFromList(entry.value).toFloat(),
                LetterColor.getColor(index),
                entry.value[0].category.icon.toImageBitmap()
            )
        )
    }
    val transactionChartData = PieChartData(
        slices = listPieCharSlice,
        plotType = PlotType.Donut
    )

    val donutChartConfig = PieChartConfig(
        widthChart = 200.dp,
        heightChart = 200.dp,
        labelVisible = true,
        labelFontSize = 24.sp,
        strokeWidthActive = 30f,
        strokeWidth = 10f,
        labelColor = Color.Black,
        isSumVisible = true,
        activeSliceAlpha = .9f,
        isAnimationEnable = true,
    )

    DonutPieChart(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .align(Alignment.Center)
            .height(300.dp),
        transactionChartData,
        donutChartConfig
    )
}