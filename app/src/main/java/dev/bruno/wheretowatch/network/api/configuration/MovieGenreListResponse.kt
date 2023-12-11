package dev.bruno.wheretowatch.network.api.configuration

import kotlinx.serialization.Serializable

@Serializable
data class MovieGenreListResponse(val genres: List<GenreDto>)

@Serializable
data class GenreDto(val id: Int, val name: String)
