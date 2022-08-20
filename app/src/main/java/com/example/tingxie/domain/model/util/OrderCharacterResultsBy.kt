package com.example.tingxie.domain.model.util

sealed class OrderCharacterResultsBy(val ordering: Ordering) {
    fun isAscending() : Boolean {
        return ordering == Ordering.Acsending
    }

    class CharacterNumber(ordering: Ordering) : OrderCharacterResultsBy(ordering)
    class Character(ordering: Ordering) : OrderCharacterResultsBy(ordering)
    class Best(ordering: Ordering) : OrderCharacterResultsBy(ordering)
    class Worst(ordering: Ordering) : OrderCharacterResultsBy(ordering)
}