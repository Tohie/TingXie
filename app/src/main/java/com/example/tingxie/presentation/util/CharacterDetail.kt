package com.example.tingxie.presentation.util

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Expand
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tingxie.domain.model.Categories
import com.example.tingxie.domain.model.Character
import kotlin.math.exp

@Composable
fun CharacterDetail(
    character: Character,
    modifier: Modifier = Modifier,
    showCharacter: Boolean = true,
    Categories: @Composable () -> Unit,
    AdditionalContent: @Composable () -> Unit,

) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier,
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
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
                        text = "${character.characterNumber} - ${character.character}",
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
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 10.sp
                    )
                }
                AdditionalContent()
            }

            IconButton(onClick = { expanded = !expanded }) {
                Icon(imageVector = Icons.Default.Expand , contentDescription = "Expand categories view")
            }
            if (expanded) {
                Categories()
            }
        }
    }
}

@Composable
fun CategoryClips(
    categories: List<Categories>,
    onClick: (Categories) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(
            items = categories
        ) { category ->
            Chip(
                name = category.categoryName,
                onClick = { onClick(category) },
            )
        }
    }
}


@Composable
fun Chip(
    name: String = "Chip",
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.padding(4.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.primary
    ) {
        Row(modifier = Modifier
            .clickable { onClick() }
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.body2,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}