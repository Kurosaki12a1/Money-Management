package com.kuro.money.navigation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.money.navigation.routes.NavigationGraphRoute.RootGraph
import com.kuro.money.navigation.routes.NavigationGraphRoute.TransactionGraph
import com.kuro.money.navigation.routes.NavigationRoute.Transaction
import com.kuro.money.presenter.account.feature.wallets.WalletScreen
import com.kuro.money.presenter.transactions.TransactionScreen
import com.kuro.money.presenter.transactions.TransactionViewModel
import com.kuro.money.presenter.transactions.feature.AdvanceSearchViewModel
import com.kuro.money.presenter.transactions.feature.AdvancedSearchTransactionScreen
import com.kuro.money.presenter.transactions.feature.SearchTransactionScreen
import com.kuro.money.presenter.transactions.feature.SelectWalletScreen
import com.kuro.money.presenter.utils.horizontalComposable

fun NavGraphBuilder.transactionGraph(
    navHostController: NavHostController,
    paddingValues: PaddingValues
) {
    navigation(
        startDestination = Transaction.route,
        route = TransactionGraph.route,
        builder = {
            composable(route = Transaction.route) {
                val parentEntry = remember(it) {
                    // Lifecycle through lifecycle of app
                    navHostController.getBackStackEntry(RootGraph.route)
                }
                val transactionViewModel = hiltViewModel<TransactionViewModel>(parentEntry)
                TransactionScreen(navHostController, paddingValues, transactionViewModel)
            }
            horizontalComposable(route = Transaction.SelectWallet.route) {
                val parentEntry = remember(it) {
                    // Lifecycle through lifecycle of app
                    navHostController.getBackStackEntry(RootGraph.route)
                }
                val transactionViewModel = hiltViewModel<TransactionViewModel>(parentEntry)
                WalletScreen(navHostController, transactionViewModel)
            }
            horizontalComposable(route = Transaction.SearchTransaction.route) {
                SearchTransactionScreen(navController = navHostController)
            }
            navigation(
                startDestination = Transaction.AdvancedSearchTransaction.route,
                route = TransactionGraph.AdvancedSearch.route,
                builder = {
                    horizontalComposable(route = Transaction.AdvancedSearchTransaction.route) {
                        val parentEntry = remember(it) {
                            // Lifecycle through lifecycle of backStab
                            navHostController.getBackStackEntry(TransactionGraph.AdvancedSearch.route)
                        }
                        val advanceSearch = hiltViewModel<AdvanceSearchViewModel>(parentEntry)
                        AdvancedSearchTransactionScreen(navHostController, advanceSearch)
                    }
                    horizontalComposable(route = Transaction.AdvancedSearchTransaction.SelectWallet.route) {
                        val parentEntry = remember(it) {
                            // Lifecycle through lifecycle of backStab
                            navHostController.getBackStackEntry(TransactionGraph.AdvancedSearch.route)
                        }
                        val advanceSearch = hiltViewModel<AdvanceSearchViewModel>(parentEntry)
                        SelectWalletScreen(navHostController, advanceSearch)
                    }
                }
            )

        }
    )
}
