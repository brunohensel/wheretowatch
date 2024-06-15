package dev.bruno.wheretowatch.di

import android.app.Activity
import androidx.lifecycle.ViewModel
import dagger.MapKey
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.reflect.KClass

@Scope
annotation class SingleIn(val scope: KClass<*>)

@Qualifier
annotation class ApplicationContext

@MapKey
annotation class ActivityKey(val value: KClass<out Activity>)

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(value = AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
