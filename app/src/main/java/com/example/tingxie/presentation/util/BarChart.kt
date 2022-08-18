package com.example.tingxie.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tingxie.domain.model.CharacterQuizBarChartData
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun StatisticsBarChart(
    modifier: Modifier = Modifier,
    barChartData: List<CharacterQuizBarChartData>,
) {
    if (barChartData.isEmpty()) return

    AndroidView(
        modifier = modifier,
        factory = {
            BarChart(it)
        },
        update = { barChart ->
            val entries = barChartData.mapIndexed { index, barChartData ->
                BarEntry(
                    index.toFloat(),
                    barChartData.value
                )
            }

            val xLabels = barChartData.map { barChartData ->
                barChartData.label
            }
            barChart.xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(xLabels)
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                labelRotationAngle = 15f
                setCenterAxisLabels(true)
                isEnabled = true
                setDrawAxisLine(true)
                setLabelCount(xLabels.size, true)
            }
            barChart.axisLeft.apply {
                setDrawGridLines(false)
            }
            barChart.axisRight.apply {
                setDrawGridLines(false)
            }

            val dataSet = BarDataSet(entries, "Percentage").apply {
                color = barChartData.first().color.toArgb()
            }
            val barData = BarData(dataSet)

            barChart.apply {
                data = barData
                animateY(1000, Easing.EaseInQuad)
            }
        }
    )
}

