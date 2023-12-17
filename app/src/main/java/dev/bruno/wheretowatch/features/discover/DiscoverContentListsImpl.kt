package dev.bruno.wheretowatch.features.discover

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Action
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.HarryPotterCollection
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Horror
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Netflix
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Popular
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.TopRated
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Trending
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Upcoming
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.War
import dev.bruno.wheretowatch.features.discover.movies.CollectionMovieSource
import dev.bruno.wheretowatch.features.discover.movies.PopularMovieSource
import dev.bruno.wheretowatch.features.discover.movies.StreamProviderMovieSource
import dev.bruno.wheretowatch.features.discover.movies.TopRatedSource
import dev.bruno.wheretowatch.features.discover.movies.TrendingMovieSource
import dev.bruno.wheretowatch.features.discover.movies.UpcomingMovieSource
import dev.bruno.wheretowatch.services.discover.MovieCollection.HARRY_POTTER
import dev.bruno.wheretowatch.services.discover.MovieGenre
import dev.bruno.wheretowatch.services.discover.StreamerProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class DiscoverContentListsImpl @Inject constructor(
    private val trendingSource: TrendingMovieSource,
    private val popularSource: PopularMovieSource,
    private val upcomingSource: UpcomingMovieSource,
    private val streamSource: StreamProviderMovieSource,
    private val topRatedSource: TopRatedSource,
    private val collectionSource: CollectionMovieSource,
) : DiscoverPresenter.HomeContentLists {

    private val feedMap = ConcurrentHashMap<DiscoverSections, DiscoverContent>()
    private val feedState = MutableStateFlow(DiscoverFeed(mapOf()))

    override val feedFlow: Flow<DiscoverFeed>
        get() = feedState.asStateFlow()

    override suspend fun getContent(contentType: DiscoverContentType) {
        when (contentType) {
            is Trending -> {
                val sourceContent = trendingSource.get(contentType.window)
                val stateContent = feedMap[DiscoverSections.Trending] as? DiscoverTrending

                if (stateContent?.trendWindow != contentType.window) {
                    feedMap[DiscoverSections.Trending] =
                        DiscoverTrending(contentType.window, sourceContent.items)
                }
                updateSorted()
            }

            Popular -> popularSource.get()
                .update(section = DiscoverSections.Popular)

            Upcoming -> upcomingSource.get()
                .update(section = DiscoverSections.Upcoming)

            TopRated -> topRatedSource.get()
                .update(section = DiscoverSections.TopRated)

            Action -> popularSource.get(MovieGenre.ACTION)
                .update(section = DiscoverSections.Action)

            Horror -> popularSource.get(MovieGenre.HORROR)
                .update(section = DiscoverSections.Horror)

            Netflix -> streamSource.get(StreamerProvider.NETFLIX)
                .update(section = DiscoverSections.Netflix)

            War -> popularSource.get(MovieGenre.WAR)
                .update(section = DiscoverSections.War)

            HarryPotterCollection -> collectionSource.get(HARRY_POTTER)
                .update(section = DiscoverSections.HarryPotter)
        }
    }

    private fun DiscoverContent.update(section: DiscoverSections) {
        feedMap.putIfAbsent(section, this)
        updateSorted()
    }

    private fun updateSorted() {
        val sortedMap = feedMap.toSortedMap(compareBy { it.order })
        feedState.update { it.copy(section = sortedMap) }
    }
}
