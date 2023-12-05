package dev.bruno.wheretowatch.network.api.discover

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.discover.DiscoverCategory
import dev.bruno.wheretowatch.services.discover.DiscoverContentResultDto
import dev.bruno.wheretowatch.services.discover.DiscoverMovieRemote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class KtorDiscoverRemote @Inject constructor(
    private val httpClient: HttpClient,
) : DiscoverMovieRemote {

    override suspend fun getContent(category: DiscoverCategory): DiscoverContentResultDto {
        return when (category) {
            DiscoverCategory.Popular -> fetchPopularMovies()
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
}
