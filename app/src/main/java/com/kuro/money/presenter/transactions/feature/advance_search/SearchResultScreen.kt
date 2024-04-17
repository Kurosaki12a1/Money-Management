package com.kuro.money.presenter.transactions.feature.advance_search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Notes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kuro.customimagevector.EmptyBox
import com.kuro.money.R
import com.kuro.money.constants.Constants
import com.kuro.money.data.AppCache
import com.kuro.money.data.model.TransactionEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.navigation.routes.NavigationRoute
import com.kuro.money.presenter.utils.DecimalFormatter
import com.kuro.money.presenter.utils.getBalance
import com.kuro.money.presenter.utils.popBackStackWithLifeCycle
import com.kuro.money.presenter.utils.string
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun SearchResultScreen(
    navController: NavController,
    viewModel: AdvanceSearchViewModel
) {
    BackHandler {
        navController.popBackStackWithLifeCycle(NavigationRoute.Transaction.route)
    }

    val result = remember { mutableStateListOf<TransactionEntity>() }
    val symbol = remember { mutableStateOf("") }
    LaunchedEffect(true) {
        viewModel.searchTransaction.collectLatest {
            if (it is Resource.Success) {
                result.clear()
                result.addAll(it.value)
            }
        }
    }

    LaunchedEffect(Unit) {
        AppCache.defaultCurrencyEntity.collectLatest {
            if (it != null) symbol.value = it.symbol
        }
    }

    val decimalFormatter = DecimalFormatter()
    val income =
        result.filter { it.category.type == Constants.INCOME }.sumOf { getBalance(it) }
    val expense =
        result.filter { it.category.type == Constants.EXPENSE }.sumOf { getBalance(it) }
    val balance = income - expense

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.noRippleClickable {
                    navController.popBackStackWithLifeCycle(NavigationRoute.Transaction.route)
                })
            Text(
                text = stringResource(id = R.string.search_result), color = Color.Black,
                style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.noRippleClickable {
                    navController.navigate(NavigationRoute.Transaction.SearchTransaction.route)
                }
            )
            Icon(
                imageVector = Icons.Default.ManageSearch,
                contentDescription = "Advanced search",
                modifier = Modifier.noRippleClickable {
                    navController.popBackStackWithLifeCycle()
                })
        }
        if (result.isEmpty()) {
            EmptyResultScreen()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Gray)
                    .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "${result.size} Results",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.income),
                                modifier = Modifier.weight(1f),
                                color = Color.Black.copy(0.7f),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${decimalFormatter.formatForVisual(income.string())} ${symbol.value}",
                                color = Color.Cyan,
                                style = MaterialTheme.typography.body1
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.expense),
                                modifier = Modifier.weight(1f),
                                color = Color.Black.copy(0.7f),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${decimalFormatter.formatForVisual(expense.string())} ${symbol.value}",
                                color = Color.Red,
                                style = MaterialTheme.typography.body1
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.total_balance),
                                modifier = Modifier.weight(1f),
                                color = Color.Black.copy(0.7f),
                                style = MaterialTheme.typography.body1,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${decimalFormatter.formatForVisual(balance.string())} ${symbol.value}",
                                color = Color.Black,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
                val listTransactionOfDayInMonth = result
                    .sortedByDescending { it.displayDate }
                    .groupBy { it.displayDate }
                items(listTransactionOfDayInMonth.keys.toList(), key = { it.dayOfYear }) { date ->
                    ListSearchedTransactions(date, listTransactionOfDayInMonth[date]) { id ->
                        navController.navigate("${NavigationRoute.Home.TransactionDetails.route}/$id")
                    }
                }
            }
        }
    }
}

@Composable
private fun ListSearchedTransactions(
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
                    text = date.dayOfWeek.getDisplayName(
                        java.time.format.TextStyle.FULL,
                        Locale.getDefault()
                    ),
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
                            imageVector = Icons.Default.Notes,
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
private fun EmptyResultScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(imageVector = EmptyBox, contentDescription = "EmptyBox")
        Text(
            text = stringResource(id = R.string.no_result),
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = stringResource(id = R.string.no_result_search_hint),
            color = Color.Black.copy(0.5f),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}
