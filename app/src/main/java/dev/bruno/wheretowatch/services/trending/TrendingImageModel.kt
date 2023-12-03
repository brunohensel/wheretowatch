package dev.bruno.wheretowatch.services.trending

import dev.bruno.wheretowatch.ds.components.ImageType

data class TrendingImageModel(
    val backdropPath: String?,
    val posterPath: String?,
    val type: ImageType
)
