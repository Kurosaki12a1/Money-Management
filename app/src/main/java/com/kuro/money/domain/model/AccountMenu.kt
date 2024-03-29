package com.kuro.money.domain.model

import android.content.Context
import androidx.compose.runtime.Composable
import com.kuro.money.R

data class AccountMenu (
    val text: String,
    val icon : Int,
    val navigateScreenTo : ScreenSelection
)

fun generateListAccountMenu(context: Context) : List<AccountMenu> {
    val result = mutableListOf<AccountMenu>()
    result.add(
        AccountMenu(
            text = context.getString(R.string.my_wallets),
            icon = R.drawable.ic_bk_cashbook,
            navigateScreenTo = ScreenSelection.MY_WALLET_SCREEN
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.categories),
            icon = R.drawable.ic_bk_category_manager,
            navigateScreenTo = ScreenSelection.MY_CATEGORY_SCREEN
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.events),
            icon = R.drawable.ic_bk_events,
            navigateScreenTo = ScreenSelection.EVENT_SCREEN
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.recurring_transactions),
            icon = R.drawable.ic_bk_recurring_transaction,
            navigateScreenTo = ScreenSelection.MY_RECURRING_TRANSACTIONS
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.bills),
            icon = R.drawable.ic_w_bills,
            navigateScreenTo = ScreenSelection.MY_BILLS
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.debt),
            icon = R.drawable.ic_bk_debts,
            navigateScreenTo = ScreenSelection.MY_DEBTS
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.tools),
            icon = R.drawable.ic_bk_tools,
            navigateScreenTo = ScreenSelection.MY_TOOLS
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.travel_mode),
            icon = R.drawable.ic_bk_travel_mode,
            navigateScreenTo = ScreenSelection.MY_TRAVEL_MODE
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.settings),
            icon = R.drawable.ic_bk_settings,
            navigateScreenTo = ScreenSelection.MY_SETTINGS
        )
    )
    result.add(
        AccountMenu(
            text = context.getString(R.string.about),
            icon = R.drawable.ic_bk_crown,
            navigateScreenTo = ScreenSelection.MY_ABOUT
        )
    )
    return result
}