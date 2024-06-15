package dev.bruno.wheretowatch.di.viewmodel

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

abstract class ViewModelFactory {

    protected abstract fun ViewModelProviderFactory(
        defaultArgs: Bundle? = null,
        navHostController: NavHostController?,
    ): ViewModelProvider.Factory

    @PublishedApi
    internal fun createViewModelFactory(
        defaultArgs: Bundle? = null,
        navHostController: NavHostController?,
    ): ViewModelProvider.Factory = ViewModelProviderFactory(defaultArgs, navHostController)


    @PublishedApi
    @Composable
    internal inline fun <reified T : ViewModel> getViewModel(
        extras: Bundle? = null,
        navController: NavHostController?,
    ): T {
        return viewModel(
            key = T::class.java.name,
            factory = createViewModelFactory(defaultArgs = extras, navController)
        )
    }
}
