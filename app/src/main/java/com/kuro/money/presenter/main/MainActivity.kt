package com.kuro.money.presenter.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kuro.money.R
import com.kuro.money.domain.model.BottomNavItem
import com.kuro.money.domain.model.ScreenSelection
import com.kuro.money.domain.model.generateListBottomNavItem
import com.kuro.money.presenter.add_transaction.AddTransactionScreen
import com.kuro.money.presenter.utils.SlideUpContent
import com.kuro.money.presenter.utils.customEnterTransition
import com.kuro.money.presenter.utils.customExitTransition
import com.kuro.money.ui.theme.Teal200
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
private fun MainScreen(
    vm: MainViewModel = viewModel()
) {
    val navController = rememberNavController()
    val homeRoute = stringResource(id = R.string.home)
    val navigateScreen = vm.navigateScreenTo.collectAsState().value
    val currentRoute = remember { mutableStateOf(homeRoute) }
    Scaffold(bottomBar = {
        BottomBar(routeSelected = currentRoute.value) {
            currentRoute.value = it
            navController.navigate(currentRoute.value) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    },
        floatingActionButton = {
            MainFloatingButton {
                vm.setOpenAddTransactionScreen(true)
                currentRoute.value = it
                navController.navigate(currentRoute.value) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        content = {
            Navigation(navController = navController, paddingValues = it)
        })
    SlideUpContent(navigateScreen == ScreenSelection.ADD_TRANSACTION_SCREEN) {
        AddTransactionScreen()
    }
}

@Composable
private fun Navigation(
    navController: NavHostController, paddingValues: PaddingValues
) {
    val homeDestination = stringResource(id = R.string.home)
    val transactionDestination = stringResource(id = R.string.transactions)
    val addTransactionDestination = stringResource(id = R.string.add)
    val budgetDestination = stringResource(id = R.string.budget)
    val accountDestination = stringResource(id = R.string.account)
    NavHost(
        navController = navController, startDestination = homeDestination
    ) {
        composable(homeDestination,
            enterTransition = { customEnterTransition(offsetX = 1000, duration = 700) },
            exitTransition = { customExitTransition(offsetX = -1000, duration = 700) },
            popEnterTransition = { customEnterTransition(offsetX = -1000, duration = 700) },
            popExitTransition = { customExitTransition(offsetX = 1000, duration = 700) }) {

        }
        composable(transactionDestination,
            enterTransition = { customEnterTransition(offsetX = 1000, duration = 700) },
            exitTransition = { customExitTransition(offsetX = -1000, duration = 700) },
            popEnterTransition = { customEnterTransition(offsetX = -1000, duration = 700) },
            popExitTransition = { customExitTransition(offsetX = 1000, duration = 700) }) {}
        composable(addTransactionDestination) {
            // Do nothing
        }
        composable(budgetDestination,
            enterTransition = { customEnterTransition(offsetX = 1000, duration = 700) },
            exitTransition = { customExitTransition(offsetX = -1000, duration = 700) },
            popEnterTransition = { customEnterTransition(offsetX = -1000, duration = 700) },
            popExitTransition = { customExitTransition(offsetX = 1000, duration = 700) }) {

        }
        composable(accountDestination,
            enterTransition = { customEnterTransition(offsetX = 1000, duration = 700) },
            exitTransition = { customExitTransition(offsetX = -1000, duration = 700) },
            popEnterTransition = { customEnterTransition(offsetX = -1000, duration = 700) },
            popExitTransition = { customExitTransition(offsetX = 1000, duration = 700) }) {}
    }
}

@Composable
private fun MainFloatingButton(
    onClick: (String) -> Unit
) {
    val homeRoute = stringResource(id = R.string.home)
    FloatingActionButton(
        onClick = { onClick(homeRoute) }, backgroundColor = Teal200
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add transaction",
            tint = Color.White
        )
    }
}

@Composable
private fun BottomBar(
    routeSelected: String, onClick: (String) -> Unit
) {
    val listItems = generateListBottomNavItem(LocalContext.current)
    BottomAppBar(
        cutoutShape = MaterialTheme.shapes.large.copy(CornerSize(36.dp)),
        contentColor = Color.White,
        backgroundColor = Color.White
    ) {
        BottomNavigation(
            backgroundColor = Color.White, contentColor = Color.Black
        ) {
            listItems.forEach { item ->
                BottomNavItem(item, routeSelected, onClick)
            }
        }
    }
}

@Composable
private fun RowScope.BottomNavItem(
    item: BottomNavItem, routeSelected: String, onClick: (String) -> Unit
) {
    val isSelected = routeSelected == item.route
    BottomNavigationItem(selected = isSelected, onClick = { onClick(item.route) }, icon = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (item.badgeCount > 0) {
                BadgedBox(badge = {
                    Text(text = item.badgeCount.toString())
                }) {
                    if (item.icon != null) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name,
                            tint = if (isSelected) Color.Black else Color.Black.copy(
                                alpha = 0.3f
                            )
                        )
                    }
                }
            } else {
                if (item.icon != null) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.name,
                        tint = if (isSelected) Color.Black else Color.Black.copy(
                            alpha = 0.3f
                        )
                    )
                }
            }
        }
    }, label = {
        Text(
            text = item.name,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = if (isSelected) Color.Black else Color.Black.copy(alpha = 0.3f),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }, alwaysShowLabel = true
    )
}