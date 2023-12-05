package dev.bruno.wheretowatch.services.model

data class DiscoverContent(
    val id: Int,
    val title: String,
    val popularity: Float,
    val genresIds: List<Int>,
    val originalTitle: String,
    val originalLanguage: String,
    val voteCount: Int,
    val voteAverage: Float,
    val posterPath: String?,
    val backdropPath: String?,
)
