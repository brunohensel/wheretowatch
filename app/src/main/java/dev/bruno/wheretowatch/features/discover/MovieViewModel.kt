package dev.bruno.wheretowatch.features.discover

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.MovieDetail
import dev.bruno.wheretowatch.di.ViewModelKey
import dev.bruno.wheretowatch.di.ViewModelScope
import dev.bruno.wheretowatch.features.discover.MovieScreenState.Event
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ContributesMultibinding(ViewModelScope::class)
@ViewModelKey(MovieViewModel::class)
class MovieViewModel @Inject constructor(
    private val homeContentLists: HomeContentLists,
    private val navigator: dev.bruno.wheretowatch.Navigator,
) : ViewModel() {

    init {
        viewModelScope.launch {
            homeContentLists.getContent(DiscoverContentType.Popular)
            homeContentLists.getContent(DiscoverContentType.Action)
            homeContentLists.getContent(DiscoverContentType.Horror)
            homeContentLists.getContent(DiscoverContentType.Upcoming)
            homeContentLists.getContent(DiscoverContentType.War)
            homeContentLists.getContent(DiscoverContentType.Comedy)
            homeContentLists.getContent(DiscoverContentType.Crime)
            homeContentLists.getContent(DiscoverContentType.Documentary)
            homeContentLists.getContent(DiscoverContentType.Drama)
            homeContentLists.getContent(DiscoverContentType.Family)
            homeContentLists.getContent(DiscoverContentType.Fantasy)
            homeContentLists.getContent(DiscoverContentType.History)
            homeContentLists.getContent(DiscoverContentType.Music)
            homeContentLists.getContent(DiscoverContentType.Romance)
            homeContentLists.getContent(DiscoverContentType.Thriller)
            homeContentLists.getContent(StreamContent.Netflix)
            homeContentLists.getContent(StreamContent.AmazonPrime)
            homeContentLists.getContent(StreamContent.DisneyPlus)
            homeContentLists.getContent(StreamContent.AppleTvPlus)
            homeContentLists.getContent(CollectionContent.HarryPotter)
            homeContentLists.getContent(CollectionContent.Avengers)
            homeContentLists.getContent(CollectionContent.HungerGames)
            homeContentLists.getContent(CollectionContent.LordOfTheRings)
        }
    }

    @Composable
    fun state(): MovieScreenState {

        val discoverFeed by homeContentLists.feedFlow.collectAsState()

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
