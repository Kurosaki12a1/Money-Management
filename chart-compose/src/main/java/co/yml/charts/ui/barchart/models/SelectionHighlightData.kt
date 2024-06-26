package co.yml.charts.ui.barchart.models

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.getTextBackgroundRect
import java.util.Locale

/**
 * Used to customise the highlighted text and the bar
 *
 * @param highlightTextOffset: Padding between the highlighted bar and the text
 * @param highlightTextSize: Text size of the highlighted bar text
 * @param highlightTextColor: Text color of the highlighted bar text
 * @param highlightTextBackgroundColor: Background color of the highlight background
 * @param highlightTextBackgroundAlpha: Alpha for the highlighted text background
 * @param highlightTextTypeface: Typeface of the highlighted bar text
 * @param highlightBarCornerRadius: Corner radius of the highlighted bar
 * @param highlightBarColor: Color of the highlighted bar
 * @param highlightBarStrokeWidth: Stroke width of the highlighted bar
 * @param highlightPopUpCornerRadius: Corner radius of the highlighted background
 * @param backgroundColorFilter: ColorFilter to apply to the color when drawn into the destination.
 * @param backgroundBlendMode:Blending algorithm to be applied to the path when it is drawn.
 * @param backgroundStyle: Whether or not the path is stroked or filled in.
 * @param isHighlightBarRequired: Boolean flag to enable disable highlight
 * @param popUpLabel : The text that can be shown on the popup given 2 input params x and y values
 * @param drawPopUp: Override this to change the default background implementation. You are provided
 * with the selected offset, x, y values, center point of bar.
 * @param drawHighlightBar: draw override this to change the default drawRoundRect implementation. You are provided
 * with the actual point x, y, height, width.
 * @param groupBarPopUpLabel: Popup label for selected bar in group bar chart.
 * @param drawGroupBarPopUp: draw override this to change the default popup implementation. You are provided
 * with the actual point x, y, height, width.
 */
data class SelectionHighlightData(
    //highlight text 
    val highlightTextOffset: Dp = 15.dp,
    val highlightTextSize: TextUnit = 12.sp,
    val highlightTextColor: Color = Color.Black,
    val highlightTextBackgroundColor: Color = Color.Yellow,
    val highlightTextBackgroundAlpha: Float = 0.7f,
    val highlightTextTypeface: Typeface = Typeface.DEFAULT,
    // highlight bar
    val highlightBarCornerRadius: Dp = 2.dp,
    val highlightBarColor: Color = Color.Black,
    val highlightBarStrokeWidth: Dp = 2.dp,

    val highlightPopUpCornerRadius: CornerRadius = CornerRadius(5f),
    val backgroundColorFilter: ColorFilter? = null,
    val backgroundBlendMode: BlendMode = DrawScope.DefaultBlendMode,
    val backgroundStyle: DrawStyle = Fill,
    val highlightLabelAlignment: Paint.Align = Paint.Align.CENTER,
    val isHighlightBarRequired: Boolean = true,
    val isHighlightFullBar: Boolean = false,
    val popUpLabel: (Float) -> (String) = { value-> value.toString() },

    val drawPopUp: DrawScope.(Offset, BarData, Float, Float, BarChartType) -> Unit = { selectedOffset, identifiedPoint, centerPointOfBar, selectedXAxisWidth, barChartType ->
        val highlightTextPaint = TextPaint().apply {
            textSize = highlightTextSize.toPx()
            color = highlightTextColor.toArgb()
            textAlign = highlightLabelAlignment
            typeface = highlightTextTypeface
        }

        val label = if (barChartType == BarChartType.VERTICAL) popUpLabel(identifiedPoint.point.y) else popUpLabel(identifiedPoint.point.x)
        drawContext.canvas.nativeCanvas.apply {
            val x =
                if (barChartType == BarChartType.VERTICAL) centerPointOfBar else selectedXAxisWidth + highlightTextOffset.toPx()
            val y =
                if (barChartType == BarChartType.VERTICAL) selectedOffset.y else centerPointOfBar
            val background = getTextBackgroundRect(
                x,
                y,
                label,
                highlightTextPaint
            )
            drawRoundRect(
                color = highlightTextBackgroundColor,
                topLeft = Offset(
                    background.left.toFloat(),
                    if (barChartType == BarChartType.VERTICAL) background.top.toFloat() - highlightTextOffset.toPx() else (y - background.height() / 2)
                ),
                size = Size(background.width().toFloat(), background.height().toFloat()),
                alpha = highlightTextBackgroundAlpha,
                cornerRadius = highlightPopUpCornerRadius,
                colorFilter = backgroundColorFilter,
                blendMode = backgroundBlendMode,
                style = backgroundStyle
            )
            val drawTextX =
                if (barChartType == BarChartType.VERTICAL) centerPointOfBar else selectedXAxisWidth + highlightTextOffset.toPx()
            val drawTextY =
                if (barChartType == BarChartType.VERTICAL) selectedOffset.y - highlightTextOffset.toPx() else (y + background.height() / 2) - (highlightTextPaint.fontMetrics.descent)
            drawText(
                label,
                drawTextX,
                drawTextY,
                highlightTextPaint
            )
        }
    },

    val drawHighlightBar: DrawScope.(Float, Float, Float, Float, BarChartType) -> Unit = { x, y, width, height, barChartType ->
        val rectWidth = if (barChartType == BarChartType.VERTICAL) width else height
        val rectHeight = if (barChartType == BarChartType.VERTICAL) height else width
        drawRoundRect(
            color = highlightBarColor,
            topLeft = Offset(x, y),
            size = Size(rectWidth, rectHeight),
            cornerRadius = CornerRadius(
                highlightBarCornerRadius.toPx(),
                highlightBarCornerRadius.toPx()
            ),
            style = Stroke(width = highlightBarStrokeWidth.toPx())
        )
    },

    val groupBarPopUpLabel: (String, Float) -> (String) = { name, value ->
        val yLabel = "Value : ${String.format(Locale.getDefault(),"%.2f", value)}"
        "$name $yLabel"
    },


    val drawGroupBarPopUp: DrawScope.(Offset, BarData, Float) -> Unit = { selectedOffset, identifiedPoint, centerPointOfBar ->
        val highlightTextPaint = TextPaint().apply {
            textSize = highlightTextSize.toPx()
            color = highlightTextColor.toArgb()
            textAlign = highlightLabelAlignment
            typeface = highlightTextTypeface
        }
        val xLabel = identifiedPoint.label
        val label = groupBarPopUpLabel(xLabel, identifiedPoint.point.y)
        drawContext.canvas.nativeCanvas.apply {
            val background = getTextBackgroundRect(
                centerPointOfBar,
                selectedOffset.y,
                label,
                highlightTextPaint
            )
            drawRoundRect(
                color = highlightTextBackgroundColor,
                topLeft = Offset(
                    background.left.toFloat(),
                    background.top.toFloat() - highlightTextOffset.toPx()
                ),
                size = Size(background.width().toFloat(), background.height().toFloat()),
                alpha = highlightTextBackgroundAlpha,
                cornerRadius = highlightPopUpCornerRadius,
                colorFilter = backgroundColorFilter,
                blendMode = backgroundBlendMode,
                style = backgroundStyle
            )
            drawText(
                label,
                centerPointOfBar,
                selectedOffset.y - highlightTextOffset.toPx(),
                highlightTextPaint
            )
        }
    },
    val barChartType: BarChartType = BarChartType.VERTICAL
)
