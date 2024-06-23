package dev.bruno.wheretowatch.services.images

import coil3.size.Dimension
import dev.bruno.wheretowatch.ds.components.ImageType
import javax.inject.Inject

class ImageUrlResolver @Inject constructor(
    private val imageConfigSupplier: ImageConfigSupplier,
) {

    fun resolve(type: ImageType, path: String, width: Dimension): String {
        if (width !is Dimension.Pixels) return ""

        return when (type) {
            ImageType.Backdrop -> resolveBackdropUrl(width, path)
            ImageType.Poster -> resolvePosterUrl(width, path)
        }
    }

    private fun resolveBackdropUrl(width: Dimension.Pixels, path: String): String {
        val imageConfig = imageConfigSupplier.get()
        val bestSize = ImageSizeChooser.chooseBestSize(imageConfig.backdropSizes, width.px)
        return "${imageConfig.baseUrl}$bestSize$path"
    }

    private fun resolvePosterUrl(width: Dimension.Pixels, path: String): String {
        val imageConfig = imageConfigSupplier.get()
        val bestSize = ImageSizeChooser.chooseBestSize(imageConfig.posterSizes, width.px)
        return "${imageConfig.baseUrl}$bestSize$path"
    }
}
