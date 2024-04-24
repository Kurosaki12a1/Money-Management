package com.kuro.money.navigation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.money.navigation.routes.NavigationGraphRoute
import com.kuro.money.navigation.routes.NavigationGraphRoute.RootGraph
import com.kuro.money.navigation.routes.NavigationRoute
import com.kuro.money.navigation.routes.NavigationRoute.Transaction
import com.kuro.money.presenter.account.feature.wallets.WalletScreen
import com.kuro.money.presenter.report.ReportScreen
import com.kuro.money.presenter.report.ReportViewModel
import com.kuro.money.presenter.transactions.TransactionViewModel
import com.kuro.money.presenter.utils.horizontalComposable

fun NavGraphBuilder.budgetsGraph(
    navHostController: NavHostController,
    paddingValues: PaddingValues
) {
    navigation(
        startDestination = NavigationRoute.Report.route,
        route = NavigationGraphRoute.ReportGraph.route,
        builder = {
            composable(NavigationRoute.Report.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(NavigationGraphRoute.ReportGraph.route)
                }
                val viewModel = hiltViewModel<ReportViewModel>(parentEntry)
                ReportScreen(
                    navHostController,
                    viewModel,
                    paddingValues,
                )
            }
            horizontalComposable(route = NavigationRoute.Report.SelectWallet.route) {
                val parentEntry = remember(it) {
                    // Lifecycle through lifecycle of app
                    navHostController.getBackStackEntry(NavigationGraphRoute.ReportGraph.route)
                }
                val reportViewModel = hiltViewModel<ReportViewModel>(parentEntry)
                WalletScreen(navHostController, reportViewModel)
            }
        }
    )
}
