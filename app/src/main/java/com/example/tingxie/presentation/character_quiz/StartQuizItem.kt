package com.example.tingxie.presentation.character_quiz

import com.example.tingxie.domain.model.util.ChooseCharactersBy

data class StartQuizItem(
    val text: String,
    val chooseCharactersBy: ChooseCharactersBy,
)
