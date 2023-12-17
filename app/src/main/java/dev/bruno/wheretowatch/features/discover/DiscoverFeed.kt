package dev.bruno.wheretowatch.features.discover

import kotlinx.collections.immutable.ImmutableList

data class DiscoverFeed(val section: Map<DiscoverSections, DiscoverContent> = mapOf())

sealed class DiscoverSections(val order: Int) {
    data object Trending : DiscoverSections(order = 5)
    data object Popular : DiscoverSections(order = 0)
    data object Action : DiscoverSections(order = 3)
    data object Horror : DiscoverSections(order = 4)
    data object War : DiscoverSections(order = 7)
    data object Netflix : DiscoverSections(order = 6)
    data object HarryPotter : DiscoverSections(order = 8)
    data object Upcoming : DiscoverSections(order = 1)
    data object TopRated : DiscoverSections(order = 2)
}

sealed interface DiscoverContent {
    val items: ImmutableList<DiscoverMovieItem>
}
data class ContentList(override val items: ImmutableList<DiscoverMovieItem>) : DiscoverContent

