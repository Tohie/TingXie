package com.example.tingxie.presentation.quiz_statistics

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.CharacterResult
import com.example.tingxie.domain.model.CharacterStatistics
import com.example.tingxie.domain.model.Quiz
import com.example.tingxie.domain.model.util.OrderCharacterResultsBy
import com.example.tingxie.domain.model.util.Ordering
import java.time.Year

data class QuizStatisticsState (
    val quizzes: Map<Quiz, List<CharacterResult>> = emptyMap(),
    val characterResults: List<CharacterStatistics> = emptyList(),
    val year: Int = 0,
    val day: Int = 0,
    val month: Int = 0,
    val isOrderingOptionsVisible: Boolean = false,
    val ordering: OrderCharacterResultsBy = OrderCharacterResultsBy.CharacterNumber(Ordering.Acsending)
)