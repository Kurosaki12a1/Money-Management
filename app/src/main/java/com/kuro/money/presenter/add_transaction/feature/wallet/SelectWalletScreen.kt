package com.kuro.money.presenter.add_transaction.feature.wallet

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.money.R
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.ScreenSelection
import com.kuro.money.presenter.add_transaction.AddTransactionViewModel
import com.kuro.money.presenter.add_transaction.feature.event.feature.add_event.AddEventScreenViewModel
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SelectWalletScreen(
    navController: NavController,
    addTransactionViewModel: AddTransactionViewModel = hiltViewModel(),
    selectWalletViewModel: SelectWalletViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    BackHandler(enabled = navBackStackEntry?.destination?.route == ScreenSelection.WALLET_SCREEN.route) {
        navController.popBackStack()
    }

    val listWallet = remember { mutableStateListOf<AccountEntity>() }

    val walletValue = addTransactionViewModel.wallet.collectAsState().value
    val selectedWallet = remember { mutableStateOf(walletValue) }

    LaunchedEffect(selectWalletViewModel.getAccountUseCase.collectAsState().value) {
        selectWalletViewModel.getAccountUseCase.collectLatest {
            if (it is Resource.Success) {
                listWallet.clear()
                listWallet.addAll(it.value)
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
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
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    })
                Text(
                    text = stringResource(id = R.string.select_wallet),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
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
                    text = stringResource(id = R.string.included_in_total),
                    style = MaterialTheme.typography.body1,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listWallet) {
                    WalletItem(item = it, isSelected = it == selectedWallet.value) {
                        addTransactionViewModel.setWallet(it)
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}

@Composable
fun SelectWalletScreen(
    addEventScreenViewModel: AddEventScreenViewModel = hiltViewModel(),
    selectWalletViewModel: SelectWalletViewModel = hiltViewModel()
) {
    BackHandler(enabled = addEventScreenViewModel.enableChildScreen.collectAsState().value == ScreenSelection.WALLET_SCREEN) {
        addEventScreenViewModel.setOpenWalletScreen(false)
    }

    val listWallet = remember { mutableStateListOf<AccountEntity>() }

    val walletValue = addEventScreenViewModel.wallet.collectAsState().value
    val selectedWallet = remember { mutableStateOf(walletValue) }

    LaunchedEffect(selectWalletViewModel.getAccountUseCase.collectAsState().value) {
        selectWalletViewModel.getAccountUseCase.collectLatest {
            if (it is Resource.Success) {
                listWallet.clear()
                listWallet.addAll(it.value)
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
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
                    modifier = Modifier.clickable {
                        addEventScreenViewModel.setOpenWalletScreen(false)
                    })
                Text(
                    text = stringResource(id = R.string.select_wallet),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
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
                    text = stringResource(id = R.string.included_in_total),
                    style = MaterialTheme.typography.body1,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listWallet) {
                    WalletItem(item = it, isSelected = it == selectedWallet.value) {item ->
                        addEventScreenViewModel.setWallet(item)
                        addEventScreenViewModel.setOpenWalletScreen(false)
                    }
                }
            }
        }
    }
}


@Composable
private fun WalletItem(
    item: AccountEntity,
    isSelected: Boolean,
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
                text = "${item.balance} ${item.currencyEntity.symbol}" ,
                style = MaterialTheme.typography.body2,
                color = Color.Black.copy(alpha = 0.3f)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (isSelected) {
            Image(
                painter = painterResource(id = R.drawable.ic_check_green),
                contentDescription = "Check"
            )
        }
    }
}