package com.example.tingxie.presentation.quiz_statistics.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsViewModel
import com.example.tingxie.presentation.util.StatisticsBarChart

@Composable
fun QuizStatisticsGraph(
    modifier: Modifier = Modifier,
    viewModel: QuizStatisticsViewModel
) {
    Box(
        modifier = modifier
    ) {
        StatisticsBarChart(
            barChartData = viewModel.getBarChartData(),
            modifier = modifier,
            labels = viewModel.getBarChartLabels()
        )
    }
}