package dev.bruno.wheretowatch.features.discover

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.discover.DiscoverCategory
import dev.bruno.wheretowatch.services.discover.DiscoverCategory.Collection
import dev.bruno.wheretowatch.services.discover.DiscoverCategory.Popular
import dev.bruno.wheretowatch.services.discover.DiscoverCategory.Streaming
import dev.bruno.wheretowatch.services.discover.DiscoverCategory.Upcoming
import dev.bruno.wheretowatch.services.discover.DiscoverContentSupplier
import dev.bruno.wheretowatch.services.discover.MovieCollection.AVENGERS
import dev.bruno.wheretowatch.services.discover.MovieCollection.HARRY_POTTER
import dev.bruno.wheretowatch.services.discover.MovieCollection.HUNGER_GAMES
import dev.bruno.wheretowatch.services.discover.MovieCollection.LORD_OF_RINGS
import dev.bruno.wheretowatch.services.discover.MovieGenre
import dev.bruno.wheretowatch.services.discover.MovieGenre.ACTION
import dev.bruno.wheretowatch.services.discover.MovieGenre.ALL
import dev.bruno.wheretowatch.services.discover.MovieGenre.COMEDY
import dev.bruno.wheretowatch.services.discover.MovieGenre.CRIME
import dev.bruno.wheretowatch.services.discover.MovieGenre.DOCUMENTARY
import dev.bruno.wheretowatch.services.discover.MovieGenre.DRAMA
import dev.bruno.wheretowatch.services.discover.MovieGenre.FAMILY
import dev.bruno.wheretowatch.services.discover.MovieGenre.FANTASY
import dev.bruno.wheretowatch.services.discover.MovieGenre.HISTORY
import dev.bruno.wheretowatch.services.discover.MovieGenre.HORROR
import dev.bruno.wheretowatch.services.discover.MovieGenre.MUSIC
import dev.bruno.wheretowatch.services.discover.MovieGenre.ROMANCE
import dev.bruno.wheretowatch.services.discover.MovieGenre.THRILLER
import dev.bruno.wheretowatch.services.discover.MovieGenre.WAR
import dev.bruno.wheretowatch.services.discover.StreamerProvider.AMAZON_PRIME
import dev.bruno.wheretowatch.services.discover.StreamerProvider.APPLE_TV_PLUS
import dev.bruno.wheretowatch.services.discover.StreamerProvider.DISNEY_PLUS
import dev.bruno.wheretowatch.services.discover.StreamerProvider.NETFLIX
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class DiscoverContentListsImpl @Inject constructor(
    private val supplier: DiscoverContentSupplier,
) : MovieViewModel.HomeContentLists {

    private val feedMap = ConcurrentHashMap<DiscoverSections, DiscoverContent>()
    private val feedState = MutableStateFlow(DiscoverFeed(mapOf()))

    override val feedFlow: StateFlow<DiscoverFeed>
        get() = feedState.asStateFlow()

    override suspend fun getContent(contentType: DiscoverContentType) {
        when (contentType) {
            DiscoverContentType.Popular -> getMovieFor(Popular(ALL))
            DiscoverContentType.Upcoming -> getMovieFor(Upcoming)
            DiscoverContentType.Action -> getMovieFor(Popular(ACTION))
            DiscoverContentType.Horror -> getMovieFor(Popular(HORROR))
            DiscoverContentType.War -> getMovieFor(Popular(WAR))
            DiscoverContentType.Comedy -> getMovieFor(Popular(COMEDY))
            DiscoverContentType.Crime -> getMovieFor(Popular(CRIME))
            DiscoverContentType.Documentary -> getMovieFor(Popular(DOCUMENTARY))
            DiscoverContentType.Drama -> getMovieFor(Popular(DRAMA))
            DiscoverContentType.Family -> getMovieFor(Popular(FAMILY))
            DiscoverContentType.Fantasy -> getMovieFor(Popular(FANTASY))
            DiscoverContentType.History -> getMovieFor(Popular(HISTORY))
            DiscoverContentType.Music -> getMovieFor(Popular(MUSIC))
            DiscoverContentType.Romance -> getMovieFor(Popular(ROMANCE))
            DiscoverContentType.Thriller -> getMovieFor(Popular(THRILLER))
            CollectionContent.HarryPotter -> getMovieFor(Collection(HARRY_POTTER))
            CollectionContent.HungerGames -> getMovieFor(Collection(HUNGER_GAMES))
            CollectionContent.Avengers -> getMovieFor(Collection(AVENGERS))
            CollectionContent.LordOfTheRings -> getMovieFor(Collection(LORD_OF_RINGS))
            StreamContent.Netflix -> getMovieFor(Streaming(NETFLIX))
            StreamContent.AmazonPrime -> getMovieFor(Streaming(AMAZON_PRIME))
            StreamContent.AppleTvPlus -> getMovieFor(Streaming(DISNEY_PLUS))
            StreamContent.DisneyPlus -> getMovieFor(Streaming(APPLE_TV_PLUS))
        }
    }

    private suspend fun getMovieFor(category: DiscoverCategory) {
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

        ContentList(items).update(category.toSection())
    }

    private fun DiscoverCategory.toSection(): DiscoverSections {
        return when (this) {
            is Collection -> when (this.collection) {
                HARRY_POTTER -> DiscoverSections.HarryPotter
                HUNGER_GAMES -> DiscoverSections.HungerGames
                AVENGERS -> DiscoverSections.Avengers
                LORD_OF_RINGS -> DiscoverSections.LordOfRings
            }

            is Popular -> when (this.genre) {
                ALL -> DiscoverSections.Popular
                ACTION -> DiscoverSections.Action
                MovieGenre.ADVENTURE -> TODO()
                MovieGenre.ANIMATION -> TODO()
                COMEDY -> DiscoverSections.Comedy
                CRIME -> DiscoverSections.Crime
                DOCUMENTARY -> DiscoverSections.Documentary
                DRAMA -> DiscoverSections.Drama
                FAMILY -> DiscoverSections.Family
                FANTASY -> DiscoverSections.Fantasy
                HISTORY -> DiscoverSections.History
                HORROR -> DiscoverSections.Horror
                MUSIC -> DiscoverSections.Music
                MovieGenre.MYSTERY -> TODO()
                ROMANCE -> DiscoverSections.Romance
                MovieGenre.SCIENCE_FICTION -> TODO()
                MovieGenre.TV_MOVIE -> TODO()
                THRILLER -> DiscoverSections.Thriller
                WAR -> DiscoverSections.War
                MovieGenre.WESTERN -> TODO()
            }

            is Streaming -> when (this.provider) {
                NETFLIX -> DiscoverSections.Netflix
                AMAZON_PRIME -> DiscoverSections.AmazonPrime
                DISNEY_PLUS -> DiscoverSections.DisneyPlus
                APPLE_TV_PLUS -> DiscoverSections.AppleTvPlus
            }

            Upcoming -> DiscoverSections.Upcoming
        }
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
