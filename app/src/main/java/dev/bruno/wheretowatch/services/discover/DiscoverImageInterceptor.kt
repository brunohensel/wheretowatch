package dev.bruno.wheretowatch.services.discover

import coil3.intercept.Interceptor
import coil3.request.ImageResult
import coil3.size.Dimension
import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.services.images.ImageUrlResolver
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class DiscoverImageInterceptor @Inject constructor(
    private val urlResolver: ImageUrlResolver,
) : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        return when (val value = chain.request.data) {
            is DiscoveryImageModel -> {
                val rq = chain.request.newBuilder()
                    .data(buildTrendUrl(value, chain.size.width))
                    .build()

                chain.withRequest(rq).proceed()
            }

            else -> chain.proceed()
        }
    }

    private fun buildTrendUrl(model: DiscoveryImageModel, width: Dimension): String {
        val path = when (model.type) {
            ImageType.Backdrop -> model.backdropPath
            ImageType.Poster -> model.posterPath
        }

        return urlResolver.resolve(
            type = model.type,
            path = path ?: "",
            width = width
        )
    }
}
