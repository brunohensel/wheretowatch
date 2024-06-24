package dev.bruno.wheretowatch.services.model

import kotlinx.datetime.LocalDate

data class MovieDetails(
    val movieId: Int = Int.MIN_VALUE,
    val homePage: String = "",
    val budget: Long = 0L,
    val revenue: Long = 0L,
    val runtime: Int = 0,
    val tagline: String = "",
    val collectionId: Int? = null,
    val videos: List<MovieVideo> = emptyList(),
)

data class MovieWithDetails(
    val id: Int = Int.MIN_VALUE,
    val title: String = "",
    val overview: String = "",
    val originalTitle: String = "",
    val popularity: Float = 0f,
    val voteCount: Int = 0,
    val voteAverage: Float = 0f,
    val genresIds: List<Int> = emptyList(),
    val releaseDate: LocalDate? = null,
    val homePage: String = "",
    val budget: Long = 0L,
    val revenue: Long = 0L,
    val runtime: Int = 0,
    val tagline: String = "",
    val collectionId: Int? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val videos: List<MovieVideo> = emptyList(),
)
