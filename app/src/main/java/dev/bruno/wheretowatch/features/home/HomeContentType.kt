package dev.bruno.wheretowatch.features.home

import dev.bruno.wheretowatch.services.trending.TrendingSupplier

sealed interface HomeContentType {
    data class Trending(val window: TrendingSupplier.TrendWindow) : HomeContentType
}
