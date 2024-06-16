package dev.bruno.wheretowatch.services.images

import coil3.size.Dimension
import dev.bruno.wheretowatch.ds.components.ImageType
import javax.inject.Inject

private val IMAGE_SIZE_PATTERN = "w(\\d+)$".toRegex()

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
        val bestSize = pickBestSize(imageConfig.backdropSizes, width.px)
        return "${imageConfig.baseUrl}$bestSize$path"
    }

    private fun resolvePosterUrl(width: Dimension.Pixels, path: String): String {
        val imageConfig = imageConfigSupplier.get()
        val bestSize = pickBestSize(imageConfig.posterSizes, width.px)
        return "${imageConfig.baseUrl}$bestSize$path"
    }

    // Get it from tivi https://github.com/chrisbanes/tivi
    // we will iterate over all possible supported sizes for a particular type and pick the best
    // based on the width calculated during the layout phase by the [ConstraintsSizeResolver]
    private fun pickBestSize(sizes: List<String>, width: Int): String {
        var previousSize: String? = null
        var previousWidth = 0

        for (i in sizes.indices) {
            val size = sizes[i]
            val sizeWidth = extractWidthAsIntFrom(size) ?: continue

            if (sizeWidth > width) {
                if (previousSize != null && width > (previousWidth + sizeWidth) / 2) {
                    return size
                } else if (previousSize != null) {
                    return previousSize
                }
            } else if (i == sizes.size - 1) {
                if (width < sizeWidth * 2) {
                    return size
                }
            }

            previousSize = size
            previousWidth = sizeWidth
        }

        return previousSize ?: sizes.last()
    }

    private fun extractWidthAsIntFrom(size: String): Int? {
        return IMAGE_SIZE_PATTERN.matchEntire(size)?.groups?.get(1)?.value?.toInt()
    }
}
