package dev.bruno.wheretowatch.features.discover

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.MovieDetail
import dev.bruno.wheretowatch.di.ViewModelKey
import dev.bruno.wheretowatch.di.ViewModelScope
import dev.bruno.wheretowatch.features.discover.MovieScreenState.Event
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

        val discoverFeed by homeContentLists.feedFlow.collectAsState()

        // TODO get content concurrently?
        LaunchedEffect(key1 = Unit) {
            homeContentLists.getContent(DiscoverContentType.Popular)
            homeContentLists.getContent(DiscoverContentType.Action)
            homeContentLists.getContent(DiscoverContentType.Horror)
            homeContentLists.getContent(DiscoverContentType.Upcoming)
            homeContentLists.getContent(DiscoverContentType.War)
            homeContentLists.getContent(DiscoverContentType.Netflix)
            homeContentLists.getContent(DiscoverContentType.HarryPotterCollection)
        }

        return MovieScreenState(
            discoverFeed = discoverFeed,
        ) { event ->
            when (event) {
                is Event.OnMovieClicked -> navigator.goTo(MovieDetail(event.movieId))
            }
        }
    }

    interface HomeContentLists {
        val feedFlow: StateFlow<DiscoverFeed>
        suspend fun getContent(contentType: DiscoverContentType)
    }
}
