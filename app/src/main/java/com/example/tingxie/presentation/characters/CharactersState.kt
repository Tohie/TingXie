package com.example.tingxie.presentation.characters

import com.example.tingxie.domain.model.CategoriesWithCharacters
import com.example.tingxie.domain.model.CharacterWithCategories
import com.example.tingxie.domain.model.util.OrderCharactersBy
import com.example.tingxie.domain.model.util.Ordering

data class CharactersState(
    val characters: List<CharacterWithCategories> = emptyList(),
    val categories: List<CategoriesWithCharacters> = emptyList(),
    val currentCategoryWithCharacters: CategoriesWithCharacters? = null,
    val isOrderingOptionsVisible: Boolean = false,
    val ordering: OrderCharactersBy = OrderCharactersBy.DateAdded(Ordering.Acsending),
    val isBottomSheetVisible: Boolean = false,
    val numberOfCharactersToTest: Int = 10
)

