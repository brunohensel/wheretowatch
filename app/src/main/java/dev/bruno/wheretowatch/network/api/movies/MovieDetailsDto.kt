package dev.bruno.wheretowatch.network.api.movies

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
    @SerialName("id")
    val movieId: Int = Int.MIN_VALUE,
    val homePage: String = "",
    val budget: Long = 0L,
    val revenue: Long = 0L,
    val runtime: Int = 0,
    val tagline: String = "",
    @SerialName("belongs_to_collection")
    val collection: MovieDetailsCollection? = null,
)

@Serializable
data class MovieDetailsCollection(
    val id: Int,
)
