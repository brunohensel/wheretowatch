package dev.bruno.wheretowatch

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import dev.bruno.wheretowatch.di.AppComponent

class WhereToWatchApp : Application(), ImageLoaderFactory {

    val appComponent by lazy { AppComponent.create(this) }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }
}