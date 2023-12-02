package dev.bruno.wheretowatch.features.home

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.features.home.trending.TrendingFlowSource
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class HomeContentListsImpl @Inject constructor(
    private val trendingSource: TrendingFlowSource,
) : HomePresenter.HomeContentLists {

    override val contents: HomeContentFlows
        get() = HomeContentFlows(
            tendingContent = trendingSource.flow,
        )

    override suspend fun getContent(contentType: HomeContentType) {
        when (contentType) {
            is HomeContentType.Trending -> trendingSource.getTrending(contentType.window)
        }
    }
}
