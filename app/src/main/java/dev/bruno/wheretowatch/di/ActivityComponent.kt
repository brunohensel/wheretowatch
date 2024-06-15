package dev.bruno.wheretowatch.di

import android.app.Activity
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Subcomponent

@SingleIn(ActivityScope::class)
@MergeSubcomponent(ActivityScope::class)
interface ActivityComponent {

    fun getViewModelComponentFactory(): ViewModelComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: Activity): ActivityComponent
    }
}
