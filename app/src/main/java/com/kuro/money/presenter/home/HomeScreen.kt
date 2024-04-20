package com.kuro.money.presenter.home

import Minus
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kuro.customimagevector.EyeHidden
import com.kuro.customimagevector.EyeOpen
import com.kuro.money.R
import com.kuro.money.constants.Constants
import com.kuro.money.data.AppCache
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.navigation.routes.NavigationRoute
import com.kuro.money.presenter.home.feature.SpendingReportChart
import com.kuro.money.presenter.utils.DecimalFormatter
import com.kuro.money.presenter.utils.getBalance
import com.kuro.money.presenter.utils.string
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import com.kuro.money.ui.theme.Green
import kotlinx.coroutines.flow.collectLatest
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun HomeScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel,
    myWalletViewModel: MyWalletViewModel,
    recentTransactionViewModel: RecentTransactionViewModel
) {

    LaunchedEffect(true) {
        homeViewModel.getBalance()
    }


    val balance = homeViewModel.balance.collectAsState().value
    val isHide = remember { mutableStateOf(true) }
    val decimalFormatter = DecimalFormatter()


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray)
            .padding(10.dp),
        contentPadding = PaddingValues(
            start = 10.dp,
            end = 10.dp,
            bottom = paddingValues.calculateBottomPadding()
        )
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (isHide.value) {
                    Text(
                        text = "************",
                        style = MaterialTheme.typography.h5
                    )
                    Image(
                        imageVector = EyeHidden,
                        contentDescription = "Hidden",
                        modifier = Modifier.noRippleClickable { isHide.value = !isHide.value }
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_approximation),
                        contentDescription = "Approximation"
                    )
                    Text(
                        text = "${decimalFormatter.formatForVisual(balance.string())} ${AppCache.defaultCurrencyEntity.value?.symbol ?: ""}",
                        style = MaterialTheme.typography.h6
                    )
                    Image(
                        imageVector = EyeOpen,
                        contentDescription = "Hidden",
                        modifier = Modifier.noRippleClickable { isHide.value = !isHide.value }
                    )
                }
            }
        }
        item {
            Text(
                text = stringResource(id = R.string.total_balance),
                color = Color.Black.copy(0.5f),
                style = MaterialTheme.typography.body2,

                )
            Spacer(modifier = Modifier.height(20.dp))
            MyWalletScreen(navController, myWalletViewModel)
        }
        /** Title spending report*/
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.spending_report),
                    color = Color.Black.copy(0.5f),
                    style = MaterialTheme.typography.body2,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.see_reports),
                    color = Green,
                    style = MaterialTheme.typography.body2,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            /** Spending Report*/
            SpendingReport(recentTransactionViewModel)
        }
        /** Title Recent Transaction*/
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.recent_transactions),
                    color = Color.Black.copy(0.5f),
                    style = MaterialTheme.typography.body2,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.see_all),
                    color = Green,
                    style = MaterialTheme.typography.body2,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            /** Recent Transaction */
            RecentTransaction(navController, homeViewModel, recentTransactionViewModel)
        }
    }
}

@Composable
fun SpendingReport(
    recentTransactionViewModel: RecentTransactionViewModel
) {
    val tabSelected = recentTransactionViewModel.tabSelected.collectAsState().value
    val decimalFormatter = DecimalFormatter()
    val listTransaction = remember { mutableStateListOf<TransactionEntity>() }
    LaunchedEffect(true) {
        recentTransactionViewModel.getTransactionsBetweenDate()
        recentTransactionViewModel.reportTransaction.collectLatest {
            if (it is Resource.Success) {
                listTransaction.clear()
                listTransaction.addAll(it.value)
            }
        }
    }
    val symbol = AppCache.defaultCurrencyEntity.collectAsState().value?.symbol ?: ""
    val textDescription = StringBuilder().apply {
        append(stringResource(id = R.string.total_spent))
    }

    val spendingValues: Pair<Double, Double>
    if (tabSelected == TypeReport.WEEK) {
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
        textDescription.append(" week")
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
        textDescription.append(" month")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray, RoundedCornerShape(16.dp))
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (tabSelected == TypeReport.WEEK) Color.White else Gray,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(5.dp)
                    .noRippleClickable {
                        recentTransactionViewModel.setTabSelected(TypeReport.WEEK)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.week),
                    color = Color.Black,
                    style = MaterialTheme.typography.body1
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (tabSelected == TypeReport.MONTH) Color.White else Gray,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(5.dp)
                    .noRippleClickable {
                        recentTransactionViewModel.setTabSelected(TypeReport.MONTH)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.month),
                    color = Color.Black,
                    style = MaterialTheme.typography.body1
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_approximation),
                contentDescription = "Approximation"
            )
            Text(
                text = decimalFormatter.formatForVisual(spendingValues.second.string()) + " " + symbol,
                style = MaterialTheme.typography.h6,
                color = Color.Black
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = textDescription.toString(),
                style = MaterialTheme.typography.body1,
                color = Color.Black.copy(0.5f)
            )
            val compareLastTime =
                if (spendingValues.first == 0.0) 0.0 else spendingValues.second / spendingValues.first
            when {
                compareLastTime == 0.0 -> {
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(0.2f), CircleShape)
                            .width(15.dp)
                            .height(15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Minus, contentDescription = "UnChanged",
                            tint = Color.Magenta
                        )
                    }
                    Text(
                        text = "0 %",
                        color = Color.Magenta,
                    )
                }

                compareLastTime > 1.0 -> {
                    Box(
                        modifier = Modifier
                            .background(Color.Gray, CircleShape)
                            .width(15.dp)
                            .height(15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward, contentDescription = "UpWard",
                            tint = Color.Red
                        )
                    }
                    Text(
                        text = "${compareLastTime * 100} %",
                        color = Color.Red,
                    )
                }

                compareLastTime < 1.0 -> {
                    Box(
                        modifier = Modifier
                            .background(Color.Gray, CircleShape)
                            .width(15.dp)
                            .height(15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward, contentDescription = "Downward",
                            tint = Green
                        )
                    }
                    Text(
                        text = "${compareLastTime * 100} %",
                        color = Green,
                    )
                }
            }
        }
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            SpendingReportChart(tabSelected, spendingValues)
        }
    }
}


