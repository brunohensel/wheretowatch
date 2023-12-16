package dev.bruno.wheretowatch.features.discover.movies

import dev.bruno.wheretowatch.features.discover.HomeMovieItem
import dev.bruno.wheretowatch.services.discover.DiscoverCategory
import dev.bruno.wheretowatch.services.discover.DiscoverContentSupplier
import dev.bruno.wheretowatch.services.discover.StreamerProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

typealias StreamMovieMap = Map<StreamerProvider, ImmutableList<HomeMovieItem>>

class StreamProviderMovieFlowSource @Inject constructor(
    private val supplier: DiscoverContentSupplier,
) {
    private val providerMovie = ConcurrentHashMap<StreamerProvider, ImmutableList<HomeMovieItem>>()
    private val state = MutableStateFlow<StreamMovieMap>(mapOf())
    val flow: Flow<StreamMovieMap> = state.asStateFlow()

    suspend fun fetchProviderMovies(provider: StreamerProvider) {
        val streamItems = supplier.get(DiscoverCategory.Streaming(provider)).map { item ->
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

        if (!providerMovie.containsKey(provider)) {
            // Maybe it is redundant to call `putIfAbsent` at this point
            providerMovie.putIfAbsent(provider, streamItems)
        }

        state.update { providerMovie }
    }
}
