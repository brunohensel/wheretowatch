package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.services.model.Movie

fun interface DiscoverMovieRemote {
    suspend fun getContent(category: DiscoverCategory): List<Movie>
}
