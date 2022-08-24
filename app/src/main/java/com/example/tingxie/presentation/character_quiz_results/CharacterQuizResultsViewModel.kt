package com.example.tingxie.presentation.character_quiz_results

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.model.CharacterResult
import com.example.tingxie.domain.model.Quiz
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
        savedStateHandle.get<Int>("quizId")?.let { quizId ->
            val results = if (quizId != -1) {
                characterUseCases.getQuizResults.getQuizResult(quizId)
            } else {
                characterUseCases.getQuizResults.getLatestQuiz()
            }
            updateScores(results)
        }
    }

    private fun updateScores(quizzes: Flow<Map<Quiz, List<CharacterResult>>>) {
        quizzes.onEach { quizResults ->
            val quiz = quizResults.toList().first()
            characterUseCases.getQuizResults
                .getCharacterQuizResultsByQuizId(quiz.first.quizId!!).onEach { stats ->
                    _state.value = _state.value.copy(
                        characterStatistics = stats,
                        quizResults = quiz.first
                    )
                }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
    }
}