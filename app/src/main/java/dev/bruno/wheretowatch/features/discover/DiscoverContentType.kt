package dev.bruno.wheretowatch.features.discover

sealed interface DiscoverContentType {
    data object Popular : DiscoverContentType
    data object Action : DiscoverContentType
    data object Horror : DiscoverContentType
    data object Upcoming : DiscoverContentType
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
}

sealed interface CollectionContent : DiscoverContentType {
    data object HarryPotter : CollectionContent
    data object HungerGames : CollectionContent
    data object Avengers : CollectionContent
    data object LordOfTheRings : CollectionContent
}

sealed interface StreamContent : DiscoverContentType {
    data object Netflix : StreamContent
    data object AmazonPrime : StreamContent
    data object DisneyPlus : StreamContent
    data object AppleTvPlus : StreamContent
}
