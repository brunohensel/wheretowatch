package dev.bruno.wheretowatch.network.api.discover

import dev.bruno.wheretowatch.services.model.Movie

internal fun DiscoverContentResultDto.toMovies(): List<Movie> = this.results.toMovies()

internal fun MovieCollectionDto.toMovies(): List<Movie> = this.parts.toMovies()

private fun List<DiscoverContentDto>.toMovies(): List<Movie> = this.map { dto ->
    Movie(
        id = dto.id,
        title = dto.title,
        overview = dto.overview,
        originalTitle = dto.originalTitle,
        popularity = dto.popularity,
        voteCount = dto.voteCount,
        voteAverage = dto.voteAverage,
        genresIds = dto.genresIds,
        releaseDate = dto.releaseDate,
        posterPath = dto.posterPath,
        backdropPath = dto.backdropPath,
    )
}
