package com.example.tingxie.presentation.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

@Composable
fun TopSearchSortBar(
    onSearchQueryChanged: (String) -> Unit,
    onExpandSortingOptions: () -> Unit,
    sortingControls: @Composable () -> Unit,
    isOrderingOptionsVisible: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ){
        SearchBar(
            onSearchQueryChanged = { searchQuery ->
                onSearchQueryChanged(searchQuery)
            },
            onExpandSortingOptions = {
                onExpandSortingOptions()
            }
        )

        Spacer(modifier = Modifier.width(12.dp))

        SortingOptions(
            isOrderingOptionsVisible = isOrderingOptionsVisible,
            SortingControls = sortingControls,
        )
    }
}

@Composable
private fun ColumnScope.SortingOptions(
    isOrderingOptionsVisible: Boolean,
    SortingControls: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = isOrderingOptionsVisible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SortingControls()
        }
    }
}