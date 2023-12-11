package dev.bruno.wheretowatch.db

import com.squareup.anvil.annotations.ContributesBinding
import dagger.Reusable
import dev.bruno.wheretowatch.WhereToWatchDatabase
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.discover.AllPopularDao
import dev.bruno.wheretowatch.services.discover.MovieGenre
import dev.bruno.wheretowatch.services.model.Movie
import javax.inject.Inject

@Reusable
@ContributesBinding(AppScope::class)
class SqlDelightPopularDao @Inject constructor(
    private val db: WhereToWatchDatabase,
    private val useCase: MovieInsertUseCase,
) : AllPopularDao {
    override suspend fun getPopularMovies(genre: MovieGenre): List<Movie> {
        return if (genre == MovieGenre.ALL) {
            getAllPopularMovies()
        } else {
            getFilteredById(genre.id.toInt())
        }
    }

    private fun getAllPopularMovies(): List<Movie> {
        return db.popularMovieEntityQueries
            .getPopularMovies(
                mapper = { key, popularId, id, title, overview, popularity, genres, originalTitle,
                           voteCount, voteAverage, releaseDate, posterPath, backdropPath ->
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
                    )
                }
            ).executeAsList()
    }

    private fun getFilteredById(genreId: Int): List<Movie> {
        return db.popularMovieEntityQueries
            .getPopularGenre(
                genreId = genreId,
                mapper = { key, popularId, id, title, overview, popularity, genres, originalTitle,
                           voteCount, voteAverage, releaseDate, posterPath, backdropPath, genreId,
                           movieId ->

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
                    )
                }
            ).executeAsList()
    }

    override suspend fun insert(movies: List<Movie>) {
        useCase.insert(movies)

        db.popularMovieEntityQueries
            .transaction {
                for (movie in movies) {
                    db.popularMovieEntityQueries.insertPopularMovie(movie.id)
                }
            }
    }
}
