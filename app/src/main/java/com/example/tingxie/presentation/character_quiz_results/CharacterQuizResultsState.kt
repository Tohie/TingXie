package com.example.tingxie.presentation.character_quiz_results

import com.example.tingxie.domain.model.CharacterResult
import com.example.tingxie.domain.model.CharacterStatistics
import com.example.tingxie.domain.model.Quiz
import com.example.tingxie.domain.model.util.OrderCharacterResultsBy
import com.example.tingxie.domain.model.util.Ordering

data class CharacterQuizResultsState (
    val quizResults: Quiz = Quiz(quizId = -1, 0, 0, 0),
    val characterStatistics: List<CharacterStatistics> = emptyList(),
)