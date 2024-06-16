package dev.bruno.wheretowatch.features.discover

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.MovieDetail
import dev.bruno.wheretowatch.di.ViewModelKey
import dev.bruno.wheretowatch.di.ViewModelScope
import dev.bruno.wheretowatch.features.discover.MovieScreenState.Event
import dev.bruno.wheretowatch.services.discover.TrendWindow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@ContributesMultibinding(ViewModelScope::class)
@ViewModelKey(MovieViewModel::class)
class MovieViewModel @Inject constructor(
    private val homeContentLists: HomeContentLists,
    private val navigator: dev.bruno.wheretowatch.Navigator,
) : ViewModel() {

    @Composable
    fun state(): MovieScreenState {

        var trendingWindow by rememberSaveable { mutableStateOf(TrendWindow.DAY) }
        val discoverFeed by homeContentLists.feedFlow.collectAsState()

        LaunchedEffect(key1 = trendingWindow) {
//            homeContentLists.getContent(DiscoverContentType.Trending(trendingWindow))
        }
        // TODO get content concurrently?
        LaunchedEffect(key1 = Unit) {
            homeContentLists.getContent(DiscoverContentType.Popular)
            homeContentLists.getContent(DiscoverContentType.Action)
            homeContentLists.getContent(DiscoverContentType.Horror)
            homeContentLists.getContent(DiscoverContentType.Upcoming)
//            homeContentLists.getContent(DiscoverContentType.TopRated)
            homeContentLists.getContent(DiscoverContentType.War)
//            homeContentLists.getContent(DiscoverContentType.Netflix)
            homeContentLists.getContent(DiscoverContentType.HarryPotterCollection)
        }

        return MovieScreenState(
            discoverFeed = discoverFeed,
        ) { event ->
            when (event) {
                is Event.ChangeTrendWindow -> trendingWindow = event.value
                is Event.OnMovieClicked -> navigator.goTo(MovieDetail(event.movieId))
            }
        }
    }

    interface HomeContentLists {
        val feedFlow: StateFlow<DiscoverFeed>
        suspend fun getContent(contentType: DiscoverContentType)
    }
}
