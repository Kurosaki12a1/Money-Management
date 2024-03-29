package com.kuro.money.presenter.add_transaction.feature.event.feature.select_icon

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuro.money.R
import com.kuro.money.domain.model.SelectionUI
import com.kuro.money.domain.model.screenRoute
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.presenter.account.feature.wallets.AddWalletViewModel
import com.kuro.money.presenter.add_transaction.feature.event.feature.add_event.AddEventScreenViewModel
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray

@Composable
fun SelectIconScreen(
    navController: NavController,
    addEventScreenViewModel: AddEventScreenViewModel,
    selectIconScreenViewModel: SelectIconScreenViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    BackHandler(
        enabled = navBackStackEntry?.destination?.route == screenRoute(
            SelectionUI.ADD_TRANSACTION.route,
            SelectionUI.SELECT_ICON.route
        )
    ) {
        navController.popBackStack()
    }

    val listIcons = selectIconScreenViewModel.getListIcon.collectAsState().value

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back",
                    modifier = Modifier.noRippleClickable {
                        navController.popBackStack()
                    })
                Text(
                    text = stringResource(id = R.string.select_icon),
                    color = Color.Black,
                    style = MaterialTheme.typography.h6
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
                contentPadding = PaddingValues(20.dp)
            ) {
                items(listIcons.chunked(5)) { item ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        item.forEach {
                            Image(
                                modifier = Modifier
                                    .weight(1f)
                                    .noRippleClickable {
                                        addEventScreenViewModel.setIconSelected(it)
                                        navController.popBackStack()
                                    },
                                painter = it.toPainterResource(),
                                contentDescription = it
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectIconScreen(
    navController: NavController,
    addWalletViewModel: AddWalletViewModel,
    selectIconScreenViewModel: SelectIconScreenViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    BackHandler(
        enabled = navBackStackEntry?.destination?.route == screenRoute(
            SelectionUI.ACCOUNT.route,
            SelectionUI.SELECT_ICON.route
        )
    ) {
        navController.popBackStack()
    }

    val listIcons = selectIconScreenViewModel.getListIcon.collectAsState().value

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back",
                    modifier = Modifier.noRippleClickable {
                        navController.popBackStack()
                    })
                Text(
                    text = stringResource(id = R.string.select_icon),
                    color = Color.Black,
                    style = MaterialTheme.typography.h6
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
                contentPadding = PaddingValues(20.dp)
            ) {
                items(listIcons.chunked(5)) { item ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        item.forEach {
                            Image(
                                modifier = Modifier
                                    .weight(1f)
                                    .noRippleClickable {
                                        addWalletViewModel.setIcon(it)
                                        navController.popBackStack()
                                    },
                                painter = it.toPainterResource(),
                                contentDescription = it
                            )
                        }
                    }
                }
            }
        }
    }
}