package dev.bruno.wheretowatch.features.home

import androidx.compose.runtime.Immutable
import dev.bruno.wheretowatch.services.discover.TrendWindow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class HomeTrending(
    val trendWindow: TrendWindow = TrendWindow.DAY,
    val items: ImmutableList<HomeMovieItem> = persistentListOf(),
)
