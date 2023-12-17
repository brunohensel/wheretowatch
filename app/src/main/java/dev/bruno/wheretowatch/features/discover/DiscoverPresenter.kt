package dev.bruno.wheretowatch.features.discover

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dev.bruno.wheretowatch.di.AppScope
import kotlinx.collections.immutable.persistentListOf

class DiscoverPresenter @AssistedInject constructor(
    private val homeContentLists: HomeContentLists,
    @Assisted private val navigator: Navigator,
) : Presenter<DiscoverScreen.State> {

    @Composable
    override fun present(): DiscoverScreen.State {

        val flowContents = homeContentLists.contents
        val trendingItems by flowContents.trendingContent.collectAsRetainedState(DiscoverTrending())
        var trendingWindow by rememberRetained { mutableStateOf(trendingItems.trendWindow) }
        val popularItems by flowContents.popularContent.collectAsRetainedState(persistentListOf())
        val actionItems by flowContents.actionContent.collectAsRetainedState(persistentListOf())
        val horrorItems by flowContents.horrorContent.collectAsRetainedState(persistentListOf())
        val upComingItems by flowContents.upcomingContent.collectAsRetainedState(persistentListOf())
        val topRatedItems by flowContents.topRatedContent.collectAsRetainedState(persistentListOf())
        val netflixItems by flowContents.netflixContent.collectAsRetainedState(persistentListOf())

        // TODO get content concurrently?
        LaunchedEffect(key1 = trendingWindow) {
            //TODO put this inside of its own LaunchEffect since it will trigger other requests
            // when changed
            homeContentLists.getContent(DiscoverContentType.Trending(trendingWindow))
            homeContentLists.getContent(DiscoverContentType.Popular)
            homeContentLists.getContent(DiscoverContentType.Action)
            homeContentLists.getContent(DiscoverContentType.Horror)
            homeContentLists.getContent(DiscoverContentType.Upcoming)
            homeContentLists.getContent(DiscoverContentType.TopRated)
            homeContentLists.getContent(DiscoverContentType.Netflix)
        }

        return DiscoverScreen.State(
            trendingItems = trendingItems,
            popularItems = popularItems,
            upComingItems = upComingItems,
            topRatedItems = topRatedItems,
            actionItems = actionItems,
            horrorItems = horrorItems,
            netflixItems = netflixItems,
        ) { event ->
            when (event) {
                is DiscoverScreen.Event.ChangeTrendWindow -> trendingWindow = event.value
            }
        }
    }

    interface HomeContentLists {
        val contents: DiscoverContentFlows
        suspend fun getContent(contentType: DiscoverContentType)
    }

    @CircuitInject(DiscoverScreen::class, AppScope::class)
    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator): DiscoverPresenter
    }
}
