package dev.bruno.wheretowatch.features.discover

sealed interface DiscoverContentType {
    data object Popular : DiscoverContentType
    data object Action : DiscoverContentType
    data object Horror : DiscoverContentType
    data object Upcoming : DiscoverContentType
    data object TopRated : DiscoverContentType
    data object Netflix : DiscoverContentType
    data object War : DiscoverContentType
    data object HarryPotterCollection : DiscoverContentType
}
