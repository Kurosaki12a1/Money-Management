package com.kuro.customimagevector

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val EyeOpen: ImageVector
    get() {
        if (_eyeOpen != null) {
            return _eyeOpen!!
        }
        _eyeOpen = Builder(
            name = "EyeOpen", defaultWidth = 30.0.dp, defaultHeight = 30.0.dp,
            viewportWidth = 512.0f, viewportHeight = 512.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(447.1f, 256.2f)
                curveTo(401.8f, 204.0f, 339.2f, 144.0f, 256.0f, 144.0f)
                curveToRelative(-33.6f, 0.0f, -64.4f, 9.5f, -96.9f, 29.8f)
                curveTo(131.7f, 191.0f, 103.6f, 215.2f, 65.0f, 255.0f)
                lineToRelative(-1.0f, 1.0f)
                lineToRelative(6.7f, 6.9f)
                curveTo(125.8f, 319.3f, 173.4f, 368.0f, 256.0f, 368.0f)
                curveToRelative(36.5f, 0.0f, 71.9f, -11.9f, 108.2f, -36.4f)
                curveToRelative(30.9f, -20.9f, 57.2f, -47.4f, 78.3f, -68.8f)
                lineToRelative(5.5f, -5.5f)
                lineTo(447.1f, 256.2f)
                close()
                moveTo(256.0f, 336.0f)
                curveToRelative(-44.1f, 0.0f, -80.0f, -35.9f, -80.0f, -80.0f)
                curveToRelative(0.0f, -44.1f, 35.9f, -80.0f, 80.0f, -80.0f)
                curveToRelative(44.1f, 0.0f, 80.0f, 35.9f, 80.0f, 80.0f)
                curveTo(336.0f, 300.1f, 300.1f, 336.0f, 256.0f, 336.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(250.4f, 226.8f)
                curveToRelative(0.0f, -6.9f, 2.0f, -13.4f, 5.5f, -18.8f)
                curveToRelative(-26.5f, 0.0f, -47.9f, 21.6f, -47.9f, 48.2f)
                curveToRelative(0.0f, 26.6f, 21.5f, 48.1f, 47.9f, 48.1f)
                reflectiveCurveToRelative(48.0f, -21.5f, 48.0f, -48.1f)
                verticalLineToRelative(0.0f)
                curveToRelative(-5.4f, 3.5f, -11.9f, 5.5f, -18.8f, 5.5f)
                curveTo(266.0f, 261.6f, 250.4f, 246.0f, 250.4f, 226.8f)
                close()
            }
        }
            .build()
        return _eyeOpen!!
    }

private var _eyeOpen: ImageVector? = null
