package dev.bruno.wheretowatch.network.api.discover

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.discover.DiscoverCategory
import dev.bruno.wheretowatch.services.discover.DiscoverMovieRemote
import dev.bruno.wheretowatch.services.discover.MovieGenre
import dev.bruno.wheretowatch.services.model.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class KtorDiscoverRemote @Inject constructor(
    private val httpClient: HttpClient,
) : DiscoverMovieRemote {

    override suspend fun getContent(category: DiscoverCategory): List<Movie> {
        return when (category) {
            DiscoverCategory.Upcoming -> fetchUpComingMovies()
            is DiscoverCategory.Popular -> fetchPopularMovies(category.genre)
            is DiscoverCategory.Streaming -> fetchStreamProviderMovies(category.provider.id)
            is DiscoverCategory.Collection -> fetchMovieCollection(category.collection.id)
        }
    }

    private suspend fun fetchMovieCollection(collectionId: Int): List<Movie> {
        val res = CollectionRequest.Id(collectionId = collectionId.toString())
        val response = httpClient.get(res).body<MovieCollectionDto>()
        return response.toMovies()
    }

    private suspend fun fetchPopularMovies(genre: MovieGenre): List<Movie> {
        val res = MovieRequest(
            page = 1,
            language = "en-US", // TODO get it from preferences
            region = "DE", // TODO get it from preferences
            // We send `null` to fetch popular movies without any genre constraint
            genre = if (genre == MovieGenre.ALL) null else genre.id,
            sortBy = "popularity.desc",
        )
        val response = httpClient.get(res).body<DiscoverContentResultDto>()
        return response.toMovies()
    }

    @Suppress("MagicNumber")
    private suspend fun fetchUpComingMovies(): List<Movie> {
        val localDate = Clock.System.todayIn(TimeZone.UTC)
        val startDate = localDate.plus(2, DateTimeUnit.DAY)
        val endDate = localDate.plus(3, DateTimeUnit.WEEK)

        val res = MovieRequest(
            page = 1,
            language = "en-US", // TODO get it from preferences
            region = "DE", // TODO get it from preferences
            sortBy = "popularity.desc",
            releaseGTE = startDate.toString(),
            releaseLTE = endDate.toString(),
        )
        val response = httpClient.get(res).body<DiscoverContentResultDto>()
        return response.toMovies()
    }

    private suspend fun fetchStreamProviderMovies(providerId: String): List<Movie> {
        val res = MovieRequest(
            page = 1,
            language = "en-US", // TODO get it from preferences
            region = "DE", // TODO get it from preferences
            watchRegion = "DE",
            streamProviderId = providerId,
            sortBy = "popularity.desc",
            voteCountGTE = 400,
        )
        val response = httpClient.get(res).body<DiscoverContentResultDto>()
        return response.toMovies(providersId = listOf(providerId.toInt()))
    }
}
