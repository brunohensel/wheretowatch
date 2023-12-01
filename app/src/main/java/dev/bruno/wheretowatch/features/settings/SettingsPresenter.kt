package dev.bruno.wheretowatch.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dev.bruno.wheretowatch.AppPreferences
import dev.bruno.wheretowatch.AppPreferences.ThemeConfig
import dev.bruno.wheretowatch.services.country.CountriesSupplier
import dev.bruno.wheretowatch.services.country.model.Country
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.features.settings.SettingsScreen.State
import kotlinx.coroutines.launch
import javax.inject.Inject

@CircuitInject(SettingsScreen::class, AppScope::class)
class SettingsPresenter @Inject constructor(
    private val preferences: AppPreferences,
    private val countriesSupplier: CountriesSupplier,
) : Presenter<State> {

    @Composable
    override fun present(): State {

        var defaultCountry by rememberRetained { mutableStateOf(Country("BR", "Brazil")) }
        var state: State by rememberRetained { mutableStateOf(State.Loading) }
        val scope = rememberStableCoroutineScope()
        val currentThemeConfig by preferences.themeConfig.collectAsState(initial = ThemeConfig.AUTO)
        val producedState by produceState(initialValue = state) {
            val countries = countriesSupplier.get()

            fun onEventSink(event: SettingsScreen.Event) {
                when (event) {
                    is SettingsScreen.Event.NewCountrySelected -> {
                        defaultCountry = countries.getOrNull(event.index) ?: defaultCountry
                        val oldValue = value as State.Success
                        val newValue = oldValue.copy(selectedCounty = defaultCountry)
                        value = newValue
                    }

                    is SettingsScreen.Event.ThemeConfigSelected -> {
                        scope.launch { preferences.setThemeConfig(event.newTheme) }
                        val oldValue = value as State.Success
                        val newValue = oldValue.copy(themeConfig = event.newTheme)
                        value = newValue
                    }
                }
            }

            value = State.Success(
                countries = countries,
                selectedCounty = defaultCountry,
                themeConfig = currentThemeConfig,
                event = ::onEventSink,
            )
        }
        state = producedState
        return state
    }
}