package dev.bruno.wheretowatch.services.discover

sealed interface DiscoverCategory {
    data class Popular(val genre: MovieGenre) : DiscoverCategory
    data object Upcoming : DiscoverCategory
    data object TopRated : DiscoverCategory
    data class Trending(val trendWindow: TrendWindow) : DiscoverCategory
}

enum class TrendWindow(val key: String) { DAY("day"), WEEK("week") }

enum class MovieGenre(val key: String) {
    NONE("none"),
    ACTION("Action"),
    ADVENTURE("Adventure"),
    ANIMATION("Animation"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DOCUMENTARY("Documentary"),
    DRAMA("Drama"),
    FAMILY("Family"),
    FANTASY("Fantasy"),
    HISTORY("History"),
    HORROR("Horror"),
    MUSIC("Music"),
    MYSTERY("Mystery"),
    ROMANCE("Romance"),
    SCIENCE_FICTION("Science Fiction"),
    TV_MOVIE("TV Movie"),
    THRILLER("Thriller"),
    WAR("War"),
    WESTERN("Western"),
}
