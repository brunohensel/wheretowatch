package dev.bruno.wheretowatch.features.discover

import kotlinx.collections.immutable.ImmutableList

data class DiscoverFeed(val section: Map<DiscoverSections, DiscoverContent> = mapOf())

// The order matters, the [ordinal] Int is used to draw sections on the ui
enum class DiscoverSections {
    Popular,
    Upcoming,
    Action,
    Avengers,
    Horror,
    Netflix,
    War,
    HarryPotter,
    Romance,
    Thriller,
    DisneyPlus,
    History,
    HungerGames,
    Comedy,
    AmazonPrime,
    Drama,
    Fantasy,
    LordOfRings,
    Family,
    Music,
    Documentary,
    AppleTvPlus,
    Crime,
    ;

    fun getSpacedName(): String {
        return this.name.split(Regex("(?=[A-Z])")).joinToString(" ")
    }
}

sealed interface DiscoverContent {
    val items: ImmutableList<DiscoverMovieItem>
}

data class ContentList(override val items: ImmutableList<DiscoverMovieItem>) : DiscoverContent

