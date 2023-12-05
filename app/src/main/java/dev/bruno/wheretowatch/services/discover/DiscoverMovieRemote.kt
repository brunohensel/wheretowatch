package dev.bruno.wheretowatch.services.discover

fun interface DiscoverMovieRemote {
    suspend fun getContent(category: DiscoverCategory): DiscoverContentResultDto
}
