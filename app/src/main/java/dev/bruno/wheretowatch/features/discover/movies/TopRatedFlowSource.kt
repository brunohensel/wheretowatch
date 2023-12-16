package dev.bruno.wheretowatch.features.discover.movies

import dev.bruno.wheretowatch.features.discover.DiscoverMovieItem
import dev.bruno.wheretowatch.services.discover.DiscoverCategory
import dev.bruno.wheretowatch.services.discover.DiscoverContentSupplier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TopRatedFlowSource @Inject constructor(
    private val supplier: DiscoverContentSupplier,
) {
    private val state = MutableStateFlow<ImmutableList<DiscoverMovieItem>>(persistentListOf())
    val flow: Flow<ImmutableList<DiscoverMovieItem>> = state.asStateFlow()

    suspend fun getTopRated() {
        val popularItems = supplier.get(DiscoverCategory.TopRated).map { item ->
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

        state.update { popularItems }
    }
}
