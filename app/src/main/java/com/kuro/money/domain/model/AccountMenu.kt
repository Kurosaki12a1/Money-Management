package com.kuro.money.domain.model

import android.content.Context
import com.kuro.money.R

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
            navigateScreenTo = SelectionUI.MY_WALLET.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.categories),
            icon = R.drawable.ic_bk_category_manager,
            navigateScreenTo = SelectionUI.MY_CATEGORY.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.events),
            icon = R.drawable.ic_bk_events,
            navigateScreenTo = SelectionUI.EVENT.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.recurring_transactions),
            icon = R.drawable.ic_bk_recurring_transaction,
            navigateScreenTo = SelectionUI.MY_RECURRING_TRANSACTIONS.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.bills),
            icon = R.drawable.ic_w_bills,
            navigateScreenTo = SelectionUI.MY_BILLS.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.debt),
            icon = R.drawable.ic_bk_debts,
            navigateScreenTo = SelectionUI.MY_DEBTS.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.tools),
            icon = R.drawable.ic_bk_tools,
            navigateScreenTo = SelectionUI.MY_TOOLS.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.travel_mode),
            icon = R.drawable.ic_bk_travel_mode,
            navigateScreenTo = SelectionUI.MY_TRAVEL_MODE.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.settings),
            icon = R.drawable.ic_bk_settings,
            navigateScreenTo = SelectionUI.MY_SETTINGS.route
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.about),
            icon = R.drawable.ic_bk_crown,
            navigateScreenTo = SelectionUI.MY_ABOUT.route
        )
    )
    return result
}