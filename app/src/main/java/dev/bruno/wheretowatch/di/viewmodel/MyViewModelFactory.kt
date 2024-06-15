package dev.bruno.wheretowatch.di.viewmodel

import android.os.Bundle
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.NavHostController
import dev.bruno.wheretowatch.Navigator
import dev.bruno.wheretowatch.di.ActivityComponentHolder
import dev.bruno.wheretowatch.di.ViewModelComponent

class MyViewModelFactory private constructor(
    private val viewModelComponentFactory: ViewModelComponent.Factory,
) : ViewModelFactory() {

    override fun ViewModelProviderFactory(
        defaultArgs: Bundle?,
        navHostController: NavHostController?,
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                if (defaultArgs != null) {
                    MutableCreationExtras(extras).apply {
                        set(DEFAULT_ARGS_KEY, defaultArgs)
                    }
                }
                val handle = extras.createSavedStateHandle()
                val navigator = if (navHostController == null) {
                    Navigator { /*no-op*/ }
                } else {
                    Navigator { navHostController.navigate(it) }
                }

                val component = viewModelComponentFactory.create(handle, navigator)
                val provider = component.getViewModelMap()
                val clazz = Class.forName(modelClass.name)
                return provider[clazz]?.get() as T
            }
        }
    }

    companion object {

        fun MyViewModelFactory(): MyViewModelFactory {
            return MyViewModelFactory(
                viewModelComponentFactory = ActivityComponentHolder
                    .getComponent()
                    .getViewModelComponentFactory()
            )
        }
    }
}
