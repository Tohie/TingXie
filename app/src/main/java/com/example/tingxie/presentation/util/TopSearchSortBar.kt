package com.example.tingxie.presentation.util

import android.util.Log
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
fun SortingOptions(
    isOrderingOptionsVisible: Boolean,
    SortingControls: @Composable () -> Unit,
) {
    Log.i("characters", "drawing sorting options")
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