package com.example.tingxie.presentation.characters.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tingxie.presentation.characters.CharactersViewModel
import com.example.tingxie.presentation.util.CharacterDetail

@Composable
fun CategoriesScreenList(
    viewModel: CharactersViewModel,
) {
    if (viewModel.state.value.currentCategoryWithCharacters == null) return
    val category = viewModel.state.value.currentCategoryWithCharacters!!
    LazyColumn(
        modifier = Modifier.padding(6.dp)
    ) {
        items(
            items = category.characters
        ) { character ->
            CharacterDetail(
                character = character,
                Categories = { },
                showCategories = false,
                showCharacter = true,
                AdditionalContent = {}
            )
        }
    }
}