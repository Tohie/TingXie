package com.example.tingxie.presentation.characters

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.OrderBy
import com.example.tingxie.domain.model.Ordering
import com.example.tingxie.domain.use_case.CharacterUseCases
import com.example.tingxie.domain.use_case.GetCharacters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val characterUseCases: CharacterUseCases
) : ViewModel() {
    private val _state = mutableStateOf<CharactersState>(CharactersState())
    val state: State<CharactersState> = _state

    private var lastDeletedCharacter: Character? = null

    init {
        getCharacters()
    }

    fun onEvent(event: CharactersEvent) {
        when (event) {
            is CharactersEvent.Delete -> {
                viewModelScope.launch {
                    characterUseCases.deleteCharacter(event.character)
                    lastDeletedCharacter = event.character
                }
            }
            is CharactersEvent.RestoreCharacter -> {
                viewModelScope.launch {
                    characterUseCases.addCharacter(lastDeletedCharacter ?: return@launch)
                    lastDeletedCharacter = null
                }
            }
            is CharactersEvent.Search -> {
                characterUseCases.getCharacters.getCharactersLike(event.character).onEach { chars ->
                    Log.i("characters", "found some chars ")
                    setCharacters(chars)
                }.launchIn(viewModelScope)
            }
            is CharactersEvent.ChangeSortingOptionsVisibility -> {
                val currentSortingOptionsVisibility = _state.value.isOrderingOptionsVisible
                _state.value = _state.value.copy(
                    isOrderingOptionsVisible = !currentSortingOptionsVisibility
                )
            }
            is CharactersEvent.ChangeSorting -> {
                _state.value = _state.value.copy(
                    ordering = event.orderBy
                )
                val sorted = GetCharacters.sortCharacters(_state.value.characters, event.orderBy)
                setCharacters(sorted)
            }
            is CharactersEvent.ChangeAmountOfCharactersToTest -> {
                _state.value = _state.value.copy(
                    numberOfCharactersToTest = event.amount.toInt()
                )
            }
        }
    }

    private fun getCharacters() {
        characterUseCases.getCharacters.getCharacters()
            .onEach { setCharacters(it) }
            .launchIn(viewModelScope)
    }

    private fun setCharacters(characters: List<Character>) {
        _state.value = _state.value.copy(
            characters = characters
        )
    }
}