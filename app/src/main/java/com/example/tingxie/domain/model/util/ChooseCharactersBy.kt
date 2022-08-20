package com.example.tingxie.domain.model.util

sealed class ChooseCharactersBy(val amount: Int) {
    class LeastCorrect(amount: Int) : ChooseCharactersBy(amount)
    class MostIncorrect(amount: Int) : ChooseCharactersBy(amount)
    class Random(amount: Int) : ChooseCharactersBy(amount)
    class LeastTested(amount: Int) : ChooseCharactersBy(amount)
}