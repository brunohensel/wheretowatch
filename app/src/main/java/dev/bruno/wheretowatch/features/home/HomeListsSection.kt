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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.ds.components.WhereToWatchCard
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalBackdropCarousel(
    items: ImmutableList<HomeMovieItem>,
    headerTitle: String,
    aspectRatio: Float,
    onClick: (id: Int) -> Unit,
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
                    .clip(MaterialTheme.shapes.large),
            ) {
                items(
                    items = items,
                    key = { it.id },
                ) { item ->
                    WhereToWatchCard(
                        model = item,
                        type = ImageType.Backdrop,
                        title = item.title,
                        onClick = { onClick(item.id) },
                        modifier = Modifier
                            .animateItemPlacement()
                            .width(240.dp) // TODO make it dynamic
                            .aspectRatio(aspectRatio)
                    )
                }
            }
        }
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
