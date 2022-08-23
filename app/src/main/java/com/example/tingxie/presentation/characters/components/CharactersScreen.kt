package com.example.tingxie.presentation.characters.components

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tingxie.domain.model.Character
import com.example.tingxie.domain.model.CharacterWithCategories
import com.example.tingxie.domain.model.util.OrderCharactersBy
import com.example.tingxie.domain.model.util.Ordering
import com.example.tingxie.presentation.characters.CharactersEvent
import com.example.tingxie.presentation.characters.CharactersState
import com.example.tingxie.presentation.characters.CharactersViewModel
import com.example.tingxie.presentation.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun CharactersScreen(
    navController: NavController,
    viewModel: CharactersViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )


    Scaffold(
        topBar = { TopBar {
            TopSearchSortBar(
                onSearchQueryChanged = { searchQuery ->
                    viewModel.onEvent(CharactersEvent.Search(searchQuery))
                },
                onExpandSortingOptions = {
                    viewModel.onEvent(CharactersEvent.ChangeSortingOptionsVisibility)
                },
                isOrderingOptionsVisible = viewModel.state.value.isOrderingOptionsVisible,
                sortingControls = { CharacterSortingControls(viewModel = viewModel) }
            )
        } },
        bottomBar = { BottomBar(navController) },
        scaffoldState = scaffoldState,
        modifier = Modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CharacterScreenCharacterList(
                state = state,
                viewModel = viewModel,
                scope = scope,
                scaffoldState = scaffoldState,
                navController = navController
            )
        }
    }
}



@Composable
private fun CharacterSortingControls(
    viewModel: CharactersViewModel
) {
    val ordering = listOf(
        SortingItem(
            text = "Ascending",
            isSelected = { viewModel.state.value.ordering.isAscending() },
            onClick = {
                val event = when (viewModel.state.value.ordering) {
                    is OrderCharactersBy.Character -> CharactersEvent.ChangeSorting(OrderCharactersBy.Character(Ordering.Acsending))
                    is OrderCharactersBy.DateAdded -> CharactersEvent.ChangeSorting(OrderCharactersBy.DateAdded(Ordering.Acsending))
                    is OrderCharactersBy.CharacterNumber -> CharactersEvent.ChangeSorting(OrderCharactersBy.CharacterNumber(Ordering.Acsending))
                }
                viewModel.onEvent(event)
            }
        ),
        SortingItem(
            text = "Descending",
            isSelected = { !viewModel.state.value.ordering.isAscending() },
            onClick = {
                val event = when (viewModel.state.value.ordering) {
                    is OrderCharactersBy.Character -> CharactersEvent.ChangeSorting(OrderCharactersBy.Character(Ordering.Descending))
                    is OrderCharactersBy.DateAdded -> CharactersEvent.ChangeSorting(OrderCharactersBy.DateAdded(Ordering.Descending))
                    is OrderCharactersBy.CharacterNumber -> CharactersEvent.ChangeSorting(OrderCharactersBy.CharacterNumber(Ordering.Descending))
                }
                viewModel.onEvent(event)
            }
        )
    )
    val orderBys = listOf(
        SortingItem(
            text = "Date Added",
            isSelected = { viewModel.state.value.ordering is OrderCharactersBy.DateAdded },
            onClick = {
                val event = when (viewModel.state.value.ordering.isAscending()) {
                    true -> CharactersEvent.ChangeSorting(OrderCharactersBy.DateAdded(Ordering.Acsending))
                    false -> CharactersEvent.ChangeSorting(OrderCharactersBy.DateAdded(Ordering.Descending))
                }
                viewModel.onEvent(event)
            }
        ),
        SortingItem(
            text = "Number",
            isSelected = { viewModel.state.value.ordering is OrderCharactersBy.CharacterNumber },
            onClick = {
                val event = when (viewModel.state.value.ordering.isAscending()) {
                    true -> CharactersEvent.ChangeSorting(OrderCharactersBy.CharacterNumber(Ordering.Acsending))
                    false -> CharactersEvent.ChangeSorting(OrderCharactersBy.CharacterNumber(Ordering.Descending))
                }
                viewModel.onEvent(event)
            }
        ),
        SortingItem(
            text = "Character",
            isSelected = { viewModel.state.value.ordering is OrderCharactersBy.Character },
            onClick = {
                val event = when (viewModel.state.value.ordering.isAscending()) {
                    true -> CharactersEvent.ChangeSorting(OrderCharactersBy.Character(Ordering.Acsending))
                    false -> CharactersEvent.ChangeSorting(OrderCharactersBy.Character(Ordering.Descending))
                }
                viewModel.onEvent(event)
            },
        )
    )

    SortingControls(
        orderings = ordering,
        orderingBys = orderBys
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CharacterScreenCharacterList(
    state: CharactersState,
    viewModel: CharactersViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(
            items = state.characters,
            key = { item: CharacterWithCategories -> item.character.id!! }
        ) { character ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CharacterScreenCharacterDetail(
                    character = character,
                    viewModel = viewModel,
                    scope = scope,
                    scaffoldState = scaffoldState,
                    navController = navController,
                    modifier = Modifier
                        .animateItemPlacement()
                        .clickable {
                            navController.navigate(
                                Screen.EditCharacterScreen.route + "?characterId=${character.character.id}"
                            )
                        }
                        .padding(4.dp),
                )
            }
        }
    }
}

@Composable
private fun CharacterScreenCharacterDetail(
    character: CharacterWithCategories,
    viewModel: CharactersViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController,
    modifier: Modifier
) {
    CharacterDetail(
        character = character.character,
        modifier = modifier,
        showCharacter = true,
        showCategories = true,
        AdditionalContent = {
            EndAlignedDeleteButton(viewModel, character, scope, scaffoldState)

        },
        Categories = {
            CategoryClips(categories = character.categories, onClick = {
                navController.navigate(Screen.EditCategoriesScreen.route + "?characterId=${character.character.id}")
            } )
        }
    )
}

@Composable
private fun EndAlignedDeleteButton(
    viewModel: CharactersViewModel,
    character: CharacterWithCategories,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Row(
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            onClick = {
                viewModel.onEvent(CharactersEvent.Delete(character))
                scope.launch {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = "Note deleted",
                        actionLabel = "Undo"
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(CharactersEvent.RestoreCharacter)
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
        }
    }
}


