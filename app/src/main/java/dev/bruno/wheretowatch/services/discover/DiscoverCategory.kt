package dev.bruno.wheretowatch.services.discover

sealed interface DiscoverCategory {
    data object Popular : DiscoverCategory
    data object Upcoming : DiscoverCategory
    data object TopRated : DiscoverCategory
    data class Trending(val trendWindow: TrendWindow) : DiscoverCategory
}

enum class TrendWindow(val key: String) { DAY("day"), WEEK("week") }
