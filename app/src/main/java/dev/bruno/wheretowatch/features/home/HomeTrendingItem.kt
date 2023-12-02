package dev.bruno.wheretowatch.features.home

import androidx.compose.runtime.Immutable

@Immutable
data class HomeTrendingItem(
    val id: Int,
    val mediaType: String,
    val originalLanguage: String,
    val originalTitle: String,
    val popularity: Double,
    val posterPath: String,
    val voteAverage: Double,
    val voteCount: Int,
)
