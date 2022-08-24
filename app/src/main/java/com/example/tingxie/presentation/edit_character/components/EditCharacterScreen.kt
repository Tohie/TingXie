package com.example.tingxie.presentation.edit_character.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tingxie.presentation.edit_character.EditCharacterEvent
import com.example.tingxie.presentation.edit_character.EditCharacterTextField
import com.example.tingxie.presentation.edit_character.EditCharacterViewModel
import com.example.tingxie.presentation.util.OutlinedHintTextField
import com.example.tingxie.presentation.util.BottomBar
import com.example.tingxie.presentation.util.TopBar
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EditCharacterScreen(
    navController: NavController,
    viewModel: EditCharacterViewModel = hiltViewModel()
) {
    val characterNumberState = viewModel.characterNumber.value
    val characterState = viewModel.character.value
    val pinyinState = viewModel.pinyin.value
    val descriptionState = viewModel.description.value
    val categoryNameState = viewModel.categoryName.value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                EditCharacterViewModel.UiEvent.SaveCharacter -> {
                    navController.navigateUp()
                }
                is EditCharacterViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }
    Scaffold(
        floatingActionButton = { FloatingActionButton(viewModel, pagerState) },
        bottomBar = { BottomBar(navController = navController) },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        topBar = { TopBar {} },
        scaffoldState = scaffoldState
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column {
                AddCharacterCategoryTabs(pagerState, scope)

                AddCharacterCategoryPager(
                    innerPadding,
                    pagerState,
                    characterNumberState,
                    viewModel,
                    characterState,
                    pinyinState,
                    descriptionState,
                    categoryNameState
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun AddCharacterCategoryPager(
    innerPadding: PaddingValues,
    pagerState: PagerState,
    characterNumberState: EditCharacterTextField,
    viewModel: EditCharacterViewModel,
    characterState: EditCharacterTextField,
    pinyinState: EditCharacterTextField,
    descriptionState: EditCharacterTextField,
    categoryNameState: EditCharacterTextField,
) {
    Box(modifier = Modifier.padding(innerPadding)) {
        HorizontalPager(
            count = 2,
            state = pagerState,
            modifier = Modifier.padding(12.dp)
        ) { page ->
            when (page) {
                0 -> AddCharacter(
                    characterNumberState,
                    viewModel,
                    characterState,
                    pinyinState,
                    descriptionState
                )
                1 -> AddCategory(viewModel = viewModel, categoryNameState = categoryNameState)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun AddCharacterCategoryTabs(
    pagerState: PagerState,
    scope: CoroutineScope
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }
    ) {
        Tab(
            selected = pagerState.currentPage == 0,
            onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
            text = { Text(text = "Add Character") }
        )
        Tab(
            selected = pagerState.currentPage == 1,
            onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
            text = { Text(text = "Add Category") }
        )
    }
}

@Composable
private fun AddCategory(
    viewModel: EditCharacterViewModel,
    categoryNameState: EditCharacterTextField,
) {
    OutlinedHintTextField(
        text = categoryNameState.text,
        onValueChange = { viewModel.onEvent(EditCharacterEvent.EnteredCategoryName(it)) },
        textStyle = MaterialTheme.typography.h5,
        singleLine = true,
        hint = categoryNameState.hint
    )
}


@Composable
private fun AddCharacter(
    characterNumberState: EditCharacterTextField,
    viewModel: EditCharacterViewModel,
    characterState: EditCharacterTextField,
    pinyinState: EditCharacterTextField,
    descriptionState: EditCharacterTextField
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedHintTextField(
            text = characterNumberState.text,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { viewModel.onEvent(EditCharacterEvent.EnteredCharacterNumber(it)) },
            textStyle = MaterialTheme.typography.h5,
            singleLine = true,
            hint = characterNumberState.hint
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedHintTextField(
            text = characterState.text,
            onValueChange = { viewModel.onEvent(EditCharacterEvent.EnteredCharacter(it)) },
            singleLine = true,
            textStyle = MaterialTheme.typography.h5,
            hint = characterState.hint
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedHintTextField(
            text = pinyinState.text,
            onValueChange = { viewModel.onEvent(EditCharacterEvent.EnteredPinyin(it)) },
            textStyle = MaterialTheme.typography.h5,
            singleLine = true,
            hint = pinyinState.hint
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedHintTextField(
            text = descriptionState.text,
            onValueChange = { viewModel.onEvent(EditCharacterEvent.EnteredDescription(it)) },
            textStyle = MaterialTheme.typography.h5,
            singleLine = false,
            hint = descriptionState.hint
        )
    }
}



@OptIn(ExperimentalPagerApi::class)
@Composable
private fun FloatingActionButton(viewModel: EditCharacterViewModel, pagerState: PagerState) {
    if (pagerState.currentPage == 2) return
    FloatingActionButton(
        onClick = {
            if (pagerState.currentPage == 0) {
                viewModel.onEvent(EditCharacterEvent.SaveCharacter)
            } else if (pagerState.currentPage == 1){
                viewModel.onEvent(EditCharacterEvent.SaveCategory)
            }
        },
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Save Note")
    }
}