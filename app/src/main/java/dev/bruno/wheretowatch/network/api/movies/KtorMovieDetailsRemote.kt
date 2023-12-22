package dev.bruno.wheretowatch.network.api.movies

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.model.MovieDetails
import dev.bruno.wheretowatch.services.movies.detail.MovieDetailsRemote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class KtorMovieDetailsRemote @Inject constructor(
    private val httpClient: HttpClient,
) : MovieDetailsRemote {
    override suspend fun fetchMovieDetail(movieId: Int): MovieDetails {
        val res = MovieDetailRequest.Id(movieId = movieId)
        val response = httpClient.get(res).body<MovieDetailsDto>()
        return response.toMovieDetails()
    }

    private fun MovieDetailsDto.toMovieDetails(): MovieDetails {
        return MovieDetails(
            movieId = this.movieId,
            homePage = this.homePage,
            budget = this.budget,
            revenue = this.revenue,
            runtime = this.runtime,
            tagline = this.tagline,
            collectionId = this.collection?.id
        )
    }
}
