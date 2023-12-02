package dev.bruno.wheretowatch.di

import android.app.Activity
import android.content.Context
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import dev.bruno.wheretowatch.Initializers
import javax.inject.Provider

@SingleIn(AppScope::class)
@MergeComponent(scope = AppScope::class)
interface AppComponent {

    val activityProviders: Map<Class<out Activity>, @JvmSuppressWildcards Provider<Activity>>
    val initializers: Set<@JvmSuppressWildcards Initializers>

    @Component.Factory
    interface Factory {
        fun create(@ApplicationContext @BindsInstance context: Context): AppComponent
    }

    companion object {
        fun create(context: Context): AppComponent = DaggerAppComponent.factory().create(context)
    }
}