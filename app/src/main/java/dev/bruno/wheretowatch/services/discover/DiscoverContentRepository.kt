package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.services.model.Movie
import javax.inject.Inject

class DiscoverPopularContentStore @Inject constructor(
    private val discoverMovieRemote: DiscoverMovieRemote,
    private val allPopularDao: AllPopularDao,
) {
    suspend fun getPopularContent(category: DiscoverCategory.Popular): List<Movie> {
        val movies = allPopularDao.getPopularMovies(category.genre)
        //TODO add a more robust logic when add pagination
        return if (movies.size < 20) {
            discoverMovieRemote
                .getContent(category)
                .also { allPopularDao.insert(it) }
        } else {
            movies
        }
    }
}
