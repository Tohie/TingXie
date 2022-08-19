package com.example.tingxie.presentation.quiz_statistics

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.CharacterResult
import com.example.tingxie.domain.model.CharacterStatistics
import com.example.tingxie.domain.model.Quiz

data class QuizStatisticsState (
    val quizzes: Map<Quiz, List<CharacterResult>> = emptyMap(),
    val characterResults: List<CharacterStatistics> = emptyList()
)