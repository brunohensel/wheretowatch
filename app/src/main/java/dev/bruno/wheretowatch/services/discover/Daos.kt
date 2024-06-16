package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.services.model.Movie

interface MovieCollectionDao : MovieInserterDao {
    suspend fun getMovies(collection: MovieCollection): List<Movie>
}

interface MovieDao : MovieInserterDao {
    suspend fun getMovies(genre: MovieGenre): List<Movie>
}

interface MovieInserterDao {
    suspend fun insert(movies: List<Movie>)
}
