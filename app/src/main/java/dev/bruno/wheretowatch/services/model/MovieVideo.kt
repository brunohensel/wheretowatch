package dev.bruno.wheretowatch.services.model

import androidx.compose.runtime.Immutable
import dev.bruno.wheretowatch.ds.components.ImageModelBuilder
import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.services.movies.detail.VideoImageModel
import kotlinx.datetime.LocalDate

@Immutable
data class MovieVideo(
    val id: String,
    val type: String,
    val key: String,
    val site: String,
    val official: Boolean,
    val publishedDate: LocalDate?,
) : ImageModelBuilder<VideoImageModel> {

    override val buildImgModel: (type: ImageType) -> VideoImageModel = { _ ->
        VideoImageModel(key)
    }
}
