package com.example.tingxie.domain.model

sealed class OrderBy(val ordering: Ordering) {
    fun isAscending() : Boolean {
        return ordering == Ordering.Acsending
    }

    abstract fun isOrderingById(): Boolean
    abstract fun isOrderingByCharacter(): Boolean

    class Id(ordering: Ordering) : OrderBy(ordering) {
        override fun isOrderingById(): Boolean {
            return true
        }

        override fun isOrderingByCharacter(): Boolean {
            return false
        }
    }
    class Character(ordering: Ordering) : OrderBy(ordering) {
        override fun isOrderingById(): Boolean {
            return false
        }

        override fun isOrderingByCharacter(): Boolean {
            return true
        }
    }
}
