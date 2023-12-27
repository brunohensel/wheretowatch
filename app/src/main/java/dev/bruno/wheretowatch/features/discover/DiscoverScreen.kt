package dev.bruno.wheretowatch.features.discover

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
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
import dev.bruno.wheretowatch.features.discover.DiscoverScreen.Event.ChangeTrendWindow
import dev.bruno.wheretowatch.features.discover.DiscoverScreen.Event.OnMovieClicked
import dev.bruno.wheretowatch.services.discover.TrendWindow
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data object DiscoverScreen : Screen {
    data class State(
        val discoverFeed: DiscoverFeed,
        val onEvent: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data class ChangeTrendWindow(val value: TrendWindow) : Event
        data class OnMovieClicked(val movieId: Int) : Event
    }
}

private const val LandscapeRatio = 16 / 11f
private const val PortraitRatio = 2 / 3f

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(DiscoverScreen::class, AppScope::class)
@Composable
fun DiscoverContent(
    state: DiscoverScreen.State,
    modifier: Modifier = Modifier,
) {

    val feed = state.discoverFeed

    Scaffold(
        modifier = modifier,
        topBar = {
            MainScreenTopBar(
                title = "Where to watch",
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .composed {
                        windowInsetsPadding(
                            WindowInsets.systemBars
                                .only(WindowInsetsSides.Horizontal)
                        )
                    }
            ) {

                items(
                    items = feed.section.keys.toList(),
                    key = { key -> key.order },
                ) { sectionKey ->
                    FeedSections(
                        sectionKey = sectionKey,
                        contentMap = feed.section,
                        onMovieClick = { id -> state.onEvent(OnMovieClicked(id)) },
                        onTrendWindowClick = { window -> state.onEvent(ChangeTrendWindow(window)) },
                    )
                }

                item(key = "bottomSpacer") {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.FeedSections(
    sectionKey: DiscoverSections,
    contentMap: Map<DiscoverSections, DiscoverContent>,
    onMovieClick: (Int) -> Unit,
    onTrendWindowClick: (TrendWindow) -> Unit,
) {
    val content = contentMap.getOrDefault(sectionKey, null)
    val items = content?.items ?: persistentListOf()
    when (sectionKey) {
        DiscoverSections.HarryPotter -> {
            HorizontalParallaxCarousel(
                items = items,
                onClick = onMovieClick,
                headerTitle = "Harry Potter Collection",
                aspectRatio = LandscapeRatio,
            )
        }

        DiscoverSections.Popular -> {
            if (items.isNotEmpty()) {

                HomeListHeader(
                    headerTitle = "Popular Movies",
                    alignment = Alignment.CenterVertically,
                )

                val pagerState =
                    rememberPagerState(pageCount = { items.size })
                HorizontalHomeMoviePager(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(MaterialTheme.shapes.large),
                    items = items,
                    pagerState = pagerState,
                    aspectRatio = LandscapeRatio,
                    onClick = onMovieClick
                )

                val coroutineScope = rememberCoroutineScope()
                HomeMoviePagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    onClick = { page ->
                        coroutineScope.launch { pagerState.scrollToPage(page) }
                    }
                )
            }
        }

        DiscoverSections.Trending -> {
            val trendContent = content as DiscoverTrending
            HorizontalParallaxCarousel(
                items = trendContent.items,
                headerTitle = "Trending Movies",
                onClick = { },
                aspectRatio = LandscapeRatio,
                rightSideContent = {
                    TrendingToggle(
                        trendWindow = trendContent.trendWindow,
                        onChange = onTrendWindowClick,
                        choices = TrendWindow.entries,
                        modifier = Modifier
                            .width(IntrinsicSize.Max),
                    )
                },
            )
        }

        else -> {
            HorizontalCarousel(
                items = items,
                headerTitle = "$sectionKey",
                carouselItemContent = { item ->
                    WhereToWatchCard(
                        model = item,
                        type = ImageType.Poster,
                        title = item.title,
                        onClick = { onMovieClick(item.id) },
                        modifier = Modifier
                            .animateItemPlacement()
                            .width(150.dp) // TODO make it dynamic
                            .aspectRatio(PortraitRatio)
                    )
                },
            )
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
