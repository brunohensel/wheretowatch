package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.services.model.Movie

interface AllPopularDao : MovieDao {
    suspend fun getPopularMovies(genre: MovieGenre): List<Movie>
}

interface MovieDao {
    suspend fun insert(movies: List<Movie>)
}
