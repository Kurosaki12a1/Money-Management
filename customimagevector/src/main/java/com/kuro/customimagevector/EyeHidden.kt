package com.kuro.customimagevector

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val EyeHidden: ImageVector
    get() {
        if (_eyeHidden != null) {
            return _eyeHidden!!
        }
        _eyeHidden = Builder(
            name = "EyeHidden", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 2.0f, strokeLineCap = Round, strokeLineJoin =
                StrokeJoin.Companion.Round, strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(20.0293f, 3.0f)
                arcTo(1.0f, 1.0f, 0.0f, false, false, 19.293f, 3.293f)
                lineTo(17.2578f, 5.3281f)
                curveTo(15.6877f, 4.5456f, 13.9127f, 4.0f, 12.0f, 4.0f)
                curveTo(8.6667f, 4.0f, 5.7336f, 5.6316f, 3.6191f, 7.3496f)
                curveTo(2.5619f, 8.2086f, 1.7059f, 9.0942f, 1.0938f, 9.8594f)
                curveTo(0.7877f, 10.242f, 0.5423f, 10.5925f, 0.3574f, 10.916f)
                curveTo(0.1726f, 11.2395f, 0.0f, 11.4583f, 0.0f, 12.0f)
                curveTo(0.0f, 12.5417f, 0.1726f, 12.7605f, 0.3574f, 13.084f)
                curveTo(0.5423f, 13.4075f, 0.7877f, 13.758f, 1.0938f, 14.1406f)
                curveTo(1.7059f, 14.9058f, 2.5619f, 15.7914f, 3.6191f, 16.6504f)
                curveTo(4.0318f, 16.9856f, 4.4832f, 17.3137f, 4.9551f, 17.6309f)
                lineTo(3.293f, 19.293f)
                arcTo(1.0f, 1.0f, 0.0f, false, false, 3.293f, 20.707f)
                arcTo(1.0f, 1.0f, 0.0f, false, false, 4.707f, 20.707f)
                lineTo(6.7422f, 18.6719f)
                curveTo(8.3123f, 19.4544f, 10.0873f, 20.0f, 12.0f, 20.0f)
                curveTo(15.3333f, 20.0f, 18.2664f, 18.3684f, 20.3809f, 16.6504f)
                curveTo(21.4381f, 15.7914f, 22.2941f, 14.9058f, 22.9063f, 14.1406f)
                curveTo(23.2123f, 13.758f, 23.4577f, 13.4075f, 23.6426f, 13.084f)
                curveTo(23.8274f, 12.7605f, 24.0f, 12.5417f, 24.0f, 12.0f)
                curveTo(24.0f, 11.4583f, 23.8274f, 11.2395f, 23.6426f, 10.916f)
                curveTo(23.4577f, 10.5925f, 23.2123f, 10.242f, 22.9063f, 9.8594f)
                curveTo(22.2941f, 9.0942f, 21.4381f, 8.2086f, 20.3809f, 7.3496f)
                curveTo(19.9682f, 7.0144f, 19.5168f, 6.6863f, 19.0449f, 6.3691f)
                lineTo(20.707f, 4.707f)
                arcTo(1.0f, 1.0f, 0.0f, false, false, 20.707f, 3.293f)
                arcTo(1.0f, 1.0f, 0.0f, false, false, 20.0293f, 3.0f)
                close()
                moveTo(12.0f, 6.0f)
                curveTo(13.2968f, 6.0f, 14.5673f, 6.3273f, 15.75f, 6.8359f)
                lineTo(12.0684f, 10.5176f)
                arcTo(2.0f, 2.0f, 0.0f, false, true, 12.0f, 10.0f)
                arcTo(2.0f, 2.0f, 0.0f, false, true, 13.1777f, 8.1777f)
                arcTo(4.0f, 4.0f, 0.0f, false, false, 12.0f, 8.0f)
                arcTo(4.0f, 4.0f, 0.0f, false, false, 11.9395f, 8.0f)
                arcTo(4.0f, 4.0f, 0.0f, false, false, 8.0f, 12.0f)
                arcTo(4.0f, 4.0f, 0.0f, false, false, 8.5547f, 14.0313f)
                lineTo(6.3984f, 16.1875f)
                curveTo(5.8572f, 15.8467f, 5.3475f, 15.4787f, 4.8809f, 15.0996f)
                curveTo(3.9381f, 14.3336f, 3.1691f, 13.5317f, 2.6563f, 12.8906f)
                curveTo(2.3998f, 12.5701f, 2.2077f, 12.2878f, 2.0957f, 12.0918f)
                curveTo(2.0733f, 12.0526f, 2.0738f, 12.0348f, 2.0605f, 12.0f)
                curveTo(2.0738f, 11.9652f, 2.0733f, 11.9474f, 2.0957f, 11.9082f)
                curveTo(2.2077f, 11.7122f, 2.3998f, 11.4299f, 2.6563f, 11.1094f)
                curveTo(3.1691f, 10.4683f, 3.9381f, 9.6664f, 4.8809f, 8.9004f)
                curveTo(6.7664f, 7.3684f, 9.3333f, 6.0f, 12.0f, 6.0f)
                close()
                moveTo(17.6016f, 7.8125f)
                curveTo(18.1428f, 8.1533f, 18.6525f, 8.5213f, 19.1191f, 8.9004f)
                curveTo(20.0619f, 9.6664f, 20.8309f, 10.4683f, 21.3438f, 11.1094f)
                curveTo(21.6002f, 11.4299f, 21.7923f, 11.7122f, 21.9043f, 11.9082f)
                curveTo(21.9267f, 11.9474f, 21.9262f, 11.9652f, 21.9395f, 12.0f)
                curveTo(21.9262f, 12.0348f, 21.9267f, 12.0526f, 21.9043f, 12.0918f)
                curveTo(21.7923f, 12.2878f, 21.6002f, 12.5701f, 21.3438f, 12.8906f)
                curveTo(20.8309f, 13.5317f, 20.0619f, 14.3336f, 19.1191f, 15.0996f)
                curveTo(17.2336f, 16.6316f, 14.6667f, 18.0f, 12.0f, 18.0f)
                curveTo(10.7032f, 18.0f, 9.4327f, 17.6727f, 8.25f, 17.1641f)
                lineTo(9.9688f, 15.4453f)
                arcTo(4.0f, 4.0f, 0.0f, false, false, 12.0f, 16.0f)
                arcTo(4.0f, 4.0f, 0.0f, false, false, 16.0f, 12.0f)
                arcTo(4.0f, 4.0f, 0.0f, false, false, 15.8223f, 10.8242f)
                arcTo(2.0f, 2.0f, 0.0f, false, true, 14.0f, 12.0f)
                arcTo(2.0f, 2.0f, 0.0f, false, true, 13.4824f, 11.9316f)
                lineTo(17.6016f, 7.8125f)
                close()
            }
        }
            .build()
        return _eyeHidden!!
    }

private var _eyeHidden: ImageVector? = null
