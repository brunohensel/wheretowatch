package dev.bruno.wheretowatch.features.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.ds.components.MainScreenTopBar
import dev.bruno.wheretowatch.features.home.HomeScreen.Event.OpenSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize

@Parcelize
data object HomeScreen : Screen {
    data class State(
        val trendingItems: ImmutableList<HomeTrendingItem>,
        val onEvent: (Event) -> Unit,
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object OpenSettings : Event
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(HomeScreen::class, AppScope::class)
@Composable
fun HomeContent(
    state: HomeScreen.State,
    modifier: Modifier = Modifier,
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            MainScreenTopBar(
                title = "Where to watch",
                onClick = { state.onEvent(OpenSettings) }
            )
        }
    ) { paddingValues ->
        paddingValues
    }
}
