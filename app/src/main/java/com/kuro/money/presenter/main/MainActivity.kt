package com.kuro.money.presenter.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.kuro.money.domain.model.BottomNavItem
import com.kuro.money.domain.model.SelectionUI
import com.kuro.money.domain.model.generateListBottomNavItem
import com.kuro.money.domain.model.screenRoute
import com.kuro.money.navigation.graph.RootNavGraph
import com.kuro.money.navigation.routes.NavigationGraphRoute
import com.kuro.money.presenter.account.AccountScreen
import com.kuro.money.presenter.account.feature.about.AboutScreen
import com.kuro.money.presenter.account.feature.wallets.AddWalletScreen
import com.kuro.money.presenter.account.feature.wallets.AddWalletViewModel
import com.kuro.money.presenter.account.feature.wallets.EditWalletScreen
import com.kuro.money.presenter.account.feature.wallets.EditWalletViewModel
import com.kuro.money.presenter.account.feature.wallets.WalletScreen
import com.kuro.money.presenter.account.feature.wallets.WalletViewModel
import com.kuro.money.presenter.add_transaction.AddTransactionScreen
import com.kuro.money.presenter.add_transaction.AddTransactionViewModel
import com.kuro.money.presenter.add_transaction.feature.event.SelectEventScreen
import com.kuro.money.presenter.add_transaction.feature.event.feature.add_event.AddEventScreen
import com.kuro.money.presenter.add_transaction.feature.event.feature.add_event.AddEventScreenViewModel
import com.kuro.money.presenter.add_transaction.feature.event.feature.select_currency.SelectCurrencyScreen
import com.kuro.money.presenter.add_transaction.feature.event.feature.select_icon.SelectIconScreen
import com.kuro.money.presenter.add_transaction.feature.note.NoteScreen
import com.kuro.money.presenter.add_transaction.feature.people.SelectPeopleScreen
import com.kuro.money.presenter.add_transaction.feature.select_category.SelectCategoryScreen
import com.kuro.money.presenter.add_transaction.feature.wallet.SelectWalletScreen
import com.kuro.money.presenter.home.HomeScreen
import com.kuro.money.presenter.home.HomeViewModel
import com.kuro.money.presenter.utils.slideHorizontalAnimation
import com.kuro.money.presenter.utils.slideVerticalAnimation
import com.kuro.money.ui.theme.Teal200
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Init
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            mainViewModel
            MainScreen()
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navRouteDestination = navBackStackEntry?.destination?.route
    val currentRoute = remember { mutableStateOf(SelectionUI.HOME.route) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (navRouteDestination == SelectionUI.HOME.route
                || navRouteDestination == SelectionUI.TRANSACTION.route
                || navRouteDestination == SelectionUI.BUDGETS.route
                || navRouteDestination == SelectionUI.ACCOUNT.route
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
            if (navRouteDestination == SelectionUI.HOME.route
                || navRouteDestination == SelectionUI.TRANSACTION.route
                || navRouteDestination == SelectionUI.BUDGETS.route
                || navRouteDestination == SelectionUI.ACCOUNT.route
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
            RootNavGraph(navController, NavigationGraphRoute.HomeGraph)
         //   Navigation(navController = navController, paddingValues = it)
        })
}

