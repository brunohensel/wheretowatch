package dev.bruno.wheretowatch.db

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.adapter.primitive.FloatColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dev.bruno.wheretowatch.GenreEntity
import dev.bruno.wheretowatch.MovieAndGenre
import dev.bruno.wheretowatch.MovieEntity
import dev.bruno.wheretowatch.PopularMovieEntity
import dev.bruno.wheretowatch.StreamProviderEntity
import dev.bruno.wheretowatch.WhereToWatchDatabase
import dev.bruno.wheretowatch.db.columnadapters.GenresAdapter
import dev.bruno.wheretowatch.db.columnadapters.LocalDateAdapter
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.di.ApplicationContext
import dev.bruno.wheretowatch.di.SingleIn

@Module
@ContributesTo(AppScope::class)
object DatabaseModule {

    @SingleIn(AppScope::class)
    @Provides
    fun providesSqlDelightDb(@ApplicationContext context: Context): WhereToWatchDatabase {
        val androidDriver = AndroidSqliteDriver(
            schema = WhereToWatchDatabase.Schema,
            context = context,
            callback = object : AndroidSqliteDriver.Callback(WhereToWatchDatabase.Schema) {
                override fun onConfigure(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true)
                }
            },
            name = "WhereToWatchDatabase.db"
        )

        return WhereToWatchDatabase(
            driver = androidDriver,
            movieEntityAdapter = MovieEntity.Adapter(
                idAdapter = IntColumnAdapter,
                popularityAdapter = FloatColumnAdapter,
                voteAverageAdapter = FloatColumnAdapter,
                releaseDateAdapter = LocalDateAdapter,
                genresAdapter = GenresAdapter,
                voteCountAdapter = IntColumnAdapter,
                collectionIdAdapter = IntColumnAdapter,
            ),
            genreEntityAdapter = GenreEntity.Adapter(
                idAdapter = IntColumnAdapter,
            ),
            popularMovieEntityAdapter = PopularMovieEntity.Adapter(
                popularIdAdapter = IntColumnAdapter,
            ),
            movieAndGenreAdapter = MovieAndGenre.Adapter(
                genreIdAdapter = IntColumnAdapter,
                movieIdAdapter = IntColumnAdapter,
            ),
            streamProviderEntityAdapter = StreamProviderEntity.Adapter(
                idAdapter = IntColumnAdapter,
            ),
        )
    }
}
