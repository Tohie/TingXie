package com.example.tingxie.presentation.characters

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.model.Categories
import com.example.tingxie.domain.model.CategoriesWithCharacters
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.CharacterWithCategories
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
    private val _state = mutableStateOf(CharactersState())
    val state: State<CharactersState> = _state

    private var lastDeletedCharacter: Character? = null

    init {
        getCharacters()
        getCategories()
    }

    fun onEvent(event: CharactersEvent) {
        when (event) {
            is CharactersEvent.Delete -> {
                viewModelScope.launch {
                    characterUseCases.deleteCharacter(event.character.character)
                    lastDeletedCharacter = event.character.character
                }
            }
            is CharactersEvent.RestoreCharacter -> {
                viewModelScope.launch {
                    characterUseCases.addCharacter(lastDeletedCharacter ?: return@launch)
                    lastDeletedCharacter = null
                }
            }
            is CharactersEvent.Search -> {
                characterUseCases.getCharacters.getCharactersWithCategoriesLike(event.character).onEach { chars ->
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
                val sorted = GetCharacters.sortCharactersWithCategories(_state.value.characters, event.orderBy)
                setCharacters(sorted)
            }
            is CharactersEvent.ChangeAmountOfCharactersToTest -> {
                _state.value = _state.value.copy(
                    numberOfCharactersToTest = event.amount.toInt()
                )
            }
            is CharactersEvent.ChangeCategories -> {
                getCategory(event.category)
            }
        }
    }

    private fun getCategory(category: Categories?) {
        category ?: return
        viewModelScope.launch {
            val characters = characterUseCases.getCharacters.getCharactersFromCategoryName(category.categoryName)
            _state.value = _state.value.copy(
                currentCategoryWithCharacters = characters.firstOrNull(),
            )
        }
    }

    private fun getCharacters() {
        characterUseCases.getCharacters.getCharactersWithCategories()
            .onEach { setCharacters(it) }
            .launchIn(viewModelScope)
    }

    private fun getCategories() {
        characterUseCases.categoryUseCases.getCategories()
            .onEach { setCategories(it) }
            .launchIn(viewModelScope)
    }

    private fun setCharacters(characters: List<CharacterWithCategories>) {
        _state.value = _state.value.copy(
            characters = characters
        )
    }

    private fun setCategories(categories: List<CategoriesWithCharacters>) {
        _state.value = _state.value.copy(
            categories = categories
        )
    }
}