package dev.bruno.wheretowatch.services.discover

sealed interface DiscoverCategory {
    data object Popular : DiscoverCategory
    data object UpComing : DiscoverCategory
}
