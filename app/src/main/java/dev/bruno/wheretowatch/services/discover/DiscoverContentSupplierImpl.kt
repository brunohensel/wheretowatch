package dev.bruno.wheretowatch.services.discover

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.model.DiscoverContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class DiscoverContentSupplierImpl @Inject constructor(
    private val discoverMovieRemote: DiscoverMovieRemote,
) : DiscoverContentSupplier {
    override suspend fun get(category: DiscoverCategory): ImmutableList<DiscoverContent> {
        return discoverMovieRemote.getContent(category).results
            .map { dto ->
                DiscoverContent(
                    id = dto.id,
                    title = dto.title,
                    popularity = dto.popularity,
                    genresIds = dto.genresIds,
                    originalTitle = dto.originalTitle,
                    originalLanguage = dto.originalLanguage,
                    voteCount = dto.voteCount,
                    voteAverage = dto.voteAverage,
                    posterPath = dto.posterPath,
                    backdropPath = dto.backdropPath,
                )
            }.toImmutableList()
    }
}
