package dev.bruno.wheretowatch.services.movies.detail

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.features.movies.MovieDetailsItem
import dev.bruno.wheretowatch.services.discover.DiscoveryImageModel
import dev.bruno.wheretowatch.services.model.MovieWithDetails
import javax.inject.Inject

interface MovieDetailsSupplier {
    suspend fun get(movieId: Int): MovieDetailsItem
}

@ContributesBinding(AppScope::class)
class MovieDetailStore @Inject constructor(
    private val movieDetailsRemote: MovieDetailsRemote,
    private val movieDetailDao: MovieDetailDao,
) : MovieDetailsSupplier {
    override suspend fun get(movieId: Int): MovieDetailsItem {
        val localMovieDetail = movieDetailDao.getMovieDetails(movieId)
        if (localMovieDetail == null) {
            movieDetailsRemote.fetchMovieDetail(movieId)
                .let { dto -> movieDetailDao.insertMovieDetails(dto) }

            return movieDetailDao.getMovieDetails(movieId).toMovieDetailItem()
        }
        return localMovieDetail.toMovieDetailItem()
    }

    private fun MovieWithDetails?.toMovieDetailItem(): MovieDetailsItem {
        requireNotNull(this)
        return MovieDetailsItem(
            id = id,
            title = title,
            overview = overview,
            originalTitle = originalTitle,
            popularity = popularity,
            voteCount = voteCount,
            voteAverage = voteAverage,
            genresIds = genresIds,
            releaseDate = releaseDate,
            homePage = homePage,
            budget = budget,
            revenue = revenue,
            runtime = runtime,
            tagline = tagline,
            collectionId = collectionId,
            videos = videos,
            buildImgModel = partialImgModel()
        )
    }

    private fun MovieWithDetails.partialImgModel(): (ImageType) -> DiscoveryImageModel = { type ->
        DiscoveryImageModel(backdropPath = backdropPath, posterPath = posterPath, type)
    }
}
