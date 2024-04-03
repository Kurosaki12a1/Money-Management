package com.kuro.money.navigation.graph

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.money.navigation.routes.NavigationGraphRoute
import com.kuro.money.navigation.routes.NavigationRoute
import com.kuro.money.presenter.home.HomeScreen
import com.kuro.money.presenter.home.HomeViewModel
import com.kuro.money.presenter.home.MyWalletViewModel
import com.kuro.money.presenter.home.RecentTransactionViewModel
import com.kuro.money.presenter.home.SpendingReportViewModel

fun NavGraphBuilder.homeNavGraph(navHostController: NavHostController) {
    navigation(
        startDestination = NavigationRoute.Home.route,
        route = NavigationGraphRoute.HomeGraph.route,
        builder = {
            /** Bottom Navigation Account Screen*/
            composable(route = NavigationRoute.Home.route) {
                val parentEntry = remember(it) {
                    navHostController.getBackStackEntry(NavigationGraphRoute.HomeGraph.route)
                }
                val homeViewModel = hiltViewModel<HomeViewModel>(parentEntry)
                val myWalletViewModel = hiltViewModel<MyWalletViewModel>(parentEntry)
                val spendingReportViewModel = hiltViewModel<SpendingReportViewModel>(parentEntry)
                val recentTransactionViewModel =
                    hiltViewModel<RecentTransactionViewModel>(parentEntry)
                HomeScreen(
                    navHostController,
                    homeViewModel,
                    myWalletViewModel,
                    spendingReportViewModel,
                    recentTransactionViewModel
                )
            }
        }
    )
}
