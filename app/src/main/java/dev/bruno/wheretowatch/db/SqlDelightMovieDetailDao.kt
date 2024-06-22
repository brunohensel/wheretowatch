package dev.bruno.wheretowatch.db

import com.squareup.anvil.annotations.ContributesBinding
import dagger.Reusable
import dev.bruno.wheretowatch.MovieAndDetail
import dev.bruno.wheretowatch.WhereToWatchDatabase
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.model.MovieDetails
import dev.bruno.wheretowatch.services.model.MovieWithDetails
import dev.bruno.wheretowatch.services.movies.detail.MovieDetailDao
import javax.inject.Inject

@Reusable
@ContributesBinding(AppScope::class)
class SqlDelightMovieDetailDao @Inject constructor(
    private val db: WhereToWatchDatabase,
) : MovieDetailDao {
    override suspend fun getMovieDetails(id: Int): MovieWithDetails? {
        return db.movieAndDetailQueries
            .getMovieDetails(
                movieId = id,
                mapper = { movieId, homePage, budget, revenue, runtime, tagline, collectionId,
                           movieVideos, _id, title, overview, popularity, genres, originalTitle, voteCount,
                           voteAverage, releaseDate, posterPath, backdropPath, _collectionId, providers ->
                    MovieWithDetails(
                        id = movieId,
                        title = title,
                        overview = overview,
                        originalTitle = originalTitle,
                        popularity = popularity,
                        voteCount = voteCount,
                        voteAverage = voteAverage,
                        genresIds = genres,
                        releaseDate = releaseDate,
                        homePage = homePage,
                        budget = budget,
                        revenue = revenue,
                        runtime = runtime,
                        tagline = tagline,
                        collectionId = _collectionId,
                        posterPath = posterPath,
                        backdropPath = backdropPath,
                        videos = movieVideos,
                    )
                }
            ).executeAsOneOrNull()
    }

    override suspend fun insertMovieDetails(details: MovieDetails) {
        db.movieAndDetailQueries
            .insertMovieDetail(
                MovieAndDetail(
                    movieId = details.movieId,
                    homePage = details.homePage,
                    budget = details.budget,
                    revenue = details.revenue,
                    runtime = details.runtime,
                    tagline = details.tagline,
                    collectionId = details.collectionId,
                    videos = details.videos,
                )
            )
    }
}
