package com.kuro.money.presenter.transactions

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kuro.customimagevector.EmptyBox
import com.kuro.money.R
import com.kuro.money.constants.Constants
import com.kuro.money.data.AppCache
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.extension.detectHorizontalWithDelay
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.navigation.routes.NavigationRoute
import com.kuro.money.presenter.utils.CrossSlide
import com.kuro.money.presenter.utils.DecimalFormatter
import com.kuro.money.presenter.utils.getBalance
import com.kuro.money.presenter.utils.string
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.zip
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TransactionScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    transactionViewModel: TransactionViewModel
) {
    val listState = rememberLazyListState()
    val listWallet = remember { mutableStateListOf<AccountEntity>() }
    val selectedWallet = transactionViewModel.selectedWallet.collectAsState().value
    val monthList = transactionViewModel.monthList.collectAsState().value
    val monthSelected = transactionViewModel.monthSelected.collectAsState().value
    // Full list transaction of month without filter wallet
    val listMonthTransaction = remember { mutableStateListOf<TransactionEntity>() }
    // First focus is 17
    val prevIndexSelected = remember { mutableIntStateOf(17) }
    val indexSelected =
        remember(monthSelected) { mutableIntStateOf(monthList.indexOf(monthSelected)) }
    // List transaction with filter wallet
    val listTransactions = remember { mutableStateListOf<TransactionEntity>() }

    LaunchedEffect(Unit) {
        transactionViewModel.getBalance()
        transactionViewModel.getAllWallets()
        transactionViewModel.setMonthSelected(monthSelected)
    }

    LaunchedEffect(indexSelected.intValue) {
        listState.scrollToItem(indexSelected.intValue)
    }

    LaunchedEffect(Unit) {
        transactionViewModel.transactionByDate.collectLatest {
            if (it is Resource.Success) {
                listMonthTransaction.clear()
                listMonthTransaction.addAll(it.value)

                listTransactions.clear()
                listTransactions.addAll(
                    if (selectedWallet?.id == Constants.GLOBAL_WALLET_ID) {
                        listMonthTransaction
                    } else
                        listMonthTransaction.filter { it.wallet.id == selectedWallet?.id }
                )
            }
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
        transactionViewModel.globalWallet.zip(transactionViewModel.allWallets) { globalWallet, list ->
            Pair(globalWallet, list)
        }.collectLatest {
            if (it.second is Resource.Success && it.first != null) {
                listWallet.clear()
                listWallet.add(it.first!!)
                listWallet.addAll((it.second as Resource.Success).value)
                if (selectedWallet == null) {
                    transactionViewModel.setSelectedWallet(it.first!!)
                }

            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 20.dp,
                bottom = paddingValues.calculateBottomPadding()
            ),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (!listWallet.isEmpty()) {
                ToolBarTransactions(
                    selectedWallet,
                    onSearchClick = {
                        navController.navigate(NavigationRoute.Transaction.SearchTransaction.route) {
                            popUpTo(NavigationRoute.Transaction.route)
                        }
                    },
                    onWalletClick = {
                        navController.navigate(NavigationRoute.Transaction.SelectWallet.route)
                    }
                )
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                state = listState
            ) {
                itemsIndexed(monthList) { index, date ->
                    TabOfMonth(
                        date = date,
                        index = index,
                        isSelected = monthSelected == date,
                        onClick = { transactionViewModel.setMonthSelected(it) }
                    )
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
                            transactionViewModel.setMonthSelected(monthList[indexSelected.intValue + 1])
                        }
                    }, onSwipeRight = {
                        if (indexSelected.intValue != prevIndexSelected.intValue) {
                            prevIndexSelected.intValue = indexSelected.intValue
                        }
                        if (indexSelected.intValue > 0) {
                            transactionViewModel.setMonthSelected(monthList[indexSelected.intValue - 1])
                        }
                    }),
                orderedContent = (0..18).toList()
            ) {
                if (listTransactions.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Gray),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            imageVector = EmptyBox,
                            contentDescription = "Empty Box",
                        )
                        Text(
                            text = stringResource(id = R.string.tap_plus_to_add),
                            color = Color.Black,
                            style = MaterialTheme.typography.h6
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                } else {
                    val listTransactionOfDayInMonth = listTransactions
                        .sortedByDescending { it.displayDate }
                        .groupBy { it.displayDate }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Gray)
                            .padding(bottom = 30.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item { ReportSpendingOfMonth(listTransactions) }
                        items(
                            listTransactionOfDayInMonth.keys.toList(),
                            key = { it.dayOfMonth }) { date ->
                            ListTransactionsOfMonth(date, listTransactionOfDayInMonth[date]) { id ->
                                navController.navigate("${NavigationRoute.Home.TransactionDetails.route}/$id")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ListTransactionsOfMonth(
    date: LocalDate,
    listTransaction: List<TransactionEntity>?,
    onClick: (Long) -> Unit
) {
    if (listTransaction == null) return
    val decimalFormatter = DecimalFormatter()
    val income =
        listTransaction.filter { it.category.type == Constants.INCOME }.sumOf { getBalance(it) }
    val expense =
        listTransaction.filter { it.category.type == Constants.EXPENSE }.sumOf { getBalance(it) }
    val balance = income - expense
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = DateTimeFormatter.ofPattern("dd").format(date),
                color = Color.Black,
                style = MaterialTheme.typography.h5,
            )
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                    color = Color.Black,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = DateTimeFormatter.ofPattern("MMMM yyyy").format(date),
                    color = Color.Black.copy(0.5f),
                    style = MaterialTheme.typography.body1
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = decimalFormatter.formatForVisual(balance.string()),
                color = Color.Black,
                style = MaterialTheme.typography.body1
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray)
        )
        listTransaction.forEach { transaction ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable { onClick(transaction.id) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = transaction.category.icon.toPainterResource(),
                    contentDescription = "Icon"
                )
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(
                        text = transaction.category.name,
                        color = Color.Black,
                        style = MaterialTheme.typography.body1
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Notes,
                            contentDescription = "Notes",
                            tint = Gray
                        )
                        Text(
                            text = transaction.note ?: "", color = Color.Black.copy(0.5f),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = decimalFormatter.formatForVisual(getBalance(transaction).string()),
                    color = if (transaction.category.type == Constants.INCOME) Color.Cyan else Color.Red
                )
            }
        }
    }
}

