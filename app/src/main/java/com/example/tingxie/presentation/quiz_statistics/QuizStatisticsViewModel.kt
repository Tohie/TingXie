package com.example.tingxie.presentation.quiz_statistics

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.model.*
import com.example.tingxie.domain.use_case.CharacterUseCases
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
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

    fun getBarChartData(): List<BarEntry> {
        return _state.value.quizzes.toList().mapIndexed() { index, (quiz, _) ->
            BarEntry(
                index.toFloat(),
                quiz.score.toFloat()
            )
        }
    }

    fun getBarChartLabels(): List<String> {
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
            .withLocale(Locale.getDefault())
        return _state.value.quizzes.map{ (quiz, _) ->
            Instant.ofEpochMilli(quiz.timestamp)
                .atZone(ZoneId.systemDefault())
                .format(formatter)
        }
    }

    fun getCorrectAnswers(character: Character): Int? {
        return _state.value.characterResults.find { it.character == character }?.correctAnswers
    }

    fun getIncorrectAnswers(character: Character): Int? {
        return _state.value.characterResults.find { it.character == character }?.incorrectAnswers
    }

    private fun getCharacterQuizResults() {
        characterUseCases.getQuizResults.getCharactersQuizResults().onEach { characterResults ->
            _state.value = _state.value.copy(characterResults = characterResults)
        }.launchIn(viewModelScope)
    }

    private fun getTestScores() {
        characterUseCases.getQuizResults.getTestScoreData().onEach { quizzes ->
            updateQuizzes(quizzes)
        }.launchIn(viewModelScope)
    }

    private fun getTestScoresLimitedBy(limit: Int) {
        characterUseCases.getQuizResults.getTestScoreData().onEach { quizzes ->
            updateQuizzes(quizzes)
        }.launchIn(viewModelScope)
    }

    private fun getTestScoresBetween(year: Int, month: Int, dayofMonth: Int) {
        characterUseCases.getQuizResults.getQuizResultsOn(year, month, dayofMonth).onEach { quizzes ->
            updateQuizzes(quizzes)
        }.launchIn(viewModelScope)
    }

    private fun updateQuizzes(newQuizzes: Map<Quiz, List<CharacterResult>>) {
        _state.value = _state.value.copy(
            quizzes = newQuizzes,
        )
    }
}