package dev.bruno.wheretowatch.services.streamproviders

import coil.intercept.Interceptor
import coil.request.ImageResult
import coil.size.Dimension
import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.images.ImageUrlResolver
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class StreamProviderImageInterceptor @Inject constructor(
    private val urlResolver: ImageUrlResolver,
) : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val rq = when (val value = chain.request.data) {
            is StreamProviderImageModel -> {
                chain.request.newBuilder()
                    .data(buildTrendUrl(value, chain.size.width))
                    .build()
            }

            else -> chain.request
        }

        return chain.proceed(rq)
    }


    private fun buildTrendUrl(model: StreamProviderImageModel, width: Dimension): String {

        return urlResolver.resolve(
            type = model.type,
            path = model.logoPath,
            width = width
        )
    }
}
