package com.kuro.money.navigation.routes

import com.kuro.money.domain.model.SelectionUI

sealed class NavigationBackStack(route: String) {
    object SUB_GRAPH_ACCOUNT : NavigationBackStack(SelectionUI.SUB_GRAPH_ACCOUNT.route)

    object SUB_GRAPH_ADD_TRANSACTION : NavigationBackStack(SelectionUI.SUB_GRAPH_ADD_TRANSACTION.route)

    object SUB_GRAPH_TRANSACTION  : NavigationBackStack(SelectionUI.SUB_GRAPH_TRANSACTION.route)

}