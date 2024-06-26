package com.kuro.money.navigation.routes

sealed class NavigationGraphRoute(val route: String) {

    data object RootGraph : NavigationGraphRoute(route = "root_graph")

    sealed class HomeGraph(route: String) : NavigationGraphRoute(route) {

        data object EditTransactionEvent : HomeGraph("sub_graph_home_edit_transactions_event")

        data object EditTransactions : HomeGraph("sub_graph_home_edit_transactions")

        data object TransactionDetails : HomeGraph("sub_graph_home_transaction_details")
        companion object : HomeGraph("home_graph")
    }

    sealed class TransactionGraph(route: String) : NavigationGraphRoute(route) {

        data object AdvancedSearch : TransactionGraph("transaction_graph_advanced_search")
        companion object : TransactionGraph("transaction_graph")
    }

    sealed class AddTransactionGraph(route: String) : NavigationGraphRoute(route) {
        data object AddEvent : AddTransactionGraph(route = "sub_graph_add_transaction_add_event")
        companion object : AddTransactionGraph("add_transaction_graph")
    }

    data object ReportGraph : NavigationGraphRoute(route = "report_graph")

    sealed class AccountGraph(route: String) : NavigationGraphRoute(route) {

        data object MyWalletGraph : NavigationGraphRoute(route = "sub_graph_account_my_wallet")

        data object AddWalletGraph : NavigationGraphRoute(route = "sub_graph_account_add_wallet")

        data object EditWalletGraph : NavigationGraphRoute(route = "sub_graph_account_edit_wallet")

        companion object : AccountGraph("account_graph")
    }

}