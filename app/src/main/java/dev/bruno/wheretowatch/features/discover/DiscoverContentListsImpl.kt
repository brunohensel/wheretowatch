package dev.bruno.wheretowatch.features.discover

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.features.discover.movies.PopularMovieFlowSource
import dev.bruno.wheretowatch.features.discover.movies.StreamProviderMovieFlowSource
import dev.bruno.wheretowatch.features.discover.movies.TopRatedFlowSource
import dev.bruno.wheretowatch.features.discover.movies.TrendingMovieFlowSource
import dev.bruno.wheretowatch.features.discover.movies.UpcomingMovieFlowSource
import dev.bruno.wheretowatch.services.discover.MovieGenre
import dev.bruno.wheretowatch.services.discover.StreamerProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class DiscoverContentListsImpl @Inject constructor(
    private val trendingSource: TrendingMovieFlowSource,
    private val popularSource: PopularMovieFlowSource,
    private val upcomingSource: UpcomingMovieFlowSource,
    private val streamSource: StreamProviderMovieFlowSource,
    private val topRatedSource: TopRatedFlowSource
) : DiscoverPresenter.HomeContentLists {

    override val contents: DiscoverContentFlows
        get() = DiscoverContentFlows(
            trendingContent = trendingSource.flow,
            popularContent = popularSource.flow.toContentFlow(key = MovieGenre.ALL),
            actionContent = popularSource.flow.toContentFlow(key = MovieGenre.ACTION),
            horrorContent = popularSource.flow.toContentFlow(key = MovieGenre.HORROR),
            netflixContent = streamSource.flow.toContentFlow(key = StreamerProvider.NETFLIX),
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
            is DiscoverContentType.Trending -> trendingSource.getTrending(contentType.window)
            DiscoverContentType.Popular -> popularSource.getPopular()
            DiscoverContentType.Upcoming -> upcomingSource.getUpComing()
            DiscoverContentType.TopRated -> topRatedSource.getTopRated()
            DiscoverContentType.Action -> popularSource.getPopular(MovieGenre.ACTION)
            DiscoverContentType.Horror -> popularSource.getPopular(MovieGenre.HORROR)
            DiscoverContentType.Netflix -> streamSource.fetchProviderMovies(StreamerProvider.NETFLIX)
        }
    }
}
