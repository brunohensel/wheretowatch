package dev.bruno.wheretowatch.features.home

import androidx.compose.runtime.Immutable
import dev.bruno.wheretowatch.services.images.ImageUrlResolver.Type
import dev.bruno.wheretowatch.services.trending.TrendingImageModel

@Immutable
data class HomeTrendingItem(
    val id: Int,
    val mediaType: String,
    val originalLanguage: String,
    val originalTitle: String,
    val popularity: Double,
    inline val buildImgModel: (type: Type) -> TrendingImageModel,
    val voteAverage: Double,
    val voteCount: Int,
)

@Suppress("NOTHING_TO_INLINE")
inline fun asImageModel(path1: String?, path2: String?): (Type) -> TrendingImageModel = { type ->
    when (type) {
        Type.Backdrop -> TrendingImageModel(path1, Type.Backdrop)
        Type.Poster -> TrendingImageModel(path2, Type.Poster)
    }
}
