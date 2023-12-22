package dev.bruno.wheretowatch.features.movies

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.model.Movie
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailScreen(val movieId: Int) : Screen {
    data class State(
        val movie: Movie = Movie(),
    ) : CircuitUiState
}

@Composable
@CircuitInject(MovieDetailScreen::class, AppScope::class)
fun MovieDetailContent(
    state: MovieDetailScreen.State,
    modifier: Modifier = Modifier,
) {

}
