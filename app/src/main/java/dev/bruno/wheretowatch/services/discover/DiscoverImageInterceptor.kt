package dev.bruno.wheretowatch.services.discover

import coil.intercept.Interceptor
import coil.request.ImageResult
import coil.size.Dimension
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
        val rq = when (val value = chain.request.data) {
            is DiscoveryImageModel -> {
                chain.request.newBuilder()
                    .data(buildTrendUrl(value, chain.size.width))
                    .build()
            }

            else -> chain.request
        }

        return chain.proceed(rq)
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
