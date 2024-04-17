package com.kuro.money.presenter.utils

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.string(pattern: String = "dd/MM/yyyy", enableToday: Boolean = true): String {
    if (this == LocalDate.now() && enableToday) return "Today"
    return DateTimeFormatter.ofPattern(pattern).format(this)
}

fun Double.string(): String {
    val decimalFormat = DecimalFormat("#.##")
    return decimalFormat.format(this)
}

fun String.double(): Double {
    return if (this.isEmpty()) 0.0
    else this.toDouble()
}

fun String.format(): String {
    if (this.isEmpty()) return ""
    val regex = "([/*+-])|\\s+".toRegex()
    val parts = this.split(regex).filter { it.isNotBlank() }
    val operators = regex.findAll(this).map { it.value.trim() }.toList()

    val decimalFormatSymbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ','
    }
    val decimalFormat = DecimalFormat("#,###.##", decimalFormatSymbols)
    val formattedParts = parts.map { part ->
        if (part.contains('.') || part.contains(',')) {
            decimalFormat.format(part.toDouble())
        } else {
            decimalFormat.format(part.toLong())
        }
    }

    var result = formattedParts[0]
    operators.forEachIndexed { index, operator ->
        result += " $operator ${formattedParts[index + 1]}"
    }
    return result
}

fun NavController.popBackStackWithLifeCycle(route : String? = null, inclusive : Boolean = false) {
    if (this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        if (route != null) this.popBackStack(route, inclusive)
        else this.popBackStack()
    }
}

fun NavGraphBuilder.horizontalComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 },
                animationSpec = tween(1000)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -1000 },
                animationSpec = tween(1000)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -1000 },
                animationSpec = tween(1000)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 1000 },
                animationSpec = tween(1000)
            )
        },
        content = content
    )
}

fun NavGraphBuilder.verticalComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = {
            slideInVertically(initialOffsetY = { it }, animationSpec = tween(1000))
        },
        exitTransition = {
            slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(1000))
        },
        popEnterTransition = {
            slideInVertically(initialOffsetY = { -it }, animationSpec = tween(1000))
        },
        popExitTransition = {
            slideOutVertically(targetOffsetY = { it }, animationSpec = tween(1000))
        },
        content = content
    )
}