package com.example.tingxie.presentation.quiz_statistics

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.use_case.CharacterUseCases
import com.example.tingxie.presentation.character_quiz.CharacterQuizState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class QuizStatisticsViewModel @Inject constructor(
    private val characterUseCases: CharacterUseCases
) : ViewModel() {
    private var _state = mutableStateOf<QuizStatisticsState>(QuizStatisticsState())
    val state: State<QuizStatisticsState> = _state

    init {
        characterUseCases.getQuizResults().onEach { quizResults ->
            Log.i("characters", "Got quiz results")
            _state.value = _state.value.copy(quizResults = quizResults)
            sortResultsByCharacters()
        }.launchIn(viewModelScope)
    }

    private fun sortResultsByCharacters() {
        val sortedByCharacter = _state.value.quizResults.toList().sortedBy { (_, value) ->
            value.id
        }.toMap()
        Log.i("characters", sortedByCharacter.toString())
        _state.value = _state.value.copy(quizResults = sortedByCharacter)
    }
}