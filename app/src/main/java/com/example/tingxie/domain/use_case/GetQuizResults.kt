package com.example.tingxie.domain.use_case

import androidx.compose.ui.graphics.Color
import com.example.tingxie.domain.model.*
import com.example.tingxie.domain.repository.CharacterRepository
import com.example.tingxie.domain.use_case.utils.Converters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class GetQuizResults(
    private val repository: CharacterRepository
) {
    operator fun invoke(): Flow<Map<QuizResult, Character>> {
        return repository.getQuizResults()
    }

    fun getCharacterQuizResults(): Flow<List<CharacterQuizStatistic>> {
        return repository.getQuizResults().map { quizResultsMap ->
            Converters.convertQuizResultsToList(quizResultsMap)
        }
    }

    fun getTestScoreData(): Flow<List<BarChartData>> {
        return repository.getQuizResults().map { quizResults ->
            Converters.convertQuizResultsToTestData(quizResults)
        }
    }
}