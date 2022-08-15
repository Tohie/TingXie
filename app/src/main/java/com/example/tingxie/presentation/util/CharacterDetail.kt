package com.example.tingxie.presentation.util

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tingxie.domain.model.Character

@Composable
fun CharacterDetail(
    character: Character,
    modifier: Modifier = Modifier,
    showCharacter: Boolean = true,
    additionalContent: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = 10.dp
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            if (showCharacter) {
                Log.i("Character", "Showing character")
                Text(
                    text = "${character.id} - ${character.character}",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Column() {
                Log.i("Character", "Pinyin: ${character.pinyin}")
                Text(
                    text = character.pinyin,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Log.i("Character", "Description: ${character.description}")
                Text(
                    text = character.description,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 10.sp
                )
            }
            additionalContent()
        }
    }
}