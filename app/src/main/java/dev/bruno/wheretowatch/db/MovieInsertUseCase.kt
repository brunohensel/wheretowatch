package dev.bruno.wheretowatch.db

import com.squareup.anvil.annotations.ContributesBinding
import dagger.Reusable
import dev.bruno.wheretowatch.MovieAndGenre
import dev.bruno.wheretowatch.WhereToWatchDatabase
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.discover.MovieInserterDao
import dev.bruno.wheretowatch.services.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
@ContributesBinding(AppScope::class)
class MovieInsertUseCase @Inject constructor(
    private val db: WhereToWatchDatabase,
) : MovieInserterDao {

    override suspend fun insert(movies: List<Movie>) {
        withContext(Dispatchers.Default) {
            db.movieEntityQueries
                .transaction {
                    for (movie in movies) {
                        db.movieEntityQueries.insertMovie(
                            id = movie.id,
                            title = movie.title,
                            overview = movie.overview,
                            popularity = movie.popularity,
                            genres = movie.genresIds,
                            originalTitle = movie.originalTitle,
                            voteCount = movie.voteCount,
                            voteAverage = movie.voteAverage,
                            releaseDate = movie.releaseDate,
                            posterPath = movie.posterPath,
                            backdropPath = movie.backdropPath,
                            collectionId = movie.collectionId,
                        )
                    }
                }

            db.movieAndGenreQueries
                .transaction {
                    for (movie in movies) {
                        for (id in movie.genresIds) {
                            db.movieAndGenreQueries
                                .insertMovieGenre(MovieAndGenre(id, movie.id))
                        }
                    }
                }
        }
    }
}
