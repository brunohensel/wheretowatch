package dev.bruno.wheretowatch.features.movies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dev.bruno.wheretowatch.di.AppScope
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailScreen(val movieId: Int) : Screen {
    data class State(
        val movie: MovieDetailsItem,
    ) : CircuitUiState
}

@Composable
@CircuitInject(MovieDetailScreen::class, AppScope::class)
fun MovieDetailContent(
    state: MovieDetailScreen.State,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            item(key = "backdropBox") {
                Box(modifier = Modifier.fillMaxSize()) {
                    val movie = state.movie
                    Text(text = movie.title)
                }
            }
        }
    }
}
