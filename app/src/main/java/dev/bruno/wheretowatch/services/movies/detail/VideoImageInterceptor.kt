package dev.bruno.wheretowatch.services.movies.detail

import coil3.intercept.Interceptor
import coil3.request.ImageResult
import coil3.size.Dimension
import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.di.AppScope
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class VideoImageInterceptor @Inject constructor() : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        return when (val value = chain.request.data) {
            is VideoImageModel -> {
                println("HENSEL size from reuqest: ${chain.request.sizeResolver.size().width}")
                println("HENSEL size from chain: ${chain.size.width}")

                val rq = chain.request.newBuilder()
                    .data(buildVideoUrl(value, chain.size.width))
                    .build()

                chain.withRequest(rq).proceed()
            }

            else -> chain.proceed()
        }
    }

    private fun buildVideoUrl(model: VideoImageModel, width: Dimension): String {
        if (width !is Dimension.Pixels) return ""

        val resolution = pickBestResolution(width)

        return YT_THUMBNAIL_URL.format(model.key, resolution)
    }

    private val supportedQuality = mapOf(
        MQ_DEFAULT to 320,
        HQ_DEFAULT to 480,
        SDD_DEFAULT to 640,
        MAX_RES_DEFAULT to 1280
    )

    private fun pickBestResolution(dimension: Dimension.Pixels): String {
        val width = dimension.px
        var previousSize: String? = null
        var previousWidth = 0

        for (key in supportedQuality.keys) {
            val resolution = supportedQuality[key] ?: 0

            if (resolution > width) {
                if (previousSize != null && width > (previousWidth + resolution) / 2) {
                    return key
                } else if (previousSize != null) {
                    return previousSize
                }
            } else if (key == supportedQuality.keys.last()) {
                if (width < resolution * 2) {
                    return key
                }
            }
            previousSize = key
            previousWidth = resolution
        }

        return previousSize ?: supportedQuality.keys.last()
    }

    private companion object {

        const val MQ_DEFAULT = "mqdefault" //Medium quality 320x180
        const val HQ_DEFAULT = "hqdefault" //High quality 480x360
        const val SDD_DEFAULT = "sddefault" //Standard resolution 640x480
        const val MAX_RES_DEFAULT = "maxresdefault" //Maximum resolution 1280x720
        const val YT_THUMBNAIL_URL = "https://i.ytimg.com/vi/%s/%s.jpg"
    }
}
