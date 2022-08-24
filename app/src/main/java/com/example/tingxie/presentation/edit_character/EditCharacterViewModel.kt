package com.example.tingxie.presentation.edit_character

import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tingxie.domain.model.*
import com.example.tingxie.domain.use_case.CharacterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class EditCharacterViewModel @Inject constructor (
    private val characterUseCases: CharacterUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _characterNumber = mutableStateOf(EditCharacterTextField(
        hint = "Enter character number from the book"
    ))
    val characterNumber: State<EditCharacterTextField> = _characterNumber

    private val _character = mutableStateOf(EditCharacterTextField(
        hint = "Enter 汉字"
    ))
    val character: State<EditCharacterTextField> = _character

    private val _pinyin = mutableStateOf(EditCharacterTextField(
        hint = "Enter pinyin"
    ))
    val pinyin: State<EditCharacterTextField> = _pinyin

    private val _description = mutableStateOf(EditCharacterTextField(
        hint = "Enter description of the character without using the 汉字. I.e. 我的peng友很好 for peng."
    ))
    val description: State<EditCharacterTextField> = _description

    private val _categoryName = mutableStateOf(EditCharacterTextField(
        hint = "Enter the name of the category you would like to create"
    ))
    val categoryName: State<EditCharacterTextField> = _categoryName

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentCharId: Int? = null

    init {
        savedStateHandle.get<Int>("characterId")?.let { characterId ->
            if(characterId != -1) {
                viewModelScope.launch {
                    getCharacter(characterId)
                }
            }
        }
    }

    private fun getCharacter(characterId: Int) {
        viewModelScope.launch {
            val newCharacter = characterUseCases.getCharacters.getCharacter(characterId)?: return@launch
            currentCharId = characterId

            _characterNumber.value = characterNumber.value.copy(
                text = newCharacter.characterNumber.toString(),
            )
            _character.value = character.value.copy(
                text = newCharacter.character,
            )
            _pinyin.value = pinyin.value.copy(
                text = newCharacter.pinyin,
            )
            _description.value = description.value.copy(
                text = newCharacter.description,
            )
        }

    }


    fun onEvent(event: EditCharacterEvent) {
        when (event) {
            is EditCharacterEvent.EnteredCharacterNumber -> {
                _characterNumber.value = characterNumber.value.copy(
                    event.value
                )
            }

            is EditCharacterEvent.EnteredCharacter -> {
                _character.value = character.value.copy(
                    event.value
                )
            }

            is EditCharacterEvent.EnteredPinyin -> {
                _pinyin.value = pinyin.value.copy(
                    event.value
                )
            }

            is EditCharacterEvent.EnteredDescription -> {
                _description.value = description.value.copy(
                    event.value
                )
            }

            is EditCharacterEvent.EnteredCategoryName -> {
                _categoryName.value = categoryName.value.copy(
                    event.value
                )
            }

            is EditCharacterEvent.SaveCategory -> {
                saveCategory()
            }

            is EditCharacterEvent.SaveCharacter -> {
                saveCharacter()
            }
        }
    }

    private fun saveCharacter() {
        viewModelScope.launch {
            try {
                characterUseCases.addCharacter(
                    Character(
                        characterNumber = characterNumber.value.text.toInt(),
                        character = character.value.text,
                        pinyin = pinyin.value.text,
                        description = description.value.text,
                        id = currentCharId
                    )
                )
                _eventFlow.emit(UiEvent.SaveCharacter)
            } catch (e: InvalidCharacterException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        message = e.message ?: "Couldn't save note"
                    )
                )
            } catch (e: NumberFormatException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        message = "Please enter a valid number"
                    )
                )
            }
        }
    }

    private fun saveCategory() {
        viewModelScope.launch {
            try {
                characterUseCases.categoryUseCases.insertCategory(
                    Categories(
                        categoryId = null,
                        categoryName = categoryName.value.text
                    )
                )
                _eventFlow.emit(UiEvent.SaveCharacter)
            } catch (e: InvalidCategoryException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        message = e.message ?: "Couldn't save category"
                    )
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveCharacter: UiEvent()
    }
}