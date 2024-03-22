package com.kuro.money.presenter.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kuro.customimagevector.Delete
import com.kuro.money.ui.theme.Gray
import com.kuro.money.ui.theme.Teal200
import org.mozilla.javascript.Context

@Composable
fun CustomKeyBoard(
    onClear: () -> Unit,
    onBack: () -> Unit,
    onInput: (String) -> Unit,
    onConfirm: () -> Unit,
    shakeEnabled: Boolean = false
) {
    val shakeAnimation = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        shakeAnimation.animateTo(
            targetValue = 1f, // Giá trị không quan trọng vì chúng ta sử dụng `keyframes`
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 500
                    // Định nghĩa các giá trị dịch chuyển cho hiệu ứng lắc
                    0f at 0
                    -10f at 100
                    10f at 200
                    -10f at 300
                    10f at 400
                    0f at 500
                }
            )
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                if (shakeEnabled)
                    translationX = shakeAnimation.value
            }
            .background(Color.White),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Gray)
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onClear() }) {
                Text(
                    text = "C",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("7") }) {
                Text(
                    text = "7",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("4") }) {
                Text(
                    text = "4",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("1") }) {
                Text(
                    text = "1",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("0") }) {
                Text(
                    text = "0",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Gray)
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("/") }) {
                Text(
                    text = "/",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("8") }) {
                Text(
                    text = "8",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("5") }) {
                Text(
                    text = "5",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("2") }) {
                Text(
                    text = "2",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("000") }) {
                Text(
                    text = "000",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Gray)
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("*") }) {
                Text(
                    text = "X",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("9") }) {
                Text(
                    text = "9",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("6") }) {
                Text(
                    text = "6",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("3") }) {
                Text(
                    text = "3",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput(".") }) {
                Text(
                    text = ".",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Gray)
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onBack() }) {
                Icon(
                    imageVector = Delete, contentDescription = Delete.name,
                    tint = Teal200
                )
            }
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .background(Gray)
                    .fillMaxWidth()
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("-") }) {
                Text(
                    text = "-",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Gray)
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onInput("+") }) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                modifier = Modifier
                    .weight(2f)
                    .background(Gray)
                    .fillMaxWidth()
                    .border(0.2.dp, color = Color.Black.copy(alpha = 0.3f)),
                onClick = { onConfirm() }) {
                Text(
                    text = "=",
                    style = MaterialTheme.typography.h5,
                    color = Teal200,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

fun evaluateExpression(expression: String): Double {
    val rhino = Context.enter()
    return try {
        rhino.optimizationLevel = -1
        val scope = rhino.initSafeStandardObjects()
        val value = rhino.evaluateString(scope, expression, "JavaScript", 1, null) as Double
        value
    } catch (e: Exception) {
        e.printStackTrace()
        Double.NaN
    } finally {
        Context.exit()
    }
}