package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.services.model.Movie
import javax.inject.Inject

class DiscoverContentStore @Inject constructor(
    private val discoverMovieRemote: DiscoverMovieRemote,
    private val allPopularDao: AllPopularDao,
    private val collectionDao: MovieCollectionDao,
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

    suspend fun getCollectionContent(movieCollection: MovieCollection): List<Movie> {
        val collection = collectionDao.getMovieCollection(collection = movieCollection)
        return collection.ifEmpty {
            discoverMovieRemote
                .getContent(DiscoverCategory.Collection(movieCollection))
                .also { collectionDao.insert(it) }
        }
    }
}
