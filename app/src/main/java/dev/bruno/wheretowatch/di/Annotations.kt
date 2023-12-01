package dev.bruno.wheretowatch.di

import android.app.Activity
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