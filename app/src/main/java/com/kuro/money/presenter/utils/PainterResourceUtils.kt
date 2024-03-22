package com.kuro.money.presenter.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.kuro.money.R

@SuppressLint("DiscouragedApi")
@Composable
fun String.toPainterResource(): Painter {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(this, "drawable", context.packageName)
    return if (resId != 0) painterResource(id = resId)
    else painterResource(id = R.mipmap.ic_launcher)
}