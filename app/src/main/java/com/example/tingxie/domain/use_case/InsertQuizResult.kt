package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.QuizResult
import com.example.tingxie.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class InsertQuizResult (
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(quizResult: QuizResult) {
        return repository.insertQuizResult(quizResult)
    }
}