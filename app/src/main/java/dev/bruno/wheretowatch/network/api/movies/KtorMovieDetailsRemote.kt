package dev.bruno.wheretowatch.network.api.movies

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.model.MovieDetails
import dev.bruno.wheretowatch.services.model.MovieVideo
import dev.bruno.wheretowatch.services.movies.detail.MovieDetailsRemote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class KtorMovieDetailsRemote @Inject constructor(
    private val httpClient: HttpClient,
) : MovieDetailsRemote {
    override suspend fun fetchMovieDetail(movieId: Int): MovieDetails {
        val movieRes = MovieDetailRequest.Id(movieId = movieId)
        val videoRes = MovieDetailRequest.Id.Videos(movieRes)

        return coroutineScope {
            val detailDeferred = async { httpClient.get(movieRes).body<MovieDetailsDto>() }
            val videosDeferred = async { httpClient.get(videoRes).body<MovieVideoResponse>() }

            detailDeferred.await().toMovieDetails(videosDeferred.await().results)
        }
    }

    private fun MovieDetailsDto.toMovieDetails(videosDto: List<MovieVideoDto>): MovieDetails {
        return MovieDetails(
            movieId = this.movieId,
            homePage = this.homepage,
            budget = this.budget,
            revenue = this.revenue,
            runtime = this.runtime,
            tagline = this.tagline,
            videos = videosDto.toVideos(),
            collectionId = this.collection?.id
        )
    }

    private fun List<MovieVideoDto>.toVideos(): List<MovieVideo> = this.map {
        MovieVideo(
            id = it.id,
            key = it.key,
            site = it.site,
            official = it.official,
            publishedDate = it.date
        )
    }
}

