package dev.bruno.wheretowatch.features.discover

sealed interface DiscoverContentType {
    data object Popular : DiscoverContentType
    data object Action : DiscoverContentType
    data object Horror : DiscoverContentType
    data object Upcoming : DiscoverContentType
    data object Netflix : DiscoverContentType
    data object War : DiscoverContentType
    data object Romance : DiscoverContentType
    data object Thriller : DiscoverContentType
    data object History : DiscoverContentType
    data object Comedy : DiscoverContentType
    data object Drama : DiscoverContentType
    data object Fantasy : DiscoverContentType
    data object Family : DiscoverContentType
    data object Music : DiscoverContentType
    data object Documentary : DiscoverContentType
    data object Crime : DiscoverContentType
    data object HarryPotterCollection : DiscoverContentType
}
