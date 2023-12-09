package dev.bruno.wheretowatch.features.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.ds.components.MainScreenTopBar
import dev.bruno.wheretowatch.ds.components.WhereToWatchCard
import dev.bruno.wheretowatch.features.home.HomeScreen.Event
import dev.bruno.wheretowatch.features.home.HomeScreen.Event.OpenSettings
import dev.bruno.wheretowatch.services.discover.TrendWindow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data object HomeScreen : Screen {
    data class State(
        val trendingItems: HomeTrending,
        val popularItems: ImmutableList<HomeMovieItem>,
        val upComingItems: ImmutableList<HomeMovieItem>,
        val topRatedItems: ImmutableList<HomeMovieItem>,
        val actionItems: ImmutableList<HomeMovieItem>,
        val horrorItems: ImmutableList<HomeMovieItem>,
        val netflixItems: ImmutableList<HomeMovieItem>,
        val onEvent: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object OpenSettings : Event
        data class ChangeTrendWindow(val value: TrendWindow) : Event
    }
}

private const val LandscapeRatio = 16 / 11f
private const val PortraitRatio = 2 / 3f

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@CircuitInject(HomeScreen::class, AppScope::class)
@Composable
fun HomeContent(
    state: HomeScreen.State,
    modifier: Modifier = Modifier,
) {

    val trendingItems = state.trendingItems.items
    val popularItems = state.popularItems
    val upcomingItems = state.upComingItems
    val topRatedItems = state.topRatedItems
    val actionItems = state.actionItems
    val horrorItems = state.horrorItems
    val netflixItems = state.netflixItems

    Scaffold(
        modifier = modifier,
        topBar = {
            MainScreenTopBar(
                title = "Where to watch",
                onClick = { state.onEvent(OpenSettings) }
            )
        }
    ) { paddingValues ->
        Box {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .fillMaxWidth()
                    .composed {
                        windowInsetsPadding(
                            WindowInsets.systemBars
                                .only(WindowInsetsSides.Horizontal)
                        )
                    }
            ) {

                item(key = "popular Items") {
                    Spacer(Modifier.height(8.dp))

                    HomeListHeader(
                        headerTitle = "Popular Movies",
                        modifier = Modifier,
                        alignment = Alignment.CenterVertically,
                    )

                    if (popularItems.isNotEmpty()) {
                        val pagerState = rememberPagerState(pageCount = { popularItems.size })
                        HorizontalHomeMoviePager(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clip(MaterialTheme.shapes.large),
                            items = popularItems,
                            pagerState = pagerState,
                            aspectRatio = LandscapeRatio,
                        )

                        val coroutineScope = rememberCoroutineScope()
                        HomeMoviePagerIndicator(
                            pagerState = pagerState,
                            modifier = Modifier
                                .height(24.dp)
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.Start,
                            onClick = { page ->
                                coroutineScope.launch { pagerState.scrollToPage(page) }
                            }
                        )
                    }
                }


                item(key = "upcoming Items") {
                    HorizontalCarousel(
                        items = upcomingItems,
                        headerTitle = "Upcoming Movies",
                        carouselItemContent = { item ->
                            WhereToWatchCard(
                                model = item,
                                type = ImageType.Poster,
                                title = item.title,
                                onClick = { },
                                modifier = Modifier
                                    .animateItemPlacement()
                                    .width(150.dp) // TODO make it dynamic
                                    .aspectRatio(PortraitRatio)
                            )
                        },
                    )
                }

                item(key = "top rated Items") {
                    HorizontalCarousel(
                        items = topRatedItems,
                        headerTitle = "Top rated Movies",
                        carouselItemContent = { item ->
                            WhereToWatchCard(
                                model = item,
                                type = ImageType.Poster,
                                title = item.title,
                                onClick = { },
                                modifier = Modifier
                                    .animateItemPlacement()
                                    .width(150.dp) // TODO make it dynamic
                                    .aspectRatio(PortraitRatio)
                            )
                        },
                    )
                }

                item(key = "action Items") {
                    HorizontalCarousel(
                        items = actionItems,
                        headerTitle = "Action Movies",
                        carouselItemContent = { item ->
                            WhereToWatchCard(
                                model = item,
                                type = ImageType.Poster,
                                title = item.title,
                                onClick = { },
                                modifier = Modifier
                                    .animateItemPlacement()
                                    .width(150.dp) // TODO make it dynamic
                                    .aspectRatio(PortraitRatio)
                            )
                        },
                    )
                }

                item(key = "horror Items") {
                    HorizontalCarousel(
                        items = horrorItems,
                        headerTitle = "Horror Movies",
                        carouselItemContent = { item ->
                            WhereToWatchCard(
                                model = item,
                                type = ImageType.Poster,
                                title = item.title,
                                onClick = { },
                                modifier = Modifier
                                    .animateItemPlacement()
                                    .width(150.dp) // TODO make it dynamic
                                    .aspectRatio(PortraitRatio)
                            )
                        },
                    )
                }

                item(key = "trending Items") {
                    HorizontalParallaxCarousel(
                        items = trendingItems,
                        headerTitle = "Trending Movies",
                        aspectRatio = LandscapeRatio,
                        rightSideContent = {
                            TrendingToggle(
                                trendWindow = state.trendingItems.trendWindow,
                                onChange = { state.onEvent(Event.ChangeTrendWindow(it)) },
                                choices = TrendWindow.entries,
                                modifier = Modifier
                                    .width(IntrinsicSize.Max),
                            )
                        },
                    )
                }

                item(key = "netflix Items") {
                    HorizontalCarousel(
                        items = netflixItems,
                        headerTitle = "On Netflix",
                        carouselItemContent = { item ->
                            WhereToWatchCard(
                                model = item,
                                type = ImageType.Poster,
                                title = item.title,
                                onClick = { },
                                modifier = Modifier
                                    .animateItemPlacement()
                                    .width(150.dp) // TODO make it dynamic
                                    .aspectRatio(PortraitRatio)
                            )
                        },
                    )
                }

                item {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun TrendingToggle(
    trendWindow: TrendWindow,
    onChange: (TrendWindow) -> Unit,
    modifier: Modifier = Modifier,
    choices: List<TrendWindow> = TrendWindow.entries
) {
    val colorScheme = MaterialTheme.colorScheme
    val selectedTint = colorScheme.primary
    val onSelectedTint = colorScheme.onPrimary
    val unselectedTint = colorScheme.surface
    val onUnselectedTint = colorScheme.onSurface

    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .background(unselectedTint)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        for (i in choices.indices) {
            val current = choices[i] == trendWindow
            val background = if (current) selectedTint else Color.Transparent
            val textColor = if (current) onSelectedTint else onUnselectedTint

            Row(
                modifier = Modifier
                    .background(background, shape = RoundedCornerShape(12.dp))
                    .toggleable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        value = current,
                        onValueChange = { selected ->
                            if (selected) {
                                onChange(choices[i])
                            }
                        }
                    )
            ) {
                Text(
                    text = getText(choices[i]),
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
}

// TODO extract to resources
private fun getText(window: TrendWindow): String {
    return when (window) {
        TrendWindow.DAY -> "Today"
        TrendWindow.WEEK -> "This week"
    }
}
