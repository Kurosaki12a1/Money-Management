package com.kuro.money.navigation.routes

import com.kuro.money.domain.model.SelectionUI

sealed class NavigationGraphRoute(val route: String) {

    object RootGraph : NavigationGraphRoute(route = "root_graph")

    object HomeGraph : NavigationGraphRoute(route = SelectionUI.SUB_GRAPH_HOME.route)

    object TransactionGraph : NavigationGraphRoute(route = SelectionUI.SUB_GRAPH_TRANSACTION.route)

    object AddTransactionGraph : NavigationGraphRoute(route = SelectionUI.SUB_GRAPH_ADD_TRANSACTION.route)

    object BudgetsGraph : NavigationGraphRoute(route = SelectionUI.BUDGETS.route)

    object AccountGraph : NavigationGraphRoute(route = SelectionUI.SUB_GRAPH_ACCOUNT.route)

    object MyWalletGraph : NavigationGraphRoute(route = "sub_graph_wallet")

    object AddWalletGraph : NavigationGraphRoute(route = "sub_graph_add_wallet")

    object EditWalletGraph : NavigationGraphRoute(route = "sub_graph_edit_wallet")
}