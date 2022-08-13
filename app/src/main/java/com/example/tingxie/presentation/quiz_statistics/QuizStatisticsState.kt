package com.example.tingxie.presentation.quiz_statistics

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.QuizResult

data class QuizStatisticsState (
    val quizResults: List<CharacterQuizStatistic> = listOf()
)