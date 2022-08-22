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
    onClick: (Categories) -> Unit,
    includeNoneOption: Boolean = false,
    onNoneClicked: () -> Unit = {},
    content: @Composable () -> Unit = {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add category")
    }
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        content()
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = !expanded }) {
            if (includeNoneOption) {
                DropdownMenuItem(onClick = { onNoneClicked() }) {
                    Text(text = "None")
                }
                Divider()
            }
            categories.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        onClick(category)
                    }
                ) {
                    Text(text = category.categoryName)
                }
                Divider()

            }
        }
    }
}