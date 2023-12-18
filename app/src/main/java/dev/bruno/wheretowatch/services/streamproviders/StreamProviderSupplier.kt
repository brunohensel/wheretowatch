package dev.bruno.wheretowatch.services.streamproviders

import dev.bruno.wheretowatch.services.model.StreamProvider
import kotlinx.collections.immutable.ImmutableList

fun interface StreamProviderSupplier {
    suspend fun get(): ImmutableList<StreamProvider>
}
