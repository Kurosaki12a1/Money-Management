package com.kuro.money.navigation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.money.navigation.routes.NavigationGraphRoute
import com.kuro.money.navigation.routes.NavigationRoute
import com.kuro.money.presenter.account.feature.wallets.WalletScreen
import com.kuro.money.presenter.add_transaction.feature.wallet.SelectWalletScreen
import com.kuro.money.presenter.transactions.TransactionScreen
import com.kuro.money.presenter.transactions.TransactionViewModel
import com.kuro.money.presenter.transactions.feature.SearchTransactionScreen
import com.kuro.money.presenter.utils.horizontalComposable
import com.kuro.money.presenter.utils.verticalComposable

fun NavGraphBuilder.transactionGraph(
    navHostController: NavHostController,
    paddingValues: PaddingValues
) {
    navigation(
        startDestination = NavigationRoute.Transaction.route,
        route = NavigationGraphRoute.TransactionGraph.route,
        builder = {
            composable(route = NavigationRoute.Transaction.route) {
                val parentEntry = remember(it) {
                    // Lifecycle through lifecycle of app
                    navHostController.getBackStackEntry(NavigationGraphRoute.RootGraph.route)
                }
                val transactionViewModel = hiltViewModel<TransactionViewModel>(parentEntry)
                TransactionScreen(navHostController, paddingValues, transactionViewModel)
            }
            horizontalComposable(route = NavigationRoute.Transaction.SelectWallet.route) {
                val parentEntry = remember(it) {
                    // Lifecycle through lifecycle of app
                    navHostController.getBackStackEntry(NavigationGraphRoute.RootGraph.route)
                }
                val transactionViewModel = hiltViewModel<TransactionViewModel>(parentEntry)
                WalletScreen(navHostController, transactionViewModel)
            }
            verticalComposable(route = NavigationRoute.Transaction.SearchTransaction.route) {
                SearchTransactionScreen(navController = navHostController)
            }
            horizontalComposable(route = NavigationRoute.Transaction.AdvancedSearchTransaction.route) {

            }
        }
    )
}
