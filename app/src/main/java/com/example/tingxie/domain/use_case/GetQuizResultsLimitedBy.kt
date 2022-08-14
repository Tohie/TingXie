package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.QuizResult
import com.example.tingxie.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetQuizResultsLimitedBy(
    private val repository: CharacterRepository
) {
    operator fun invoke(limit: Int): Flow<Map<QuizResult, Character>>  {
        return repository.getQuizResultsLimitedBy(limit)
    }
}