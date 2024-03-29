package com.kuro.money.presenter.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.kuro.money.domain.model.BottomNavItem
import com.kuro.money.domain.model.ScreenSelection
import com.kuro.money.domain.model.generateListBottomNavItem
import com.kuro.money.presenter.account.AccountScreen
import com.kuro.money.presenter.account.AccountViewModel
import com.kuro.money.presenter.account.feature.about.AboutScreen
import com.kuro.money.presenter.account.feature.wallets.AddWalletScreen
import com.kuro.money.presenter.account.feature.wallets.WalletScreen
import com.kuro.money.presenter.add_transaction.AddTransactionScreen
import com.kuro.money.presenter.add_transaction.AddTransactionViewModel
import com.kuro.money.presenter.add_transaction.feature.event.SelectEventScreen
import com.kuro.money.presenter.add_transaction.feature.event.feature.select_currency.SelectCurrencyScreen
import com.kuro.money.presenter.add_transaction.feature.event.feature.select_icon.SelectIconScreen
import com.kuro.money.presenter.add_transaction.feature.note.NoteScreen
import com.kuro.money.presenter.add_transaction.feature.people.SelectPeopleScreen
import com.kuro.money.presenter.add_transaction.feature.select_category.SelectCategoryScreen
import com.kuro.money.presenter.add_transaction.feature.wallet.SelectWalletScreen
import com.kuro.money.presenter.utils.slideHorizontalAnimation
import com.kuro.money.presenter.utils.slideVerticalAnimation
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
private fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navRouteDestination = navBackStackEntry?.destination?.route
    val currentRoute = remember { mutableStateOf(ScreenSelection.HOME_SCREEN.route) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (navRouteDestination == ScreenSelection.HOME_SCREEN.route
                || navRouteDestination == ScreenSelection.TRANSACTION_SCREEN.route
                || navRouteDestination == ScreenSelection.BUDGETS_SCREEN.route
                || navRouteDestination == ScreenSelection.ACCOUNT_SCREEN.route
            ) {
                BottomBar(routeSelected = currentRoute.value) {
                    currentRoute.value = it
                    navController.navigate(currentRoute.value) {
                        launchSingleTop = true
                    }
                }
            }
        },
        floatingActionButton = {
            if (navRouteDestination == ScreenSelection.HOME_SCREEN.route
                || navRouteDestination == ScreenSelection.TRANSACTION_SCREEN.route
                || navRouteDestination == ScreenSelection.BUDGETS_SCREEN.route
                || navRouteDestination == ScreenSelection.ACCOUNT_SCREEN.route
            ) {
                MainFloatingButton {
                    currentRoute.value = it
                    navController.navigate(currentRoute.value) {
                        launchSingleTop = true
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        content = {
            Navigation(navController = navController, paddingValues = it)
        })
}

@Composable
private fun Navigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
    mainViewModel: MainViewModel = viewModel()
) {
    val slideUpAnimation = slideVerticalAnimation()
    val slideHorizontalAnimation = slideHorizontalAnimation()
    NavHost(
        navController = navController, startDestination = ScreenSelection.HOME_SCREEN.route
    ) {
        composable(ScreenSelection.HOME_SCREEN.route) {}
        composable(ScreenSelection.TRANSACTION_SCREEN.route) {}
        navigation(
            startDestination = ScreenSelection.ADD_TRANSACTION_SCREEN.route,
            route = ScreenSelection.SUB_GRAPH_TRANSACTION.route
        ) {
            composable(
                ScreenSelection.ADD_TRANSACTION_SCREEN.route,
                enterTransition = { slideUpAnimation.enterTransition },
                exitTransition = { slideUpAnimation.exitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(ScreenSelection.SUB_GRAPH_TRANSACTION.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                AddTransactionScreen(navController, transactionViewModel)
            }
            composable(ScreenSelection.NOTE_SCREEN.route,
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(ScreenSelection.SUB_GRAPH_TRANSACTION.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                NoteScreen(navController, transactionViewModel)
            }
            composable(ScreenSelection.SELECT_CATEGORY_SCREEN.route,
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(ScreenSelection.SUB_GRAPH_TRANSACTION.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectCategoryScreen(navController, transactionViewModel)
            }
            composable(ScreenSelection.WALLET_SCREEN.route,
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(ScreenSelection.SUB_GRAPH_TRANSACTION.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectWalletScreen(navController, transactionViewModel)
            }
            composable(ScreenSelection.WITH_SCREEN.route,
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(ScreenSelection.SUB_GRAPH_TRANSACTION.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectPeopleScreen(navController, transactionViewModel)
            }
            composable(ScreenSelection.EVENT_SCREEN.route,
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                SelectEventScreen(navController)
            }
        }
        composable(ScreenSelection.BUDGETS_SCREEN.route) {}
        navigation(
            startDestination = ScreenSelection.ACCOUNT_SCREEN.route,
            route = ScreenSelection.SUB_GRAPH_ACCOUNT.route
        ) {
            composable(ScreenSelection.ACCOUNT_SCREEN.route) {
                AccountScreen(navController)
            }
            composable(ScreenSelection.SELECT_CURRENCY_SCREEN.route){
                SelectCurrencyScreen(navController)
            }
            composable(ScreenSelection.SELECT_ICON_SCREEN.route) {
                SelectIconScreen(navController)
            }
            composable(ScreenSelection.ADD_WALLET_SCREEN.route) {
                AddWalletScreen(navController)
            }
            composable(ScreenSelection.MY_WALLET_SCREEN.route) {
                WalletScreen(navController)
            }
            composable(ScreenSelection.MY_ABOUT.route) {
                AboutScreen(navController)
            }
        }
    }
}

@Composable
private fun MainFloatingButton(
    onClick: (String) -> Unit
) {
    FloatingActionButton(
        onClick = { onClick(ScreenSelection.SUB_GRAPH_TRANSACTION.route) },
        backgroundColor = Teal200
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