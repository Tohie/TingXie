package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.QuizResult
import com.example.tingxie.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetQuizResultsBetween(
    private val repository: CharacterRepository
) {
    operator fun invoke(start: Long, end: Long): Flow<Map<QuizResult, Character>> {
        return repository.getQuizResultBetween(start, end)
    }
}