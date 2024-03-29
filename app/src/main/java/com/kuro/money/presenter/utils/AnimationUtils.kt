package com.kuro.money.presenter.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable

data class NavigationAnimation(
    val enterTransition: EnterTransition,
    val exitTransition: ExitTransition,
    val popEnterTransition: EnterTransition,
    val popExitTransition: ExitTransition
)

fun slideVerticalAnimation(duration: Int = 500) = NavigationAnimation(
    enterTransition = slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(duration)
    ),
    exitTransition = slideOutVertically(
        targetOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(duration)
    ),
    popEnterTransition = slideInVertically(
        initialOffsetY = { fullHeight -> -fullHeight },
        animationSpec = tween(duration)
    ),
    popExitTransition = slideOutVertically(
        targetOffsetY = { fullHeight -> -fullHeight },
        animationSpec = tween(duration)
    ),
)

fun slideHorizontalAnimation(duration: Int = 500) = NavigationAnimation(
    enterTransition = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(duration)
    ),
    exitTransition = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(duration)
    ),
    popEnterTransition = slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(duration)
    ),
    popExitTransition = slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(duration)
    ),
)

fun customEnterTransition(offsetX: Int, duration: Int): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(duration)
    ) + fadeIn(animationSpec = tween(duration))
}

fun customExitTransition(offsetX: Int, duration: Int): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(duration)
    ) + fadeOut(animationSpec = tween(duration))
}

@Composable
fun SlideUpContent(isVisible: Boolean, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(500)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(500)
        )
    ) {
        if (isVisible) {
            content()
        }
    }
}

