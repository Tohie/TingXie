package com.example.tingxie.presentation.util

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultBlendMode
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tingxie.domain.model.BarChartData

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    barChartData: List<BarChartData>,
    padding: Dp = 0.dp,
    fontSize: Dp = 40.dp
) {
    if (barChartData.isEmpty()) return

    Canvas(modifier = modifier) {
        val totalTests = barChartData.size
        val withNoPaddingBarSize = size.width / (totalTests + 1)
        val barSize = withNoPaddingBarSize - padding.times(2).toPx()

        val height = size.height
        val actualHeight = height - padding.toPx() - fontSize.toPx()
        val maxValue = barChartData.maxBy { it.value }.value
        val fontSpace = fontSize.toPx() // One text above, one below

        barChartData.forEachIndexed { index, barChartData ->
            val barHeight =  ((barChartData.value / maxValue) * actualHeight)
            val topLeft = Offset(2 * padding.toPx() + (withNoPaddingBarSize * index), (actualHeight - barHeight ))
            val size = Size(barSize, barHeight)

            drawRect(
                color = barChartData.color,
                topLeft = topLeft,
                alpha = 1.0f,
                style = Fill,
                colorFilter = null,
                blendMode = DefaultBlendMode,
                size = size
            )

            drawLine(
                start = Offset(padding.toPx() + (withNoPaddingBarSize * index), actualHeight),
                end = Offset(padding.toPx() + (withNoPaddingBarSize * index), actualHeight - 20),
                color = Color.Black,
                strokeWidth = 5f
            )

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    barChartData.label,
                    ((2 * padding.toPx()) + (withNoPaddingBarSize * index )) + (barSize / 2),
                    height - padding.toPx() - (fontSpace / 2),
                    Paint().apply {
                        textSize = 20f
                        color = Color.Black.toArgb()
                        textAlign = Paint.Align.CENTER
                    }
                )
            }

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    barChartData.value.toString(),
                    ((2 * padding.toPx()) + (withNoPaddingBarSize * index)) + (barSize / 2),
                    height - (barHeight + fontSpace) - (fontSpace/2),
                    Paint().apply {
                        textSize = 50f
                        color = Color.Black.toArgb()
                        textAlign = Paint.Align.CENTER
                    }
                )
            }
        }

        drawLine(
            start = Offset(padding.toPx(), actualHeight),
            end = Offset(size.width - padding.toPx(), actualHeight),
            color = Color.Black,
            strokeWidth = 5f
        )

        drawLine(
            start = Offset(padding.toPx(), 0f),
            end = Offset(padding.toPx(), actualHeight),
            color = Color.Black,
            strokeWidth = 5f
        )
    }
}