package dev.bruno.wheretowatch.services.discover

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscoverContentResultDto(val results: List<DiscoverContentDto> = emptyList())

@Serializable
data class DiscoverContentDto(
    val id: Int,
    val title: String,
    val popularity: Float,
    @SerialName("genre_ids") val genresIds: List<Int>,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("vote_count") val voteCount: Int,
    @SerialName("vote_average") val voteAverage: Float,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
)
