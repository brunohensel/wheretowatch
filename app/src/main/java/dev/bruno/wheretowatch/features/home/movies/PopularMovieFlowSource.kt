package dev.bruno.wheretowatch.features.home.movies

import dev.bruno.wheretowatch.features.home.HomeMovieItem
import dev.bruno.wheretowatch.services.discover.DiscoverCategory
import dev.bruno.wheretowatch.services.discover.DiscoverContentSupplier
import dev.bruno.wheretowatch.services.discover.MovieGenre
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class PopularMovieFlowSource @Inject constructor(
    private val supplier: DiscoverContentSupplier,
) {

    private val state = MutableStateFlow<ImmutableList<HomeMovieItem>>(persistentListOf())
    val flow: Flow<ImmutableList<HomeMovieItem>> = state.asStateFlow()

    suspend fun getPopular(genre: MovieGenre = MovieGenre.NONE) {
        val popularItems = supplier.get(DiscoverCategory.Popular(genre)).map { item ->
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

        state.update { popularItems }
    }
}
