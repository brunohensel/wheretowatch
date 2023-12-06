package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.ds.components.ImageType

data class DiscoveryImageModel(
    val backdropPath: String?,
    val posterPath: String?,
    val type: ImageType
)
