package dev.bruno.wheretowatch.services.discover

sealed interface DiscoverCategory {
    data class Popular(val genre: MovieGenre) : DiscoverCategory
    data object Upcoming : DiscoverCategory
    data object TopRated : DiscoverCategory
    data class Trending(val trendWindow: TrendWindow) : DiscoverCategory
    data class Streaming(val provider: StreamerProvider) : DiscoverCategory
}

enum class TrendWindow(val key: String) { DAY("day"), WEEK("week") }

//TODO fetch it from genre/movie/list and store?
enum class MovieGenre(val id: String) {
    ALL("none"),
    ACTION("28"),
    ADVENTURE("12"),
    ANIMATION("16"),
    COMEDY("35"),
    CRIME("80"),
    DOCUMENTARY("99"),
    DRAMA("18"),
    FAMILY("10751"),
    FANTASY("14"),
    HISTORY("36"),
    HORROR("27"),
    MUSIC("10402"),
    MYSTERY("9648"),
    ROMANCE("10749"),
    SCIENCE_FICTION("878"),
    TV_MOVIE("10770"),
    THRILLER("53"),
    WAR("10752"),
    WESTERN("37"),
}

// TODO fetch it from watch/providers/movie to get provider's Logo
enum class StreamerProvider(val id: String) {
    NETFLIX(id = "8"),
    AMAZON_PRIME(id = "9"),
    DISNEY_PLUS(id = "337"),
    APPLE_TV_PLUS(id = "350"),
}
