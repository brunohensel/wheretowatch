package dev.bruno.wheretowatch.di.viewmodel

import androidx.lifecycle.ViewModel
import javax.inject.Provider

typealias ViewModelMap = Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
