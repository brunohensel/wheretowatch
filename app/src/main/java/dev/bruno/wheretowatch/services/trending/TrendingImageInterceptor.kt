package dev.bruno.wheretowatch.services.trending

import android.content.Context
import coil.intercept.Interceptor
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.size.Dimension
import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.di.ApplicationContext
import dev.bruno.wheretowatch.ds.components.ImageType.Backdrop
import dev.bruno.wheretowatch.ds.components.ImageType.Poster
import dev.bruno.wheretowatch.services.images.ImageUrlResolver
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class TrendingImageInterceptor @Inject constructor(
    private val urlResolver: ImageUrlResolver,
    @ApplicationContext private val context: Context,
) : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val rq = when (val value = chain.request.data) {
            is TrendingImageModel -> {
                ImageRequest.Builder(context)
                    .data(buildTrendUrl(value, chain.size.width))
                    .build()
            }

            else -> chain.request
        }

        return chain.proceed(rq)
    }

    private fun buildTrendUrl(model: TrendingImageModel, width: Dimension): String {
        val path = when (model.type) {
            Backdrop -> model.backdropPath
            Poster -> model.posterPath
        }

        return urlResolver.resolve(
            type = model.type,
            path = path ?: "",
            width = width
        )
    }
}
