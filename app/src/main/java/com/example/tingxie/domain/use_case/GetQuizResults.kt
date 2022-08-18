package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.*
import com.example.tingxie.domain.repository.CharacterRepository
import com.example.tingxie.domain.use_case.utils.toCharacterQuizStatistics
import com.example.tingxie.domain.use_case.utils.toTestResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

class GetQuizResults(
    private val repository: CharacterRepository
) {
    operator fun invoke(): Flow<Map<Character, List<QuizResult>>> {
        return repository.getCharacterResults()
    }

    fun getCharacterQuizResults(): Flow<List<CharacterQuizStatistics>> {
        return repository.getCharacterResults().map { it.toCharacterQuizStatistics() }
    }

    fun getTestScoreData(): Flow<List<CharacterQuizBarChartData>> {
        return repository.getQuizResults().map { it.toTestResultData() }
    }

    fun getQuizResult(timestamp: Long): Flow<Map<Character, QuizResult>> {
        return repository.getQuizResult(timestamp)
    }

    fun getQuizResultsOn(year: Int, month: Int, dayOfMonth: Int): Flow<List<CharacterQuizBarChartData>> {
        val (start, end) = timestampToStartAndEndOfDay(year, month, dayOfMonth)

        return repository.getQuizResultBetween(start, end).map { it.toTestResultData() }
    }

    fun getQuizResultsLimitedBy(limit: Int): Flow<List<CharacterQuizBarChartData>> {
        return repository.getQuizResultsLimitedBy(limit).map { it.toTestResultData() }
    }

    fun getLatestQuiz(): Flow<List<QuizResults>> {
        return repository.getQuizResults().map { quizResults ->
            val sortedQuizResults = quizResults.keys.sortedByDescending { it.timestamp }
            val firstTimestamp = sortedQuizResults.first().timestamp
            quizResults
                .toList()
                .sortedByDescending { (quizResult, _) -> quizResult.timestamp }
                .takeWhile { (quizResult, _) -> quizResult.timestamp == firstTimestamp }
                .map { (quizResult, char) ->
                    QuizResults(
                        character = char,
                        wasCorrect = quizResult.isCorrect
                    )
                }
        }
    }

    fun getCharacterResults(character: String): Flow<Map<Character, List<QuizResult>>> {
        return repository.getCharacterResults(character)
    }

    companion object{
        fun timestampToStartAndEndOfDay(year: Int, month: Int, dayOfMonth: Int): Pair<Long, Long> {
            val calendar = GregorianCalendar(year, month, dayOfMonth)
            val instant = calendar.toInstant()
            val zonedDateTime = instant.atZone(ZoneId.systemDefault())
            val localDate = zonedDateTime.toLocalDate()

            val startOfDay: LocalDateTime = localDate.atTime(LocalTime.MIN)
            val endOfDay = localDate.atTime(LocalTime.MAX)

            val startOfDayTimeStamp = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endOfDayTimeStamp = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            return Pair(startOfDayTimeStamp, endOfDayTimeStamp)
        }
    }
}