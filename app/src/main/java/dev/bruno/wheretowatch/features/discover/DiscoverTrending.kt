package dev.bruno.wheretowatch.features.discover

import androidx.compose.runtime.Immutable
import dev.bruno.wheretowatch.services.discover.TrendWindow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class DiscoverTrending(
    val trendWindow: TrendWindow = TrendWindow.DAY,
    val items: ImmutableList<DiscoverMovieItem> = persistentListOf(),
)
