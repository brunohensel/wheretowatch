package dev.bruno.wheretowatch.features.movies

import dev.bruno.wheretowatch.ds.components.ImageModelBuilder
import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.services.discover.DiscoveryImageModel
import kotlinx.datetime.LocalDate

data class MovieDetailsItem(
    val id: Int = Int.MIN_VALUE,
    val title: String = "",
    val overview: String = "",
    val originalTitle: String = "",
    val popularity: Float = 0f,
    val voteCount: Int = 0,
    val voteAverage: Float = 0f,
    val genresIds: List<Int> = emptyList(),
    val releaseDate: LocalDate? = null,
    val homePage: String = "",
    val budget: Long = 0L,
    val revenue: Long = 0L,
    val runtime: Int = 0,
    val tagline: String = "",
    val collectionId: Int? = null,
    override val buildImgModel: (type: ImageType) -> DiscoveryImageModel,
) : ImageModelBuilder<DiscoveryImageModel> {

    companion object {
        val EMPTY_ITEM = MovieDetailsItem(
            buildImgModel = { type ->
                DiscoveryImageModel(null, null, type)
            }
        )
    }
}
