package dev.bruno.wheretowatch.features.home

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.features.home.movies.PopularMap
import dev.bruno.wheretowatch.features.home.movies.PopularMovieFlowSource
import dev.bruno.wheretowatch.features.home.movies.TopRatedFlowSource
import dev.bruno.wheretowatch.features.home.movies.TrendingMovieFlowSource
import dev.bruno.wheretowatch.features.home.movies.UpcomingMovieFlowSource
import dev.bruno.wheretowatch.services.discover.MovieGenre
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
    private val topRatedSource: TopRatedFlowSource
) : HomePresenter.HomeContentLists {

    override val contents: HomeContentFlows
        get() = HomeContentFlows(
            trendingContent = trendingSource.flow,
            popularContent = popularSource.flow.asGenreContent(MovieGenre.ALL),
            upcomingContent = upcomingSource.flow,
            topRatedContent = topRatedSource.flow,
        )

    private fun Flow<PopularMap>.asGenreContent(
        genre: MovieGenre
    ): Flow<ImmutableList<HomeMovieItem>> {
        return this.map { it.getOrDefault(genre, persistentListOf()) }
    }

    override suspend fun getContent(contentType: HomeContentType) {
        when (contentType) {
            is HomeContentType.Trending -> trendingSource.getTrending(contentType.window)
            HomeContentType.Popular -> popularSource.getPopular()
            HomeContentType.Upcoming -> upcomingSource.getUpComing()
            HomeContentType.TopRated -> topRatedSource.getTopRated()
        }
    }
}
