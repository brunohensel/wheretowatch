package dev.bruno.wheretowatch.features.home

import androidx.compose.runtime.Immutable
import dev.bruno.wheretowatch.ds.components.ImageModelBuilder
import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.services.discover.DiscoveryImageModel

@Immutable
data class HomeMovieItem(
    val id: Int,
    val title: String,
    val popularity: Float,
    val originalTitle: String,
    val voteCount: Int,
    val voteAverage: Float,
    override inline val buildImgModel: (type: ImageType) -> DiscoveryImageModel,
) : ImageModelBuilder<DiscoveryImageModel>
