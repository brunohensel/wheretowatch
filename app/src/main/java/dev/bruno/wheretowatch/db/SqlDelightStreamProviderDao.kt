package dev.bruno.wheretowatch.db

import com.squareup.anvil.annotations.ContributesBinding
import dagger.Reusable
import dev.bruno.wheretowatch.WhereToWatchDatabase
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.model.StreamProvider
import dev.bruno.wheretowatch.services.streamproviders.StreamProviderDao
import javax.inject.Inject

@Reusable
@ContributesBinding(AppScope::class)
class SqlDelightStreamProviderDao @Inject constructor(
    private val db: WhereToWatchDatabase,
) : StreamProviderDao {
    override suspend fun insert(providers: List<StreamProvider>) {
        val streamQueries = db.streamProviderEntityQueries
        streamQueries.transaction {
            for (provider in providers) {
                streamQueries.insertProvider(
                    id = provider.id,
                    name = provider.name,
                    logoPath = provider.logoPath,
                )
            }
        }
    }

    override suspend fun getProviders(): List<StreamProvider> {
        return db.streamProviderEntityQueries
            .getProviders(
                mapper = { id, name, logoPath ->
                    StreamProvider(
                        id = id,
                        logoPath = logoPath,
                        name = name,
                    )
                }
            ).executeAsList()
    }
}
