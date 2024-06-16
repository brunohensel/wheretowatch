package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.services.model.Movie
import javax.inject.Inject

class DiscoverContentStore @Inject constructor(
    private val discoverMovieRemote: DiscoverMovieRemote,
    private val movieDao: MovieDao,
    private val collectionDao: MovieCollectionDao,
) {
    suspend fun getMovies(category: DiscoverCategory.Popular): List<Movie> {
        val movies = movieDao.getMovies(category.genre)
        //TODO add a more robust logic when add pagination
        return if (movies.size < 20) {
            discoverMovieRemote
                .getContent(category)
                .also { movieDao.insert(it) }
        } else {
            movies
        }
    }

    suspend fun getMovies(movieCollection: MovieCollection): List<Movie> {
        val collection = collectionDao.getMovies(collection = movieCollection)
        return collection.ifEmpty {
            discoverMovieRemote
                .getContent(DiscoverCategory.Collection(movieCollection))
                .also { collectionDao.insert(it) }
        }
    }
}
