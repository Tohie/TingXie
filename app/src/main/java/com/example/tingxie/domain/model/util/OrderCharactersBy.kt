package com.example.tingxie.domain.model.util

sealed class OrderCharactersBy(val ordering: Ordering) {
    fun isAscending() : Boolean {
        return ordering == Ordering.Acsending
    }

    class DateAdded(ordering: Ordering) : OrderCharactersBy(ordering)
    class CharacterNumber(ordering: Ordering) : OrderCharactersBy(ordering)
    class Character(ordering: Ordering) : OrderCharactersBy(ordering)
}
