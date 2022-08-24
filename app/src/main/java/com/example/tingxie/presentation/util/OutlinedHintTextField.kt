package com.example.tingxie.presentation.edit_character

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OutlinedHintTextField(
    text: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    hint: String
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            placeholder = {
                Text(
                    text = hint,
                    style = textStyle,
                    fontSize = 20.sp
                )
            }
        )
        /*
        Spacer(modifier = Modifier.width(4.dp))
        if (isHintVisible) {
            Text(
                text = hint,
                style = textStyle,
                modifier = Modifier.offset(x = 10.dp, y = 13.dp),
                fontSize = 20.sp
            )
        }
         */
    }
}