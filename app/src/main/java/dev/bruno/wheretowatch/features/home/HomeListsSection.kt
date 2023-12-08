package dev.bruno.wheretowatch.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import kotlinx.collections.immutable.ImmutableList

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
