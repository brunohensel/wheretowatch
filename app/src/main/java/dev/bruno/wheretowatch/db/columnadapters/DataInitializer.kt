package dev.bruno.wheretowatch.db.columnadapters

import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.Initializers
import dev.bruno.wheretowatch.db.SqlDelightConfigMovie
import dev.bruno.wheretowatch.di.AppScope
import javax.inject.Inject

@ContributesMultibinding(AppScope::class, boundType = Initializers::class)
class DataInitializer @Inject constructor(
    private val config: SqlDelightConfigMovie,
) : Initializers {
    override suspend fun init() {
        config.insertConfigMovieGenres()
        config.insertConfigMovieProviders()
    }
}
