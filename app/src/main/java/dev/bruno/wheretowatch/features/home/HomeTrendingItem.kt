package dev.bruno.wheretowatch.features.home

import androidx.compose.runtime.Immutable
import dev.bruno.wheretowatch.ds.components.ImageModelBuilder
import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.services.trending.TrendingImageModel

@Immutable
data class HomeTrendingItem(
    val id: Int,
    val mediaType: String,
    val originalLanguage: String,
    val originalTitle: String,
    val popularity: Double,
    val voteAverage: Double,
    val voteCount: Int,
    override inline val buildImgModel: (type: ImageType) -> TrendingImageModel,
) : ImageModelBuilder<TrendingImageModel>