@Composable
private fun ReportSpendingOfMonth(
    listTransaction: List<TransactionEntity>
) {
    val decimalFormatter = DecimalFormatter()
    val income =
        listTransaction.filter { it.category.type == Constants.INCOME }.sumOf { getBalance(it) }
    val expense =
        listTransaction.filter { it.category.type == Constants.EXPENSE }.sumOf { getBalance(it) }
    val symbol = AppCache.defaultCurrencyEntity.value?.symbol ?: ""
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.inflow),
                style = MaterialTheme.typography.body2,
                color = Color.Black.copy(0.5f)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${decimalFormatter.formatForVisual(income.string())} $symbol",
                color = Color.Cyan,
                style = MaterialTheme.typography.body2
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.outflow),
                style = MaterialTheme.typography.body2,
                color = Color.Black.copy(0.5f)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${decimalFormatter.formatForVisual(expense.string())} $symbol",
                color = Color.Red,
                style = MaterialTheme.typography.body2
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(1.dp)
                .background(Color.Black.copy(0.5f))
                .align(Alignment.End)
        )
        Text(
            text = "${decimalFormatter.formatForVisual((income - expense).string())} $symbol",
            color = Color.Black,
            modifier = Modifier.align(Alignment.End)
        )
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(backgroundColor = Gray)
        ) {
            Text(
                text = stringResource(id = R.string.view_report_this_period),
                color = Color.Black,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TabOfMonth(
    date: String,
    index: Int,
    isSelected: Boolean,
    onClick: (String) -> Unit
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
        Text(
            text = textMonth,
            color = if (isSelected) Color.Black else Color.Black.copy(0.5f),
            style = MaterialTheme.typography.caption,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.onGloballyPositioned {
                widthText.value = it.size
            }
        )
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
private fun ToolBarTransactions(
    wallet: AccountEntity?,
    onSearchClick: () -> Unit,
    onWalletClick: () -> Unit
) {
    if (wallet == null) return
    val decimalFormatter = DecimalFormatter()
    Column(
        modifier = Modifier.fillMaxWidth(),
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
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.noRippleClickable { onSearchClick() })
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.noRippleClickable { onWalletClick() }
            ) {
                Image(painter = wallet.icon.toPainterResource(), contentDescription = "Icon")
                Text(text = wallet.name)
                Icon(imageVector = Icons.Default.ExpandMore, contentDescription = "Expand More")
            }
        }
    }
}