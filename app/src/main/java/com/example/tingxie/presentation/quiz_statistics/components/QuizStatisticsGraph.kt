package com.example.tingxie.presentation.quiz_statistics.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            barChartData = viewModel.state.value.testScoreBarChartData,
            modifier = modifier,
        )
    }
}