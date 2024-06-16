package dev.bruno.wheretowatch.services.discover

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.model.DiscoverContent
import dev.bruno.wheretowatch.services.model.Movie
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class DiscoverContentSupplierImpl @Inject constructor(
    private val discoverMovieRemote: DiscoverMovieRemote,
    private val discoverContentStore: DiscoverContentStore,
) : DiscoverContentSupplier {
    override suspend fun get(category: DiscoverCategory): List<DiscoverContent> {
        return when (category) {
            is DiscoverCategory.Collection -> discoverContentStore.getMovies(category.collection)
            is DiscoverCategory.Popular -> discoverContentStore.getMovies(category)
            else -> discoverMovieRemote.getContent(category)
        }.intoDiscoverContent()
    }

    private fun List<Movie>.intoDiscoverContent() = map { dto ->
        DiscoverContent(
            id = dto.id,
            title = dto.title,
            popularity = dto.popularity,
            genresIds = dto.genresIds,
            originalTitle = dto.originalTitle,
            voteCount = dto.voteCount,
            voteAverage = dto.voteAverage,
            releaseDate = dto.releaseDate,
            posterPath = dto.posterPath,
            backdropPath = dto.backdropPath,
        )
    }
}
