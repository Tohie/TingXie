package com.example.tingxie.presentation.character_quiz_results

import com.example.tingxie.domain.model.QuizResults

data class CharacterQuizResultsState (
    val quizResults: List<QuizResults> = emptyList(),
    val userScore: Int = 0,
    val totalScore: Int = 0
)