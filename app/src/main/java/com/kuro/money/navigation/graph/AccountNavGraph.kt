package com.kuro.money.navigation.graph

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.money.navigation.routes.NavigationGraphRoute
import com.kuro.money.navigation.routes.NavigationRoute
import com.kuro.money.presenter.account.AccountScreen
import com.kuro.money.presenter.account.feature.wallets.AddWalletScreen
import com.kuro.money.presenter.account.feature.wallets.AddWalletViewModel
import com.kuro.money.presenter.account.feature.wallets.EditWalletScreen
import com.kuro.money.presenter.account.feature.wallets.EditWalletViewModel
import com.kuro.money.presenter.account.feature.wallets.WalletScreen
import com.kuro.money.presenter.account.feature.wallets.WalletViewModel
import com.kuro.money.presenter.add_transaction.feature.event.feature.select_currency.SelectCurrencyScreen
import com.kuro.money.presenter.add_transaction.feature.event.feature.select_icon.SelectIconScreen

fun NavGraphBuilder.accountNavGraph(navHostController: NavHostController) {
    navigation(
        startDestination = NavigationRoute.ACCOUNT.route,
        route = NavigationGraphRoute.AccountGraph.route,
        builder = {
            /** Bottom Navigation Account Screen*/
            composable(route = NavigationRoute.ACCOUNT.route) {
                AccountScreen(navHostController)
            }
            navigation(
                startDestination = NavigationRoute.MY_WALLET.route,
                route = NavigationGraphRoute.MyWalletGraph.route, builder = {
                    composable(route = NavigationRoute.MY_WALLET.route) {
                        val parentEntry = remember(it) {
                            navHostController.getBackStackEntry(NavigationGraphRoute.AccountGraph.route)
                        }
                        val childEntry = remember(it) {
                            navHostController.getBackStackEntry(NavigationRoute.MY_WALLET.route)
                        }
                        val walletViewModel = hiltViewModel<WalletViewModel>(parentEntry)
                        val editWalletViewModel = hiltViewModel<EditWalletViewModel>(childEntry)
                        WalletScreen(navHostController, walletViewModel, editWalletViewModel)
                    }
                    navigation(
                        startDestination = NavigationRoute.EDIT_WALLET.route,
                        route = NavigationGraphRoute.EditWalletGraph.route, builder = {
                            composable(NavigationRoute.EDIT_WALLET.route) {
                                val parentEntry = remember(it) {
                                    navHostController.getBackStackEntry(NavigationRoute.MY_WALLET.route)
                                }
                                val viewModel = hiltViewModel<EditWalletViewModel>(parentEntry)
                                EditWalletScreen(navHostController, viewModel = viewModel)
                            }
                            composable(route = NavigationRoute.EditWalletSelectCurrency.route) {
                                val parentEntry = remember(it) {
                                    navHostController.getBackStackEntry(NavigationRoute.MY_WALLET.route)
                                }
                                val viewModel = hiltViewModel<EditWalletViewModel>(parentEntry)
                                SelectCurrencyScreen(navHostController, viewModel)
                            }
                            composable(route = NavigationRoute.EditWalletSelectIcon.route) {
                                val parentEntry = remember(it) {
                                    navHostController.getBackStackEntry(NavigationRoute.MY_WALLET.route)
                                }
                                val viewModel = hiltViewModel<EditWalletViewModel>(parentEntry)
                                SelectIconScreen(navHostController, viewModel)
                            }
                        }
                    )
                    navigation(
                        startDestination = NavigationRoute.ADD_WALLET.route,
                        route = NavigationGraphRoute.AddWalletGraph.route, builder = {
                            composable(route = NavigationRoute.ADD_WALLET.route) {
                                val parentEntry = remember(it) {
                                    navHostController.getBackStackEntry(NavigationGraphRoute.AddWalletGraph.route)
                                }
                                val viewModel = hiltViewModel<AddWalletViewModel>(parentEntry)
                                AddWalletScreen(navHostController, viewModel)
                            }
                            composable(route = NavigationRoute.AddWalletSelectCurrency.route) {
                                val parentEntry = remember(it) {
                                    navHostController.getBackStackEntry(NavigationGraphRoute.AddWalletGraph.route)
                                }
                                val viewModel = hiltViewModel<AddWalletViewModel>(parentEntry)
                                SelectCurrencyScreen(navHostController, viewModel)
                            }
                            composable(route = NavigationRoute.AddWalletSelectIcon.route) {
                                val parentEntry = remember(it) {
                                    navHostController.getBackStackEntry(NavigationGraphRoute.AddWalletGraph.route)
                                }
                                val viewModel = hiltViewModel<AddWalletViewModel>(parentEntry)
                                SelectIconScreen(navHostController, viewModel)
                            }
                        }
                    )
                }
            )

            /** Item About of Account Screen*/
            composable(route = NavigationRoute.MY_ABOUT.route) {
                AccountScreen(navHostController)
            }
        }
    )
}