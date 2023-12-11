package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.services.model.Movie
import javax.inject.Inject

class DiscoverPopularContentStore @Inject constructor(
    private val discoverMovieRemote: DiscoverMovieRemote,
    private val allPopularDao: AllPopularDao,
) {
    suspend fun getPopularContent(category: DiscoverCategory.Popular): List<Movie> {
        val movies = allPopularDao.getPopularMovies(category.genre)
        return movies.ifEmpty {
            discoverMovieRemote
                .getContent(category)
                .also { movies -> allPopularDao.insert(movies) }
        }
    }
}
