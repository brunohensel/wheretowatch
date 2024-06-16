package dev.bruno.wheretowatch.features.discover

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.bruno.wheretowatch.di.viewmodel.viewModel
import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.ds.components.MainScreenTopBar
import dev.bruno.wheretowatch.ds.components.WhereToWatchCard
import dev.bruno.wheretowatch.features.discover.MovieScreenState.Event.OnMovieClicked
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

data class MovieScreenState(
    val discoverFeed: DiscoverFeed,
    val onEvent: (Event) -> Unit,
) {

    sealed interface Event {
        data class OnMovieClicked(val movieId: Int) : Event
    }
}

@Composable
fun MovieScreen(
    navController: NavHostController,
    viewModel: MovieViewModel = viewModel(navController),
) {
    val state: MovieScreenState = viewModel.state()

    MovieContentScreen(state)
}


private const val LandscapeRatio = 16 / 11f
private const val PortraitRatio = 2 / 3f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieContentScreen(
    state: MovieScreenState,
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
                    .composed {
                        windowInsetsPadding(
                            WindowInsets.systemBars
                                .only(WindowInsetsSides.Horizontal)
                        )
                    }
            ) {

                items(
                    items = feed.section.keys.toList(),
                    key = { key -> key.ordinal },
                ) { sectionKey ->
                    FeedSections(
                        sectionKey = sectionKey,
                        contentMap = feed.section,
                        onMovieClick = { id -> state.onEvent(OnMovieClicked(id)) },
                    )
                }

                item {
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
) {
    val content = contentMap.getOrDefault(sectionKey, null)
    val items = content?.items ?: persistentListOf()
    when (sectionKey) {
        DiscoverSections.HarryPotter,
        DiscoverSections.HungerGames,
        DiscoverSections.LordOfRings,
        DiscoverSections.Avengers -> {
            HorizontalParallaxCarousel(
                items = items,
                onClick = onMovieClick,
                headerTitle = sectionKey.getSpacedName(),
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
