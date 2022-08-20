package com.example.tingxie.presentation.character_quiz

import com.example.tingxie.domain.model.util.ChooseCharactersBy

sealed class CharacterQuizEvents {
    data class ChangeCharacterNumber(val number: Int): CharacterQuizEvents()
    data class PageChange(val number: Int): CharacterQuizEvents()
    data class ChangeCharacterVisibility(val index: Int, val isCharacterVisible: Boolean = false) : CharacterQuizEvents()
    data class ChangeCharacterCorrect(val index: Int, val isCharacterCorrect: Boolean = false) : CharacterQuizEvents()
    data class ChangeQuitWithoutSavingDialogueVisibility(val isVisible: Boolean = false) : CharacterQuizEvents ()
    data class StartQuiz(val chooseCharactersBy: ChooseCharactersBy): CharacterQuizEvents()

    object QuitWithoutSaving : CharacterQuizEvents()
    object SaveQuizResults : CharacterQuizEvents()

}
