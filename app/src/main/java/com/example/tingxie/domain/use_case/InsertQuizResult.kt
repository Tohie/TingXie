package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.QuizResult
import com.example.tingxie.domain.repository.CharacterRepository

class InsertQuizResult (
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(quizResult: QuizResult) {
        return repository.insertQuizResult(quizResult)
    }

    suspend operator fun invoke(quizResults: List<QuizResult>) {
        return repository.insetQuizResults(quizResults)
    }
}