package dev.bruno.wheretowatch.features.discover.movies

import dev.bruno.wheretowatch.features.discover.DiscoverContent
import dev.bruno.wheretowatch.features.discover.DiscoverMovieItem
import dev.bruno.wheretowatch.features.discover.DiscoverTrending
import dev.bruno.wheretowatch.services.discover.DiscoverCategory
import dev.bruno.wheretowatch.services.discover.DiscoverContentSupplier
import dev.bruno.wheretowatch.services.discover.TrendWindow
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class TrendingMovieFlowSource @Inject constructor(
    private val supplier: DiscoverContentSupplier,
) {
    private val state = MutableStateFlow(DiscoverTrending())
    val flow: Flow<DiscoverTrending> = state.asStateFlow()

    suspend fun get(window: TrendWindow): DiscoverContent {
        val trendingItem = supplier.get(DiscoverCategory.Trending(window)).map { item ->
            DiscoverMovieItem(
                id = item.id,
                title = item.title,
                originalTitle = item.originalTitle,
                popularity = item.popularity,
                voteAverage = item.voteAverage,
                voteCount = item.voteCount,
                buildImgModel = item.curried()
            )
        }.toImmutableList()

        return DiscoverTrending(trendWindow = window, items = trendingItem)
    }
}
