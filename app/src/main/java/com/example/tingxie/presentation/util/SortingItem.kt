package com.example.tingxie.presentation.util

import com.example.tingxie.domain.model.util.Ordering

data class SortingItem(
    val text: String,
    val isSelected: () -> Boolean,
    val onClick: () -> Unit
)