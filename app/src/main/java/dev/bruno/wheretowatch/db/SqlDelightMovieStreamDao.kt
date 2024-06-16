package dev.bruno.wheretowatch.db

import com.squareup.anvil.annotations.ContributesBinding
import dagger.Reusable
import dev.bruno.wheretowatch.MovieAndProvider
import dev.bruno.wheretowatch.WhereToWatchDatabase
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.discover.MovieStreamDao
import dev.bruno.wheretowatch.services.discover.StreamerProvider
import dev.bruno.wheretowatch.services.model.Movie
import javax.inject.Inject

@Reusable
@ContributesBinding(AppScope::class)
class SqlDelightMovieStreamDao @Inject constructor(
    private val db: WhereToWatchDatabase,
    private val useCase: MovieInsertUseCase,
) : MovieStreamDao {
    override suspend fun getMovies(stream: StreamerProvider): List<Movie> {
        return db.movieEntityQueries.getMovieByProvider(
            providerIds = listOf(stream.id.toInt()),
            mapper = { id, title, overview, popularity, genres, originalTitle, voteCount, voteAverage,
                       releaseDate, posterPath, backdropPath, collectionId, streamProviders, providerId, movieId ->
                Movie(
                    id = id,
                    title = title,
                    overview = overview,
                    originalTitle = originalTitle,
                    popularity = popularity,
                    voteCount = voteCount,
                    voteAverage = voteAverage,
                    genresIds = genres,
                    releaseDate = releaseDate,
                    posterPath = posterPath,
                    backdropPath = backdropPath,
                    collectionId = collectionId,
                    streamProviders = streamProviders
                )
            }
        ).executeAsList()
    }

    override suspend fun insert(stream: StreamerProvider, movies: List<Movie>) {
        val moviesToBeReplaced = mutableListOf<Movie>()
        val localMovies = getMovies(stream).associateBy { it.id }
        val providerId = stream.id.toInt()

        movies.forEach { movie ->
            val localMovie = localMovies[movie.id]
            when {
                localMovie == null -> movie
                localMovie.streamProviders.isNullOrEmpty() -> movie
                localMovie.streamProviders.contains(providerId) -> return
                else -> movie.copy(streamProviders = localMovie.streamProviders + listOf(providerId))
            }.also { moviesToBeReplaced.add(it) }
        }

        useCase.insert(moviesToBeReplaced)

        db.movieAndProviderQueries
            .transaction {
                for (m in moviesToBeReplaced) {
                    db.movieAndProviderQueries
                        .insertMovieProvider(
                            MovieAndProvider(
                                providerId = providerId,
                                movieId = m.id,
                            )
                        )
                }
            }

    }
}
