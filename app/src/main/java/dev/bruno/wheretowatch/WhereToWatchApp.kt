package dev.bruno.wheretowatch

import android.app.Application
import dev.bruno.wheretowatch.di.AppComponent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class WhereToWatchApp : Application() {

    val appComponent by lazy { AppComponent.create(this) }
    private val initializers by lazy { appComponent.initializers }

    // TODO to check if the better approach would be to use ProcessLifecycleOwner from
    //  androidx.lifecycle:lifecycle-process:$lifecycle_version
    private val scope = MainScope()
    override fun onCreate() {
        super.onCreate()

        scope.launch {
            for (initializer in initializers) {
                initializer.init()
            }
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        scope.cancel()
    }
}
