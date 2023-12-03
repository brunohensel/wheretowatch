package dev.bruno.wheretowatch.features.home

import androidx.compose.runtime.Immutable
import dev.bruno.wheretowatch.ds.components.ImageModelBuilder
import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.services.trending.TrendingImageModel
import dev.bruno.wheretowatch.services.trending.TrendingSupplier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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

@Immutable
data class HomeTrending(
    val trendWindow: TrendingSupplier.TrendWindow = TrendingSupplier.TrendWindow.DAY,
    val items: ImmutableList<HomeTrendingItem> = persistentListOf(),
)
