package dev.bruno.wheretowatch.network.api.discover

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.discover.DiscoverCategory
import dev.bruno.wheretowatch.services.discover.DiscoverContentResultDto
import dev.bruno.wheretowatch.services.discover.DiscoverMovieRemote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class KtorDiscoverRemote @Inject constructor(
    private val httpClient: HttpClient,
) : DiscoverMovieRemote {

    override suspend fun getContent(category: DiscoverCategory): DiscoverContentResultDto {
        return when (category) {
            DiscoverCategory.Popular -> fetchPopularMovies()
            DiscoverCategory.Upcoming -> fetchUpComingMovies()
            DiscoverCategory.TopRated -> fetchTopRatedMovies()
        }
    }

    private suspend fun fetchPopularMovies(): DiscoverContentResultDto {
        val res = MovieRequest(
            page = 1,
            language = "en-US", // TODO get it from preferences
            region = "DE", // TODO get it from preferences
            sortBy = "popularity.desc",
        )
        return httpClient.get(res).body()
    }

    @Suppress("MagicNumber")
    private suspend fun fetchUpComingMovies(): DiscoverContentResultDto {
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

        return httpClient.get(res).body()
    }

    @Suppress("MagicNumber")
    private suspend fun fetchTopRatedMovies(): DiscoverContentResultDto {
        val startDate = LocalDate(2000, 1, 1)
        val endDate = Clock.System.todayIn(TimeZone.UTC)

        val res = MovieRequest(
            page = 1,
            language = "en-US", // TODO get it from preferences
            region = "DE", // TODO get it from preferences
            sortBy = "vote_average.desc",
            voteCountGTE = 400,
            releaseGTE = startDate.toString(),
            releaseLTE = endDate.toString(),
        )

        return httpClient.get(res).body()
    }
}
