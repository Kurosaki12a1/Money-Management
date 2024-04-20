package com.kuro.money.presenter.home.feature


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import com.kuro.money.R
import com.kuro.money.presenter.home.TypeReport
import com.kuro.money.ui.theme.Gray


@Composable
fun BoxWithConstraintsScope.SpendingReportChart(
    typeReport: TypeReport,
    spendingValues: Pair<Double, Double>
) {
    val width = this.maxWidth
    val chartData: List<BarData>
    // Sample value
    val isNoData = spendingValues.first == 0.0 && spendingValues.second == 0.0
    val barWidth = width / 5
    if (isNoData) {
        chartData = listOf(
            BarData(
                point = Point(0f, 100f),
                color = Gray,
                label = if (typeReport == TypeReport.WEEK)
                    stringResource(id = R.string.last_week) else stringResource(id = R.string.last_month),
                dataCategoryOptions = DataCategoryOptions()
            ),
            BarData(
                point = Point(1f, 100f),
                color = Gray,
                label = if (typeReport == TypeReport.WEEK)
                    stringResource(id = R.string.this_week) else stringResource(id = R.string.this_month_ext),
                dataCategoryOptions = DataCategoryOptions()
            )
        )
    } else {
        chartData = listOf(
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
    }

    val xAxisData = AxisData.Builder()
        .steps(chartData.size - 1)
        .startDrawPadding(barWidth)
        .axisLabelColor(Color.Black.copy(0.5f))
        .axisLabelFontSize(MaterialTheme.typography.body1.fontSize)
        .shouldDrawAxisLineTillEnd(true)
        .labelData { index -> chartData[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(1)
        .axisConfig(
            AxisConfig(
                shouldEllipsizeAxisLabel = !isNoData,
                minTextWidthToEllipsize = barWidth
            )
        )
        .shouldDrawAxisLine(false)
        .labelData { index ->
            if (isNoData && index != 0) {
                ""
            } else (index * maxOf(spendingValues.first, spendingValues.second)).toString()
        }
        .build()
    BarChart(
        modifier = Modifier
            .wrapContentWidth()
            .height(200.dp)
            .align(Alignment.Center)
            .background(Color.White)
            .padding(top = if (isNoData) 0.dp else 10.dp),
        barChartData = BarChartData(
            barChartType = BarChartType.VERTICAL,
            chartData = chartData,
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            barStyle = BarStyle(
                barWidth = barWidth,
                paddingBetweenBars = barWidth,
                selectionHighlightData = if (isNoData) null else SelectionHighlightData()
            )
        )
    )
    if (isNoData) {
        Text(
            text = stringResource(id = R.string.empty_spending_report),
            color = Color.Black.copy(0.5f),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}