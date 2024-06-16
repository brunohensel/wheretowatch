package dev.bruno.wheretowatch.services.discover

sealed interface DiscoverCategory {
    data class Popular(val genre: MovieGenre) : DiscoverCategory
    data object Upcoming : DiscoverCategory
    data class Streaming(val provider: StreamerProvider) : DiscoverCategory
    data class Collection(val collection: MovieCollection) : DiscoverCategory
}


//TODO fetch it from genre/movie/list and store?
enum class MovieGenre(val id: String) {
    ALL("-1"),
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

// Complete list available at https://developer.themoviedb.org/docs/daily-id-exports
enum class MovieCollection(val id: Int) {
    HARRY_POTTER(id = 1241),
    HUNGER_GAMES(id = 131635),
    AVENGERS(id = 86311),
    LORD_OF_RINGS(id = 119)
}
