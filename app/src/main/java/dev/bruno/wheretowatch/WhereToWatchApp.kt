package dev.bruno.wheretowatch

import android.app.Application
import dev.bruno.wheretowatch.di.AppComponent

class WhereToWatchApp : Application() {

    val appComponent by lazy { AppComponent.create(this) }
}