package com.kuro.money.presenter.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kuro.money.R
import com.kuro.money.constants.Constants
import com.kuro.money.data.AppCache
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.extension.detectHorizontalWithDelay
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.navigation.routes.NavigationRoute
import com.kuro.money.presenter.report.feature.chart.ExpenseChart
import com.kuro.money.presenter.report.feature.chart.IncomeChart
import com.kuro.money.presenter.report.feature.chart.NetIncomeChart
import com.kuro.money.presenter.utils.CrossSlide
import com.kuro.money.presenter.utils.DecimalFormatter
import com.kuro.money.presenter.utils.getBalanceFromList
import com.kuro.money.presenter.utils.getExpenseFromList
import com.kuro.money.presenter.utils.getIncomeFromList
import com.kuro.money.presenter.utils.string
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import java.time.LocalDate
import java.time.Month

@Composable
fun ReportScreen(
    navController: NavController, reportViewModel: ReportViewModel, paddingValues: PaddingValues
) {
    val listState = rememberLazyListState()
    val listWallet = remember { mutableStateListOf<AccountEntity>() }
    val selectedWallet = reportViewModel.selectedWallet.collectAsState().value
    val monthList = reportViewModel.monthList.collectAsState().value
    val monthSelected = reportViewModel.monthSelected.collectAsState().value
    // List Transaction selected this time and prev time
    // Full list transaction of month without filter wallet
    val listMonthTransaction = remember { mutableStateListOf<TransactionEntity>() }
    // First focus is 17
    val prevIndexSelected = remember { mutableIntStateOf(17) }
    val indexSelected =
        remember(monthSelected) { mutableIntStateOf(monthList.indexOf(monthSelected)) }
    // List transaction with filter wallet
    val listTransactions = remember { mutableStateListOf<TransactionEntity>() }

    val decimalFormatter = DecimalFormatter()
    LaunchedEffect(Unit) {
        reportViewModel.getBalance()
        reportViewModel.getAllWallets()
        reportViewModel.setMonthSelected(monthSelected)
    }

    LaunchedEffect(indexSelected.intValue) {
        listState.scrollToItem(indexSelected.intValue)
    }

    LaunchedEffect(Unit) {
        reportViewModel.transactionSelected.collectLatest {
            if (it is Resource.Success) {
                listMonthTransaction.clear()
                listMonthTransaction.addAll(it.value)
            }

            listTransactions.clear()
            listTransactions.addAll(
                if (selectedWallet?.id == Constants.GLOBAL_WALLET_ID) {
                    listMonthTransaction
                } else
                    listMonthTransaction.filter { it.wallet.id == selectedWallet?.id }
            )
        }
    }

    LaunchedEffect(selectedWallet) {
        listTransactions.clear()
        listTransactions.addAll(
            if (selectedWallet?.id == Constants.GLOBAL_WALLET_ID) {
                listMonthTransaction
            } else
                listMonthTransaction.filter { it.wallet.id == selectedWallet?.id }
        )
    }

    LaunchedEffect(Unit) {
        reportViewModel.globalWallet.zip(reportViewModel.allWallets) { globalWallet, list ->
            Pair(globalWallet, list)
        }.collectLatest {
            if (it.second is Resource.Success && it.first != null) {
                listWallet.clear()
                listWallet.add(it.first!!)
                listWallet.addAll((it.second as Resource.Success).value)
                if (selectedWallet == null) {
                    reportViewModel.setSelectedWallet(it.first!!)
                }
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(
                top = 20.dp, bottom = paddingValues.calculateBottomPadding()
            ), color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (!listWallet.isEmpty()) {
                ToolBarReport(selectedWallet,
                    onShareClick = {

                    },
                    onTimeRangeClick = {

                    },
                    onWalletClick = {
                        navController.navigate(NavigationRoute.Report.SelectWallet.route)
                    })
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp), state = listState
            ) {
                itemsIndexed(monthList) { index, date ->
                    TabOfMonth(date = date,
                        index = index,
                        isSelected = monthSelected == date,
                        onClick = { reportViewModel.setMonthSelected(it) })
                }
            }
            CrossSlide(
                currentState = prevIndexSelected.intValue,
                targetState = indexSelected.intValue,
                modifier = Modifier
                    .fillMaxSize()
                    .detectHorizontalWithDelay(onSwipeLeft = {
                        if (indexSelected.intValue != prevIndexSelected.intValue) {
                            prevIndexSelected.intValue = indexSelected.intValue
                        }
                        if (indexSelected.intValue < 17) {
                            reportViewModel.setMonthSelected(monthList[indexSelected.intValue + 1])
                        }
                    }, onSwipeRight = {
                        if (indexSelected.intValue != prevIndexSelected.intValue) {
                            prevIndexSelected.intValue = indexSelected.intValue
                        }
                        if (indexSelected.intValue > 0) {
                            reportViewModel.setMonthSelected(monthList[indexSelected.intValue - 1])
                        }
                    }),
                orderedContent = (0..18).toList()
            ) { index ->
                // Index of "This month" is 17
                val currentMonth = LocalDate.now().minusMonths((17 - index).toLong()).month
                val transactions =
                    listTransactions.filter { it.displayDate.month == currentMonth }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Gray)
                        .padding(bottom = 30.dp),
                    verticalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    item {
                        ReportBalance(transactions, currentMonth)
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(16.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                                    .noRippleClickable {
                                        navController.navigate(NavigationRoute.Report.OverViewDetails.route)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.net_income),
                                    color = Color.Black,
                                    style = MaterialTheme.typography.h6
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = stringResource(id = R.string.see_details),
                                    color = Color.Green,
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                            Text(
                                text = decimalFormatter.formatForVisual(
                                    getBalanceFromList(
                                        transactions
                                    ).string()
                                ),
                                color = Color.Black,
                                style = MaterialTheme.typography.body1
                            )
                            BoxWithConstraints(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, RoundedCornerShape(16.dp))
                                    .padding(10.dp)
                            ) {
                                NetIncomeChart(transactions)
                            }
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(16.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.all_expense),
                                    color = Color.Black,
                                    style = MaterialTheme.typography.h6
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = stringResource(id = R.string.see_details),
                                    color = Color.Green,
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                            Text(
                                text = decimalFormatter.formatForVisual(
                                    getExpenseFromList(
                                        transactions
                                    ).string()
                                ),
                                color = Color.Red,
                                style = MaterialTheme.typography.body1
                            )
                            BoxWithConstraints(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, RoundedCornerShape(16.dp))
                                    .padding(10.dp)
                            ) {
                                ExpenseChart(transactions.filter { it.category.type == Constants.EXPENSE })
                            }
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(16.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.all_income),
                                    color = Color.Black,
                                    style = MaterialTheme.typography.h6
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = stringResource(id = R.string.see_details),
                                    color = Color.Green,
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                            Text(
                                text = decimalFormatter.formatForVisual(
                                    getIncomeFromList(
                                        transactions
                                    ).string()
                                ),
                                color = Color.Green,
                                style = MaterialTheme.typography.body1
                            )
                            BoxWithConstraints(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, RoundedCornerShape(16.dp))
                                    .padding(10.dp)
                            ) {
                                IncomeChart(transactions.filter { it.category.type == Constants.INCOME })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportBalance(
    listTransaction: List<TransactionEntity>?, month: Month
) {
    val symbol = AppCache.defaultCurrencyEntity.collectAsState().value?.symbol ?: ""
    if (listTransaction.isNullOrEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.opening_balance),
                    color = Color.Black.copy(0.5f),
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "0 $symbol", color = Color.Black, style = MaterialTheme.typography.body1
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.ending_balance),
                    color = Color.Black.copy(0.5f),
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "0 $symbol", color = Color.Black, style = MaterialTheme.typography.body1
                )
            }
        }
        return
    }
    val openingBalance =
        getBalanceFromList(listTransaction.filter { it.displayDate.month == month - 1 })
    val endingBalance = getBalanceFromList(listTransaction.filter { it.displayDate.month == month })
    val decimalFormatter = DecimalFormatter()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.opening_balance),
                color = Color.Black.copy(0.5f),
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${decimalFormatter.formatForVisual(openingBalance.string())} $symbol",
                color = Color.Black,
                style = MaterialTheme.typography.body1
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.ending_balance),
                color = Color.Black.copy(0.5f),
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${decimalFormatter.formatForVisual(endingBalance.string())} $symbol",
                color = Color.Black,
                style = MaterialTheme.typography.body1
            )
        }
    }
}


