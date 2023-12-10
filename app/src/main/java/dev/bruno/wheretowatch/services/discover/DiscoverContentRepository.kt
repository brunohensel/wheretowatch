package dev.bruno.wheretowatch.services.discover

import dev.bruno.wheretowatch.GetPopularMovies
import dev.bruno.wheretowatch.MovieEntity
import javax.inject.Inject

class DiscoverPopularContentStore @Inject constructor(
    private val discoverMovieRemote: DiscoverMovieRemote,
    private val allPopularDao: AllPopularDao,
) {
    suspend fun getPopularContent(category: DiscoverCategory): DiscoverContentResultDto {
        return if (category == DiscoverCategory.Popular(MovieGenre.ALL)) {
            val entities = allPopularDao.getPopularEntity()
            if (entities.isEmpty()) {
                discoverMovieRemote.getContent(category).also {
                    allPopularDao.insertPopularEntity(it.results.toPopularEntity())
                }
            } else {
                val content = entities.toContentDto()
                DiscoverContentResultDto(results = content)
            }
        } else {
            discoverMovieRemote.getContent(category)
        }
    }

    private fun List<GetPopularMovies>.toContentDto(): List<DiscoverContentDto> {
        return map { entity ->
            DiscoverContentDto(
                id = entity.popularId,
                title = entity.title,
                popularity = entity.popularity,
                genresIds = entity.genres.split(",").map { it.trim().toInt() },
                originalTitle = entity.originalTitle,
                originalLanguage = entity.originalLanguage,
                voteCount = entity.voteCount.toInt(),
                voteAverage = entity.voteAverage,
                releaseDate = entity.releaseDate,
                posterPath = entity.posterPath,
                backdropPath = entity.backdropPath,
            )
        }
    }

    private fun List<DiscoverContentDto>.toPopularEntity(): List<MovieEntity> {
        return map { dto ->
            MovieEntity(
                id = dto.id,
                title = dto.title,
                popularity = dto.popularity,
                genres = dto.genresIds.joinToString(),
                originalTitle = dto.originalTitle,
                originalLanguage = dto.originalLanguage,
                voteCount = dto.voteCount.toLong(),
                voteAverage = dto.voteAverage,
                releaseDate = dto.releaseDate,
                posterPath = dto.posterPath,
                backdropPath = dto.backdropPath,
            )
        }
    }
}
