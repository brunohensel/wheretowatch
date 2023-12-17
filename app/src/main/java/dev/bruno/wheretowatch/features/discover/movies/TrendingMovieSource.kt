package dev.bruno.wheretowatch.features.discover.movies

import dev.bruno.wheretowatch.features.discover.DiscoverContent
import dev.bruno.wheretowatch.features.discover.DiscoverMovieItem
import dev.bruno.wheretowatch.features.discover.DiscoverTrending
import dev.bruno.wheretowatch.services.discover.DiscoverCategory
import dev.bruno.wheretowatch.services.discover.DiscoverContentSupplier
import dev.bruno.wheretowatch.services.discover.TrendWindow
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class TrendingMovieSource @Inject constructor(
    private val supplier: DiscoverContentSupplier,
) {
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
