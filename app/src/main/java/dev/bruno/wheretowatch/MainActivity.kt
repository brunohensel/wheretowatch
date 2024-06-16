package dev.bruno.wheretowatch

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.AppPreferences.ThemeConfig.AUTO
import dev.bruno.wheretowatch.AppPreferences.ThemeConfig.DARK
import dev.bruno.wheretowatch.AppPreferences.ThemeConfig.LIGHT
import dev.bruno.wheretowatch.di.ActivityKey
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.ds.theme.WhereToWatchTheme
import javax.inject.Inject

@ContributesMultibinding(scope = AppScope::class, boundType = Activity::class)
@ActivityKey(MainActivity::class)
class MainActivity @Inject constructor(
    private val preferences: AppPreferences,
    private val imageLoader: ImageLoader
) : ComponentActivity() {
    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WhereToWatchApp.createComponent(this)

        setContent {
            setSingletonImageLoaderFactory { imageLoader }

            val currentThemeConfig by preferences.themeConfig.collectAsState(initial = AUTO)
            val applyDarkTheme = when (currentThemeConfig) {
                AUTO -> isSystemInDarkTheme()
                LIGHT -> false
                DARK -> true
            }

            val navController = rememberNavController()

            WhereToWatchTheme(darkTheme = applyDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Scaffold { paddingValues ->
                        AppNavigation(
                            navController = navController,
                            modifier = Modifier.padding(paddingValues)
                        )
                    }
                }
            }
        }
    }
}