@Composable
private fun TabOfMonth(
    date: String, index: Int, isSelected: Boolean, onClick: (String) -> Unit
) {
    val textMonth = when (index) {
        17 -> stringResource(id = R.string.this_month)
        16 -> stringResource(id = R.string.prev_month)
        else -> date
    }
    val widthText = remember { mutableStateOf(IntSize.Zero) }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .noRippleClickable { onClick(date) },
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = textMonth,
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


@Composable
private fun ToolBarReport(
    wallet: AccountEntity?,
    onShareClick: () -> Unit,
    onTimeRangeClick: () -> Unit,
    onWalletClick: () -> Unit
) {
    if (wallet == null) return
    val decimalFormatter = DecimalFormatter()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.balance),
                color = Color.Black.copy(0.5f),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(imageVector = Icons.Default.Share,
                    contentDescription = "Search",
                    modifier = Modifier.noRippleClickable { onShareClick() })
                Icon(imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "More",
                    modifier = Modifier.noRippleClickable { onTimeRangeClick() })
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_approximation),
                contentDescription = "Approximation"
            )
            Text(
                text = "${decimalFormatter.formatForVisual(wallet.balance.string())} ${wallet.currency.symbol}",
                color = Color.Black,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }
        Box(
            modifier = Modifier
                .background(Gray, RoundedCornerShape(16.dp))
                .align(Alignment.CenterHorizontally)
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.noRippleClickable { onWalletClick() }) {
                Image(painter = wallet.icon.toPainterResource(), contentDescription = "Icon")
                Text(text = wallet.name)
                Icon(imageVector = Icons.Default.ExpandMore, contentDescription = "Expand More")
            }
        }
    }
}