@Composable
private fun Navigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    val slideUpAnimation = slideVerticalAnimation()
    val slideHorizontalAnimation = slideHorizontalAnimation()
    NavHost(
        navController = navController, startDestination = SelectionUI.HOME.route
    ) {
        composable(SelectionUI.HOME.route) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(SelectionUI.HOME.route)
            }
            val homeViewModel = hiltViewModel<HomeViewModel>(parentEntry)
            HomeScreen(navController, homeViewModel)
        }
        composable(SelectionUI.TRANSACTION.route) {}
        navigation(
            startDestination = SelectionUI.ADD_TRANSACTION.route,
            route = SelectionUI.SUB_GRAPH_ADD_TRANSACTION.route
        ) {
            val parentRoute = SelectionUI.ADD_TRANSACTION.route
            composable(
                parentRoute,
                enterTransition = { slideUpAnimation.enterTransition },
                exitTransition = { slideUpAnimation.exitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(SelectionUI.SUB_GRAPH_ADD_TRANSACTION.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                AddTransactionScreen(navController, transactionViewModel)
            }
            composable(
                route = screenRoute(parentRoute, SelectionUI.NOTE.route),
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(SelectionUI.SUB_GRAPH_ADD_TRANSACTION.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                NoteScreen(navController, transactionViewModel)
            }
            composable(
                route = screenRoute(parentRoute, SelectionUI.SELECT_CATEGORY.route),
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(SelectionUI.SUB_GRAPH_ADD_TRANSACTION.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectCategoryScreen(navController, transactionViewModel)
            }
            composable(
                route = screenRoute(parentRoute, SelectionUI.WALLET.route),
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(SelectionUI.SUB_GRAPH_ADD_TRANSACTION.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectWalletScreen(navController, transactionViewModel)
            }
            composable(
                route = screenRoute(SelectionUI.EVENT.route, SelectionUI.WALLET.route),
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(
                        screenRoute(parentRoute, SelectionUI.ADD_EVENT.route),
                    )
                }
                val addEventScreenViewModel = hiltViewModel<AddEventScreenViewModel>(parentEntry)
                SelectWalletScreen(navController, addEventScreenViewModel)
            }
            composable(
                route = screenRoute(parentRoute, SelectionUI.WITH.route),
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(SelectionUI.SUB_GRAPH_ADD_TRANSACTION.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectPeopleScreen(navController, transactionViewModel)
            }
            composable(
                screenRoute(parentRoute, SelectionUI.EVENT.route),
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(SelectionUI.SUB_GRAPH_ADD_TRANSACTION.route)
                }
                val transactionViewModel = hiltViewModel<AddTransactionViewModel>(parentEntry)
                SelectEventScreen(navController, transactionViewModel)
            }
            composable(
                screenRoute(parentRoute, SelectionUI.ADD_EVENT.route),
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(
                        screenRoute(parentRoute, SelectionUI.ADD_EVENT.route),
                    )
                }
                val addEventScreenViewModel = hiltViewModel<AddEventScreenViewModel>(parentEntry)
                AddEventScreen(navController, addEventScreenViewModel)
            }
            composable(
                screenRoute(parentRoute, SelectionUI.SELECT_CURRENCY.route),
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(
                        screenRoute(parentRoute, SelectionUI.ADD_EVENT.route),
                    )
                }
                val addEventScreenViewModel = hiltViewModel<AddEventScreenViewModel>(parentEntry)
                SelectCurrencyScreen(navController, addEventScreenViewModel)
            }
            composable(
                screenRoute(parentRoute, SelectionUI.SELECT_ICON.route),
                enterTransition = { slideHorizontalAnimation.enterTransition },
                exitTransition = { slideHorizontalAnimation.exitTransition },
                popExitTransition = { slideHorizontalAnimation.popExitTransition },
                popEnterTransition = { slideHorizontalAnimation.popEnterTransition }) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(
                        screenRoute(parentRoute, SelectionUI.ADD_EVENT.route),
                    )
                }
                val addEventScreenViewModel = hiltViewModel<AddEventScreenViewModel>(parentEntry)
                SelectIconScreen(navController, addEventScreenViewModel)
            }
        }
        composable(SelectionUI.BUDGETS.route) {}
        navigation(
            startDestination = SelectionUI.ACCOUNT.route,
            route = SelectionUI.SUB_GRAPH_ACCOUNT.route
        ) {
            val parentRoute = SelectionUI.ACCOUNT.route
            composable(parentRoute) {
                AccountScreen(navController)
            }
            composable(
                screenRoute(parentRoute, SelectionUI.SELECT_CURRENCY.route)
            ) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(
                        screenRoute(
                            parentRoute,
                            SelectionUI.ADD_WALLET.route
                        )
                    )
                }
                val viewModel = hiltViewModel<AddWalletViewModel>(parentEntry)
                SelectCurrencyScreen(navController, viewModel)
            }
            composable(screenRoute(parentRoute, SelectionUI.SELECT_ICON.route)) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(
                        screenRoute(
                            parentRoute,
                            SelectionUI.ADD_WALLET.route
                        )
                    )
                }
                val viewModel = hiltViewModel<AddWalletViewModel>(parentEntry)
                SelectIconScreen(navController, viewModel)
            }
            composable(screenRoute(parentRoute, SelectionUI.EDIT_WALLET.route)) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(
                        screenRoute(parentRoute, SelectionUI.MY_WALLET.route)
                    )
                }
                val viewModel = hiltViewModel<EditWalletViewModel>(parentEntry)
                EditWalletScreen(navController = navController, viewModel = viewModel)
            }
            composable(screenRoute(SelectionUI.EDIT_WALLET.route, SelectionUI.SELECT_ICON.route)) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(
                        screenRoute(parentRoute, SelectionUI.MY_WALLET.route)
                    )
                }
                val viewModel = hiltViewModel<EditWalletViewModel>(parentEntry)
                SelectIconScreen(navController, viewModel)
            }
            composable(
                screenRoute(SelectionUI.EDIT_WALLET.route, SelectionUI.SELECT_CURRENCY.route)
            ) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(
                        screenRoute(parentRoute, SelectionUI.MY_WALLET.route)
                    )
                }
                val viewModel = hiltViewModel<EditWalletViewModel>(parentEntry)
                SelectCurrencyScreen(navController, viewModel)
            }
            composable(screenRoute(parentRoute, SelectionUI.ADD_WALLET.route)) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(
                        screenRoute(
                            parentRoute,
                            SelectionUI.ADD_WALLET.route
                        )
                    )
                }
                val viewModel = hiltViewModel<AddWalletViewModel>(parentEntry)
                AddWalletScreen(navController, viewModel)
            }
            composable(screenRoute(parentRoute, SelectionUI.MY_WALLET.route)) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(SelectionUI.SUB_GRAPH_ACCOUNT.route)
                }
                val childEntry = remember(it) {
                    navController.getBackStackEntry(
                        screenRoute(parentRoute, SelectionUI.MY_WALLET.route)
                    )
                }
                val walletViewModel = hiltViewModel<WalletViewModel>(parentEntry)
                val editWalletViewModel = hiltViewModel<EditWalletViewModel>(childEntry)
                WalletScreen(navController, walletViewModel, editWalletViewModel)
            }
            composable(screenRoute(parentRoute, SelectionUI.MY_ABOUT.route)) {
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
        onClick = { onClick(SelectionUI.SUB_GRAPH_ADD_TRANSACTION.route) },
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