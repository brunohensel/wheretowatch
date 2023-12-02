package dev.bruno.wheretowatch

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.AppPreferences.ThemeConfig.AUTO
import dev.bruno.wheretowatch.AppPreferences.ThemeConfig.DARK
import dev.bruno.wheretowatch.AppPreferences.ThemeConfig.LIGHT
import dev.bruno.wheretowatch.di.ActivityKey
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.ds.theme.WhereToWatchTheme
import dev.bruno.wheretowatch.features.home.HomeScreen
import javax.inject.Inject

@ContributesMultibinding(scope = AppScope::class, boundType = Activity::class)
@ActivityKey(MainActivity::class)
class MainActivity @Inject constructor(
    private val circuit: Circuit,
    private val preferences: AppPreferences,
) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val currentThemeConfig by preferences.themeConfig.collectAsState(initial = AUTO)
            val applyDarkTheme = when (currentThemeConfig) {
                AUTO -> isSystemInDarkTheme()
                LIGHT -> false
                DARK -> true
            }

            WhereToWatchTheme(darkTheme = applyDarkTheme) {
                CircuitCompositionLocals(circuit = circuit) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val backstack = rememberSaveableBackStack { push(HomeScreen) }
                        val circuitNav = rememberCircuitNavigator(backstack = backstack)
                        NavigableCircuitContent(navigator = circuitNav, backstack)
                    }
                }
            }
        }
    }
}
