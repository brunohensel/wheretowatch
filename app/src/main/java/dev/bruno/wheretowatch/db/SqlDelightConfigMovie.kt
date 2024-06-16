package dev.bruno.wheretowatch.db

import dev.bruno.wheretowatch.GenreEntity
import dev.bruno.wheretowatch.StreamProviderEntity
import dev.bruno.wheretowatch.WhereToWatchDatabase
import dev.bruno.wheretowatch.services.discover.MovieGenre
import dev.bruno.wheretowatch.services.discover.StreamerProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SqlDelightConfigMovie @Inject constructor(
    private val db: WhereToWatchDatabase,
) {

    suspend fun insertConfigMovieProviders() {
        val queries = db.streamProviderEntityQueries

        withContext(Dispatchers.IO) {
            if (queries.getProvidersCount().executeAsOne() == 0L) {
                val genres =
                    StreamerProvider.entries.map {
                        StreamProviderEntity(it.id.toInt(), it.name.lowercase())
                    }

                queries.transaction {
                    for ((id, name) in genres) {
                        queries.insertProvider(id, name)
                    }
                }
            }
        }
    }

    suspend fun insertConfigMovieGenres() {
        val queries = db.genreEntityQueries

        withContext(Dispatchers.IO) {
            if (queries.getGenresCount().executeAsOne() == 0L) {
                val genres = MovieGenre.entries.map {
                    GenreEntity(it.id.toInt(), it.name.lowercase())
                }
                queries.transaction {
                    for ((id, name) in genres) {
                        queries.insertGenre(id, name)
                    }
                }
            }
        }
    }
}
