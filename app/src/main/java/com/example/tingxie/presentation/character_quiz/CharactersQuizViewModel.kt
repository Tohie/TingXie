package com.example.tingxie.presentation.character_quiz

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.use_case.CharacterUseCases
import com.example.tingxie.presentation.edit_character.EditCharacterViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersQuizViewModel @Inject constructor(
    private val characterUseCases: CharacterUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var _state = mutableStateOf<CharacterQuizState>(CharacterQuizState())
    val state: State<CharacterQuizState> = _state

    private val _eventFlow = MutableSharedFlow<CharactersQuizViewModel.UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var totalCharacters: Int? = null

    init {
        savedStateHandle.get<Int>("characterNumber")?.let { characterNumber ->
            getNRandomCharacters(characterNumber)
            Log.i("Characters", "Got random characters")
            Log.i("Characters", "Current character is ${state.value.currentCharacter}")
        }
    }

    fun onEvent(event: CharacterQuizEvents) {
        when (event) {
            is CharacterQuizEvents.NextCharacter -> {
                // Add 1 to the correct answers and hide the character
                _state.value = state.value.copy(
                    correctAnswers = if (event.wasCorrect) {
                        _state.value.correctAnswers + 1
                    } else {
                        _state.value.correctAnswers
                    },
                    currentCharacter = _state.value.currentCharacter,
                    characters = _state.value.characters,
                    isCharacterVisible = false
                )

                if (_state.value.characters.isNotEmpty()) {
                    _state.value = state.value.copy(
                        correctAnswers = _state.value.correctAnswers,
                        currentCharacter = _state.value.characters.removeFirst(),
                        characters = _state.value.characters
                    )
                    Log.i("Characters", "Updated the state")
                } else {
                    onEvent(CharacterQuizEvents.FinishedQuiz)
                }
            }
            CharacterQuizEvents.FinishedQuiz -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.QuizFinished(
                            "Congrats you got ${_state.value.correctAnswers} out of ${totalCharacters}"
                        )
                    )
                }
            }
            is CharacterQuizEvents.ChangeCharacterVisibility -> {
                _state.value = _state.value.copy(
                    correctAnswers = _state.value.correctAnswers,
                    currentCharacter = _state.value.currentCharacter,
                    characters = _state.value.characters,
                    isCharacterVisible = event.isCharacterVisible
                )
            }
        }
    }
    private fun getNRandomCharacters(number: Int) {
        characterUseCases.getNRandomCharacters(number).onEach { characters ->
            Log.i("Characters", "Got ${characters.first().character}")
            _state.value = _state.value.copy(
                correctAnswers = _state.value.correctAnswers,
                characters = characters.toMutableList()
            )
            totalCharacters = characters.size
            onEvent(CharacterQuizEvents.NextCharacter(wasCorrect = false))
        }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class QuizFinished(val finalMessage: String): UiEvent()
    }
}