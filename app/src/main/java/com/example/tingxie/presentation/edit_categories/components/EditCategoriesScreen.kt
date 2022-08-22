package com.example.tingxie.presentation.edit_categories.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tingxie.domain.model.Categories
import com.example.tingxie.presentation.edit_categories.EditCategoriesEvents
import com.example.tingxie.presentation.edit_categories.EditCategoriesViewModel
import com.example.tingxie.presentation.edit_character.EditCharacterEvent
import com.example.tingxie.presentation.edit_character.EditCharacterViewModel
import com.example.tingxie.presentation.util.BottomBar
import com.example.tingxie.presentation.util.BottomNavigationBar
import com.example.tingxie.presentation.util.CategoryDropDown
import com.example.tingxie.presentation.util.TopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun EditCategoriesScreen(
    navController: NavController,
    viewModel: EditCategoriesViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is EditCategoriesViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar {
                CategoryDropDown(
                    categories = viewModel.state.value.allCategories,
                    onClick = { category ->
                        viewModel.onEvent(EditCategoriesEvents.AddNewCharacterCategoryCrossRef(category))
                    }
                )
            }
        },
        bottomBar = { BottomBar(navController = navController)}
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Box(modifier = Modifier.padding(12.dp)) {
                CategoriesList(viewModel, scope, scaffoldState)
            }
        }
    }
}

@Composable
private fun CategoriesList(
    viewModel: EditCategoriesViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
    ) {
        items(
            items = viewModel.state.value.currentCategories,
            key = { it.categoryId!! }
        ) { categoryFromCharacters ->
            CategoryCard(categoryFromCharacters, viewModel, scope, scaffoldState)
        }
    }
}

@Composable
private fun CategoryCard(
    categoryFromCharacters: Categories,
    viewModel: EditCategoriesViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        elevation = 6.dp,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(text = categoryFromCharacters.categoryName)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                DeleteCategoryButton(
                    viewModel,
                    categoryFromCharacters,
                    scope,
                    scaffoldState
                )
            }
        }

    }
}

@Composable
private fun DeleteCategoryButton(
    viewModel: EditCategoriesViewModel,
    categoryFromCharacters: Categories,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    IconButton(
        onClick = {
            viewModel.onEvent(
                EditCategoriesEvents.DeleteCharacterCategoryCrossRef(
                    categoryFromCharacters
                )
            )
            scope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = "Note deleted",
                    actionLabel = "Undo"
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.onEvent(EditCategoriesEvents.UndoLastDeleted)
                }
            }
        },
    ) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete category")
    }
}