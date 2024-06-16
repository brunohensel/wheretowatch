package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.services.model.Movie

interface MovieCollectionDao : MovieInserterDao {
    suspend fun getMovies(collection: MovieCollection): List<Movie>
}

interface MovieStreamDao {
    suspend fun getMovies(stream: StreamerProvider): List<Movie>
    suspend fun insert(stream: StreamerProvider ,movies: List<Movie>)
}

interface MovieDao : MovieInserterDao {
    suspend fun getMovies(genre: MovieGenre): List<Movie>
}

interface MovieInserterDao {
    suspend fun insert(movies: List<Movie>)
}
