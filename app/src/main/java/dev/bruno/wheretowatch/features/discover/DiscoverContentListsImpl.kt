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
import dev.bruno.wheretowatch.features.discover.movies.StreamProviderMovieSource
import dev.bruno.wheretowatch.features.discover.movies.TopRatedSource
import dev.bruno.wheretowatch.features.discover.movies.TrendingMovieSource
import dev.bruno.wheretowatch.features.discover.movies.UpcomingMovieSource
import dev.bruno.wheretowatch.services.discover.DiscoverCategory
import dev.bruno.wheretowatch.services.discover.DiscoverContentSupplier
import dev.bruno.wheretowatch.services.discover.MovieCollection.HARRY_POTTER
import dev.bruno.wheretowatch.services.discover.MovieGenre
import dev.bruno.wheretowatch.services.discover.StreamerProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import dev.bruno.wheretowatch.services.model.DiscoverContent as DiscoverContentModel

@ContributesBinding(AppScope::class)
class DiscoverContentListsImpl @Inject constructor(
    private val trendingSource: TrendingMovieSource,
    private val upcomingSource: UpcomingMovieSource,
    private val streamSource: StreamProviderMovieSource,
    private val topRatedSource: TopRatedSource,
    private val collectionSource: CollectionMovieSource,
    private val supplier: DiscoverContentSupplier,
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

            Popular -> getPopularMovieContent()
                .update(section = DiscoverSections.Popular)

            Upcoming -> upcomingSource.get()
                .update(section = DiscoverSections.Upcoming)

            TopRated -> topRatedSource.get()
                .update(section = DiscoverSections.TopRated)

            Action -> getPopularMovieContent(MovieGenre.ACTION)
                .update(section = DiscoverSections.Action)

            Horror -> getPopularMovieContent(MovieGenre.HORROR)
                .update(section = DiscoverSections.Horror)

            Netflix -> streamSource.get(StreamerProvider.NETFLIX)
                .update(section = DiscoverSections.Netflix)

            War -> getPopularMovieContent(MovieGenre.WAR)
                .update(section = DiscoverSections.War)

            HarryPotterCollection -> collectionSource.get(HARRY_POTTER)
                .update(section = DiscoverSections.HarryPotter)
        }
    }

    private suspend fun getPopularMovieContent(
        genre: MovieGenre = MovieGenre.ALL,
    ): DiscoverContent {
        return supplier
            .get(DiscoverCategory.Popular(genre))
            .asDiscoverMovieItem()
    }

    private fun ImmutableList<DiscoverContentModel>.asDiscoverMovieItem(): DiscoverContent {
        val items = this.map { item ->
            DiscoverMovieItem(
                id = item.id,
                title = item.title,
                originalTitle = item.originalTitle,
                popularity = item.popularity,
                voteAverage = item.voteAverage,
                voteCount = item.voteCount,
                buildImgModel = item.curried()
            )
        }.toImmutableList()

        return ContentList(items)
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
