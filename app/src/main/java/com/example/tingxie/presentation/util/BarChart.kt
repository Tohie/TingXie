package com.example.tingxie.presentation.util

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
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
    barChartData: List<BarEntry>,
    labels: List<String>,
    color: Color = MaterialTheme.colors.primary
) {
    if (barChartData.isEmpty()) return

    AndroidView(
        modifier = modifier,
        factory = {
            BarChart(it)
        },
        update = { barChart ->
            val entries = barChartData

            barChart.xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(labels)
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                labelRotationAngle = 15f
                setCenterAxisLabels(true)
                isEnabled = true
                setDrawAxisLine(true)
                setLabelCount(labels.size, true)
            }
            barChart.axisLeft.apply {
                setDrawGridLines(false)
            }
            barChart.axisRight.apply {
                setDrawGridLines(false)
            }

            val dataSet = BarDataSet(entries, "Percentage")
            dataSet.color = color.toArgb()
            val barData = BarData(dataSet)

            barChart.apply {
                data = barData
                animateY(1000, Easing.EaseInQuad)
            }
        }
    )
}

