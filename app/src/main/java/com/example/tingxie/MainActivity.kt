package com.example.tingxie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tingxie.presentation.character_quiz.components.CharacterQuizScreen
import com.example.tingxie.presentation.characters.components.CharactersScreen
import com.example.tingxie.presentation.edit_character.EditCharacterScreen
import com.example.tingxie.presentation.quiz_statistics.components.QuizStatisticsGraphScreen
import com.example.tingxie.presentation.util.Screen
import com.example.tingxie.ui.theme.TingXieTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val systemUiController = rememberSystemUiController()
            TingXieTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    systemUiController.setSystemBarsColor(MaterialTheme.colors.primary)
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.CharactersScreen.route) {
                        composable(route = Screen.CharactersScreen.route) {
                            CharactersScreen(navController = navController)
                        }
                        
                        composable(
                            route = Screen.EditCharacterScreen.route + "?characterId={characterId}",
                            arguments = listOf(
                                navArgument(
                                    name = "characterId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                            )
                        ) {
                            EditCharacterScreen(navController = navController)
                        }

                        composable(
                            route = Screen.CharacterQuizScreen.route + "?characterNumber={characterNumber}",
                            arguments = listOf(
                                navArgument(
                                    name = "characterNumber"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = 20
                                }
                            )
                        ) {
                            CharacterQuizScreen(navController = navController)
                        }

                        composable(
                            route = Screen.QuizStatisticsScreen.route
                        ) {
                            QuizStatisticsGraphScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
