package com.kuro.money.navigation.routes

sealed class NavigationRoute(val route: String) {
    sealed class AddTransaction(route: String) : NavigationRoute(route) {
        data object SelectCategory : AddTransaction(route = "add_transaction_select_category")

        data object SelectWallet : AddTransaction(route = "add_transactions_select_wallet")

        data object SelectCurrency : AddTransaction(route = "add_transaction_select_currency")

        data object Note : AddTransaction(route = "add_transaction_note")

        data object SelectEvent : AddTransaction(route = "add_transactions_select_event")

        data object With : AddTransaction(route = "add_transactions_with")

        sealed class AddEvent(route: String) : AddTransaction(route) {

            data object SelectWallet : AddEvent("add_transaction_add_event_select_wallet")
            data object SelectCurrency : AddEvent("add_transaction_add_event_select_currency")
            data object SelectIcon : AddEvent("add_Transaction_add_event_select_icon")
            companion object : AddEvent("add_transaction_add_event")
        }

        companion object : AddTransaction("add_transaction")
    }

    sealed class Transaction(route: String) : NavigationRoute(route) {

        data object SelectWallet : Transaction("transaction_select_wallet")

        companion object : Transaction("transaction")
    }

    sealed class Budgets(route: String) : NavigationRoute(route) {
        companion object : Budgets("budgets")
    }

    sealed class Account(route: String) : NavigationRoute(route) {

        data object About : NavigationRoute(route = "account_about")

        data object Category : NavigationRoute(route = "account_category")

        data object Event : NavigationRoute(route = "account_route")

        data object RecurringTransaction : NavigationRoute(route = "account_recurring_transaction")

        data object Bills : NavigationRoute(route = "account_bills")

        data object Debts : NavigationRoute(route = "account_debts")

        data object Tools : NavigationRoute(route = "account_tools")

        data object TravelMode : NavigationRoute(route = "account_travel_mode")

        data object Settings : NavigationRoute(route = "account_settings")

        sealed class Wallet(route: String) : Account(route) {

            sealed class AddWallet(route: String) : Wallet(route) {
                data object SelectCurrency : EditWallet("account_wallet_add_select_currency")
                data object SelectIcon : EditWallet("account_wallet_add_select_icon")
                companion object : AddWallet("account_wallet_add")
            }

            sealed class EditWallet(route: String) : Wallet(route) {
                data object SelectCurrency : EditWallet("account_wallet_edit_select_currency")
                data object SelectIcon : EditWallet("account_wallet_edit_select_icon")
                companion object : EditWallet("account_wallet_edit")
            }

            companion object : Wallet("account_wallet")
        }

        companion object : Account("account")
    }

    sealed class Home(route: String) : NavigationRoute(route) {

        sealed class Wallet(route: String) : Home(route) {
            sealed class Add(route: String) : Wallet(route) {
                data object SelectCurrency : Add("home_add_wallet_currency")
                data object SelectIcon : Add("home_add_wallet_icon")
                companion object : Add("home_add_wallet")
            }

            companion object : Wallet("home_wallet")
        }

        sealed class TransactionDetails(route: String) : Home(route) {

            sealed class Edit(route: String) : TransactionDetails(route) {

                data object SelectCategory : AddTransaction(route = "home_transaction_edit_select_category")

                data object SelectWallet : AddTransaction(route = "home_transaction_edits_select_wallet")

                data object SelectCurrency : AddTransaction(route = "home_transaction_edit_select_currency")

                data object Note : AddTransaction(route = "home_transaction_edit_note")

                data object SelectEvent : AddTransaction(route = "home_transaction_edits_select_event")

                data object With : AddTransaction(route = "home_transaction_edits_with")

                sealed class AddEvent(route: String) : Edit(route) {

                    data object SelectWallet : AddEvent("home_transaction_edit_add_event_select_wallet")
                    data object SelectCurrency : AddEvent("home_transaction_edit_add_event_select_currency")
                    data object SelectIcon : AddEvent("home_transaction_edit_add_event_select_icon")
                    companion object : AddEvent("home_transaction_edit_add_event")
                }
                companion object : Edit("home_transaction_edit")
            }
            companion object : TransactionDetails("home_transaction_details")
        }

        companion object : Home("home")
    }
}

