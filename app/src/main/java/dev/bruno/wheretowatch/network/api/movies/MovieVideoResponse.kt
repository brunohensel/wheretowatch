package dev.bruno.wheretowatch.network.api.movies

import dev.bruno.wheretowatch.services.discover.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideoResponse(val results: List<MovieVideoDto>)

@Serializable
data class MovieVideoDto(
    val id: String,
    val type: String,
    val key: String,
    val site: String,
    val official: Boolean,
    @Serializable(LocalDateSerializer::class)
    @SerialName("published_at")
    val date: LocalDate?,
)


