package com.example.tingxie.presentation.quiz_statistics.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsViewModel
import com.example.tingxie.presentation.util.BottomBar
import com.example.tingxie.presentation.util.TopBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun QuizStatisticsGraphScreen(
    viewModel: QuizStatisticsViewModel = hiltViewModel(),
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    Scaffold(
        topBar = { TopBar {} },
        scaffoldState = scaffoldState,
        bottomBar = { BottomBar(navController = navController)}
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
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
                        text = { Text(text = "Graphs")}
                    )
                    Tab(
                        selected = pagerState.currentPage == 1,
                        onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                        text = { Text(text = "Characters")}
                    )
                }

                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    HorizontalPager(
                        count = 2,
                        state = pagerState
                    ) { page ->
                        when (page) {
                            0 -> {
                                QuizStatisticsGraphScreen(
                                    viewModel = viewModel,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                )
                            }
                            1 -> {
                                if (viewModel.state.value.quizResults.isEmpty()) return@HorizontalPager
                                QuizStatisticsCharacterList (
                                    viewModel = viewModel,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                )

                            }
                            else -> {
                                Text(text = "Woops....")
                            }
                        }
                    }
                }
            }
        }
    }
}