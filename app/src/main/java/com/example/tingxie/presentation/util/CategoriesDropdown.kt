package com.example.tingxie.presentation.util

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import com.example.tingxie.domain.model.Categories

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