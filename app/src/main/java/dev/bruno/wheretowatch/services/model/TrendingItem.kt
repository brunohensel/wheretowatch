package dev.bruno.wheretowatch.services.model

import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.services.trending.TrendingImageModel

data class TrendingItem(
    val id: Int,
    val genreIds: List<Int>,
    val mediaType: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
) : CurryModel<TrendingImageModel> {
    override fun curried(): (ImageType) -> TrendingImageModel = { type ->
        TrendingImageModel(backdropPath, posterPath, type)
    }
}
