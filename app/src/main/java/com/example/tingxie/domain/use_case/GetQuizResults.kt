package com.example.tingxie.domain.use_case

import com.example.tingxie.domain.model.*
import com.example.tingxie.domain.model.util.OrderCharacterResultsBy
import com.example.tingxie.domain.model.util.Ordering
import com.example.tingxie.domain.repository.CharacterRepository
import com.example.tingxie.domain.use_case.utils.toCharacterStatistics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

class GetQuizResults(
    private val repository: CharacterRepository
) {
    operator fun invoke(): Flow<Map<Character, List<CharacterResult?>>> {
        return repository.getCharacterResults()
    }

    fun getCharactersQuizResults(): Flow<List<CharacterStatistics>> {
       return repository.getCharacterResults().map { it.toCharacterStatistics() }
    }

    fun getCharactersQuizResultsLike(searchWord: String): Flow<List<CharacterStatistics>> {
        return  repository.getCharacterResultsLike(searchWord).map { it.toCharacterStatistics() }
    }

    fun getCharacterQuizResultsByQuizId(quizId: Int): Flow<List<CharacterStatistics>> {
        return repository.getCharacterResultsByQuiz(quizId).map { it.toCharacterStatistics() }
    }

    fun getTestScoreData(): Flow<Map<Quiz, List<CharacterResult>>> {
        return repository.getQuizResults()
    }

    fun getQuizResult(quizId: Int): Flow<Map<Quiz, List<CharacterResult>>> {
        return repository.getQuizResult(quizId)
    }

    fun getQuizResultsOn(year: Int, month: Int, dayOfMonth: Int): Flow<Map<Quiz, List<CharacterResult>>> {
        val (start, end) = timestampToStartAndEndOfDay(year, month, dayOfMonth)

        return repository.getQuizResultBetween(start, end)
    }

    fun getQuizResultsLimitedBy(limit: Int): Flow<Map<Quiz, List<CharacterResult>>> {
        return repository.getQuizResultsLimitedBy(limit)
    }

    fun getLatestQuiz(): Flow<Map<Quiz, List<CharacterResult>>> {
        return repository.getLatestQuiz()
    }

    fun getCharacterResults(character: String): Flow<Map<Character, List<CharacterResult>>> {
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

        fun sortCharacterStatistics(characterStatistics: List<CharacterStatistics>, orderingBy: OrderCharacterResultsBy): List<CharacterStatistics> {
            return when (orderingBy) {
                is OrderCharacterResultsBy.Best -> {
                    when (orderingBy.ordering) {
                        Ordering.Acsending -> { characterStatistics.sortedBy { it.correctAnswers }}
                        Ordering.Descending -> { characterStatistics.sortedByDescending { it.correctAnswers } }
                    }
                }
                is OrderCharacterResultsBy.Character -> {
                    when (orderingBy.ordering) {
                        Ordering.Acsending -> { characterStatistics.sortedBy { it.character.pinyin }}
                        Ordering.Descending -> { characterStatistics.sortedByDescending { it.character.pinyin } }
                    }
                }
                is OrderCharacterResultsBy.CharacterNumber -> {
                    when (orderingBy.ordering) {
                        Ordering.Acsending -> { characterStatistics.sortedBy { it.character.characterNumber }}
                        Ordering.Descending -> { characterStatistics.sortedByDescending { it.character.characterNumber } }
                    }
                }
                is OrderCharacterResultsBy.Worst -> {
                    when (orderingBy.ordering) {
                        Ordering.Acsending -> { characterStatistics.sortedBy { it.incorrectAnswers }}
                        Ordering.Descending -> { characterStatistics.sortedByDescending { it.incorrectAnswers } }
                    }
                }
            }
        }
    }
}