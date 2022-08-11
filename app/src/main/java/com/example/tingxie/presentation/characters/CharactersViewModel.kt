package com.example.tingxie.presentation.characters

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.use_case.CharacterUseCases
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
        }
    }

    private fun getCharacters() {
        characterUseCases.getCharacters()
            .onEach { characters ->
                _state.value = _state.value.copy(
                    characters = characters
                )
            }
            .launchIn(viewModelScope)
    }
}