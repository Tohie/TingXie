package com.example.tingxie.presentation.util

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Expand
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tingxie.presentation.characters.CharactersEvent
import com.example.tingxie.presentation.characters.CharactersViewModel

@Composable
fun SearchBar(
    onSearchQueryChanged: (String) -> Unit,
    onExpandSortingOptions: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { newSearch ->
                searchText = newSearch
                onSearchQueryChanged(searchText)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .weight(6f),
            textStyle = MaterialTheme.typography.h5,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = MaterialTheme.colors.secondary
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(
            onClick = {
                onExpandSortingOptions()
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
        ) {
            Icon(imageVector = Icons.Default.Expand, contentDescription = "Expand search options")
        }
    }
}