package dev.bruno.wheretowatch.features.movies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
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
                Box {
                    AndroidAsyncImage(
                        model = movie.buildImgModel(ImageType.Backdrop),
                        contentDescription = "Movie detail backdrop image",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .drawWithCache {
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(
                                        Brush.verticalGradient(
                                        0.8f to Color.Black.copy(alpha=.0F),
                                        0.9f to Color.Black.copy(alpha=.2F),
                                        1F to Color.Black.copy(alpha=.6F),
                                    ))
                                }
                            }
                            .fillMaxWidth()
                            .aspectRatio(16f / 11)
                    )

                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        text = movie.tagline,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            item(key = "poster") {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    AndroidAsyncImage(
                        model = movie.buildImgModel(ImageType.Poster),
                        contentDescription = "Movie detail poster image",
                        modifier = Modifier
                            .width(150.dp)
                            .aspectRatio(2f / 3)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            text = movie.title,
                        )
                    }
                }
            }
        }
    }
}
