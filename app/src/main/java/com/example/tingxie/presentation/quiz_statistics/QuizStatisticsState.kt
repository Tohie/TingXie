package com.example.tingxie.presentation.quiz_statistics

import com.example.tingxie.domain.model.CharacterQuizBarChartData
import com.example.tingxie.domain.model.CharacterQuizStatistics

data class QuizStatisticsState (
    val quizResults: List<CharacterQuizStatistics> = listOf(),
    val testScoreBarChartData: List<CharacterQuizBarChartData> = listOf()
)