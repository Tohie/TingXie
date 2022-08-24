package com.example.tingxie.presentation.util

data class SortingItem(
    val text: String,
    val isSelected: () -> Boolean,
    val onClick: () -> Unit
)