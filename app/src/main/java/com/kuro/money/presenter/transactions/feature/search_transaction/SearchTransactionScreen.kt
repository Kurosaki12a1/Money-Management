@file:OptIn(FlowPreview::class)

package com.kuro.money.presenter.transactions.feature.search_transaction

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Notes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun SearchTransactionScreen(
    navController: NavController,
    searchTransactionViewModel: SearchTransactionViewModel = hiltViewModel()
) {
    val searchTextFlow = remember { MutableStateFlow("") }
    val isSearching = remember { mutableStateOf(false) }
    val searchText = searchTextFlow.collectAsState().value
    val resultSearch = remember { mutableStateListOf<TransactionEntity>() }

    BackHandler {
        if (isSearching.value) {
            isSearching.value = false
            resultSearch.clear()
            return@BackHandler
        }
        navController.popBackStackWithLifeCycle(NavigationRoute.Transaction.route)
    }

    LaunchedEffect(Unit) {
        searchTextFlow.debounce(300).collectLatest {
            if (isSearching.value) {
                searchTransactionViewModel.getListSearchTransaction(it)
            }
            //    isSearching.value = true
        }
    }

    LaunchedEffect(Unit) {
        searchTransactionViewModel.listSearchTransaction.collectLatest {
            if (it is Resource.Success) {
                resultSearch.clear()
                if (it.value != null) {
                    resultSearch.addAll(it.value)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 20.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.noRippleClickable {
                    navController.popBackStackWithLifeCycle(
                        NavigationRoute.Transaction.route
                    )
                })
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (searchText.isBlank()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.transaction_search_hint),
                        color = Color.Black.copy(0.5f),
                        style = MaterialTheme.typography.body1
                    )
                }
                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = searchText,
                    onValueChange = {
                        searchTextFlow.value = it
                        isSearching.value = true
                    },
                    textStyle = TextStyle(
                        color = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        // Done search
                        onSearch = {
                            isSearching.value = false
                            searchTransactionViewModel.getListSearchTransaction(searchTextFlow.value)
                        }
                    )
                )
            }
            if (isSearching.value && searchText.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "Cancel",
                    modifier = Modifier.noRippleClickable {
                        isSearching.value = false
                        searchTextFlow.value = ""
                    })
            } else {
                Icon(
                    imageVector = Icons.Default.ManageSearch,
                    contentDescription = "Advanced search",
                    modifier = Modifier.noRippleClickable {
                        navController.navigate(NavigationRoute.Transaction.AdvancedSearchTransaction.route) {
                            popUpTo(NavigationRoute.Transaction.route)
                        }
                    })
            }
        }
        if (resultSearch.isEmpty()) {
            EmptyResultScreen()
        } else {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray)
            )
            ResultSearchedScreen(resultSearch) { id ->
                navController.navigate("${NavigationRoute.Home.TransactionDetails.route}/$id")
            }
        }
    }
}

@Composable
private fun ResultSearchedScreen(
    resultSearch: List<TransactionEntity>,
    onClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray),
        contentPadding = PaddingValues(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        val listTransactionOfDayInMonth = resultSearch
            .sortedByDescending { it.displayDate }
            .groupBy { it.displayDate }
        item { SummarySearchedTransactions(resultSearch) }
        items(listTransactionOfDayInMonth.keys.toList(), key = { it.dayOfYear }) { date ->
            ListSearchedTransactions(date, listTransactionOfDayInMonth[date]) { id -> onClick(id) }
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

@Composable
private fun SummarySearchedTransactions(list: List<TransactionEntity>) {
    val decimalFormatter = DecimalFormatter()
    val income =
        list.filter { it.category.type == Constants.INCOME }.sumOf { getBalance(it) }
    val expense =
        list.filter { it.category.type == Constants.EXPENSE }.sumOf { getBalance(it) }
    val symbol = AppCache.defaultCurrencyEntity.value?.symbol ?: ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 10.dp, horizontal = 20.dp),
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
    }
}
