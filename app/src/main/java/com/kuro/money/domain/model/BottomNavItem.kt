package com.kuro.money.domain.model

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
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
            route =context.getString(R.string.home),
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            name = context.getString(R.string.transactions),
            route = context.getString(R.string.transactions),
            icon = Icons.Default.MonetizationOn
        ),
        BottomNavItem(
            name = "",
            route = context.getString(R.string.add)
        ),
        BottomNavItem(
            name = context.getString(R.string.budget),
            route = context.getString(R.string.budget),
            icon = Icons.Default.Payments
        ),
        BottomNavItem(
            name = context.getString(R.string.account),
            route = context.getString(R.string.account),
            icon = Icons.Default.AccountBox
        )
    )
}
