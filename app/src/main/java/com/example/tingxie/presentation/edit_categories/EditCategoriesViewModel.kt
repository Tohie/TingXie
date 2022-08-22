package com.example.tingxie.presentation.edit_categories

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tingxie.domain.model.Categories
import com.example.tingxie.domain.model.CharacterCategoryCrossRef
import com.example.tingxie.domain.use_case.CharacterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCategoriesViewModel @Inject constructor(
    val characterUseCases: CharacterUseCases,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val _state = mutableStateOf(EditCategoriesState())
    val state: State<EditCategoriesState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("characterId")?.let { characterId ->
            if(characterId != -1) {
                viewModelScope.launch {
                    getCharacter(characterId)
                }
                getAllCategories()
            }
        }?: run {
            viewModelScope.launch {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar("Could not load character, please return to home and try again")
                )
            }
        }
    }

    fun onEvent(event: EditCategoriesEvents) {
        when (event) {
            is EditCategoriesEvents.DeleteCharacterCategoryCrossRef -> {
                deleteCharacterCategoryCrossRef(event)
            }

            EditCategoriesEvents.UndoLastDeleted -> {
                _state.value.lastDeletedCategory?.let { crossRef ->
                    addCharacterCategoryCrossRef(crossRef)
                    getCharacter(_state.value.character!!.id!!)
                }
            }
            is EditCategoriesEvents.AddNewCharacterCategoryCrossRef -> {
                val characterCategoryCrossRef = CharacterCategoryCrossRef(
                    id = _state.value.character!!.id!!,
                    categoryId = event.category.categoryId!!
                )

                addCharacterCategoryCrossRef(characterCategoryCrossRef)
            }
        }
    }

    private fun deleteCharacterCategoryCrossRef(event: EditCategoriesEvents.DeleteCharacterCategoryCrossRef) {
        _state.value.character?.let { char ->
            val crossRef = CharacterCategoryCrossRef(
                id = char.id!!,
                categoryId = event.category.categoryId!!
            )

            setLastDeletedCrossRef(crossRef)
            viewModelScope.launch {
                characterUseCases.categoryUseCases.deleteCharacterFromCategory(crossRef)
            }
            setCurrentCategories(
                _state.value.currentCategories
                    .filterNot { it.categoryId == event.category.categoryId }
            )
        } ?: run {
            viewModelScope.launch {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        message = "You must save the character first before adding categories to it."
                    )
                )
            }
        }
    }

    private fun getCharacter(characterId: Int) {
        characterUseCases.getCharacters.getCharactersCategoriesWithId(characterId).onEach { charactersWithCategories ->
            val characterWithCategories = charactersWithCategories.first()
            _state.value = _state.value.copy(
                currentCategories = characterWithCategories.categories,
                character = characterWithCategories.character
            )
        }.launchIn(viewModelScope)
    }

    private fun getAllCategories() {
        characterUseCases.categoryUseCases.getCategories().onEach { categories ->
            _state.value = _state.value.copy(
                allCategories = categories.map { it.category }
            )
        }.launchIn(viewModelScope)
    }

    private fun addCharacterCategoryCrossRef(characterCategoryCrossRef: CharacterCategoryCrossRef) {
        viewModelScope.launch {
            characterUseCases.categoryUseCases.addCharacterToCategory(characterCategoryCrossRef)
        }
        viewModelScope.launch {
            characterUseCases.categoryUseCases.getCategory(characterCategoryCrossRef.categoryId)?.let { category ->
                val currentCategories = _state.value.currentCategories
                val withNewCategory = currentCategories.toMutableList()
                withNewCategory.add(category)

                setCurrentCategories(withNewCategory)
            }?: run {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar("Failed to add new category to this character")
                )
            }
        }
    }

    private fun setLastDeletedCrossRef(value: CharacterCategoryCrossRef?) {
        _state.value = _state.value.copy(
            lastDeletedCategory = value
        )
    }

    private fun setCurrentCategories(categories: List<Categories>) {
        _state.value = _state.value.copy(
            currentCategories = categories
        )
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}