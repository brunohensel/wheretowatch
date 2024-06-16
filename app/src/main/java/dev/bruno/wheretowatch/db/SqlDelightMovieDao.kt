package dev.bruno.wheretowatch.db

import com.squareup.anvil.annotations.ContributesBinding
import dagger.Reusable
import dev.bruno.wheretowatch.WhereToWatchDatabase
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.discover.MovieDao
import dev.bruno.wheretowatch.services.discover.MovieGenre
import dev.bruno.wheretowatch.services.model.Movie
import javax.inject.Inject

@Reusable
@ContributesBinding(AppScope::class)
class SqlDelightMovieDao @Inject constructor(
    private val db: WhereToWatchDatabase,
    private val useCase: MovieInsertUseCase,
) : MovieDao {
    override suspend fun getMovies(genre: MovieGenre): List<Movie> {
        return if (genre == MovieGenre.ALL) {
            getAllMovies()
        } else {
            getMoviesByGenreId(genre.id.toInt())
        }
    }

    private fun getAllMovies(): List<Movie> {
        return db.movieEntityQueries
            .getMovies(
                limitedBy = 20,
                mapper = { id, title, overview, popularity, genres, originalTitle, voteCount, voteAverage,
                           releaseDate, posterPath, backdropPath, collectionId ->
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
                        collectionId = collectionId
                    )
                }
            ).executeAsList()
    }

    private fun getMoviesByGenreId(genreId: Int): List<Movie> {
        return db.movieEntityQueries
            .getMoviesByGenre(
                genreId = genreId,
                mapper = { id, title, overview, popularity, genres, originalTitle, voteCount, voteAverage,
                           releaseDate, posterPath, backdropPath, collectionId, _genreId, movieId ->
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
                    )
                }
            ).executeAsList()
    }

    override suspend fun insert(movies: List<Movie>) {
        useCase.insert(movies)
    }
}
