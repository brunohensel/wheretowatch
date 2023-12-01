package dev.bruno.wheretowatch.db

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dev.bruno.wheretowatch.WhereToWatchDatabase
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
            name = "WhereToWatchDatabase.db"
        )

        return WhereToWatchDatabase(
            driver = androidDriver,
        )
    }
}