package dev.bruno.wheretowatch

import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.intercept.Interceptor
import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.di.ApplicationContext
import javax.inject.Inject

@ContributesMultibinding(AppScope::class, boundType = Initializers::class)
class CoilInitializer @Inject constructor(
    private val interceptors: Set<@JvmSuppressWildcards Interceptor>,
    @ApplicationContext private val context: Context,
) : Initializers {
    override suspend fun init() {
        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
                for (interceptor in interceptors) {
                    add(interceptor)
                }
            }
            .build()

        Coil.setImageLoader(imageLoader)
    }
}
