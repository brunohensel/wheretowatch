package dev.bruno.wheretowatch.di

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.intercept.Interceptor
import coil3.memory.MemoryCache
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides

@Module
@ContributesTo(AppScope::class)
object CoilModule {

    @SingleIn(AppScope::class)
    @Provides
    fun providesImageLoader(
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
        @ApplicationContext context: Context
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder()
                    // Set the max size to 25% of the app's available memory.
                    .maxSizePercent(context, percent = 0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .build()
            }
            .crossfade(true)
            .apply {
                logger(DebugLogger())
            }
            .components {
                add(SvgDecoder.Factory())
                for (interceptor in interceptors) {
                    add(interceptor)
                }
            }
            .build()
    }
}
