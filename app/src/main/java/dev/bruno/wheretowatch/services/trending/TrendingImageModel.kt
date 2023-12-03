package dev.bruno.wheretowatch.services.trending

import dev.bruno.wheretowatch.services.images.ImageUrlResolver

data class TrendingImageModel(
    val path: String?,
    val type: ImageUrlResolver.Type
)
