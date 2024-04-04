package com.kuro.money.navigation.routes

sealed class NavigationGraphRoute(val route: String) {

    data object RootGraph : NavigationGraphRoute(route = "root_graph")

    sealed class HomeGraph(route: String) : NavigationGraphRoute(route) {

        data object TransactionDetails : HomeGraph("sub_graph_home_transaction_details")
        companion object : HomeGraph("home_graph")
    }

    data object TransactionGraph : NavigationGraphRoute(route = "transaction_graph")

    sealed class AddTransactionGraph(route: String) : NavigationGraphRoute(route) {
        data object AddEvent : AddTransactionGraph(route = "sub_graph_add_transaction_add_event")
        companion object : AddTransactionGraph("add_transaction_graph")
    }

    data object BudgetsGraph : NavigationGraphRoute(route = "budgets_graph")

    sealed class AccountGraph(route: String) : NavigationGraphRoute(route) {

        data object MyWalletGraph : NavigationGraphRoute(route = "sub_graph_account_my_wallet")

        data object AddWalletGraph : NavigationGraphRoute(route = "sub_graph_account_add_wallet")

        data object EditWalletGraph : NavigationGraphRoute(route = "sub_graph_account_edit_wallet")

        companion object : AccountGraph("account_graph")
    }

}