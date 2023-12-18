package dev.bruno.wheretowatch.services.streamproviders

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.model.StreamProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class StreamProviderSupplierImpl @Inject constructor(
    private val streamProviderDao: StreamProviderDao,
) : StreamProviderSupplier {
    override suspend fun get(): ImmutableList<StreamProvider> {
        return streamProviderDao
            .getProviders()
            .toImmutableList()
    }
}
