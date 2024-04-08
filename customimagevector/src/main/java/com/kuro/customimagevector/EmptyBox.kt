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

val EmptyBox: ImageVector
    get() {
        if (_emptyBox != null) {
            return _emptyBox!!
        }
        _emptyBox = Builder(
            name = "EmptyBox", defaultWidth = 72.0.dp, defaultHeight = 72.0.dp,
            viewportWidth = 1024.0f, viewportHeight = 1024.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(660.0f, 103.2f)
                lineToRelative(-149.6f, 76.0f)
                lineToRelative(2.4f, 1.6f)
                lineToRelative(-2.4f, -1.6f)
                lineToRelative(-157.6f, -80.8f)
                lineTo(32.0f, 289.6f)
                lineToRelative(148.8f, 85.6f)
                verticalLineToRelative(354.4f)
                lineToRelative(329.6f, -175.2f)
                lineToRelative(324.8f, 175.2f)
                verticalLineTo(375.2f)
                lineTo(992.0f, 284.8f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF6A576D)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(180.8f, 737.6f)
                curveToRelative(-1.6f, 0.0f, -3.2f, 0.0f, -4.0f, -0.8f)
                curveToRelative(-2.4f, -1.6f, -4.0f, -4.0f, -4.0f, -7.2f)
                verticalLineTo(379.2f)
                lineTo(28.0f, 296.0f)
                curveToRelative(-2.4f, -0.8f, -4.0f, -4.0f, -4.0f, -6.4f)
                reflectiveCurveToRelative(1.6f, -5.6f, 4.0f, -7.2f)
                lineToRelative(320.8f, -191.2f)
                curveToRelative(2.4f, -1.6f, 5.6f, -1.6f, 8.0f, 0.0f)
                lineToRelative(154.4f, 79.2f)
                lineTo(656.0f, 96.0f)
                curveToRelative(2.4f, -1.6f, 4.8f, -0.8f, 7.2f, 0.0f)
                lineToRelative(332.0f, 181.6f)
                curveToRelative(2.4f, 1.6f, 4.0f, 4.0f, 4.0f, 7.2f)
                reflectiveCurveToRelative(-1.6f, 5.6f, -4.0f, 7.2f)
                lineToRelative(-152.8f, 88.0f)
                verticalLineToRelative(350.4f)
                curveToRelative(0.0f, 3.2f, -1.6f, 5.6f, -4.0f, 7.2f)
                curveToRelative(-2.4f, 1.6f, -5.6f, 1.6f, -8.0f, 0.0f)
                lineToRelative(-320.0f, -174.4f)
                lineToRelative(-325.6f, 173.6f)
                curveToRelative(-1.6f, 0.8f, -2.4f, 0.8f, -4.0f, 0.8f)
                close()
                moveTo(48.0f, 289.6f)
                lineTo(184.8f, 368.0f)
                curveToRelative(2.4f, 1.6f, 4.0f, 4.0f, 4.0f, 7.2f)
                verticalLineToRelative(341.6f)
                lineToRelative(317.6f, -169.6f)
                curveToRelative(2.4f, -1.6f, 5.6f, -1.6f, 7.2f, 0.0f)
                lineToRelative(312.8f, 169.6f)
                verticalLineTo(375.2f)
                curveToRelative(0.0f, -3.2f, 1.6f, -5.6f, 4.0f, -7.2f)
                lineTo(976.0f, 284.8f)
                lineTo(659.2f, 112.8f)
                lineTo(520.0f, 183.2f)
                curveToRelative(0.0f, 0.8f, -0.8f, 0.8f, -0.8f, 1.6f)
                curveToRelative(-2.4f, 4.0f, -7.2f, 4.8f, -11.2f, 2.4f)
                lineToRelative(-1.6f, -1.6f)
                horizontalLineToRelative(-0.8f)
                lineToRelative(-152.8f, -78.4f)
                lineTo(48.0f, 289.6f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF121519)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(510.4f, 179.2f)
                lineToRelative(324.8f, 196.0f)
                verticalLineToRelative(354.4f)
                lineTo(510.4f, 554.4f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF121519)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(510.4f, 179.2f)
                lineTo(180.8f, 375.2f)
                verticalLineToRelative(354.4f)
                lineToRelative(329.6f, -175.2f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF6A576D)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(835.2f, 737.6f)
                curveToRelative(-1.6f, 0.0f, -2.4f, 0.0f, -4.0f, -0.8f)
                lineToRelative(-324.8f, -176.0f)
                curveToRelative(-2.4f, -1.6f, -4.0f, -4.0f, -4.0f, -7.2f)
                verticalLineTo(179.2f)
                curveToRelative(0.0f, -3.2f, 1.6f, -5.6f, 4.0f, -7.2f)
                curveToRelative(2.4f, -1.6f, 5.6f, -1.6f, 8.0f, 0.0f)
                lineTo(839.2f, 368.0f)
                curveToRelative(2.4f, 1.6f, 4.0f, 4.0f, 4.0f, 7.2f)
                verticalLineToRelative(355.2f)
                curveToRelative(0.0f, 3.2f, -1.6f, 5.6f, -4.0f, 7.2f)
                horizontalLineToRelative(-4.0f)
                close()
                moveTo(518.4f, 549.6f)
                lineToRelative(308.8f, 167.2f)
                verticalLineTo(379.2f)
                lineTo(518.4f, 193.6f)
                verticalLineToRelative(356.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF6A576D)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(180.8f, 737.6f)
                curveToRelative(-1.6f, 0.0f, -3.2f, 0.0f, -4.0f, -0.8f)
                curveToRelative(-2.4f, -1.6f, -4.0f, -4.0f, -4.0f, -7.2f)
                lineTo(172.8f, 375.2f)
                curveToRelative(0.0f, -3.2f, 1.6f, -5.6f, 4.0f, -7.2f)
                lineToRelative(329.6f, -196.0f)
                curveToRelative(2.4f, -1.6f, 5.6f, -1.6f, 8.0f, 0.0f)
                curveToRelative(2.4f, 1.6f, 4.0f, 4.0f, 4.0f, 7.2f)
                verticalLineToRelative(375.2f)
                curveToRelative(0.0f, 3.2f, -1.6f, 5.6f, -4.0f, 7.2f)
                lineToRelative(-329.6f, 176.0f)
                horizontalLineToRelative(-4.0f)
                close()
                moveTo(188.8f, 379.2f)
                verticalLineToRelative(337.6f)
                lineToRelative(313.6f, -167.2f)
                lineTo(502.4f, 193.6f)
                lineTo(188.8f, 379.2f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFD6AB7F)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(510.4f, 550.4f)
                lineTo(372.0f, 496.0f)
                lineTo(180.8f, 374.4f)
                verticalLineToRelative(355.2f)
                lineToRelative(329.6f, 196.0f)
                lineToRelative(324.8f, -196.0f)
                verticalLineTo(374.4f)
                lineTo(688.8f, 483.2f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF6A576D)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(510.4f, 933.6f)
                curveToRelative(-1.6f, 0.0f, -3.2f, 0.0f, -4.0f, -0.8f)
                lineTo(176.8f, 736.8f)
                curveToRelative(-2.4f, -1.6f, -4.0f, -4.0f, -4.0f, -7.2f)
                lineTo(172.8f, 374.4f)
                curveToRelative(0.0f, -3.2f, 1.6f, -5.6f, 4.0f, -7.2f)
                curveToRelative(2.4f, -1.6f, 5.6f, -1.6f, 8.0f, 0.0f)
                lineTo(376.0f, 488.8f)
                lineToRelative(135.2f, 53.6f)
                lineToRelative(174.4f, -66.4f)
                lineTo(830.4f, 368.0f)
                curveToRelative(2.4f, -1.6f, 5.6f, -2.4f, 8.0f, -0.8f)
                curveToRelative(2.4f, 1.6f, 4.0f, 4.0f, 4.0f, 7.2f)
                verticalLineToRelative(355.2f)
                curveToRelative(0.0f, 3.2f, -1.6f, 5.6f, -4.0f, 7.2f)
                lineToRelative(-324.8f, 196.0f)
                reflectiveCurveToRelative(-1.6f, 0.8f, -3.2f, 0.8f)
                close()
                moveTo(188.8f, 725.6f)
                lineToRelative(321.6f, 191.2f)
                lineToRelative(316.8f, -191.2f)
                lineTo(827.2f, 390.4f)
                lineTo(693.6f, 489.6f)
                curveToRelative(-0.8f, 0.8f, -1.6f, 0.8f, -1.6f, 0.8f)
                lineToRelative(-178.4f, 68.0f)
                curveToRelative(-1.6f, 0.8f, -4.0f, 0.8f, -5.6f, 0.0f)
                lineTo(369.6f, 504.0f)
                curveToRelative(-0.8f, 0.0f, -0.8f, -0.8f, -1.6f, -0.8f)
                lineTo(188.8f, 389.6f)
                verticalLineToRelative(336.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF121519)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(510.4f, 925.6f)
                lineToRelative(324.8f, -196.0f)
                verticalLineTo(374.4f)
                lineTo(665.6f, 495.2f)
                lineToRelative(-155.2f, 55.2f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF6A576D)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(510.4f, 933.6f)
                curveToRelative(-1.6f, 0.0f, -2.4f, 0.0f, -4.0f, -0.8f)
                curveToRelative(-2.4f, -1.6f, -4.0f, -4.0f, -4.0f, -7.2f)
                lineTo(502.4f, 550.4f)
                curveToRelative(0.0f, -3.2f, 2.4f, -6.4f, 5.6f, -7.2f)
                lineTo(662.4f, 488.0f)
                lineToRelative(168.0f, -120.0f)
                curveToRelative(2.4f, -1.6f, 5.6f, -1.6f, 8.0f, -0.8f)
                curveToRelative(2.4f, 1.6f, 4.0f, 4.0f, 4.0f, 7.2f)
                verticalLineToRelative(355.2f)
                curveToRelative(0.0f, 3.2f, -1.6f, 5.6f, -4.0f, 7.2f)
                lineToRelative(-324.8f, 196.0f)
                reflectiveCurveToRelative(-1.6f, 0.8f, -3.2f, 0.8f)
                close()
                moveTo(518.4f, 556.0f)
                verticalLineToRelative(355.2f)
                lineToRelative(308.8f, -185.6f)
                lineTo(827.2f, 390.4f)
                lineTo(670.4f, 501.6f)
                curveToRelative(-0.8f, 0.8f, -1.6f, 0.8f, -1.6f, 0.8f)
                lineToRelative(-150.4f, 53.6f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF121519)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(252.8f, 604.0f)
                lineToRelative(257.6f, 145.6f)
                verticalLineTo(550.4f)
                lineToRelative(-147.2f, -49.6f)
                lineToRelative(-182.4f, -126.4f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(32.0f, 460.0f)
                lineToRelative(148.8f, -85.6f)
                lineToRelative(329.6f, 176.0f)
                lineTo(352.0f, 640.8f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF121519)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(659.2f, 693.6f)
                lineToRelative(176.0f, -90.4f)
                verticalLineTo(375.2f)
                lineTo(692.0f, 480.8f)
                lineToRelative(-179.2f, 68.0f)
                lineToRelative(-2.4f, 1.6f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(510.4f, 550.4f)
                lineToRelative(148.8f, 85.6f)
                lineTo(992.0f, 464.8f)
                lineToRelative(-156.8f, -89.6f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF6A576D)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(352.0f, 648.8f)
                curveToRelative(-1.6f, 0.0f, -2.4f, 0.0f, -4.0f, -0.8f)
                lineToRelative(-320.0f, -180.8f)
                curveToRelative(-2.4f, -1.6f, -4.0f, -4.0f, -4.0f, -7.2f)
                reflectiveCurveToRelative(1.6f, -5.6f, 4.0f, -7.2f)
                lineTo(176.8f, 368.0f)
                curveToRelative(2.4f, -1.6f, 5.6f, -1.6f, 8.0f, 0.0f)
                lineToRelative(329.6f, 176.0f)
                curveToRelative(2.4f, 1.6f, 4.0f, 4.0f, 4.0f, 7.2f)
                reflectiveCurveToRelative(-1.6f, 5.6f, -4.0f, 7.2f)
                lineTo(356.0f, 648.0f)
                curveToRelative(-0.8f, 0.8f, -2.4f, 0.8f, -4.0f, 0.8f)
                close()
                moveTo(48.0f, 460.0f)
                lineTo(352.0f, 632.0f)
                lineToRelative(141.6f, -80.8f)
                lineTo(180.8f, 384.0f)
                lineTo(48.0f, 460.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF6A576D)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(659.2f, 644.0f)
                curveToRelative(-1.6f, 0.0f, -2.4f, 0.0f, -4.0f, -0.8f)
                lineTo(506.4f, 557.6f)
                curveToRelative(-2.4f, -1.6f, -4.0f, -4.0f, -4.0f, -7.2f)
                reflectiveCurveToRelative(1.6f, -5.6f, 4.0f, -7.2f)
                lineToRelative(324.8f, -176.0f)
                curveToRelative(2.4f, -1.6f, 5.6f, -1.6f, 8.0f, 0.0f)
                lineToRelative(156.8f, 90.4f)
                curveToRelative(2.4f, 1.6f, 4.0f, 4.0f, 4.0f, 7.2f)
                reflectiveCurveToRelative(-1.6f, 5.6f, -4.0f, 7.2f)
                lineTo(663.2f, 643.2f)
                curveToRelative(-1.6f, 0.8f, -2.4f, 0.8f, -4.0f, 0.8f)
                close()
                moveTo(527.2f, 550.4f)
                lineToRelative(132.8f, 76.0f)
                lineTo(976.0f, 464.0f)
                lineToRelative(-141.6f, -80.0f)
                lineToRelative(-307.2f, 166.4f)
                close()
            }
        }
            .build()
        return _emptyBox!!
    }

private var _emptyBox: ImageVector? = null
