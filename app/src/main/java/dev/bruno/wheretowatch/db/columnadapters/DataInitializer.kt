package dev.bruno.wheretowatch.db.columnadapters

import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.GenreEntity
import dev.bruno.wheretowatch.Initializers
import dev.bruno.wheretowatch.db.SqlDelightConfigMovieGenres
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.discover.MovieGenre
import javax.inject.Inject

@ContributesMultibinding(AppScope::class, boundType = Initializers::class)
class DataInitializer @Inject constructor(
    private val genreConfig: SqlDelightConfigMovieGenres,
) : Initializers {
    override suspend fun init() {
        if (genreConfig.getGenresCount() == 0L) {
            val genres = MovieGenre.entries.map { GenreEntity(it.id.toInt(), it.name.lowercase()) }
            genreConfig.insertConfigMovieGenres(genres)
        }
    }
}
