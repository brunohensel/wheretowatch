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
import dev.bruno.wheretowatch.features.discover.DiscoverScreen
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

            CircuitCompositionLocals(circuit = circuit) {
                WhereToWatchTheme(darkTheme = applyDarkTheme) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val backstack = rememberSaveableBackStack(listOf(DiscoverScreen))
                        val circuitNav = rememberCircuitNavigator(backStack = backstack)

                        Scaffold { paddingValues ->
                            NavigableCircuitContent(
                                navigator = circuitNav,
                                backStack = backstack,
                                modifier = Modifier
                                    // this don't do anything since we are not setting top bar in this scaffold
                                    // the bottom bar collapse when user scroll the feed, so we don't need to
                                    // reserve an space there by passing padding value for the bottom slot.
                                    .padding(top = paddingValues.calculateTopPadding())
                                    .fillMaxSize(),
                            )
                        }
                    }
                }
            }
        }
    }
}
