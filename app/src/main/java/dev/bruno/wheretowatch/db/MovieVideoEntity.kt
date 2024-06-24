package dev.bruno.wheretowatch.db

import dev.bruno.wheretowatch.services.model.MovieVideo
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideoEntity(
    val id: String,
    val type: String,
    val key: String,
    val site: String,
    val official: Boolean,
    val publishedDate: LocalDate?,
) {
    constructor(model: MovieVideo) : this(
        id = model.id,
        type = model.type,
        key = model.key,
        site = model.site,
        official = model.official,
        publishedDate = model.publishedDate,
    )
}
