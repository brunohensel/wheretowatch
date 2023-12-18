package dev.bruno.wheretowatch.services.streamproviders

import dev.bruno.wheretowatch.ds.components.ImageType

data class StreamProviderImageModel(
    val logoPath: String,
    val type: ImageType,
)
