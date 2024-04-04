package com.kuro.money.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlin.math.abs
import kotlin.random.Random


@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier {
    return this.clickable(
        indication = null,
        interactionSource = remember(this) { MutableInteractionSource() }) {
        onClick()
    }
}

@Composable
fun Modifier.detectHorizontalWithDelay(
    onSwipeLeft: () -> Unit = {}, onSwipeRight: () -> Unit = {}
): Modifier {
    var lastDragTime = remember { 0L }
    return this.pointerInput(Unit) {
        detectHorizontalDragGestures { change, dragAmout ->
            val currentTime = System.currentTimeMillis()
            change.consume()
            if (currentTime - lastDragTime > 250L && abs(dragAmout) > 50) {
                lastDragTime = currentTime
                if (dragAmout > 0) onSwipeRight()
                else onSwipeLeft()
            }
        }
    }
}

@Composable
fun Dp.toPx(): Float {
    val density = LocalDensity.current.density
    return value * density
}