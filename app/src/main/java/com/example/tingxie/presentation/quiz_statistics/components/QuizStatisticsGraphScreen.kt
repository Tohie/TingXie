package com.example.tingxie.presentation.quiz_statistics.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsEvent
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsViewModel
import java.util.*

@Composable
fun QuizStatisticsScreen(
    viewModel: QuizStatisticsViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            GraphControls(
                onDateChanged = { _, year, month, dayOfMonth ->
                    // Send Date changed event
                }, onClick = { amount ->
                    viewModel.onEvent(QuizStatisticsEvent.ChangeNumberOfTestsDisplayed(amount))
                }
            )
            QuizStatisticsGraph(
                viewModel = viewModel,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun GraphControls(
    modifier: Modifier = Modifier,
    onDateChanged: (DatePicker, Int, Int, Int) -> Unit,
    onClick: (Int) -> Unit
) {
    Box(modifier = modifier) {
        Column {
            GraphDateSelectorControls(onDateChanged = onDateChanged)
            GraphButtons(onClick = onClick)
        }
    }
}

@Composable
fun GraphDateSelectorControls(
    modifier: Modifier = Modifier,
    onDateChanged: (DatePicker, Int, Int, Int) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        onDateChanged,
        year,
        month,
        day
    )

    Box(
        modifier = modifier
    ) {
        Button(
            onClick = {
                datePickerDialog.show()
            }
        ) {
            Text(text = "Choose tests from a specific date")
        }
    }
}

@Composable
fun GraphButtons (
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    Box (
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Show last selected amount of texts")
            for (i in listOf(5, 10, 15)) {
                GraphButton(onClick = { onClick(i) }, text = i.toString() )
            }
        }
    }
}

@Composable
fun GraphButton(
    onClick: () -> Unit,
    text: String
) {
    Button(onClick = onClick) {
        Text(text = String())
    }
}