package dev.bruno.wheretowatch.db

import com.squareup.anvil.annotations.ContributesBinding
import dagger.Reusable
import dev.bruno.wheretowatch.GetPopularMovies
import dev.bruno.wheretowatch.MovieEntity
import dev.bruno.wheretowatch.WhereToWatchDatabase
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.discover.AllPopularDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
@ContributesBinding(AppScope::class)
class SqlDelightPopularDao @Inject constructor(
    private val db: WhereToWatchDatabase,
) : AllPopularDao {
    override suspend fun getPopularEntity(): List<GetPopularMovies> {
        return db.popularMovieEntityQueries.getPopularMovies().executeAsList()
    }

    override suspend fun insertPopularEntity(entities: List<MovieEntity>) = coroutineScope {
        withContext(Dispatchers.Default) {
            db.movieEntityQueries
                .transaction {
                    for (entity in entities) {
                        db.movieEntityQueries.insertMovie(
                            id = entity.id,
                            title = entity.title,
                            popularity = entity.popularity,
                            genres = entity.genres,
                            originalTitle = entity.originalTitle,
                            originalLanguage = entity.originalLanguage,
                            voteCount = entity.voteCount,
                            voteAverage = entity.voteAverage,
                            releaseDate = entity.releaseDate,
                            posterPath = entity.posterPath,
                            backdropPath = entity.backdropPath,
                        )
                    }
                }

            db.popularMovieEntityQueries
                .transaction {
                    for (entity in entities) {
                        db.popularMovieEntityQueries.insertPopularMovie(entity.id)
                    }
                }
        }
    }
}
