package com.example.tingxie.presentation.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tingxie.domain.model.Categories
import com.example.tingxie.presentation.edit_categories.EditCategoriesEvents
import com.example.tingxie.presentation.edit_categories.EditCategoriesViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun CategoryDropDown(
    categories: List<Categories>,
    onClick: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add category")
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = !expanded }) {
            categories.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        onClick()
                    }
                ) {
                    Text(text = category.categoryName)
                }
            }
        }
    }
}