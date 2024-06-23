package dev.bruno.wheretowatch.services.movies.detail

import coil3.intercept.Interceptor
import coil3.request.ImageResult
import coil3.size.Dimension
import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.images.ImageSizeChooser
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

        val sizes = supportedQuality.keys.toList()
        val bestSize = ImageSizeChooser.chooseBestSize(sizes, width.px)

        return YT_THUMBNAIL_URL.format(model.key, supportedQuality[bestSize])
    }

    private val supportedQuality = mapOf(
        "320" to MQ_DEFAULT,
        "480" to HQ_DEFAULT,
        "640" to SDD_DEFAULT,
        "1280" to MAX_RES_DEFAULT,
    )

    private companion object {

        const val MQ_DEFAULT = "mqdefault" //Medium quality 320x180
        const val HQ_DEFAULT = "hqdefault" //High quality 480x360
        const val SDD_DEFAULT = "sddefault" //Standard resolution 640x480
        const val MAX_RES_DEFAULT = "maxresdefault" //Maximum resolution 1280x720
        const val YT_THUMBNAIL_URL = "https://i.ytimg.com/vi/%s/%s.jpg"
    }
}
