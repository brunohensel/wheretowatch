package dev.bruno.wheretowatch.services.trending

import dev.bruno.wheretowatch.services.model.TrendingItem
import kotlinx.collections.immutable.ImmutableList

fun interface TrendingSupplier {
    suspend fun get(window: TrendWindow): ImmutableList<TrendingItem>

    enum class TrendWindow { DAY, WEEK }
}