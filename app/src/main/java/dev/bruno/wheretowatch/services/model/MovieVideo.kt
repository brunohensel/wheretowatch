package dev.bruno.wheretowatch.services.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideo(
    val id: String,
    val key: String,
    val site: String,
    val official: Boolean,
    val publishedDate: LocalDate?,
)
