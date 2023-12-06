package dev.bruno.wheretowatch.services.model

import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.services.discover.DiscoveryImageModel

data class DiscoverContent(
    val id: Int,
    val title: String,
    val popularity: Float,
    val genresIds: List<Int>,
    val originalTitle: String,
    val originalLanguage: String,
    val voteCount: Int,
    val voteAverage: Float,
    val posterPath: String?,
    val backdropPath: String?,
) : CurryModel<DiscoveryImageModel> {
    override fun curried(): (ImageType) -> DiscoveryImageModel = { type ->
        DiscoveryImageModel(backdropPath, posterPath, type)
    }
}
