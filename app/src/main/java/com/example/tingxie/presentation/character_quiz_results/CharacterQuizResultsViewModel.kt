package com.example.tingxie.presentation.character_quiz_results

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.model.QuizResults
import com.example.tingxie.domain.use_case.CharacterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CharacterQuizResultsViewModel@Inject constructor(
    private val characterUseCases: CharacterUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = mutableStateOf(CharacterQuizResultsState())
    val state: State<CharacterQuizResultsState> = _state

    init {
        savedStateHandle.get<Long>("timestamp")?.let { timestamp ->
            val results = if (timestamp != (-1).toLong()) {
                characterUseCases.getQuizResults.getQuizResult(timestamp)
            } else {
                characterUseCases.getQuizResults.getLatestQuiz()
            }
            updateScores(results)
        }
    }

    private fun updateScores(results: Flow<List<QuizResults>>) {
        results.onEach { quizResults ->
            val userScore = quizResults.size
            val totalScore = quizResults.foldRight(0) { result, acc ->
                if (result.wasCorrect) acc + 1 else acc
            }
            _state.value = _state.value.copy(
                userScore = userScore,
                totalScore = totalScore,
                quizResults = quizResults
            )
        }.launchIn(viewModelScope)
    }
}