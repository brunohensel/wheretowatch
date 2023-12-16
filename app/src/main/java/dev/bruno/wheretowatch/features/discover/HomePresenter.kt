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
import dev.bruno.wheretowatch.features.settings.SettingsScreen
import kotlinx.collections.immutable.persistentListOf

class HomePresenter @AssistedInject constructor(
    private val homeContentLists: HomeContentLists,
    @Assisted private val navigator: Navigator,
) : Presenter<DiscoverScreen.State> {

    @Composable
    override fun present(): DiscoverScreen.State {

        val flowContents = homeContentLists.contents
        val trendingItems by flowContents.trendingContent.collectAsRetainedState(HomeTrending())
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
            homeContentLists.getContent(HomeContentType.Trending(trendingWindow))
            homeContentLists.getContent(HomeContentType.Popular)
            homeContentLists.getContent(HomeContentType.Action)
            homeContentLists.getContent(HomeContentType.Horror)
            homeContentLists.getContent(HomeContentType.Upcoming)
            homeContentLists.getContent(HomeContentType.TopRated)
            homeContentLists.getContent(HomeContentType.Netflix)
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
                DiscoverScreen.Event.OpenSettings -> navigator.goTo(SettingsScreen)
                is DiscoverScreen.Event.ChangeTrendWindow -> trendingWindow = event.value
            }
        }
    }

    interface HomeContentLists {
        val contents: HomeContentFlows
        suspend fun getContent(contentType: HomeContentType)
    }

    @CircuitInject(DiscoverScreen::class, AppScope::class)
    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }
}
