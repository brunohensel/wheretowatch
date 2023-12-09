package dev.bruno.wheretowatch.features.home

import dev.bruno.wheretowatch.services.discover.TrendWindow

sealed interface HomeContentType {
    data class Trending(val window: TrendWindow) : HomeContentType
    data object Popular : HomeContentType
    data object Action : HomeContentType
    data object Horror : HomeContentType
    data object Upcoming : HomeContentType
    data object TopRated : HomeContentType
    data object Netflix: HomeContentType
}
