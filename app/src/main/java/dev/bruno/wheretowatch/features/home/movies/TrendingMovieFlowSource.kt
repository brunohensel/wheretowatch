package dev.bruno.wheretowatch.features.home.movies

import dev.bruno.wheretowatch.features.home.HomeMovieItem
import dev.bruno.wheretowatch.features.home.HomeTrending
import dev.bruno.wheretowatch.services.discover.DiscoverCategory
import dev.bruno.wheretowatch.services.discover.DiscoverContentSupplier
import dev.bruno.wheretowatch.services.discover.TrendWindow
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TrendingMovieFlowSource @Inject constructor(
    private val supplier: DiscoverContentSupplier,
) {
    private val state = MutableStateFlow(HomeTrending())
    val flow: Flow<HomeTrending> = state.asStateFlow()

    suspend fun getTrending(window: TrendWindow) {
        val trendingItem = supplier.get(DiscoverCategory.Trending(window)).map { item ->
            HomeMovieItem(
                id = item.id,
                title = item.title,
                originalTitle = item.originalTitle,
                popularity = item.popularity,
                voteAverage = item.voteAverage,
                voteCount = item.voteCount,
                buildImgModel = item.curried()
            )
        }.toImmutableList()

        state.update { HomeTrending(trendWindow = window, items = trendingItem) }
    }
}
