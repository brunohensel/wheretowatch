package dev.bruno.wheretowatch.db

import dagger.Reusable
import dev.bruno.wheretowatch.GenreEntity
import dev.bruno.wheretowatch.WhereToWatchDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
class SqlDelightConfigMovieGenres @Inject constructor(
    private val db: WhereToWatchDatabase,
) {

    suspend fun getGenresCount(): Long {
        return withContext(Dispatchers.IO) {
            val queries = db.genreQueries
            queries.getGenresCount().executeAsOne()
        }
    }

    suspend fun insertConfigMovieGenres(genres: List<GenreEntity>) {
        withContext(Dispatchers.IO) {
            val queries = db.genreQueries
            queries.transaction {
                for ((id, name) in genres) {
                    queries.insertGenre(id, name)
                }
            }
        }
    }
}
