package dev.bruno.wheretowatch.features.home

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.features.home.movies.PopularMovieFlowSource
import dev.bruno.wheretowatch.features.home.movies.UpComingMovieFlowSource
import dev.bruno.wheretowatch.features.home.trending.TrendingFlowSource
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class HomeContentListsImpl @Inject constructor(
    private val trendingSource: TrendingFlowSource,
    private val popularSource: PopularMovieFlowSource,
    private val upComingSource: UpComingMovieFlowSource,
) : HomePresenter.HomeContentLists {

    override val contents: HomeContentFlows
        get() = HomeContentFlows(
            trendingContent = trendingSource.flow,
            popularContent = popularSource.flow,
            upComingContent = upComingSource.flow,
        )

    override suspend fun getContent(contentType: HomeContentType) {
        when (contentType) {
            is HomeContentType.Trending -> trendingSource.getTrending(contentType.window)
            HomeContentType.Popular -> popularSource.getPopular()
            HomeContentType.UpComing -> upComingSource.getUpComing()
        }
    }
}
