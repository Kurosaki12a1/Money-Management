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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.money.R
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.ScreenSelection
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import com.kuro.money.ui.theme.Teal200
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@Composable
fun WalletScreen(
    navController: NavController,
    walletViewModel: WalletViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val listWallet = remember { mutableStateListOf<AccountEntity>() }
    val fullListWallet = remember { mutableStateListOf<AccountEntity>() }
    val isSearching = remember { mutableStateOf(false) }
    val searchTextFlow = remember { MutableStateFlow("") }

    BackHandler(enabled = navBackStackEntry?.destination?.route == ScreenSelection.MY_WALLET_SCREEN.route) {
        if (isSearching.value) {
            isSearching.value = false
            listWallet.clear()
            listWallet.addAll(fullListWallet)
            return@BackHandler
        }
        navController.popBackStack()
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

    LaunchedEffect(walletViewModel.getWallets.collectAsState().value) {
        walletViewModel.getWallets.collectLatest { data ->
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
                modifier = Modifier.clickable {
                    navController.popBackStack()
                })
            if (isSearching.value) {
                BasicTextField(
                    value = searchTextFlow.collectAsState().value,
                    onValueChange = { searchTextFlow.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                )
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
                Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(listWallet, key = { it.id }) { item ->
                WalletItem(item = item) {

                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.End)
        ) {
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                backgroundColor = Teal200,
                onClick = {
                    navController.navigate(ScreenSelection.ADD_WALLET_SCREEN.route)
                }) {
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

@Composable
private fun WalletItem(
    item: AccountEntity,
    onClick: (AccountEntity) -> Unit
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
                text = "${item.balance} ${item.currencyEntity.symbol}",
                style = MaterialTheme.typography.body2,
                color = Color.Black.copy(alpha = 0.3f)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
    }
}

@Composable
fun AddWalletScreen(
    navController: NavController,
    walletViewModel: WalletViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    BackHandler(navBackStackEntry?.destination?.route == ScreenSelection.ADD_WALLET_SCREEN.route) {
        navController.popBackStack()
    }

    val nameWallet = remember { mutableStateOf("") }
    val currencySelected = walletViewModel.currencySelected.collectAsState().value
    val balance = remember { mutableStateOf(0f) }
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
                    navController.popBackStack()
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
                style = MaterialTheme.typography.body1
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
                Image(painter = painterResource(id = R.drawable.icon), contentDescription = "Icon",
                    modifier = Modifier.noRippleClickable {
                        navController.navigate(ScreenSelection.SELECT_ICON_SCREEN.route)
                    })
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (nameWallet.value.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.name),
                            style = MaterialTheme.typography.h6,
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                    }
                    BasicTextField(
                        value = nameWallet.value,
                        onValueChange = { nameWallet.value = it },
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    )
                }
            }
            /* Currency Selection */
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                BasicTextField(
                    value = "${currencySelected?.symbol ?: ""} ${balance.value}",
                    onValueChange = { balance.value = it.toFloat() },
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