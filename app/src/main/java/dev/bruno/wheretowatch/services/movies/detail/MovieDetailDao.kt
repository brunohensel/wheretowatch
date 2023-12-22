package dev.bruno.wheretowatch.services.movies.detail

import dev.bruno.wheretowatch.services.model.MovieDetails
import dev.bruno.wheretowatch.services.model.MovieWithDetails

interface MovieDetailDao {
    suspend fun getMovieDetails(id: Int): MovieWithDetails?
    suspend fun insertMovieDetails(details: MovieDetails)
}
