package dev.bruno.wheretowatch.features.discover

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Action
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.HarryPotterCollection
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Horror
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Netflix
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Popular
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.TopRated
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Trending
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Upcoming
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.War
import dev.bruno.wheretowatch.features.discover.movies.CollectionMovieFlowSource
import dev.bruno.wheretowatch.features.discover.movies.PopularMovieFlowSource
import dev.bruno.wheretowatch.features.discover.movies.StreamProviderMovieFlowSource
import dev.bruno.wheretowatch.features.discover.movies.TopRatedFlowSource
import dev.bruno.wheretowatch.features.discover.movies.TrendingMovieFlowSource
import dev.bruno.wheretowatch.features.discover.movies.UpcomingMovieFlowSource
import dev.bruno.wheretowatch.services.discover.MovieCollection.HARRY_POTTER
import dev.bruno.wheretowatch.services.discover.MovieGenre
import dev.bruno.wheretowatch.services.discover.StreamerProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class DiscoverContentListsImpl @Inject constructor(
    private val trendingSource: TrendingMovieFlowSource,
    private val popularSource: PopularMovieFlowSource,
    private val upcomingSource: UpcomingMovieFlowSource,
    private val streamSource: StreamProviderMovieFlowSource,
    private val topRatedSource: TopRatedFlowSource,
    private val collectionSource: CollectionMovieFlowSource,
) : DiscoverPresenter.HomeContentLists {

    private val feedMap = ConcurrentHashMap<DiscoverSections, DiscoverContent>()
    private val feedState = MutableStateFlow(DiscoverFeed(mapOf()))

    override val feedFlow: Flow<DiscoverFeed>
        get() = feedState.asStateFlow()

    override val contents: DiscoverContentFlows
        get() = DiscoverContentFlows(
            trendingContent = trendingSource.flow,
            popularContent = popularSource.flow.toContentFlow(key = MovieGenre.ALL),
            actionContent = popularSource.flow.toContentFlow(key = MovieGenre.ACTION),
            horrorContent = popularSource.flow.toContentFlow(key = MovieGenre.HORROR),
            warContent = popularSource.flow.toContentFlow(key = MovieGenre.WAR),
            netflixContent = streamSource.flow.toContentFlow(key = StreamerProvider.NETFLIX),
            harryPotterContent = collectionSource.flow.toContentFlow(key = HARRY_POTTER),
            upcomingContent = upcomingSource.flow,
            topRatedContent = topRatedSource.flow,
        )

    private fun <T, K> Flow<T>.toContentFlow(
        key: K,
    ): Flow<ImmutableList<DiscoverMovieItem>> where T : Map<K, ImmutableList<DiscoverMovieItem>> {
        return this.map { it.getOrDefault(key, persistentListOf()) }
    }

    override suspend fun getContent(contentType: DiscoverContentType) {
        when (contentType) {
//            is Trending -> trendingSource.getTrending(contentType.window)
//            Popular -> popularSource.getPopular()
//            Upcoming -> upcomingSource.getUpComing()
//            TopRated -> topRatedSource.getTopRated()
//            Action -> popularSource.getPopular(MovieGenre.ACTION)
//            Horror -> popularSource.getPopular(MovieGenre.HORROR)
//            Netflix -> streamSource.fetchProviderMovies(StreamerProvider.NETFLIX)
//            War -> popularSource.getPopular(MovieGenre.WAR)
//            HarryPotterCollection -> collectionSource.getCollection(HARRY_POTTER)
            is Trending -> {
                val sourceContent = trendingSource.get(contentType.window)
                val stateContent = feedMap[DiscoverSections.Trending] as? DiscoverTrending

                if (stateContent?.trendWindow != contentType.window) {
                    feedMap[DiscoverSections.Trending] =
                        DiscoverTrending(contentType.window, sourceContent.items)
                }
                feedState.update { it.copy(section = feedMap.toMap()) }
            }

            Popular -> popularSource.get().updateMapState(DiscoverSections.Popular)
            Upcoming -> upcomingSource.getUpComingV2().updateMapState(DiscoverSections.Upcoming)
            TopRated -> topRatedSource.getTopRatedV2().updateMapState(DiscoverSections.TopRated)
            Action -> popularSource.get(MovieGenre.ACTION).updateMapState(DiscoverSections.Action)
            Horror -> popularSource.get(MovieGenre.HORROR).updateMapState(DiscoverSections.Horror)
            Netflix -> streamSource.fetchProvider(StreamerProvider.NETFLIX).updateMapState(DiscoverSections.Netflix)
            War -> popularSource.get(MovieGenre.WAR).updateMapState(DiscoverSections.War)
            HarryPotterCollection -> collectionSource.get(HARRY_POTTER).updateMapState(DiscoverSections.HarryPotter)
        }
    }

    private fun DiscoverContent.updateMapState(sections: DiscoverSections) {
        feedMap.putIfAbsent(sections, this)
        feedState.update { it.copy(section = feedMap.toMap()) }
    }
}
