package dev.bruno.wheretowatch.services.movies.detail

import dev.bruno.wheretowatch.services.model.MovieDetails

interface MovieDetailsRemote {
    suspend fun fetchMovieDetail(movieId: Int): MovieDetails
}
