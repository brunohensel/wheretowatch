package dev.bruno.wheretowatch.services.trending

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingResultDto(
    val page: Int,
    val results: List<TrendingDto>
)

@Serializable
data class TrendingDto(
    val adult: Boolean,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    val id: Int,
    @SerialName("media_type")
    val mediaType: String,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String? = null,
    val overview: String,
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
)
