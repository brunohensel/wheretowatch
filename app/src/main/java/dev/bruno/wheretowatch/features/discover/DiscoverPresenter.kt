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
import dev.bruno.wheretowatch.features.discover.DiscoverScreen.Event
import dev.bruno.wheretowatch.features.movies.MovieDetailScreen
import dev.bruno.wheretowatch.services.discover.TrendWindow
import kotlinx.coroutines.flow.Flow

class DiscoverPresenter @AssistedInject constructor(
    private val homeContentLists: HomeContentLists,
    @Assisted private val navigator: Navigator,
) : Presenter<DiscoverScreen.State> {

    @Composable
    override fun present(): DiscoverScreen.State {

        var trendingWindow by rememberRetained { mutableStateOf(TrendWindow.DAY) }
        val discoverFeed by homeContentLists.feedFlow.collectAsRetainedState(initial = DiscoverFeed())

        LaunchedEffect(key1 = trendingWindow) {
            homeContentLists.getContent(DiscoverContentType.Trending(trendingWindow))
        }
        // TODO get content concurrently?
        LaunchedEffect(key1 = Unit) {
            homeContentLists.getContent(DiscoverContentType.Popular)
            homeContentLists.getContent(DiscoverContentType.Action)
            homeContentLists.getContent(DiscoverContentType.Horror)
            homeContentLists.getContent(DiscoverContentType.Upcoming)
            homeContentLists.getContent(DiscoverContentType.TopRated)
            homeContentLists.getContent(DiscoverContentType.War)
            homeContentLists.getContent(DiscoverContentType.Netflix)
            homeContentLists.getContent(DiscoverContentType.HarryPotterCollection)
        }

        return DiscoverScreen.State(
            discoverFeed = discoverFeed,
        ) { event ->
            when (event) {
                is Event.ChangeTrendWindow -> trendingWindow = event.value
                is Event.OnMovieClicked -> navigator.goTo(MovieDetailScreen(event.movieId))
            }
        }
    }

    interface HomeContentLists {
        val feedFlow: Flow<DiscoverFeed>
        suspend fun getContent(contentType: DiscoverContentType)
    }

    @CircuitInject(DiscoverScreen::class, AppScope::class)
    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator): DiscoverPresenter
    }
}
