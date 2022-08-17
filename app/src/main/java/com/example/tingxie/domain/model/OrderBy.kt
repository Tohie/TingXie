package com.example.tingxie.domain.model

sealed class OrderBy(val ordering: Ordering) {
    fun isAscending() : Boolean {
        return ordering == Ordering.Acsending
    }

    class DateAdded(ordering: Ordering) : OrderBy(ordering)
    class CharacterNumber(ordering: Ordering) : OrderBy(ordering)
    class Character(ordering: Ordering) : OrderBy(ordering)
}
