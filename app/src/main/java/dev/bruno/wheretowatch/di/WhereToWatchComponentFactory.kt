package dev.bruno.wheretowatch.di

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.core.app.AppComponentFactory
import dev.bruno.wheretowatch.WhereToWatchApp
import javax.inject.Provider

class WhereToWatchComponentFactory : AppComponentFactory() {

    override fun instantiateApplicationCompat(cl: ClassLoader, className: String): Application {
        val app = super.instantiateApplicationCompat(cl, className) as WhereToWatchApp
        activityProviders = app.appComponent.activityProviders
        return app
    }

    override fun instantiateActivityCompat(
        cl: ClassLoader,
        className: String,
        intent: Intent?
    ): Activity {
        return getInstance(cl, className, activityProviders)
            ?: super.instantiateActivityCompat(cl, className, intent)
    }

    private inline fun <reified T> getInstance(
        cl: ClassLoader,
        className: String,
        providers: Map<Class<out T>, @JvmSuppressWildcards Provider<T>>,
    ): T? {
        val clazz = Class.forName(className, false, cl).asSubclass(T::class.java)
        val modelProvider = providers[clazz] ?: return null
        return modelProvider.get() as T
    }

    companion object {
        private lateinit var activityProviders: Map<Class<out Activity>, Provider<Activity>>
    }
}