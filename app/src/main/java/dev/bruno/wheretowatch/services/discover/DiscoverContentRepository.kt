package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.services.model.Movie
import javax.inject.Inject

class DiscoverPopularContentStore @Inject constructor(
    private val discoverMovieRemote: DiscoverMovieRemote,
    private val allPopularDao: AllPopularDao,
) {
    suspend fun getPopularContent(category: DiscoverCategory): List<Movie> {
        return if (category == DiscoverCategory.Popular(MovieGenre.ALL)) {
            val movies = allPopularDao.getPopularMovies()
            movies.ifEmpty {
                discoverMovieRemote.getContent(category)
                    .also { movies -> allPopularDao.insertPopularMovies(movies) }
            }
        } else {
            discoverMovieRemote.getContent(category)
        }
    }
}
