package dev.bruno.wheretowatch.features.home

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.features.home.movies.PopularMovieFlowSource
import dev.bruno.wheretowatch.features.home.movies.StreamProviderMovieFlowSource
import dev.bruno.wheretowatch.features.home.movies.TopRatedFlowSource
import dev.bruno.wheretowatch.features.home.movies.TrendingMovieFlowSource
import dev.bruno.wheretowatch.features.home.movies.UpcomingMovieFlowSource
import dev.bruno.wheretowatch.services.discover.MovieGenre
import dev.bruno.wheretowatch.services.discover.StreamerProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class HomeContentListsImpl @Inject constructor(
    private val trendingSource: TrendingMovieFlowSource,
    private val popularSource: PopularMovieFlowSource,
    private val upcomingSource: UpcomingMovieFlowSource,
    private val streamSource: StreamProviderMovieFlowSource,
    private val topRatedSource: TopRatedFlowSource
) : HomePresenter.HomeContentLists {

    override val contents: HomeContentFlows
        get() = HomeContentFlows(
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
    ): Flow<ImmutableList<HomeMovieItem>> where T : Map<K, ImmutableList<HomeMovieItem>> {
        return this.map { it.getOrDefault(key, persistentListOf()) }
    }

    override suspend fun getContent(contentType: HomeContentType) {
        when (contentType) {
            is HomeContentType.Trending -> trendingSource.getTrending(contentType.window)
            HomeContentType.Popular -> popularSource.getPopular()
            HomeContentType.Upcoming -> upcomingSource.getUpComing()
            HomeContentType.TopRated -> topRatedSource.getTopRated()
            HomeContentType.Action -> popularSource.getPopular(MovieGenre.ACTION)
            HomeContentType.Horror -> popularSource.getPopular(MovieGenre.HORROR)
            HomeContentType.Netflix -> streamSource.fetchProviderMovies(StreamerProvider.NETFLIX)
        }
    }
}
