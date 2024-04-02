package com.kuro.money.navigation.graph

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.money.navigation.routes.NavigationGraphRoute.AccountGraph
import com.kuro.money.navigation.routes.NavigationRoute.Account
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
        startDestination = Account.route,
        route = AccountGraph.route
    ) {
        /** Bottom Navigation Account Screen*/
        composable(route = Account.route) {
            AccountScreen(navHostController)
        }
        /** Item My Wallet */
        navigation(
            startDestination = Account.Wallet.route,
            route = AccountGraph.MyWalletGraph.route, builder = {
                composable(route = Account.Wallet.route) {
                    val parentEntry = remember(it) {
                        navHostController.getBackStackEntry(AccountGraph.MyWalletGraph.route)
                    }
                    val childEntry = remember(it) {
                        navHostController.getBackStackEntry(AccountGraph.MyWalletGraph.route)
                    }
                    val walletViewModel = hiltViewModel<WalletViewModel>(parentEntry)
                    val editWalletViewModel = hiltViewModel<EditWalletViewModel>(childEntry)
                    WalletScreen(navHostController, walletViewModel, editWalletViewModel)
                }

                /* Edit Wallet */

                navigation(
                    startDestination = Account.Wallet.EditWallet.route,
                    route = AccountGraph.EditWalletGraph.route, builder = {
                        composable(Account.Wallet.EditWallet.route) {
                            val parentEntry = remember(it) {
                                navHostController.getBackStackEntry(AccountGraph.MyWalletGraph.route)
                            }
                            val viewModel = hiltViewModel<EditWalletViewModel>(parentEntry)
                            EditWalletScreen(navHostController, viewModel = viewModel)
                        }
                        composable(route = Account.Wallet.EditWallet.SelectCurrency.route) {
                            val parentEntry = remember(it) {
                                navHostController.getBackStackEntry(AccountGraph.EditWalletGraph.route)
                            }
                            val viewModel = hiltViewModel<EditWalletViewModel>(parentEntry)
                            SelectCurrencyScreen(navHostController, viewModel)
                        }
                        composable(route = Account.Wallet.EditWallet.SelectIcon.route) {
                            val parentEntry = remember(it) {
                                navHostController.getBackStackEntry(AccountGraph.EditWalletGraph.route)
                            }
                            val viewModel = hiltViewModel<EditWalletViewModel>(parentEntry)
                            SelectIconScreen(navHostController, viewModel)
                        }
                    }
                )

                /* Add Wallet */

                navigation(
                    startDestination = Account.Wallet.AddWallet.route,
                    route = AccountGraph.AddWalletGraph.route, builder = {
                        composable(route = Account.Wallet.AddWallet.route) {
                            val parentEntry = remember(it) {
                                navHostController.getBackStackEntry(AccountGraph.AddWalletGraph.route)
                            }
                            val viewModel = hiltViewModel<AddWalletViewModel>(parentEntry)
                            AddWalletScreen(navHostController, viewModel)
                        }
                        composable(route = Account.Wallet.AddWallet.SelectCurrency.route) {
                            val parentEntry = remember(it) {
                                navHostController.getBackStackEntry(AccountGraph.AddWalletGraph.route)
                            }
                            val viewModel = hiltViewModel<AddWalletViewModel>(parentEntry)
                            SelectCurrencyScreen(navHostController, viewModel)
                        }
                        composable(route = Account.Wallet.AddWallet.SelectIcon.route) {
                            val parentEntry = remember(it) {
                                navHostController.getBackStackEntry(AccountGraph.AddWalletGraph.route)
                            }
                            val viewModel = hiltViewModel<AddWalletViewModel>(parentEntry)
                            SelectIconScreen(navHostController, viewModel)
                        }
                    }
                )
            }
        )

        /** Item About of Account Screen*/
        composable(route = Account.About.route) {
            AccountScreen(navHostController)
        }
    }
}