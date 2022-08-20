package com.example.tingxie.domain.model.util

sealed class OrderCharacterBy(val ordering: Ordering) {
    fun isAscending() : Boolean {
        return ordering == Ordering.Acsending
    }

    class DateAdded(ordering: Ordering) : OrderCharacterBy(ordering)
    class CharacterNumber(ordering: Ordering) : OrderCharacterBy(ordering)
    class Character(ordering: Ordering) : OrderCharacterBy(ordering)
}
