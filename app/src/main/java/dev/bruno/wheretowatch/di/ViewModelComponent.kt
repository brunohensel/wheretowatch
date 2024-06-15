package dev.bruno.wheretowatch.di

import androidx.lifecycle.SavedStateHandle
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Subcomponent
import dev.bruno.wheretowatch.Navigator
import dev.bruno.wheretowatch.di.viewmodel.ViewModelMap

@SingleIn(ViewModelScope::class)
@MergeSubcomponent(ViewModelScope::class)
interface ViewModelComponent {

    fun getViewModelMap(): ViewModelMap

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance savedStateHandle: SavedStateHandle,
            @BindsInstance navigator: Navigator,
        ): ViewModelComponent
    }
}
