package dev.bruno.wheretowatch.features.home.trending

import dev.bruno.wheretowatch.features.home.HomeTrendingItem
import dev.bruno.wheretowatch.features.home.asImageModel
import dev.bruno.wheretowatch.services.trending.TrendingSupplier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TrendingFlowSource @Inject constructor(
    private val trendingSupplier: TrendingSupplier,
) {

    private val state = MutableStateFlow<ImmutableList<HomeTrendingItem>>(persistentListOf())

    val flow: Flow<ImmutableList<HomeTrendingItem>> = state.asStateFlow()
    suspend fun getTrending(window: TrendingSupplier.TrendWindow) {
        val trendingItem = trendingSupplier.get(window).map { item ->
            HomeTrendingItem(
                id = item.id,
                mediaType = item.mediaType,
                originalLanguage = item.originalLanguage,
                originalTitle = item.originalTitle,
                popularity = item.popularity,
                voteAverage = item.voteAverage,
                voteCount = item.voteCount,
                buildImgModel = asImageModel(item.backdropPath, item.posterPath)
            )
        }.toImmutableList()

        state.update { trendingItem }
    }
}
