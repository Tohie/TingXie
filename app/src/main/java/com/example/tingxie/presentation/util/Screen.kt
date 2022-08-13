package com.example.tingxie.presentation.util

sealed class Screen(val route: String) {
    object CharactersScreen: Screen("characters_screen")
    object EditCharacterScreen: Screen("edit_characters_screen")
    object CharacterQuizScreen: Screen("characters_quiz_screen")
    object QuizStatisticsScreen: Screen("quiz_statistics_screen")
}
