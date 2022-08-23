package com.example.tingxie.presentation.character_quiz

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.model.Quiz
import com.example.tingxie.domain.model.QuizResult
import com.example.tingxie.domain.model.util.ChooseCharactersBy
import com.example.tingxie.domain.use_case.CharacterUseCases
import com.example.tingxie.presentation.character_quiz.components.CharacterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class CharactersQuizViewModel @Inject constructor(
    private val characterUseCases: CharacterUseCases,
) : ViewModel() {
    private var _state = mutableStateOf<CharacterQuizState>(CharacterQuizState())
    val state: State<CharacterQuizState> = _state

    private val _eventFlow = MutableSharedFlow<CharactersQuizViewModel.UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var totalCharacters: Int? = null

    init {
        characterUseCases.categoryUseCases.getCategories().onEach { categories ->
            _state.value = _state.value.copy(
                categories = categories.map { it.category }
            )
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: CharacterQuizEvents) {
        when (event) {
            is CharacterQuizEvents.StartQuiz -> {
                getCharactersBy(event.chooseCharactersBy)
            }

            is CharacterQuizEvents.ChangeCharacterNumber -> {
                _state.value = _state.value.copy(
                    numberOfCharacters = event.number
                )
            }

            is CharacterQuizEvents.ChangeCharacterCorrect -> {
                val currentCharacter = getCurrentCharacter()
                val newCurrentCharacter = currentCharacter.copy(
                     character = currentCharacter.character,
                     isVisible = currentCharacter.isVisible,
                     isCorrect = event.isCharacterCorrect
                )

                changeCharacter(currentCharacter, newCurrentCharacter)
            }

            is CharacterQuizEvents.ChangeCharacterVisibility -> {
                val currentCharacter = getCurrentCharacter()
                val newCurrentCharacter = currentCharacter.copy(
                    character = currentCharacter.character,
                    isVisible = event.isCharacterVisible,
                    isCorrect = currentCharacter.isCorrect
                )
                changeCharacter(currentCharacter, newCurrentCharacter)
            }

            CharacterQuizEvents.QuitWithoutSaving -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.QuitQuiz
                    )
                    // So when user comes back, the quiz is gone
                    resetState()
                }
            }

            is CharacterQuizEvents.PageChange -> {
                _state.value = _state.value.copy(currentCharacter = event.number)
            }

            CharacterQuizEvents.SaveQuizResults -> {
                saveQuizResults()
            }
            is CharacterQuizEvents.ChangeQuitWithoutSavingDialogueVisibility -> {
                _state.value = _state.value.copy(
                    isQuitWithoutSavingDialogueVisible = event.isVisible
                )
            }
            is CharacterQuizEvents.ChangeCategory -> {
                _state.value = _state.value.copy(
                    currentCategory = event.newCategory
                )
            }
        }
    }

    private fun saveQuizResults() {
        val timestamp = Instant.now().toEpochMilli()
        viewModelScope.launch {
            val newQuizId = makeNewQuiz(timestamp)

            val results = getResults(timestamp, newQuizId)

            characterUseCases.insertQuizResult(results)

            // Go to results page
            saveAndFinish()

        }
    }

    private suspend fun makeNewQuiz(
        timestamp: Long
    ) = characterUseCases.addQuiz(
        Quiz(
            quizId = null,
            timestamp = timestamp,
            numberOfCharacters = _state.value.characters.size,
            score = finalScore(),
        )
    )

    private fun getResults(
        timestamp: Long,
        newQuizId: Long
    ) = state.value.characters.map { characterState ->
        QuizResult(
            resultId = null,
            characterIdMap = characterState.character.id!!, // if the character id is null, we're in trouble
            isCorrect = characterState.isCorrect,
            timestamp = timestamp,
            quizResultsIdMap = newQuizId.toInt()
        )
    }

    private suspend fun saveAndFinish() {
        // Reset all state in case user comes back
        resetState()

        _eventFlow.emit(
            UiEvent.SaveAndFinishQuiz
        )
    }

    private fun resetState() {
        _state.value = _state.value.copy(
            characters = emptyList(),
            numberOfCharacters = 0,
            currentCharacter = 0,
        )
    }

    fun getCurrentCharacter(): CharacterState {
        return _state.value.characters.get(_state.value.currentCharacter)
    }

    private fun getCharactersBy(chooseCharactersBy: ChooseCharactersBy) {
        viewModelScope.launch {
            val characters = characterUseCases.getCharacters.getCharactersBy(chooseCharactersBy, _state.value.currentCategory)
            _state.value = _state.value.copy(
                characters = characters.map { character ->
                    CharacterState(character = character, isCorrect = false, isVisible = false)
                }.toMutableList(),
                currentCharacter = 0
            )
            totalCharacters = characters.size
        }
    }

    private fun finalScore(): Int {
        return _state.value.characters.foldRight(0) { characterState, acc ->
            if (characterState.isCorrect) { acc + 1} else { acc }
        }
    }

    private fun changeCharacter(oldCharacterState: CharacterState, newCharacterState: CharacterState) {
        _state.value = _state.value.copy(
            characters = _state.value.characters.map { character ->
                if (character.character == oldCharacterState.character) { newCharacterState } else { character }
            },
            currentCharacter = _state.value.currentCharacter
        )
    }
    sealed class UiEvent {
        object SaveAndFinishQuiz : UiEvent()
        object QuitQuiz : UiEvent()
    }
}