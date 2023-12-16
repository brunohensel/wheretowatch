package dev.bruno.wheretowatch.features.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.ds.components.WhereToWatchCard
import kotlinx.collections.immutable.ImmutableList
import kotlin.math.roundToInt

@Composable
fun HorizontalCarousel(
    items: ImmutableList<HomeMovieItem>,
    headerTitle: String,
    carouselItemContent: @Composable (HomeMovieItem) -> Unit,
    modifier: Modifier = Modifier,
    rightSideContent: (@Composable () -> Unit)? = null,
) {
    Column {
        Spacer(Modifier.height(8.dp))

        HomeListHeader(
            headerTitle = headerTitle,
            modifier = modifier,
            alignment = Alignment.CenterVertically,
            content = rightSideContent
        )

        if (items.isNotEmpty()) {
            val lazyListState = rememberLazyListState()
            LazyRow(
                state = lazyListState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(MaterialTheme.shapes.medium),
            ) {
                items(
                    items = items,
                    key = { it.id },
                ) { item ->
                    carouselItemContent(item)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalParallaxCarousel(
    items: ImmutableList<HomeMovieItem>,
    headerTitle: String,
    aspectRatio: Float,
    modifier: Modifier = Modifier,
    rightSideContent: (@Composable () -> Unit)? = null,
) {
    Column {
        Spacer(Modifier.height(8.dp))

        HomeListHeader(
            headerTitle = headerTitle,
            modifier = modifier,
            alignment = Alignment.CenterVertically,
            content = rightSideContent
        )

        if (items.isNotEmpty()) {
            val lazyListState = rememberLazyListState()
            LazyRow(
                state = lazyListState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(MaterialTheme.shapes.medium),
            ) {
                items(
                    items = items,
                    key = { it.id },
                ) { item ->
                    WhereToWatchCard(
                        model = item,
                        type = ImageType.Backdrop,
                        title = item.title,
                        onClick = { },
                        alignment = remember {
                            ParallaxAlignment(
                                horizontalBias = {
                                    val layoutInfo = lazyListState.layoutInfo
                                    val itemInfo = layoutInfo.visibleItemsInfo.first {
                                        it.key == item.id
                                    }

                                    val adjustedOffset =
                                        itemInfo.offset - layoutInfo.viewportStartOffset
                                    (adjustedOffset / itemInfo.size.toFloat()).coerceIn(-1f, 1f)
                                }
                            )
                        },
                        modifier = Modifier
                            .animateItemPlacement()
                            .width(260.dp) // TODO make it dynamic
                            .aspectRatio(aspectRatio)
                    )
                }
            }
        }
    }
}

// Get it from https://chrisbanes.me/posts/parallax-effect-compose/
@Stable
class ParallaxAlignment(
    private val horizontalBias: () -> Float = { 0f },
    private val verticalBias: () -> Float = { 0f },
) : Alignment {
    override fun align(
        size: IntSize,
        space: IntSize,
        layoutDirection: LayoutDirection,
    ): IntOffset {
        // Convert to Px first and only round at the end, to avoid rounding twice while calculating
        // the new positions
        val centerX = (space.width - size.width).toFloat() / 2f
        val centerY = (space.height - size.height).toFloat() / 2f
        val resolvedHorizontalBias = if (layoutDirection == LayoutDirection.Ltr) {
            horizontalBias()
        } else {
            -1 * horizontalBias()
        }

        val x = centerX * (1 + resolvedHorizontalBias)
        val y = centerY * (1 + verticalBias())
        return IntOffset(x.roundToInt(), y.roundToInt())
    }
}

@Composable
fun HomeListHeader(
    headerTitle: String,
    modifier: Modifier = Modifier,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
    content: (@Composable () -> Unit)? = null,
) {
    Row(
        verticalAlignment = alignment,
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        Text(
            text = headerTitle,
            style = MaterialTheme.typography.titleMedium
        )

        if (content != null) {
            Spacer(modifier = Modifier.weight(1f))
            content()
        }
    }
}
