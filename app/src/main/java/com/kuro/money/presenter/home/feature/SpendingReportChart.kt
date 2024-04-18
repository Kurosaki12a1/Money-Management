package com.kuro.money.presenter.home.feature

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import kotlin.random.Random


@Composable
fun SpendingReportChart() {
    val barData = listOf(
        BarDataValue("A", 10f),
        BarDataValue("B", 20f),
        BarDataValue("C", 30f),
        BarDataValue("D", 0f),
        BarDataValue("E", 40f),
        BarDataValue("F", 45f)
    )
    val barChartData =
        getBarChartDataCustom(barData, 50, BarChartType.VERTICAL, DataCategoryOptions())

    val xAxisData = AxisData.Builder()
        .axisStepSize(0.dp)
        .steps(barChartData.size - 1)
        .bottomPadding(40.dp)
        .startPadding(20.dp)
        .labelData { index -> barData[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(10)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * (50 / 10)).toString() }
        .build()

    val barChartDatas = BarChartData(
        chartData = barChartData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        barStyle = BarStyle(
            barWidth = 25.dp,
            paddingBetweenBars = 20.dp
        ),
    )
    BarChart(modifier = Modifier.height(350.dp), barChartData = barChartDatas)

}

fun getBarChartDataCustom(
    listBarData: List<BarDataValue>,
    maxRange: Int,
    barChartType: BarChartType,
    dataCategoryOptions: DataCategoryOptions
): List<BarData> {
    val list = arrayListOf<BarData>()
    for (index in listBarData.indices) {
        val point = when (barChartType) {
            BarChartType.VERTICAL -> {
                Point(
                    index.toFloat(),
                    listBarData[index].value
                )
            }

            BarChartType.HORIZONTAL -> {
                Point(
                    "%.2f".format(Random.nextDouble(1.0, maxRange.toDouble())).toFloat(),
                    index.toFloat()
                )
            }
        }

        list.add(
            BarData(
                point = point,
                color = Color(
                    Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)
                ),
                dataCategoryOptions = dataCategoryOptions,
                label = "Bar$index",
            )
        )
    }
    return list
}

data class BarDataValue(
    val label: String,
    val value: Float
)