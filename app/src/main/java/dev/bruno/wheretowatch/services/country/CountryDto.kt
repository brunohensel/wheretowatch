package dev.bruno.wheretowatch.services.country

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    @SerialName("iso_3166_1")
    val code: String,
    @SerialName("english_name")
    val name: String,
)


