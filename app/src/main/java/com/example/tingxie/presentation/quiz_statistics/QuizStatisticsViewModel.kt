package com.example.tingxie.presentation.quiz_statistics

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
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
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        updateDates(year, month, day)
    }

    fun onEvent(event: QuizStatisticsEvent) {
        when (event) {
            is QuizStatisticsEvent.DateChanged -> {
                getTestScoresBetween(event.year, event.month, event.dayOfMonth)
                updateDates(event.year, event.month, event.dayOfMonth)
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
                (quiz.score.toFloat() / quiz.numberOfCharacters) * 100 // Give score as percentage
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
            updateQuizzes(quizzes.toList().take(limit).toMap())
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

    private fun updateDates(year: Int, month: Int, day: Int) {
        _state.value = _state.value.copy(
            year = year,
            month = month,
            day = day
        )
    }
}