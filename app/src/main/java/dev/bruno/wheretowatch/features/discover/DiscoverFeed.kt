package dev.bruno.wheretowatch.features.discover

import kotlinx.collections.immutable.ImmutableList

data class DiscoverFeed(val section: Map<DiscoverSections, DiscoverContent> = mapOf())

sealed interface DiscoverSections {
    data object Trending : DiscoverSections
    data object Popular : DiscoverSections
    data object Action : DiscoverSections
    data object Horror : DiscoverSections
    data object War : DiscoverSections
    data object Netflix : DiscoverSections
    data object HarryPotter : DiscoverSections
    data object Upcoming : DiscoverSections
    data object TopRated : DiscoverSections
}

sealed interface DiscoverContent {
    val items: ImmutableList<DiscoverMovieItem>
}

data class ContentList(override val items: ImmutableList<DiscoverMovieItem>) : DiscoverContent
