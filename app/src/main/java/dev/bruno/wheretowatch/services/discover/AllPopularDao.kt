package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.GetPopularMovies
import dev.bruno.wheretowatch.MovieEntity

interface AllPopularDao {
    suspend fun getPopularEntity(): List<GetPopularMovies>
    suspend fun insertPopularEntity(entities: List<MovieEntity>)
}
