package dev.bruno.wheretowatch.features.discover.movies

import dev.bruno.wheretowatch.features.discover.ContentList
import dev.bruno.wheretowatch.features.discover.DiscoverContent
import dev.bruno.wheretowatch.features.discover.DiscoverMovieItem
import dev.bruno.wheretowatch.services.discover.DiscoverCategory
import dev.bruno.wheretowatch.services.discover.DiscoverContentSupplier
import dev.bruno.wheretowatch.services.discover.MovieGenre
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class PopularMovieSource @Inject constructor(
    private val supplier: DiscoverContentSupplier,
) {

    suspend fun get(genre: MovieGenre = MovieGenre.ALL): DiscoverContent {
        val items = supplier.get(DiscoverCategory.Popular(genre)).map { item ->
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

        return ContentList(items)
    }
}
