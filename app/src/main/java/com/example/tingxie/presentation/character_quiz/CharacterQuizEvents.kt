package com.example.tingxie.presentation.character_quiz

sealed class CharacterQuizEvents {
    data class PageChange(val number: Int): CharacterQuizEvents()
    data class ChangeCharacterVisibility(val index: Int, val isCharacterVisible: Boolean = false) : CharacterQuizEvents()
    data class ChangeCharacterCorrect(val index: Int, val isCharacterCorrect: Boolean = false) : CharacterQuizEvents()

    object FinishedQuiz : CharacterQuizEvents()
    object SaveQuizResults : CharacterQuizEvents()
}
