package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.BarChartData
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.QuizResult
import com.example.tingxie.domain.repository.CharacterRepository
import com.example.tingxie.domain.use_case.utils.Converters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetQuizResultsLimitedBy(
    private val repository: CharacterRepository
) {
    operator fun invoke(limit: Int): Flow<List<BarChartData>> {
        return repository.getQuizResultsLimitedBy(limit).map { quizResults ->
            Converters.convertQuizResultsToTestData(quizResults)
        }
    }
}