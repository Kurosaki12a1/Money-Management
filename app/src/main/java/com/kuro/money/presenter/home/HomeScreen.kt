package com.kuro.money.presenter.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kuro.money.R
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.SelectionUI
import com.kuro.money.presenter.utils.string
import com.kuro.money.ui.theme.Gray
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    navController : NavController,
    homeViewModel: HomeViewModel,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    BackHandler(navBackStackEntry?.destination?.route == SelectionUI.HOME.route) {
        navController.popBackStack()
    }

    LaunchedEffect(navController.currentDestination?.route) {
        if (navController.currentDestination?.route == SelectionUI.HOME.route) {
            homeViewModel.getAllWallets()
            homeViewModel.getBalance()
        }
    }

    val listWallets = remember { mutableStateListOf<AccountEntity>() }
    val balance = homeViewModel.balance.collectAsState().value

    LaunchedEffect(Unit) {
        homeViewModel.allWallets.collectLatest {
            if (it is Resource.Success) {
                listWallets.clear()
                listWallets.addAll(it.value)
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_approximation),
                        contentDescription = "Approximation"
                    )
                    Text(
                        text = balance.string(),
                        style = MaterialTheme.typography.h5
                    )
                    Icon(
                        imageVector = Icons.Default.RemoveRedEye,
                        contentDescription = "Hide"
                    )
                }
            }

        }
    }
}