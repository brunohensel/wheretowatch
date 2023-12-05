package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.services.model.DiscoverContent
import kotlinx.collections.immutable.ImmutableList

fun interface DiscoverContentSupplier {
    suspend fun get(category: DiscoverCategory): ImmutableList<DiscoverContent>
}
