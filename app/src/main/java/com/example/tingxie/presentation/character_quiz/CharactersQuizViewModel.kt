package com.example.tingxie.presentation.character_quiz

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.use_case.CharacterUseCases
import com.example.tingxie.presentation.character_quiz.components.CharacterState
import com.example.tingxie.presentation.characters.CharactersState
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
            is CharacterQuizEvents.ChangeCharacterCorrect -> {
                val currentCharacter = _state.value.characters.get(_state.value.currentCharacter)
                val newCurrentCharacter = currentCharacter.copy(
                     character = currentCharacter.character,
                     isVisibile = currentCharacter.isVisibile,
                     isCorrect = event.isCharacterCorrect
                )
                _state.value.copy(
                    characters = _state.value.characters.map { character ->
                        if (character.character == newCurrentCharacter.character) newCurrentCharacter else { character }
                    },
                    currentCharacter = _state.value.currentCharacter
                )
                Log.i("Characters", "Updated the correctness")
            }
            is CharacterQuizEvents.ChangeCharacterVisibility -> {
                val currentCharacter = _state.value.characters.get(_state.value.currentCharacter)
                val newCurrentCharacter = currentCharacter.copy(
                    character = currentCharacter.character,
                    isVisibile = event.isCharacterVisible,
                    isCorrect = currentCharacter.isCorrect
                )
                _state.value.copy(
                    characters = _state.value.characters.map { character ->
                        if (character.character == newCurrentCharacter.character) newCurrentCharacter else { character }
                    },
                    currentCharacter = _state.value.currentCharacter
                )
                Log.i("Characters", "Updated the currentCharacter")
            }
            CharacterQuizEvents.FinishedQuiz -> {
                // Emit finished
            }
            is CharacterQuizEvents.PageChange -> {
                _state.value = _state.value.copy(currentCharacter = event.number)
            }
        }
    }

    fun getCurrentCharacter(number: Int): Character {
        return _state.value.characters.get(number).character
    }

    private fun getNRandomCharacters(number: Int) {
        characterUseCases.getNRandomCharacters(number).onEach { characters ->
            Log.i("Characters", "Got ${characters.first().character}")
            _state.value = _state.value.copy(
                characters = characters.map { character ->
                    CharacterState(character = character, isCorrect = false, isVisibile = false)
                }.toMutableList(),
                currentCharacter = 0
            )
            totalCharacters = characters.size
            // onEvent(CharacterQuizEvents.NextCharacter(wasCorrect = false))
        }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class QuizFinished(val finalMessage: String): UiEvent()
    }
}