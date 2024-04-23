package com.kuro.money.presenter.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import com.kuro.money.R

@SuppressLint("DiscouragedApi")
@Composable
fun String?.toPainterResource(): Painter {
    if (this == null) return painterResource(id = R.mipmap.ic_launcher)
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(this, "drawable", context.packageName)
    return if (resId != 0) painterResource(id = resId)
    else painterResource(id = R.mipmap.ic_launcher)
}

@SuppressLint("DiscouragedApi")
@Composable
fun String?.toImageBitmap(): ImageBitmap {
    if (this == null) return ImageBitmap.imageResource(id = R.mipmap.ic_launcher)
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(this, "drawable", context.packageName)
    return if (resId != 0) ImageBitmap.imageResource(id = resId)
    else ImageBitmap.imageResource(id = R.mipmap.ic_launcher)
}