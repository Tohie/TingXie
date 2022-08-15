package com.example.tingxie.presentation.quiz_statistics

import android.util.Log
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.model.BarChartData
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.QuizResult
import com.example.tingxie.domain.use_case.CharacterUseCases
import com.example.tingxie.presentation.character_quiz.CharacterQuizState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*
import javax.inject.Inject

@HiltViewModel
class QuizStatisticsViewModel @Inject constructor(
    private val characterUseCases: CharacterUseCases
) : ViewModel() {
    private var _state = mutableStateOf<QuizStatisticsState>(QuizStatisticsState())
    val state: State<QuizStatisticsState> = _state

    init {
        getCharacterQuizResults()
        getTestScores()
    }

    fun onEvent(event: QuizStatisticsEvent) {
        when (event) {
            is QuizStatisticsEvent.DateChanged -> {
                val calendar = Calendar.getInstance()
                calendar.set(event.year, event.month, event.dayOfMonth)
                val time = calendar.time
                val localDate = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                val startOfDay = localDate.atStartOfDay().toInstant(ZoneOffset.MIN).toEpochMilli()
                val endOfDay = localDate.atTime(LocalTime.MAX).toInstant(ZoneOffset.MAX).toEpochMilli()

                getTestScoresBetween(startOfDay, endOfDay)
            }

            is QuizStatisticsEvent.ChangeNumberOfTestsDisplayed -> {
                getTestScoresLimitedBy(event.amount)
            }
        }
    }

    private fun getCharacterQuizResults() {
        characterUseCases.getQuizResults().onEach { quizResults ->
            val characterMap: MutableMap<Character, CharacterQuizStatistic> = mutableMapOf()
            for ((quizResult, character) in quizResults) {
                if (characterMap.containsKey(character) && quizResult.isCorrect) {
                    characterMap[character] = CharacterQuizStatistic(
                        correctAnswers = characterMap[character]!!.correctAnswers + 1,
                        incorrectAnswers = characterMap[character]!!.incorrectAnswers,
                        character = character
                    )
                } else if (!characterMap.containsKey(character)){
                    characterMap[character] = CharacterQuizStatistic(
                        character = character,
                        correctAnswers = if (quizResult.isCorrect) 1 else 0,
                        incorrectAnswers = 0
                    )
                }
            }
            _state.value = _state.value.copy(quizResults = characterMap.values.toList())
        }.launchIn(viewModelScope)
    }

    private fun getTestScores() {
        characterUseCases.getQuizResults().onEach { quizResults ->
            updateTestScoreData(convertQuizResultsToTestScoreData(quizResults))
        }.launchIn(viewModelScope)
    }

    private fun getTestScoresLimitedBy(limit: Int) {
        characterUseCases.getQuizResults().onEach { quizResults ->
            updateTestScoreData(convertQuizResultsToTestScoreData(quizResults).take(limit))
        }.launchIn(viewModelScope)
    }

    private fun getTestScoresBetween(start: Long, end: Long) {
        characterUseCases.getQuizResultsBetween(start, end).onEach { quizResults ->
            updateTestScoreData(convertQuizResultsToTestScoreData(quizResults))
        }.launchIn(viewModelScope)
    }

    private fun updateTestScoreData(newBarChartData: List<BarChartData>) {
        _state.value = _state.value.copy(
            quizResults = _state.value.quizResults,
            testScoreBarChartData = newBarChartData
        )
    }

    private fun convertQuizResultsToTestScoreData(quizResults: Map<QuizResult, Character>): List<BarChartData> {
        val testScores: MutableMap<Long, TestScore> = mutableMapOf()
        for ((quizResult, _) in quizResults) {
            if (testScores.containsKey(quizResult.timestamp)) {
                testScores[quizResult.timestamp]!!.addToTotalScore(1)
                if (quizResult.isCorrect) {
                    testScores[quizResult.timestamp]!!.addToCurrentScore(1)
                }
            } else {
                testScores[quizResult.timestamp] = TestScore(
                    currentScore = if (quizResult.isCorrect) 1 else 0,
                    totalScore = 1
                )
            }
        }

        val testScoreBarChartData = mutableListOf<BarChartData>()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy hh:ss")
            .withZone(ZoneId.systemDefault())

        for ((timestamp, testScore) in testScores) {
            val date = formatter.format(Instant.ofEpochMilli(timestamp))

            val newBarChartData = BarChartData(
                label = date!!,
                value = (testScore.currentScore.toFloat() / testScore.totalScore.toFloat())*100,
                color = Color(255, 210, 117)
            )
            testScoreBarChartData.add(newBarChartData)
        }
        return testScoreBarChartData
    }

    class TestScore(
        var currentScore: Int = 0,
        var totalScore: Int = 0
    ) {
        fun addToCurrentScore(i: Int) {
            currentScore += i
        }

        fun addToTotalScore(i: Int) {
            totalScore += i
        }
    }
}