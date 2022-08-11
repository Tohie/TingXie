package com.example.tingxie.presentation.character_quiz

sealed class CharacterQuizEvents {
    data class NextCharacter(val wasCorrect: Boolean): CharacterQuizEvents()
    data class ChangeCharacterVisibility(val isCharacterVisible: Boolean = false) : CharacterQuizEvents()
    object FinishedQuiz : CharacterQuizEvents()
}
