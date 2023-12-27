package dev.bruno.wheretowatch.features.discover

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.ds.components.WhereToWatchCard
import kotlinx.collections.immutable.ImmutableList
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.HorizontalHomeMoviePager(
    items: ImmutableList<DiscoverMovieItem>,
    pagerState: PagerState,
    aspectRatio: Float,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
) {

    HorizontalPager(
        state = pagerState,
        pageSpacing = 8.dp,
        modifier = modifier,
    ) { page ->

        WhereToWatchCard(
            model = items[page],
            type = ImageType.Backdrop,
            title = items[page].title,
            onClick = { onClick(items[page].id) },
            modifier = Modifier
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue

                    // We animate the alpha, between 50% and 100%
                    val fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    alpha = (1 - 1f - pageOffset.coerceIn(
                        minimumValue = 0f,
                        maximumValue = 1f
                    )) * 0.5f + fraction * 1f
                }
                .animateItemPlacement()
                .aspectRatio(aspectRatio)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeMoviePagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    indicatorColor: Color = MaterialTheme.colorScheme.onBackground,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    onClick: (Int) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) {
                indicatorColor
            } else indicatorColor.copy(alpha = 0.5f)

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color)
                    .weight(1f)
                    .height(4.dp)
                    .clickable { onClick(iteration) }
            )
        }
    }
}
