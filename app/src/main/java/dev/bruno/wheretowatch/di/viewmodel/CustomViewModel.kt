package dev.bruno.wheretowatch.di.viewmodel

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dev.bruno.wheretowatch.di.viewmodel.MyViewModelFactory.Companion.MyViewModelFactory

@Composable
inline fun <reified T : ViewModel> viewModel(
    navController: NavHostController? = null,
    extras: Bundle? = null,
): T {
    val factory = MyViewModelFactory()
    return factory.getViewModel(extras, navController)
}
