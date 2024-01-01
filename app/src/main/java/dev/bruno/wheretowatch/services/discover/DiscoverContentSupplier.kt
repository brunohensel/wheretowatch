package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.services.model.DiscoverContent

fun interface DiscoverContentSupplier {
    suspend fun get(category: DiscoverCategory): List<DiscoverContent>
}
