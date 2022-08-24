package com.example.tingxie.presentation.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun SortingControls(
    orderingBys: List<SortingItem>,
    orderings: List<SortingItem>,
    radioButtonColors: RadioButtonColors = RadioButtonDefaults.colors(
        selectedColor = MaterialTheme.colors.secondary,
        unselectedColor = Color.White,
        disabledColor = Color.Gray
    )
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        SortingControlRow(sortingItems = orderings, radioButtonColors = radioButtonColors)

        SortingControlRow(sortingItems = orderingBys, radioButtonColors = radioButtonColors)
    }
}

@Composable
private fun SortingControlRow(sortingItems: List<SortingItem>, radioButtonColors: RadioButtonColors) {
    LazyRow(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            items = sortingItems
        ) { sortingItem ->
            Text(
                text = sortingItem.text,
                style = MaterialTheme.typography.body2,
                fontSize = 10.sp
            )
            RadioButton(
                selected = sortingItem.isSelected(),
                onClick = sortingItem.onClick,
                colors = radioButtonColors
            )
        }
    }
}