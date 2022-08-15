package com.example.tingxie.presentation.quiz_statistics

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.model.*
import com.example.tingxie.domain.use_case.CharacterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.*
import java.time.format.DateTimeFormatter
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
                getTestScoresBetween(event.year, event.month, event.dayOfMonth)
            }
            is QuizStatisticsEvent.ChangeNumberOfTestsDisplayed -> {
                getTestScoresLimitedBy(event.amount)
            }
        }
    }

    private fun getCharacterQuizResults() {
        characterUseCases.getQuizResults.getCharacterQuizResults().onEach { quizResults ->
            _state.value = _state.value.copy(quizResults = quizResults)
        }.launchIn(viewModelScope)
    }

    private fun getTestScores() {
        characterUseCases.getQuizResults.getTestScoreData().onEach { testScores ->
            updateTestScoreData(testScores)
        }.launchIn(viewModelScope)
    }

    private fun getTestScoresLimitedBy(limit: Int) {
        characterUseCases.getQuizResults.getTestScoreData().onEach { testScores ->
            updateTestScoreData(testScores.takeLast(limit))
        }.launchIn(viewModelScope)
    }

    private fun getTestScoresBetween(year: Int, month: Int, dayofMonth: Int) {
        characterUseCases.getQuizResultsBetween(year, month, dayofMonth).onEach { testScores ->
            updateTestScoreData(testScores)
        }.launchIn(viewModelScope)
    }

    private fun updateTestScoreData(newBarChartData: List<BarChartData>) {
        _state.value = _state.value.copy(
            quizResults = _state.value.quizResults,
            testScoreBarChartData = newBarChartData
        )
    }
}