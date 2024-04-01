@file:OptIn(FlowPreview::class)

package com.kuro.money.presenter.add_transaction.feature.event.feature.select_currency

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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.money.R
import com.kuro.money.data.model.CurrencyEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.SelectionUI
import com.kuro.money.domain.model.screenRoute
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.presenter.account.feature.wallets.AddWalletViewModel
import com.kuro.money.presenter.account.feature.wallets.EditWalletViewModel
import com.kuro.money.presenter.add_transaction.feature.event.feature.add_event.AddEventScreenViewModel
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@Composable
fun SelectCurrencyScreen(
    navController: NavController,
    addEventScreenViewModel: AddEventScreenViewModel,
    selectCurrencyViewModel: SelectCurrencyViewModel = hiltViewModel()
) {
    val isSearching = remember { mutableStateOf(false) }
    val listCurrency = remember { mutableStateListOf<CurrencyEntity>() }
    val fullListCurrency = remember { mutableStateListOf<CurrencyEntity>() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    BackHandler(
        navBackStackEntry?.destination?.route == screenRoute(
            SelectionUI.ADD_TRANSACTION.route,
            SelectionUI.SELECT_CURRENCY.route
        )
    ) {
        if (isSearching.value) {
            isSearching.value = false
            listCurrency.clear()
            listCurrency.addAll(fullListCurrency)
            return@BackHandler
        }
        navController.popBackStack()
    }

    val currentFocus = LocalFocusManager.current
    val selectedCurrency = addEventScreenViewModel.currencySelected.collectAsState().value
    val searchTextFlow = remember { MutableStateFlow("") }

    LaunchedEffect(selectCurrencyViewModel.getListCurrencies.collectAsState().value) {
        selectCurrencyViewModel.getListCurrencies.collectLatest {
            if (it is Resource.Success) {
                listCurrency.clear()
                listCurrency.addAll(it.value)

                fullListCurrency.clear()
                fullListCurrency.addAll(it.value)
            }
        }
    }

    LaunchedEffect(Unit) {
        searchTextFlow
            .debounce(500)
            .collectLatest { str ->
                if (isSearching.value) {
                    listCurrency.clear()
                    listCurrency.addAll(fullListCurrency.filter {
                        it.name.lowercase().contains(str.lowercase())
                    })
                }
            }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ToolBarCurrencyScreen(
                isSearching,
                searchTextFlow,
                onBack = {
                    if (isSearching.value) {
                        isSearching.value = false
                        listCurrency.clear()
                        listCurrency.addAll(fullListCurrency)
                    } else {
                        navController.popBackStack()
                    }
                },
                onSearchDone = { str ->
                    listCurrency.clear()
                    listCurrency.addAll(fullListCurrency.filter {
                        it.name.lowercase().contains(str.lowercase())
                    })
                    // Clear keyboard after press search
                    currentFocus.clearFocus()
                }
            )
            ListCurrencies(listCurrency, selectedCurrency) {
                addEventScreenViewModel.setCurrencySelected(it)
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun SelectCurrencyScreen(
    navController: NavController,
    addWalletViewModel: AddWalletViewModel,
    selectCurrencyViewModel: SelectCurrencyViewModel = hiltViewModel()
) {
    val isSearching = remember { mutableStateOf(false) }
    val listCurrency = remember { mutableStateListOf<CurrencyEntity>() }
    val fullListCurrency = remember { mutableStateListOf<CurrencyEntity>() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    BackHandler(
        navBackStackEntry?.destination?.route == screenRoute(
            SelectionUI.ACCOUNT.route,
            SelectionUI.SELECT_CURRENCY.route
        )
    ) {
        if (isSearching.value) {
            isSearching.value = false
            listCurrency.clear()
            listCurrency.addAll(fullListCurrency)
            return@BackHandler
        }
        navController.popBackStack()
    }

    val currentFocus = LocalFocusManager.current
    val selectedCurrency = addWalletViewModel.currencySelected.collectAsState().value
    val searchTextFlow = remember { MutableStateFlow("") }

    LaunchedEffect(selectCurrencyViewModel.getListCurrencies.collectAsState().value) {
        selectCurrencyViewModel.getListCurrencies.collectLatest {
            if (it is Resource.Success) {
                listCurrency.clear()
                listCurrency.addAll(it.value)

                fullListCurrency.clear()
                fullListCurrency.addAll(it.value)
            }
        }
    }

    LaunchedEffect(Unit) {
        searchTextFlow
            .debounce(500)
            .collectLatest { str ->
                if (isSearching.value) {
                    listCurrency.clear()
                    listCurrency.addAll(fullListCurrency.filter {
                        it.name.lowercase().contains(str.lowercase())
                    })
                }
            }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ToolBarCurrencyScreen(
                isSearching,
                searchTextFlow,
                onBack = {
                    if (isSearching.value) {
                        isSearching.value = false
                        listCurrency.clear()
                        listCurrency.addAll(fullListCurrency)
                    } else {
                        navController.popBackStack()
                    }
                },
                onSearchDone = { str ->
                    listCurrency.clear()
                    listCurrency.addAll(fullListCurrency.filter {
                        it.name.lowercase().contains(str.lowercase())
                    })
                    // Clear keyboard after press search
                    currentFocus.clearFocus()
                }
            )
            ListCurrencies(listCurrency, selectedCurrency) {
                addWalletViewModel.setCurrency(it)
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun SelectCurrencyScreen(
    navController: NavController,
    editWalletViewModel: EditWalletViewModel,
    selectCurrencyViewModel: SelectCurrencyViewModel = hiltViewModel()
) {
    val isSearching = remember { mutableStateOf(false) }
    val listCurrency = remember { mutableStateListOf<CurrencyEntity>() }
    val fullListCurrency = remember { mutableStateListOf<CurrencyEntity>() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    BackHandler {
        if (isSearching.value) {
            isSearching.value = false
            listCurrency.clear()
            listCurrency.addAll(fullListCurrency)
            return@BackHandler
        }
        navController.popBackStack()
    }

    val currentFocus = LocalFocusManager.current
    val selectedCurrency = editWalletViewModel.currencySelected.collectAsState().value
    val searchTextFlow = remember { MutableStateFlow("") }

    LaunchedEffect(selectCurrencyViewModel.getListCurrencies.collectAsState().value) {
        selectCurrencyViewModel.getListCurrencies.collectLatest {
            if (it is Resource.Success) {
                listCurrency.clear()
                listCurrency.addAll(it.value)

                fullListCurrency.clear()
                fullListCurrency.addAll(it.value)
            }
        }
    }

    LaunchedEffect(Unit) {
        searchTextFlow
            .debounce(500)
            .collectLatest { str ->
                if (isSearching.value) {
                    listCurrency.clear()
                    listCurrency.addAll(fullListCurrency.filter {
                        it.name.lowercase().contains(str.lowercase())
                    })
                }
            }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ToolBarCurrencyScreen(
                isSearching,
                searchTextFlow,
                onBack = {
                    if (isSearching.value) {
                        isSearching.value = false
                        listCurrency.clear()
                        listCurrency.addAll(fullListCurrency)
                    } else {
                        navController.popBackStack()
                    }
                },
                onSearchDone = { str ->
                    listCurrency.clear()
                    listCurrency.addAll(fullListCurrency.filter {
                        it.name.lowercase().contains(str.lowercase())
                    })
                    // Clear keyboard after press search
                    currentFocus.clearFocus()
                }
            )
            ListCurrencies(listCurrency, selectedCurrency) {
                editWalletViewModel.setCurrency(it)
                navController.popBackStack()
            }
        }
    }
}

@Composable
private fun ToolBarCurrencyScreen(
    isSearching: MutableState<Boolean>,
    searchTextFlow: MutableStateFlow<String>,
    onBack: () -> Unit,
    onSearchDone: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val isSearchDone = remember { mutableStateOf(false) }

    LaunchedEffect(isSearching.value) {
        if (isSearching.value) {
            focusRequester.requestFocus()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.noRippleClickable { onBack() })
        if (!isSearching.value) {
            Text(
                text = stringResource(id = R.string.currency),
                color = Color.Black,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.noRippleClickable {
                    isSearching.value = true
                }
            )
        } else {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                if (searchTextFlow.value == "") {
                    Text(
                        text = stringResource(id = R.string.currency_hint),
                        color = Color.Black.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.h6
                    )
                }
                BasicTextField(
                    value = searchTextFlow.collectAsState().value,
                    onValueChange = { searchTextFlow.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        isSearchDone.value = true
                        onSearchDone(searchTextFlow.value)
                    }),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                )
            }
            if (isSearchDone.value) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier.noRippleClickable {
                        isSearchDone.value = false
                        searchTextFlow.value = ""
                        focusRequester.requestFocus()
                    }
                )
            }
        }
    }
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gray)
    )
}

@Composable
private fun ListCurrencies(
    list: List<CurrencyEntity>?,
    selectedCurrency: CurrencyEntity?,
    onClick: (CurrencyEntity) -> Unit
) {
    if (list == null) return
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(list, key = { item -> item.id }) { item ->
            ItemCurrency(item = item, itemSelected = selectedCurrency) { onClick(item) }
        }
    }
}

@Composable
private fun ItemCurrency(
    item: CurrencyEntity,
    itemSelected: CurrencyEntity?,
    onClick: (CurrencyEntity) -> Unit
) {
    val isSelected = item == itemSelected
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onClick(item) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Image(painter = item.icon.toPainterResource(), contentDescription = item.name)
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = item.name,
                color = Color.Black,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = item.symbol,
                color = Color.Black.copy(alpha = 0.5f),
                style = MaterialTheme.typography.body2
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (isSelected) {
            Image(
                painter = painterResource(id = R.drawable.ic_check_green),
                contentDescription = "Checked"
            )
        }
    }
}