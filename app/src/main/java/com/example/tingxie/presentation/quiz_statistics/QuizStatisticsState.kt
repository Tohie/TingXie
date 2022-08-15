package com.example.tingxie.presentation.quiz_statistics

import com.example.tingxie.domain.model.BarChartData
import com.example.tingxie.domain.model.CharacterQuizStatistic

data class QuizStatisticsState (
    val quizResults: List<CharacterQuizStatistic> = listOf(),
    val testScoreBarChartData: List<BarChartData> = listOf()
)