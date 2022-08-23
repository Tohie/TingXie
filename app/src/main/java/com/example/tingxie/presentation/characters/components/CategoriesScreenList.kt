package com.example.tingxie.presentation.characters.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tingxie.domain.model.Categories
import com.example.tingxie.presentation.characters.CharactersEvent
import com.example.tingxie.presentation.characters.CharactersViewModel
import com.example.tingxie.presentation.util.CategoryDropDown
import com.example.tingxie.presentation.util.CharacterDetail

@Composable
fun CategoriesScreenList(
    viewModel: CharactersViewModel,
    modifier: Modifier = Modifier
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