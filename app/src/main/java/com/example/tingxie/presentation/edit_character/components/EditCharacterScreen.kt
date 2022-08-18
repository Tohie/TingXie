package com.example.tingxie.presentation.edit_character

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditCharacterScreen(
    navController: NavController,
    viewModel: EditCharacterViewModel = hiltViewModel()
) {
    val characterNumberState = viewModel.characterNumber.value
    val characterState = viewModel.character.value
    val pinyinState = viewModel.pinyin.value
    val descriptionState = viewModel.description.value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

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
        floatingActionButton = { FloatingActionButton(viewModel) },
        bottomBar = { BottomBar(navController = navController) },
        isFloatingActionButtonDocked = true,
        topBar = { TopBar {} },
        scaffoldState = scaffoldState
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
                isHintVisible = characterState.isHintVisible,
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
                singleLine = true,
                textStyle = MaterialTheme.typography.h5
            )
        }
    }
}

@Composable
private fun FloatingActionButton(viewModel: EditCharacterViewModel) {
    FloatingActionButton(
        onClick = {
            viewModel.onEvent(EditCharacterEvent.SaveCharacter)
        },
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Icon(imageVector = Icons.Default.Done, contentDescription = "Save Note")
    }
}