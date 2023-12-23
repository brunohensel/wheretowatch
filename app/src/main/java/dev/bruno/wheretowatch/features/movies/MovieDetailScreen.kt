package dev.bruno.wheretowatch.features.movies

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.ds.components.AndroidAsyncImage
import dev.bruno.wheretowatch.ds.components.ImageType
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
            val movie = state.movie
            item(key = "backdropBox") {

                AndroidAsyncImage(
                    model = movie.buildImgModel(ImageType.Backdrop),
                    contentDescription = "Movie detail backdrop image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 11)
                )
            }

            item(key = "poster") {
                AndroidAsyncImage(
                    model = movie.buildImgModel(ImageType.Poster),
                    contentDescription = "Movie detail poster image",
                    modifier = Modifier
                        .width(150.dp)
                        .aspectRatio(2f / 3)
                )
            }
        }
    }
}
