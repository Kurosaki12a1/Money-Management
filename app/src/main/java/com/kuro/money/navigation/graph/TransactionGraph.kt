package com.kuro.money.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.money.navigation.routes.NavigationGraphRoute
import com.kuro.money.navigation.routes.NavigationRoute

fun NavGraphBuilder.transactionGraph(navHostController: NavHostController) {
    navigation(
        startDestination = NavigationRoute.Transaction.route,
        route = NavigationGraphRoute.TransactionGraph.route,
        builder = {
            composable(route = NavigationRoute.Transaction.route) {

            }
        }
    )
}
