package dev.bruno.wheretowatch.services.model

import kotlinx.datetime.LocalDate

data class Movie(
    val id: Int = Int.MIN_VALUE,
    val title: String = "",
    val overview: String = "",
    val originalTitle: String = "",
    val popularity: Float = 0f,
    val voteCount: Int = 0,
    val voteAverage: Float = 0f,
    val genresIds: List<Int> = emptyList(),
    val releaseDate: LocalDate? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val collectionId: Int? = null,
)
