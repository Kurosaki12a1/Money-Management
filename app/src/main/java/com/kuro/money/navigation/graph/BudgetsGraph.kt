package com.kuro.money.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kuro.money.navigation.routes.NavigationGraphRoute
import com.kuro.money.navigation.routes.NavigationRoute

fun NavGraphBuilder.budgetsGraph(navHostController: NavHostController) {
    navigation(
        startDestination = NavigationRoute.Budgets.route,
        route = NavigationGraphRoute.BudgetsGraph.route,
        builder = {
            composable(NavigationRoute.Budgets.route) {

            }
        }
    )
}
