package com.kuro.money.presenter.account.feature.wallets

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.kuro.money.R
import com.kuro.money.data.AppCache
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.WalletOptions
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.navigation.routes.NavigationGraphRoute
import com.kuro.money.navigation.routes.NavigationRoute
import com.kuro.money.presenter.home.MyWalletViewModel
import com.kuro.money.presenter.report.ReportViewModel
import com.kuro.money.presenter.transactions.TransactionViewModel
import com.kuro.money.presenter.utils.popBackStackWithLifeCycle
import com.kuro.money.presenter.utils.string
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import com.kuro.money.ui.theme.Teal200
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

@OptIn(FlowPreview::class)
@Composable
fun WalletScreen(
    navController: NavController,
    walletViewModel: WalletViewModel,
    editWalletViewModel: EditWalletViewModel
) {
    LaunchedEffect(navController.currentDestination?.route) {
        if (navController.currentDestination?.route == NavigationRoute.Account.Wallet.route) {
            walletViewModel.getWallets()
        }
    }

    val listWallet = remember { mutableStateListOf<AccountEntity>() }
    val fullListWallet = remember { mutableStateListOf<AccountEntity>() }
    val isSearching = remember { mutableStateOf(false) }
    val searchTextFlow = remember { MutableStateFlow("") }
    val isClickAllowed = remember { AtomicBoolean(true) }
    val shouldShowMoreOption = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf(0L) }
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()

    BackHandler {
        if (isSearching.value) {
            isSearching.value = false
            listWallet.clear()
            listWallet.addAll(fullListWallet)
            return@BackHandler
        }
        navController.popBackStackWithLifeCycle()
    }

    LaunchedEffect(isSearching.value) {
        if (isSearching.value)
            focusRequester.requestFocus()
    }

    LaunchedEffect(Unit) {
        searchTextFlow
            .debounce(500)
            .collectLatest { str ->
                if (isSearching.value) {
                    listWallet.clear()
                    listWallet.addAll(fullListWallet.filter {
                        it.name.lowercase().contains(str.lowercase())
                    })
                }
            }
    }

    LaunchedEffect(Unit) {
        walletViewModel.getWallets.collect { data ->
            if (data is Resource.Success) {
                listWallet.clear()
                fullListWallet.clear()
                data.value?.let {
                    listWallet.addAll(it)
                    fullListWallet.addAll(it)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        walletViewModel.deleteWallet.distinctUntilChanged { old, new ->
            if (old is Resource.Success && new is Resource.Success) {
                new.value != old.value
            } else {
                true
            }
        }.collect {
            if (it is Resource.Success) {
                walletViewModel.getWallets()
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
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable {
                    navController.popBackStackWithLifeCycle()
                })
            if (isSearching.value) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (searchTextFlow.collectAsState().value.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.search),
                            color = Color.Black.copy(0.5f),
                            style = MaterialTheme.typography.h6
                        )
                    }
                    BasicTextField(
                        value = searchTextFlow.collectAsState().value,
                        modifier = Modifier.focusRequester(focusRequester),
                        onValueChange = { searchTextFlow.value = it },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier.noRippleClickable {
                        searchTextFlow.value = ""
                    }
                )
            } else {
                Text(
                    text = stringResource(id = R.string.my_wallets),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.noRippleClickable { isSearching.value = true }
                )
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.included_in_total),
                style = MaterialTheme.typography.body1,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listWallet, key = { it.id }) { item ->
                    WalletItem(
                        item = item,
                        shouldShowMoreOptions = item.id == selectedItem.value && shouldShowMoreOption.value,
                        onClickMoreOption = {
                            shouldShowMoreOption.value = true
                            selectedItem.value = item.id
                        },
                        onDismissRequest = {
                            shouldShowMoreOption.value = false
                            selectedItem.value = 0L
                        },
                        onClick = {
                            if (isClickAllowed.getAndSet(false)) {
                                editWalletViewModel.getWalletById(it.id)
                                navController.navigate(NavigationGraphRoute.AccountGraph.EditWalletGraph.route)
                                scope.launch {
                                    delay(200)
                                    isClickAllowed.set(true)
                                }
                            }
                        },
                        onSelectMoreOption = { wallet, option ->
                            when (option) {
                                WalletOptions.TRANSFER_MONEY -> {
                                    // TODO
                                }

                                WalletOptions.EDIT -> {
                                    editWalletViewModel.getWalletById(wallet.id)
                                    navController.navigate(NavigationGraphRoute.AccountGraph.EditWalletGraph.route)
                                }

                                WalletOptions.ARCHIVE -> {
                                    // TODO
                                }

                                else -> {
                                    walletViewModel.deleteWalletById(wallet.id)
                                }
                            }
                        })
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(20.dp)
                    .align(Alignment.BottomEnd)
            ) {
                FloatingActionButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    backgroundColor = Teal200,
                    onClick = { navController.navigate(NavigationGraphRoute.AccountGraph.AddWalletGraph.route) }) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WalletScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel
) {
    BackHandler { navController.popBackStackWithLifeCycle() }

    if (navController.currentDestination?.route == NavigationRoute.Home.Wallet.route) {
        transactionViewModel.getAllWallets()
        transactionViewModel.getBalance()
    }

    val listWallet = remember { mutableStateListOf<AccountEntity>() }
    val balance = transactionViewModel.balance.collectAsState().value

    LaunchedEffect(Unit) {
        transactionViewModel.allWallets.collect { data ->
            if (data is Resource.Success) {
                listWallet.clear()
                data.value.let { listWallet.addAll(it) }
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
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable {
                    navController.popBackStackWithLifeCycle()
                })

            Text(
                text = stringResource(id = R.string.my_wallets),
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Search",
                modifier = Modifier.noRippleClickable {
                    navController.navigate(NavigationRoute.Account.Wallet.route)
                }
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Gray)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp)
                .noRippleClickable {
                    transactionViewModel.setSelectedWallet(transactionViewModel.globalWallet.value!!)
                    navController.popBackStackWithLifeCycle()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(painterResource(id = R.drawable.ic_category_all), contentDescription = "All")
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = stringResource(id = R.string.total),
                    style = MaterialTheme.typography.body1,
                    color = Color.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_approximation),
                        contentDescription = "Approximation"
                    )
                    Text(
                        text = "${balance.string()} ${AppCache.defaultCurrencyEntity.value?.symbol ?: ""}",
                        style = MaterialTheme.typography.body2,
                        color = Color.Black.copy(0.5f)
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.included_in_total),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listWallet, key = { it.id }) { item ->
                    WalletItem(item) {
                        transactionViewModel.setSelectedWallet(item)
                        navController.popBackStackWithLifeCycle()
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(20.dp)
                    .align(Alignment.BottomEnd)
            ) {
                FloatingActionButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    backgroundColor = Teal200,
                    onClick = { navController.navigate(NavigationRoute.Account.Wallet.AddWallet.route) }) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun WalletScreen(
    navController: NavController,
    reportViewModel: ReportViewModel
) {
    BackHandler { navController.popBackStackWithLifeCycle() }

    if (navController.currentDestination?.route == NavigationRoute.Home.Wallet.route) {
        reportViewModel.getAllWallets()
        reportViewModel.getBalance()
    }

    val listWallet = remember { mutableStateListOf<AccountEntity>() }
    val balance = reportViewModel.balance.collectAsState().value

    LaunchedEffect(Unit) {
        reportViewModel.allWallets.collect { data ->
            if (data is Resource.Success) {
                listWallet.clear()
                data.value.let { listWallet.addAll(it) }
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
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable {
                    navController.popBackStackWithLifeCycle()
                })

            Text(
                text = stringResource(id = R.string.my_wallets),
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Search",
                modifier = Modifier.noRippleClickable {
                    navController.navigate(NavigationRoute.Account.Wallet.route)
                }
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Gray)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp)
                .noRippleClickable {
                    reportViewModel.setSelectedWallet(reportViewModel.globalWallet.value!!)
                    navController.popBackStackWithLifeCycle()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(painterResource(id = R.drawable.ic_category_all), contentDescription = "All")
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = stringResource(id = R.string.total),
                    style = MaterialTheme.typography.body1,
                    color = Color.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_approximation),
                        contentDescription = "Approximation"
                    )
                    Text(
                        text = "${balance.string()} ${AppCache.defaultCurrencyEntity.value?.symbol ?: ""}",
                        style = MaterialTheme.typography.body2,
                        color = Color.Black.copy(0.5f)
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.included_in_total),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listWallet, key = { it.id }) { item ->
                    WalletItem(item) {
                        reportViewModel.setSelectedWallet(item)
                        navController.popBackStackWithLifeCycle()
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(20.dp)
                    .align(Alignment.BottomEnd)
            ) {
                FloatingActionButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    backgroundColor = Teal200,
                    onClick = { navController.navigate(NavigationRoute.Account.Wallet.AddWallet.route) }) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WalletScreen(
    navController: NavController,
    myWalletViewModel: MyWalletViewModel
) {
    BackHandler { navController.popBackStackWithLifeCycle() }

    if (navController.currentDestination?.route == NavigationRoute.Home.Wallet.route) {
        myWalletViewModel.getAllWallets()
        myWalletViewModel.getBalance()
    }

    val listWallet = remember { mutableStateListOf<AccountEntity>() }
    val balance = myWalletViewModel.balance.collectAsState().value

    LaunchedEffect(Unit) {
        myWalletViewModel.allWallets.collect { data ->
            if (data is Resource.Success) {
                listWallet.clear()
                data.value.let { listWallet.addAll(it) }
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
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable {
                    navController.popBackStackWithLifeCycle()
                })

            Text(
                text = stringResource(id = R.string.my_wallets),
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Search",
                modifier = Modifier.noRippleClickable {
                    navController.navigate(NavigationRoute.Account.Wallet.route)
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.my_wallet_hint),
                style = MaterialTheme.typography.body2,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(painterResource(id = R.drawable.ic_category_all), contentDescription = "All")
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    text = stringResource(id = R.string.total),
                    style = MaterialTheme.typography.body1,
                    color = Color.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_approximation),
                        contentDescription = "Approximation"
                    )
                    Text(
                        text = "${balance.string()} ${AppCache.defaultCurrencyEntity.value?.symbol ?: ""}",
                        style = MaterialTheme.typography.body2,
                        color = Color.Black.copy(0.5f)
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.included_in_total),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listWallet, key = { it.id }) { item ->
                    WalletItem(item = item)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(20.dp)
                    .align(Alignment.BottomEnd)
            ) {
                FloatingActionButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    backgroundColor = Teal200,
                    onClick = { navController.navigate(NavigationRoute.Account.Wallet.AddWallet.route) }) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun WalletItem(
    item: AccountEntity,
    shouldShowMoreOptions: Boolean,
    onDismissRequest: () -> Unit,
    onClick: (AccountEntity) -> Unit,
    onClickMoreOption: () -> Unit,
    onSelectMoreOption: (AccountEntity, WalletOptions) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(item) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Image(painter = item.icon.toPainterResource(), contentDescription = item.name)
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
            Text(
                text = "${item.balance} ${item.currency.symbol}",
                style = MaterialTheme.typography.body2,
                color = Color.Black.copy(alpha = 0.3f)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Box {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                modifier = Modifier.noRippleClickable { onClickMoreOption() }
            )
            MoreOptions(
                isExpanded = shouldShowMoreOptions,
                onDismissRequest = { onDismissRequest() }) {
                onSelectMoreOption(item, it)
            }
        }
    }
}

@Composable
private fun WalletItem(
    item: AccountEntity,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Image(painter = item.icon.toPainterResource(), contentDescription = item.name)
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
            Text(
                text = "${item.balance} ${item.currency.symbol}",
                style = MaterialTheme.typography.body2,
                color = Color.Black.copy(alpha = 0.3f)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun AddWalletScreen(
    navController: NavController,
    addWalletViewModel: AddWalletViewModel
) {
    BackHandler { navController.popBackStackWithLifeCycle() }

    LaunchedEffect(Unit) {
        addWalletViewModel.insertWallet.collectLatest {
            if (it is Resource.Success) {
                navController.popBackStackWithLifeCycle()
            }
        }
    }

    val nameWallet = addWalletViewModel.name.collectAsState().value
    val iconSelected = addWalletViewModel.iconSelected.collectAsState().value
    val currencySelected = addWalletViewModel.currencySelected.collectAsState().value
    val balance = addWalletViewModel.balance.collectAsState().value
    val isTickEnableNotification = remember { mutableStateOf(false) }
    val isTickExcludeFromTotal = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray)
    ) {
        /* ToolBar */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.noRippleClickable {
                    navController.popBackStackWithLifeCycle()
                }
            )
            Text(
                text = stringResource(id = R.string.add_wallet),
                style = MaterialTheme.typography.h6,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.save),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.noRippleClickable { addWalletViewModel.submit() }
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            /* Wallet name and icon */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(painter = iconSelected.toPainterResource(), contentDescription = "Icon",
                    modifier = Modifier.noRippleClickable {
                        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                            navController.navigate(NavigationRoute.Account.Wallet.AddWallet.SelectIcon.route)
                        }
                    })
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (nameWallet.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.name),
                            style = MaterialTheme.typography.h6,
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                    }
                    BasicTextField(
                        value = nameWallet,
                        onValueChange = { addWalletViewModel.setName(it) },
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    )
                }
            }
            /* Currency Selection */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                        navController.navigate(NavigationRoute.Account.Wallet.AddWallet.SelectCurrency.route)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (currencySelected == null) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_w_credit),
                        contentDescription = "Currency",
                        colorFilter = ColorFilter.tint(Color.Black),
                    )
                    Text(
                        text = stringResource(id = R.string.currency),
                        color = Color.Black.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.body1
                    )
                } else {
                    Image(
                        painter = currencySelected.icon.toPainterResource(),
                        contentDescription = currencySelected.name
                    )
                    Text(
                        text = currencySelected.name,
                        color = Color.Black.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            /* Initial Balance */
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(50.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.initial_balance),
                    color = Color.Black.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.body1
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(text = currencySelected?.symbol ?: "")
                    BasicTextField(
                        value = "${balance ?: ""}",
                        onValueChange = { addWalletViewModel.setBalance(it.toLong()) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            color = Color.Black.copy(0.5f),
                            fontSize = 18.sp
                        )
                    )
                }

            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 20.dp, horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(end = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.enable_notifications),
                        color = Color.Black,
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = stringResource(id = R.string.enable_notifications_hint),
                        color = Color.Black.copy(0.5f),
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    modifier = Modifier.width(20.dp),
                    checked = isTickEnableNotification.value,
                    onCheckedChange = {
                        isTickEnableNotification.value = it
                    })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(end = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.exclude_from_total),
                        color = Color.Black,
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = stringResource(id = R.string.exclude_from_total_hint),
                        color = Color.Black.copy(0.5f),
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    modifier = Modifier.width(20.dp),
                    checked = isTickExcludeFromTotal.value,
                    onCheckedChange = { isTickExcludeFromTotal.value = it })
            }
        }
    }
}

@Composable
fun MoreOptions(
    isExpanded: Boolean,
    onDismissRequest: () -> Unit,
    onClick: (WalletOptions) -> Unit
) {
    val items = WalletOptions.entries
    listOf(
        stringResource(id = R.string.transfer_money),
        stringResource(id = R.string.edit),
        stringResource(id = R.string.archive),
        stringResource(id = R.string.delete)
    )
    DropdownMenu(expanded = isExpanded,
        onDismissRequest = { onDismissRequest() }) {
        items.forEachIndexed { _, walletOption ->
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .noRippleClickable { onClick(walletOption) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(imageVector = walletOption.icon, contentDescription = walletOption.value)
                Text(
                    text = walletOption.value,
                    color = Color.Black,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}