package com.example.tingxie.presentation.edit_character

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tingxie.presentation.util.BottomBar
import com.example.tingxie.presentation.util.TopBar
import com.google.accompanist.pager.*
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
                        text = { Text(text = "Add Character")}
                    )
                    Tab(
                        selected = pagerState.currentPage == 1,
                        onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                        text = { Text(text = "Add Category")}
                    )
                }

                Box(modifier = Modifier.padding(innerPadding)) {
                    HorizontalPager(
                        count = 2,
                        state = pagerState
                    ) { page ->
                        when (page) {
                            0 -> AddCharacter(characterNumberState, viewModel, characterState, pinyinState, descriptionState)
                            1 -> AddCategory(viewModel = viewModel, categoryNameState = categoryNameState)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddCategory(
    viewModel: EditCharacterViewModel,
    categoryNameState: EditCharacterTextField,
) {
    OutlinedHintTextField(
        text = categoryNameState.text,
        hint = categoryNameState.hint,
        onValueChange = { viewModel.onEvent(EditCharacterEvent.EnteredCategoryName(it)) },
        onFocusChange = { viewModel.onEvent(EditCharacterEvent.ChangeCategoryNameFocus(it)) },
        isHintVisible = categoryNameState.isHintVisible,
        singleLine = true,
        textStyle = MaterialTheme.typography.h5
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
            hint = characterNumberState.hint,
            onValueChange = { viewModel.onEvent(EditCharacterEvent.EnteredCharacterNumber(it)) },
            onFocusChange = { viewModel.onEvent(EditCharacterEvent.ChangeCharacterNumberFocus(it)) },
            isHintVisible = characterNumberState.isHintVisible,
            singleLine = true,
            textStyle = MaterialTheme.typography.h5,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedHintTextField(
            text = characterState.text,
            hint = characterState.hint,
            onValueChange = { viewModel.onEvent(EditCharacterEvent.EnteredCharacter(it)) },
            onFocusChange = { viewModel.onEvent(EditCharacterEvent.ChangeCharacterFocus(it)) },
            isHintVisible = characterState.isHintVisible,
            singleLine = true,
            textStyle = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedHintTextField(
            text = pinyinState.text,
            hint = pinyinState.hint,
            onValueChange = { viewModel.onEvent(EditCharacterEvent.EnteredPinyin(it)) },
            onFocusChange = { viewModel.onEvent(EditCharacterEvent.ChangePinyinFocus(it)) },
            isHintVisible = pinyinState.isHintVisible,
            singleLine = true,
            textStyle = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedHintTextField(
            text = descriptionState.text,
            hint = descriptionState.hint,
            onValueChange = { viewModel.onEvent(EditCharacterEvent.EnteredDescription(it)) },
            onFocusChange = { viewModel.onEvent(EditCharacterEvent.ChangeDescriptionFocus(it)) },
            isHintVisible = descriptionState.isHintVisible,
            singleLine = false,
            height = 120.dp,
            textStyle = MaterialTheme.typography.h5
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun FloatingActionButton(viewModel: EditCharacterViewModel, pagerState: PagerState) {
    FloatingActionButton(
        onClick = {
            if (pagerState.currentPage == 0) {
                viewModel.onEvent(EditCharacterEvent.SaveCharacter)
            } else {
                viewModel.onEvent(EditCharacterEvent.SaveCategory)
            }
        },
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Save Note")
    }
}