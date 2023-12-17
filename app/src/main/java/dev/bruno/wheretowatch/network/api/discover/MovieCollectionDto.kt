package dev.bruno.wheretowatch.network.api.discover

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCollectionDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("parts") val parts: List<DiscoverContentDto>,
)
