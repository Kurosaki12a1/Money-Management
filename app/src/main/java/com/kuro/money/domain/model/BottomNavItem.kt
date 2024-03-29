package com.kuro.money.domain.model

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.ui.graphics.vector.ImageVector
import com.kuro.money.R

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector? = null,
    val badgeCount: Int = 0
)

fun generateListBottomNavItem(context: Context): MutableList<BottomNavItem> {
    return mutableListOf(
        BottomNavItem(
            name = context.getString(R.string.home),
            route = ScreenSelection.HOME_SCREEN.route,
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            name = context.getString(R.string.transactions),
            route = ScreenSelection.TRANSACTION_SCREEN.route,
            icon = Icons.Default.MonetizationOn
        ),
        BottomNavItem(
            name = "",
            route = ScreenSelection.SUB_GRAPH_TRANSACTION.route,
        ),
        BottomNavItem(
            name = context.getString(R.string.budget),
            route = ScreenSelection.BUDGETS_SCREEN.route,
            icon = Icons.Default.Payments
        ),
        BottomNavItem(
            name = context.getString(R.string.account),
            route = ScreenSelection.SUB_GRAPH_ACCOUNT.route,
            icon = Icons.Default.AccountBox
        )
    )
}
