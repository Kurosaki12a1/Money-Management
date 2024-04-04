package com.kuro.money.presenter.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import com.kuro.money.data.AppCache
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.navigation.routes.NavigationRoute
import com.kuro.money.presenter.utils.string
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import com.kuro.money.ui.theme.Green
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel,
    myWalletViewModel: MyWalletViewModel,
    spendingReportViewModel: SpendingReportViewModel,
    recentTransactionViewModel: RecentTransactionViewModel
) {

    LaunchedEffect(navController.currentDestination?.route) {
        if (navController.currentDestination?.route == NavigationRoute.Home.route) {
            homeViewModel.getBalance()
        }
    }

    val balance = homeViewModel.balance.collectAsState().value
    val isHide = remember { mutableStateOf(true) }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray)
            .padding(10.dp),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp , bottom = paddingValues.calculateBottomPadding())
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
                        text = "${balance.string()} ${AppCache.defaultCurrencyEntity.value?.symbol ?: ""}",
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
            SpendingReport(navController, spendingReportViewModel)
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
fun SpendingReport(navController: NavController, spendingReportViewModel: SpendingReportViewModel) {
    Surface(
        modifier = Modifier.background(Color.White, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {

    }
}

@Composable
fun RecentTransaction(
    navController: NavController,
    homeViewModel: HomeViewModel,
    recentTransactionViewModel: RecentTransactionViewModel
) {

    val listRecentTransactions = remember { mutableStateListOf<TransactionEntity>() }
    if (navController.currentDestination?.route == NavigationRoute.Home.route) {
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
                    navController.navigate(NavigationRoute.Home.TransactionDetails.route)
                }
            }
        }
    }
}

@Composable
private fun ItemRecentTransactions(item: TransactionEntity, onClick: (TransactionEntity) -> Unit) {
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
            text = "${item.amount.string()} ${item.currency.symbol}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (item.category.type == "income") Color.Cyan else Color.Red,
            style = MaterialTheme.typography.body1,
        )
    }
}

@Composable
fun LazyItemScope.MyWalletScreen(
    navController: NavController,
    myWalletViewModel: MyWalletViewModel
) {
    if (navController.currentDestination?.route == NavigationRoute.Home.route) {
        myWalletViewModel.getAllWallets()
    }

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
                    style = MaterialTheme.typography.body2
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
            text = "${item.balance.string()} ${item.currencyEntity.symbol}",
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