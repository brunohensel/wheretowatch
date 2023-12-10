package dev.bruno.wheretowatch.db

import com.squareup.anvil.annotations.ContributesBinding
import dagger.Reusable
import dev.bruno.wheretowatch.WhereToWatchDatabase
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.discover.AllPopularDao
import dev.bruno.wheretowatch.services.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
@ContributesBinding(AppScope::class)
class SqlDelightPopularDao @Inject constructor(
    private val db: WhereToWatchDatabase,
) : AllPopularDao {
    override suspend fun getPopularMovies(): List<Movie> {
        return db.popularMovieEntityQueries
            .getPopularMovies(
                mapper = { key, popularId, id, title, overview, popularity, genres, originalTitle,
                           voteCount, voteAverage, releaseDate, posterPath, backdropPath ->
                    Movie(
                        id = id,
                        title = title,
                        overview = overview,
                        originalTitle = originalTitle,
                        popularity = popularity.toFloat(),
                        voteCount = voteCount.toInt(),
                        voteAverage = voteAverage,
                        genresIds = genres.split(",").map { it.trim().toInt() },
                        releaseDate = releaseDate,
                        posterPath = posterPath,
                        backdropPath = backdropPath,
                    )
                }
            )
            .executeAsList()
    }

    override suspend fun insertPopularMovies(movies: List<Movie>) {
        withContext(Dispatchers.Default) {
            db.movieEntityQueries
                .transaction {
                    for (movie in movies) {
                        db.movieEntityQueries.insertMovie(
                            id = movie.id,
                            title = movie.title,
                            overview = movie.overview,
                            popularity = movie.popularity,
                            genres = movie.genresIds.joinToString(),
                            originalTitle = movie.originalTitle,
                            voteCount = movie.voteCount.toLong(),
                            voteAverage = movie.voteAverage,
                            releaseDate = movie.releaseDate,
                            posterPath = movie.posterPath,
                            backdropPath = movie.backdropPath,
                        )
                    }
                }

            db.popularMovieEntityQueries
                .transaction {
                    for (movie in movies) {
                        db.popularMovieEntityQueries.insertPopularMovie(movie.id)
                    }
                }
        }
    }
}
