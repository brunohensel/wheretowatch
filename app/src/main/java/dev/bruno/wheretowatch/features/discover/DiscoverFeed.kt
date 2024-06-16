package dev.bruno.wheretowatch.features.discover

import kotlinx.collections.immutable.ImmutableList

data class DiscoverFeed(val section: Map<DiscoverSections, DiscoverContent> = mapOf())

// The order matters, the [ordinal] Int is used to draw sections on the ui
enum class DiscoverSections {
    Popular,
    Upcoming,
    Action,
    Horror,
    Netflix,
    War,
    HarryPotter,
    Romance,
    Thriller,
    History,
    Comedy,
    Drama,
    Fantasy,
    Family,
    Music,
    Documentary,
    Crime,
}

sealed interface DiscoverContent {
    val items: ImmutableList<DiscoverMovieItem>
}

data class ContentList(override val items: ImmutableList<DiscoverMovieItem>) : DiscoverContent

