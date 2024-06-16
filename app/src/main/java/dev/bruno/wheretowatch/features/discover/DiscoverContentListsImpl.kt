package dev.bruno.wheretowatch.features.discover

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Action
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Horror
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Popular
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.Upcoming
import dev.bruno.wheretowatch.features.discover.DiscoverContentType.War
import dev.bruno.wheretowatch.features.discover.movies.StreamProviderMovieSource
import dev.bruno.wheretowatch.features.discover.movies.UpcomingMovieSource
import dev.bruno.wheretowatch.services.discover.DiscoverCategory
import dev.bruno.wheretowatch.services.discover.DiscoverContentSupplier
import dev.bruno.wheretowatch.services.discover.MovieCollection
import dev.bruno.wheretowatch.services.discover.MovieCollection.AVENGERS
import dev.bruno.wheretowatch.services.discover.MovieCollection.HARRY_POTTER
import dev.bruno.wheretowatch.services.discover.MovieCollection.HUNGER_GAMES
import dev.bruno.wheretowatch.services.discover.MovieCollection.LORD_OF_RINGS
import dev.bruno.wheretowatch.services.discover.MovieGenre
import dev.bruno.wheretowatch.services.discover.StreamerProvider
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class DiscoverContentListsImpl @Inject constructor(
    private val upcomingSource: UpcomingMovieSource,
    private val streamSource: StreamProviderMovieSource,
    private val supplier: DiscoverContentSupplier,
) : MovieViewModel.HomeContentLists {

    private val feedMap = ConcurrentHashMap<DiscoverSections, DiscoverContent>()
    private val feedState = MutableStateFlow(DiscoverFeed(mapOf()))

    override val feedFlow: StateFlow<DiscoverFeed>
        get() = feedState.asStateFlow()

    override suspend fun getContent(contentType: DiscoverContentType) {
        when (contentType) {

            Popular -> getPopularMovieContent()
                .update(section = DiscoverSections.Popular)

            Upcoming -> upcomingSource.get()
                .update(section = DiscoverSections.Upcoming)

            Action -> getPopularMovieContent(MovieGenre.ACTION)
                .update(section = DiscoverSections.Action)

            Horror -> getPopularMovieContent(MovieGenre.HORROR)
                .update(section = DiscoverSections.Horror)

            War -> getPopularMovieContent(MovieGenre.WAR)
                .update(section = DiscoverSections.War)

            CollectionContent.HarryPotter -> getMovieCollectionContent(HARRY_POTTER)
                .update(section = DiscoverSections.HarryPotter)

            CollectionContent.HungerGames -> getMovieCollectionContent(HUNGER_GAMES)
                .update(section = DiscoverSections.HungerGames)

            CollectionContent.Avengers -> getMovieCollectionContent(AVENGERS)
                .update(section = DiscoverSections.Avengers)

            CollectionContent.LordOfTheRings -> getMovieCollectionContent(LORD_OF_RINGS)
                .update(section = DiscoverSections.LordOfRings)

            DiscoverContentType.Comedy -> getPopularMovieContent(MovieGenre.COMEDY)
                .update(section = DiscoverSections.Comedy)

            DiscoverContentType.Crime -> getPopularMovieContent(MovieGenre.CRIME)
                .update(section = DiscoverSections.Crime)

            DiscoverContentType.Documentary -> getPopularMovieContent(MovieGenre.DOCUMENTARY)
                .update(section = DiscoverSections.Documentary)

            DiscoverContentType.Drama -> getPopularMovieContent(MovieGenre.DRAMA)
                .update(section = DiscoverSections.Drama)

            DiscoverContentType.Family -> getPopularMovieContent(MovieGenre.FAMILY)
                .update(section = DiscoverSections.Family)

            DiscoverContentType.Fantasy -> getPopularMovieContent(MovieGenre.FANTASY)
                .update(section = DiscoverSections.Fantasy)

            DiscoverContentType.History -> getPopularMovieContent(MovieGenre.HISTORY)
                .update(section = DiscoverSections.History)

            DiscoverContentType.Music -> getPopularMovieContent(MovieGenre.MUSIC)
                .update(section = DiscoverSections.Music)

            DiscoverContentType.Romance -> getPopularMovieContent(MovieGenre.ROMANCE)
                .update(section = DiscoverSections.Romance)

            DiscoverContentType.Thriller -> getPopularMovieContent(MovieGenre.THRILLER)
                .update(section = DiscoverSections.Thriller)

            StreamContent.Netflix -> streamSource.get(StreamerProvider.NETFLIX)
                .update(section = DiscoverSections.Netflix)

            StreamContent.AmazonPrime -> streamSource.get(StreamerProvider.AMAZON_PRIME)
                .update(section = DiscoverSections.AmazonPrime)

            StreamContent.AppleTvPlus -> streamSource.get(StreamerProvider.DISNEY_PLUS)
                .update(section = DiscoverSections.DisneyPlus)

            StreamContent.DisneyPlus -> streamSource.get(StreamerProvider.APPLE_TV_PLUS)
                .update(section = DiscoverSections.AppleTvPlus)
        }
    }

    private suspend fun getPopularMovieContent(
        genre: MovieGenre = MovieGenre.ALL,
    ): DiscoverContent {
        return getMovieFor(category = DiscoverCategory.Popular(genre))
    }

    private suspend fun getMovieCollectionContent(
        collection: MovieCollection,
    ): DiscoverContent {
        return getMovieFor(category = DiscoverCategory.Collection(collection))
    }

    private suspend fun getMovieFor(category: DiscoverCategory): DiscoverContent {
        val items = supplier.get(category)
            .map { item ->
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
        val sortedMap = feedMap.toSortedMap(compareBy { it.ordinal })
        feedState.update { it.copy(section = sortedMap) }
    }
}
