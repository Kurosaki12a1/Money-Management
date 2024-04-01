package com.kuro.money.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kuro.money.navigation.routes.NavigationGraphRoute


@Composable
fun RootNavGraph(
    navHostController: NavHostController,
    startGraphDestination: NavigationGraphRoute,
) {
    NavHost(
        navController = navHostController,
        route = NavigationGraphRoute.RootGraph.route,
        startDestination = startGraphDestination.route,
        builder = {
            homeNavGraph(navHostController)
            addTransactionNavGraph(navHostController)
            accountNavGraph(navHostController)
        }
    )
}