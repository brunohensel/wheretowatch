package dev.bruno.wheretowatch.db

import com.squareup.anvil.annotations.ContributesBinding
import dagger.Reusable
import dev.bruno.wheretowatch.WhereToWatchDatabase
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.discover.MovieCollection
import dev.bruno.wheretowatch.services.discover.MovieCollectionDao
import dev.bruno.wheretowatch.services.model.Movie
import javax.inject.Inject

@Reusable
@ContributesBinding(AppScope::class)
class SqlDelightMovieCollectionDao @Inject constructor(
    private val db: WhereToWatchDatabase,
    private val useCase: MovieInsertUseCase,
) : MovieCollectionDao {

    override suspend fun getMovies(collection: MovieCollection): List<Movie> {
        return db.movieEntityQueries
            .getMovieCollection(
                collectionId = collection.id,
                mapper = { id, title, overview, popularity, genres, originalTitle, voteCount,
                           voteAverage, releaseDate, posterPath, backdropPath, collectionId ->
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
        val moviesToBeReplaced = mutableListOf<Movie>()
        for (movie in movies) {
            val localMovie = db.movieEntityQueries.getMovie(movie.id).executeAsOneOrNull()
            if (localMovie?.collectionId == null) {
                moviesToBeReplaced.add(movie)
            }
        }

        useCase.insert(moviesToBeReplaced)
    }
}
