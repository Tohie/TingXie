package com.example.tingxie.presentation.util

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    additional: @Composable () -> Unit = {},
    hasUnderBar: Boolean = false,
    underBar: @Composable () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "听写",
                fontSize = 32.sp,
                modifier = Modifier.padding(12.dp),
            )

            Log.i("Character", "Drawing row with additional")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                additional()
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
        if (hasUnderBar) {
            underBar()
        }
    }
}

@Composable
fun TopSearchSortBar2(
    onSearchQueryChanged: (String) -> Unit,
    sortingControls: @Composable () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    TopBar(
        additional = {
            SearchBar(
                onSearchQueryChanged = { searchQuery ->
                    onSearchQueryChanged(searchQuery)
                },
                onExpandSortingOptions = { isExpanded = !isExpanded },
            )
        },
        hasUnderBar = isExpanded,
        underBar = {
            SortingOptions(
                isOrderingOptionsVisible = isExpanded,
                SortingControls = sortingControls,
            )
        }
    )
}