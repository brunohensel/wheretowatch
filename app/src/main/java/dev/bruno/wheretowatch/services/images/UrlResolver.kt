package dev.bruno.wheretowatch.services.images

private val IMAGE_SIZE_PATTERN = "w(\\d+)$".toRegex()

object ImageSizeChooser {

    // Get it from tivi https://github.com/chrisbanes/tivi
    // we will iterate over all possible supported sizes for a particular type and pick the best
    // based on the width calculated during the layout phase by the [ConstraintsSizeResolver]
    fun chooseBestSize(
        possibleSizes: List<String>,
        componentWidth: Int,
    ): String {
        var previousSize: String? = null
        var previousWidth = 0

        for (i in possibleSizes.indices) {
            val size = possibleSizes[i]
            val sizeWidth = extractWidthAsIntFrom(size) ?: continue

            if (sizeWidth > componentWidth) {
                if (previousSize != null && componentWidth > (previousWidth + sizeWidth) / 2) {
                    return size
                } else if (previousSize != null) {
                    return previousSize
                }
            } else if (i == possibleSizes.size - 1) {
                if (componentWidth < sizeWidth * 2) {
                    return size
                }
            }

            previousSize = size
            previousWidth = sizeWidth
        }

        return previousSize ?: possibleSizes.last()
    }

    private fun extractWidthAsIntFrom(size: String): Int? {
        //in case the size is not formated like w500
        if (size.first() != 'w') return size.toIntOrNull()
        return IMAGE_SIZE_PATTERN.matchEntire(size)?.groups?.get(1)?.value?.toInt()
    }
}