@Composable
fun RecentTransaction(
    navController: NavController,
    homeViewModel: HomeViewModel,
    recentTransactionViewModel: RecentTransactionViewModel
) {

    val listRecentTransactions = remember { mutableStateListOf<TransactionEntity>() }

    LaunchedEffect(true) {
        recentTransactionViewModel.getAllTransactions()
    }


    LaunchedEffect(Unit) {
        recentTransactionViewModel.allTransactions.collectLatest {
            if (it is Resource.Success) {
                listRecentTransactions.clear()
                listRecentTransactions.addAll(it.value?.reversed()?.take(5) ?: listOf())
                listRecentTransactions.reversed()
            }
        }
    }

    Surface(
        modifier = Modifier.background(Color.White, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listRecentTransactions.forEach { item ->
                ItemRecentTransactions(item) {
                    homeViewModel.setSelectedTransaction(item)
                    navController.navigate("${NavigationRoute.Home.TransactionDetails.route}/${item.id}")
                }
            }
        }
    }
}

@Composable
private fun ItemRecentTransactions(item: TransactionEntity, onClick: (TransactionEntity) -> Unit) {
    val decimalFormatter = DecimalFormatter()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onClick(item) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Image(painter = item.category.icon.toPainterResource(), contentDescription = "Icon")
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = item.category.name,
                color = Color.Black,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = item.displayDate.string(),
                color = Color.Black.copy(0.5f),
                style = MaterialTheme.typography.body2
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${decimalFormatter.formatForVisual(item.amount.string())} ${item.currency.symbol}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (item.category.type == "income") Color.Cyan else Color.Red,
            style = MaterialTheme.typography.body1,
        )
    }
}

@Composable
fun MyWalletScreen(
    navController: NavController,
    myWalletViewModel: MyWalletViewModel
) {

    LaunchedEffect(true) { myWalletViewModel.getAllWallets() }

    val listWallets = remember { mutableStateListOf<AccountEntity>() }
    LaunchedEffect(Unit) {
        myWalletViewModel.allWallets.collectLatest {
            if (it is Resource.Success) {
                listWallets.clear()
                listWallets.addAll(it.value.take(5))
            }
        }
    }

    Surface(
        modifier = Modifier.background(Color.White, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.my_wallets),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.see_all),
                    color = Green,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.noRippleClickable { navController.navigate(NavigationRoute.Home.Wallet.route) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray)
            )
            listWallets.forEachIndexed { index, item ->
                WalletItem(item, index == listWallets.size - 1) {

                }
            }
        }
    }
}

@Composable
private fun WalletItem(
    item: AccountEntity,
    isLastIndex: Boolean,
    onClick: (AccountEntity) -> Unit
) {
    val decimalFormatter = DecimalFormatter()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onClick(item) }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(painter = item.icon.toPainterResource(), contentDescription = item.name)
        Text(
            text = item.name,
            color = Color.Black,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${decimalFormatter.formatForVisual(item.balance.string())} ${item.currency.symbol}",
            color = Color.Black,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
    }
    if (!isLastIndex) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray)
        )
    }
}