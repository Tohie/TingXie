package com.example.tingxie.presentation.characters

import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.OrderBy
import com.example.tingxie.domain.model.Ordering

data class CharactersState(
    val characters: List<Character> = emptyList(),
    val isOrderingOptionsVisible: Boolean = false,
    val ordering: OrderBy = OrderBy.DateAdded(Ordering.Acsending),
    val isBottomSheetVisible: Boolean = false,
    val numberOfCharactersToTest: Int = 10
)

