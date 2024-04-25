package com.kuro.money.presenter.report.feature.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import com.kuro.money.R
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.presenter.utils.popBackStackWithLifeCycle
import com.kuro.money.ui.theme.Gray

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
  //  val selectedTime = reportDetailsViewModel.selectedTime.collectAsState().value
    // Tab Selected
    val indexSelected = reportDetailsViewModel.indexSelected.collectAsState().value

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
            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Calendar",
                modifier = Modifier.noRippleClickable {
                    //TODO open dialog timeRange
                }
            )
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