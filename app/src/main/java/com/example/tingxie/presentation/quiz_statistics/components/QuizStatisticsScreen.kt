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
import com.example.tingxie.domain.model.CharacterStatistics
import com.example.tingxie.domain.model.util.OrderCharacterResultsBy
import com.example.tingxie.domain.model.util.OrderCharactersBy
import com.example.tingxie.domain.model.util.Ordering
import com.example.tingxie.presentation.characters.CharactersEvent
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsEvent
import com.example.tingxie.presentation.quiz_statistics.QuizStatisticsViewModel
import com.example.tingxie.presentation.util.*
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
        topBar = {
            // Only show search box when user is looking at character statistics
            if (pagerState.currentPage == 1) {
                TopSearchSortBar(
                    onSearchQueryChanged = { searchQuery ->
                        viewModel.onEvent(QuizStatisticsEvent.Search(searchQuery))
                    },
                    sortingControls = { QuizStatisticsSortingControls(viewModel = viewModel) },
                )
            } else {
                TopBar()
            }
        },
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
                                if (viewModel.state.value.characterResults.isEmpty()) return@HorizontalPager
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

@Composable
fun QuizStatisticsSortingControls(viewModel: QuizStatisticsViewModel) {
    val ordering = listOf(
        SortingItem(
            text = "Ascending",
            isSelected = { viewModel.state.value.ordering.isAscending() },
            onClick = {
                val ordering = when (viewModel.state.value.ordering) {
                    is OrderCharacterResultsBy.Best -> OrderCharacterResultsBy.Best(Ordering.Acsending)
                    is OrderCharacterResultsBy.Character -> OrderCharacterResultsBy.Character(
                        Ordering.Acsending
                    )
                    is OrderCharacterResultsBy.CharacterNumber -> OrderCharacterResultsBy.CharacterNumber(
                        Ordering.Acsending
                    )
                    is OrderCharacterResultsBy.Worst -> OrderCharacterResultsBy.Worst(Ordering.Acsending)
                }
                viewModel.onEvent(QuizStatisticsEvent.OrderResultsBy(ordering))
            }
        ),
        SortingItem(
            text = "Descending",
            isSelected = { !viewModel.state.value.ordering.isAscending() },
            onClick = {
                val ordering = when (viewModel.state.value.ordering) {
                    is OrderCharacterResultsBy.Best -> OrderCharacterResultsBy.Best(Ordering.Descending)
                    is OrderCharacterResultsBy.Character -> OrderCharacterResultsBy.Character(Ordering.Descending)
                    is OrderCharacterResultsBy.CharacterNumber -> OrderCharacterResultsBy.CharacterNumber(Ordering.Descending)
                    is OrderCharacterResultsBy.Worst -> OrderCharacterResultsBy.Worst(Ordering.Descending)
                }
                viewModel.onEvent(QuizStatisticsEvent.OrderResultsBy(ordering))
            }
        ),
    )

    val orderingBy = listOf(
        SortingItem(
            text = "Best",
            isSelected = { viewModel.state.value.ordering is OrderCharacterResultsBy.Best},
            onClick = {
                val event = when (viewModel.state.value.ordering.isAscending()) {
                    true -> QuizStatisticsEvent.OrderResultsBy(OrderCharacterResultsBy.Best(Ordering.Acsending))
                    false -> QuizStatisticsEvent.OrderResultsBy(OrderCharacterResultsBy.Best(Ordering.Descending))
                }
                viewModel.onEvent(event)
            }
        ),
        SortingItem(
            text = "Worst",
            isSelected = { viewModel.state.value.ordering is OrderCharacterResultsBy.Worst},
            onClick = {
                val event = when (viewModel.state.value.ordering.isAscending()) {
                    true -> QuizStatisticsEvent.OrderResultsBy(OrderCharacterResultsBy.Worst(Ordering.Acsending))
                    false -> QuizStatisticsEvent.OrderResultsBy(OrderCharacterResultsBy.Worst(Ordering.Descending))
                }
                viewModel.onEvent(event)
            }
        ),
        SortingItem(
            text = "Character",
            isSelected = { viewModel.state.value.ordering is OrderCharacterResultsBy.Character},
            onClick = {
                val event = when (viewModel.state.value.ordering.isAscending()) {
                    true -> QuizStatisticsEvent.OrderResultsBy(OrderCharacterResultsBy.Character(Ordering.Acsending))
                    false -> QuizStatisticsEvent.OrderResultsBy(OrderCharacterResultsBy.Character(Ordering.Descending))
                }
                viewModel.onEvent(event)
            }
        ),
        SortingItem(
            text = "Character\nNumber",
            isSelected = { viewModel.state.value.ordering is OrderCharacterResultsBy.CharacterNumber},
            onClick = {
                val event = when (viewModel.state.value.ordering.isAscending()) {
                    true -> QuizStatisticsEvent.OrderResultsBy(OrderCharacterResultsBy.CharacterNumber(Ordering.Acsending))
                    false -> QuizStatisticsEvent.OrderResultsBy(OrderCharacterResultsBy.CharacterNumber(Ordering.Descending))
                }
                viewModel.onEvent(event)
            }
        )
    )
    SortingControls(orderingBys = orderingBy, orderings = ordering)
}