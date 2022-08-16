package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.*
import com.example.tingxie.domain.repository.CharacterRepository
import com.example.tingxie.domain.use_case.utils.toCharacterQuizStatistics
import com.example.tingxie.domain.use_case.utils.toTestResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

class GetQuizResults(
    private val repository: CharacterRepository
) {
    operator fun invoke(): Flow<Map<QuizResult, Character>> {
        return repository.getQuizResults()
    }

    fun getCharacterQuizResults(): Flow<List<CharacterQuizStatistics>> {
        return repository.getQuizResults().map { it.toCharacterQuizStatistics() }
    }

    fun getTestScoreData(): Flow<List<CharacterQuizBarChartData>> {
        return repository.getQuizResults().map { it.toTestResultData() }
    }

    fun getQuizResult(timestamp: Long): Flow<Map<QuizResult, Character>> {
        return repository.getQuizResult(timestamp)
    }

    fun getQuizResultsOn(year: Int, month: Int, dayOfMonth: Int): Flow<List<CharacterQuizBarChartData>> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val time = calendar.time
        val localDate = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val startOfDay = localDate.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli()
        val endOfDay = localDate.atTime(LocalTime.MAX).toInstant(ZoneOffset.MAX).toEpochMilli()

        return repository.getQuizResultBetween(startOfDay, endOfDay).map { it.toTestResultData() }
    }

    fun getQuizResultsLimitedBy(limit: Int): Flow<List<CharacterQuizBarChartData>> {
        return repository.getQuizResultsLimitedBy(limit).map { it.toTestResultData() }
    }

    fun getCharacterResults(character: String): Flow<Map<QuizResult, Character>> {
        return repository.getCharacterResults(character)
    }
}