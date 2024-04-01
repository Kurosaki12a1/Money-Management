package com.kuro.money.navigation.routes

import com.kuro.money.domain.model.SelectionUI

sealed class NavigationRoute(val route: String) {
    object MY_WALLET : NavigationRoute(route = SelectionUI.MY_WALLET.route)
    object MY_EVENT : NavigationRoute(route = SelectionUI.MY_EVENT.route)
    object MY_CATEGORY : NavigationRoute(route = SelectionUI.MY_CATEGORY.route)
    object MY_RECURRING_TRANSACTIONS :
        NavigationRoute(route = SelectionUI.MY_RECURRING_TRANSACTIONS.route)

    object MY_BILLS : NavigationRoute(route = SelectionUI.MY_BILLS.route)
    object MY_DEBTS : NavigationRoute(route = SelectionUI.MY_DEBTS.route)
    object MY_TOOLS : NavigationRoute(route = SelectionUI.MY_TOOLS.route)
    object MY_TRAVEL_MODE : NavigationRoute(route = SelectionUI.MY_TRAVEL_MODE.route)
    object MY_SETTINGS : NavigationRoute(route = SelectionUI.MY_SETTINGS.route)
    object MY_ABOUT : NavigationRoute(route = SelectionUI.MY_ABOUT.route)
    object SELECT_CATEGORY : NavigationRoute(route = SelectionUI.SELECT_CATEGORY.route)
    object EditWalletSelectIcon : NavigationRoute(route = "edit_wallet_select_icon")
    object EditWalletSelectCurrency : NavigationRoute(route = "edit_wallet_select_currency")
    object AddWalletSelectIcon : NavigationRoute(route = "add_wallet_select_icon")
    object AddWalletSelectCurrency : NavigationRoute(route = "add_wallet_select_currency")

    object ADD_EVENT : NavigationRoute(route = SelectionUI.ADD_EVENT.route)
    object ADD_WALLET : NavigationRoute(route = SelectionUI.ADD_WALLET.route)
    object NOTE : NavigationRoute(route = SelectionUI.NOTE.route)
    object WITH : NavigationRoute(route = SelectionUI.WITH.route)
    object EVENT : NavigationRoute(route = SelectionUI.EVENT.route)

    object HOME : NavigationRoute(route = SelectionUI.HOME.route)

    object ACCOUNT : NavigationRoute(route = SelectionUI.ACCOUNT.route)

    object EDIT_WALLET : NavigationRoute(route = SelectionUI.EDIT_WALLET.route)


}