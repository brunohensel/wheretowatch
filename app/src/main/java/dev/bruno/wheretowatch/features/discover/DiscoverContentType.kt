package dev.bruno.wheretowatch.features.discover

import dev.bruno.wheretowatch.services.discover.TrendWindow

sealed interface DiscoverContentType {
    data class Trending(val window: TrendWindow) : DiscoverContentType
    data object Popular : DiscoverContentType
    data object Action : DiscoverContentType
    data object Horror : DiscoverContentType
    data object Upcoming : DiscoverContentType
    data object TopRated : DiscoverContentType
    data object Netflix: DiscoverContentType
}
