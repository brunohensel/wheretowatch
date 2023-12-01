package dev.bruno.wheretowatch.features.settings

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dev.bruno.wheretowatch.AppPreferences
import dev.bruno.wheretowatch.services.country.model.Country
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.features.settings.SettingsScreen.Event.ThemeConfigSelected
import dev.bruno.wheretowatch.ui.theme.WhereToWatchTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize

@Parcelize
data object SettingsScreen : Screen {
    sealed interface State : CircuitUiState {
        data object Loading : State

        data class Success(
            val countries: ImmutableList<Country>,
            val selectedCounty: Country,
            val themeConfig: AppPreferences.ThemeConfig,
            val event: (Event) -> Unit = {},
        ) : State
    }

    sealed interface Event : CircuitUiEvent {
        data class NewCountrySelected(val index: Int) : Event
        data class ThemeConfigSelected(val newTheme: AppPreferences.ThemeConfig) : Event
    }
}

@CircuitInject(SettingsScreen::class, AppScope::class)
@Composable
internal fun SettingsContent(
    state: SettingsScreen.State,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier, topBar = { TopBar(state) }) { padding ->
        when (state) {
            SettingsScreen.State.Loading -> Loading(padding)
            is SettingsScreen.State.Success -> ScreenContent(state, padding)
        }
    }
}

@Composable
private fun ScreenContent(
    state: SettingsScreen.State.Success,
    padding: PaddingValues,
) {
    val country = state.selectedCounty
    val countries = state.countries
    val themeConfig = state.themeConfig

    Column(Modifier.padding(padding)) {

        Text(
            text = "Selected country",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 8.dp),
        )

        CountriesDropDownList(
            modifier = Modifier.padding(horizontal = 16.dp),
            items = countries,
            selectedIndex = countries.indexOf(country),
            onItemSelected = { i -> state.event(SettingsScreen.Event.NewCountrySelected(i)) }
        )

        Spacer(modifier = Modifier.padding(top = 8.dp))

        Text(
            text = "Dark mode preferences",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 8.dp),
        )

        Column(
            Modifier
                .padding(start = 16.dp)
                .selectableGroup()
        ) {

            fun onThemeConfigChange(theme: AppPreferences.ThemeConfig) {
                state.event(ThemeConfigSelected(newTheme = theme))
            }

            SettingsDialogThemeChooserRow(
                text = "System default",
                selected = themeConfig == AppPreferences.ThemeConfig.AUTO,
                onClick = { onThemeConfigChange(AppPreferences.ThemeConfig.AUTO) },
            )
            SettingsDialogThemeChooserRow(
                text = "Light",
                selected = themeConfig == AppPreferences.ThemeConfig.LIGHT,
                onClick = { onThemeConfigChange(AppPreferences.ThemeConfig.LIGHT) },
            )
            SettingsDialogThemeChooserRow(
                text = "Dark",
                selected = themeConfig == AppPreferences.ThemeConfig.DARK,
                onClick = { onThemeConfigChange(AppPreferences.ThemeConfig.DARK) },
            )
        }
    }
}

// Ported from Confetti
@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}


@Composable
private fun Loading(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.testTag("progress"),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(state: SettingsScreen.State) {
    if (state !is SettingsScreen.State.Success) return
    CenterAlignedTopAppBar(
        title = { Text("Settings") },
        navigationIcon = { BackPressNavIcon() },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun BackPressNavIcon(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    iconButtonContent: @Composable () -> Unit = { ClosedIconImage() },
) {
    val backPressOwner = LocalOnBackPressedDispatcherOwner.current
    val finalOnClick = remember {
        onClick
            ?: backPressOwner?.onBackPressedDispatcher?.let { dispatcher -> dispatcher::onBackPressed }
            ?: error("No local LocalOnBackPressedDispatcherOwner found.")
    }
    IconButton(modifier = modifier, onClick = finalOnClick) { iconButtonContent() }
}

@Composable
private fun ClosedIconImage(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = rememberVectorPainter(image = Icons.Filled.Close),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
        contentDescription = "Close",
    )
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    WhereToWatchTheme {
        SettingsContent(SettingsScreen.State.Loading)
    }
}