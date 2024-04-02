package com.kuro.money.domain.model

import android.content.Context
import com.kuro.money.R
import com.kuro.money.navigation.routes.NavigationRoute

data class AccountMenu(
    val text: String,
    val icon: Int,
    val navigateScreenTo: String
)

fun generateListAccountMenu(context: Context): List<AccountMenu> {
    val result = mutableListOf<AccountMenu>()
    result.add(
        AccountMenu(
            text = context.getString(R.string.my_wallets),
            icon = R.drawable.ic_bk_cashbook,
            navigateScreenTo = NavigationRoute.Account.Wallet.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.categories),
            icon = R.drawable.ic_bk_category_manager,
            navigateScreenTo = NavigationRoute.Account.Category.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.events),
            icon = R.drawable.ic_bk_events,
            navigateScreenTo = NavigationRoute.Account.Event.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.recurring_transactions),
            icon = R.drawable.ic_bk_recurring_transaction,
            navigateScreenTo = NavigationRoute.Account.RecurringTransaction.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.bills),
            icon = R.drawable.ic_w_bills,
            navigateScreenTo = NavigationRoute.Account.Bills.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.debt),
            icon = R.drawable.ic_bk_debts,
            navigateScreenTo = NavigationRoute.Account.Debts.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.tools),
            icon = R.drawable.ic_bk_tools,
            navigateScreenTo = NavigationRoute.Account.Tools.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.travel_mode),
            icon = R.drawable.ic_bk_travel_mode,
            navigateScreenTo = NavigationRoute.Account.TravelMode.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.settings),
            icon = R.drawable.ic_bk_settings,
            navigateScreenTo = NavigationRoute.Account.Settings.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.about),
            icon = R.drawable.ic_bk_crown,
            navigateScreenTo = NavigationRoute.Account.About.route
        )
    )
    return result
}