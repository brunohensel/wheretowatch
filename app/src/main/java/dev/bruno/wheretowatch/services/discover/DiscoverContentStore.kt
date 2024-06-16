package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.services.model.Movie
import javax.inject.Inject

class DiscoverContentStore @Inject constructor(
    private val discoverMovieRemote: DiscoverMovieRemote,
    private val movieDao: MovieDao,
    private val collectionDao: MovieCollectionDao,
    private val movieStreamDao: MovieStreamDao,
) {

    suspend fun getMovies(category: DiscoverCategory): List<Movie> {
        return when (category) {
            is DiscoverCategory.Popular -> getMovies(category)
            is DiscoverCategory.Collection -> getMovies(category.collection)
            is DiscoverCategory.Streaming -> getMovies(category.provider)
            else -> discoverMovieRemote.getContent(category)
        }
    }

    private suspend fun getMovies(provider: StreamerProvider): List<Movie> {
        val movies = movieStreamDao.getMovies(provider)
        return movies.ifEmpty {
            discoverMovieRemote
                .getContent(DiscoverCategory.Streaming(provider))
                .also { movieStreamDao.insert(provider, it) }
        }
    }

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
