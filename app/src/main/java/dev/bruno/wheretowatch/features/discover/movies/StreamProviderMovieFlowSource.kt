package dev.bruno.wheretowatch.features.discover.movies

import dev.bruno.wheretowatch.features.discover.ContentList
import dev.bruno.wheretowatch.features.discover.DiscoverContent
import dev.bruno.wheretowatch.features.discover.DiscoverMovieItem
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

typealias StreamMovieMap = Map<StreamerProvider, ImmutableList<DiscoverMovieItem>>

class StreamProviderMovieFlowSource @Inject constructor(
    private val supplier: DiscoverContentSupplier,
) {
    private val providerMovie =
        ConcurrentHashMap<StreamerProvider, ImmutableList<DiscoverMovieItem>>()
    private val state = MutableStateFlow<StreamMovieMap>(mapOf())
    val flow: Flow<StreamMovieMap> = state.asStateFlow()

    suspend fun fetchProviderMovies(provider: StreamerProvider) {
        val streamItems = supplier.get(DiscoverCategory.Streaming(provider)).map { item ->
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

        if (!providerMovie.containsKey(provider)) {
            // Maybe it is redundant to call `putIfAbsent` at this point
            providerMovie.putIfAbsent(provider, streamItems)
        }

        state.update { providerMovie }
    }

    suspend fun fetchProvider(provider: StreamerProvider): DiscoverContent {
        val items = supplier.get(DiscoverCategory.Streaming(provider)).map { item ->
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
