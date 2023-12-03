package dev.bruno.wheretowatch.services.trending

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.model.TrendingItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class TrendingSupplierImpl @Inject constructor(
    private val trendingRemote: TrendingRemote,
) : TrendingSupplier {

    override suspend fun get(window: TrendingSupplier.TrendWindow): ImmutableList<TrendingItem> {
        val windowName = window.name.lowercase()
        val dto = trendingRemote.getTrending(windowName).results
        return dto.asTrendingItems()
    }
}

private fun List<TrendingDto>.asTrendingItems(): ImmutableList<TrendingItem> {
    return this
        .map { dtoItem ->
            TrendingItem(
                id = dtoItem.id,
                genreIds = dtoItem.genreIds,
                mediaType = dtoItem.mediaType,
                originalLanguage = dtoItem.originalLanguage,
                originalTitle = dtoItem.title ?: dtoItem.name ?: dtoItem.originalTitle ?: "",
                overview = dtoItem.overview,
                popularity = dtoItem.popularity,
                posterPath = dtoItem.posterPath,
                backdropPath = dtoItem.backdropPath,
                releaseDate = dtoItem.releaseDate ?: "",
                video = dtoItem.video ?: false,
                voteAverage = dtoItem.voteAverage,
                voteCount = dtoItem.voteCount,
            )
        }.toImmutableList()
}
