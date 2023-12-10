package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.services.model.Movie

interface AllPopularDao {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun insertPopularMovies(movies: List<Movie>)
}
