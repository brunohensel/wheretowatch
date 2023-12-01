package dev.bruno.wheretowatch.services.model

data class TrendingItem(
    val id: Int,
    val genreIds: List<Int>,
    val mediaType: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)