package dev.bruno.wheretowatch.features.movies

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
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
            item(key = "HeaderBackdropBox") {
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
                                            0.8f to Color.Black.copy(alpha = .0F),
                                            0.9f to Color.Black.copy(alpha = .2F),
                                            1F to Color.Black.copy(alpha = .6F),
                                        )
                                    )
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

            item(key = "DetailInfoHeader") {
                DetailInfoHeader(movie = movie)
            }

            item(key = "DetailOverview") {
                DetailOverview(movie = movie)
            }
        }
    }
}

@Composable
private fun DetailOverview(movie: MovieDetailsItem) {
    Spacer(modifier = Modifier.height(8.dp))

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(
            style = MaterialTheme.typography.labelLarge,
            text = "Overview"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = movie.overview
        )
    }
}

@Composable
private fun DetailInfoHeader(
    movie: MovieDetailsItem,
) {
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
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            InfoRow(
                rowTitle = "Release date",
                rowInfo = movie.releaseDate?.toString() ?: ""
            )
            InfoRow(rowTitle = "Budget", rowInfo = movie.budget.withThousedSeparator())

            InfoRow(rowTitle = "Revenue", rowInfo = movie.revenue.withThousedSeparator())

            InfoRow(rowTitle = "Duration", rowInfo = movie.runtime.asHumanRuntime())

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                style = MaterialTheme.typography.labelLarge,
                text = "Tmdb Rating",
            )
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Row {
                    Text(
                        style = MaterialTheme.typography.labelLarge,
                        text = "User score :",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    UserScore(movie.voteAverage)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(rowTitle = "Vote count", rowInfo = movie.voteCount.withThousedSeparator())
            }
        }
    }
}

private fun Number.withThousedSeparator(): String {
    return "%,d".format(this)
}

@Composable
private fun InfoRow(
    rowTitle: String,
    rowInfo: String,
) {
    Row {
        Text(
            style = MaterialTheme.typography.labelLarge,
            text = "$rowTitle :",
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            style = MaterialTheme.typography.labelMedium,
            text = rowInfo,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun UserScore(
    voteAverage: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 4.dp,
    strokeCap: StrokeCap = StrokeCap.Square,
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = strokeCap)
    }

    // the vote average is represented as floating point (ex 7.2)
    // We need it as percentage (ex. 0.72) to ease the calculation of the sweep and also to get
    // the integer from it to draw a Text inside the circle (ex. 72%)
    val averagePercent = voteAverage / 10
    val text = "${"%.0f".format(averagePercent * 100)}%"
    val style = MaterialTheme.typography
        .bodySmall.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
    val textMeasure = rememberTextMeasurer()
    val measureResult = remember(text) { textMeasure.measure(text, style) }
    val color = if (averagePercent >= 0.65) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Yellow // TODO get this color from design system or from light/dark palette
    }

    Canvas(
        modifier = modifier.size(measureResult.size.width.dp / 2),
    ) {
        val diameterOffset = stroke.width / 2
        val arcDimen = size.width - 2 * diameterOffset
        drawArc(
            color = color.copy(alpha = .5f),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(diameterOffset, diameterOffset),
            size = Size(arcDimen, arcDimen),
            style = stroke
        )

        val sweep = averagePercent * 360
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = Offset(diameterOffset, diameterOffset),
            size = Size(arcDimen, arcDimen),
            style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
        )

        drawText(
            textMeasurer = textMeasure,
            text = text,
            style = style,
            topLeft = Offset(
                x = center.x - measureResult.size.width / 2,
                y = center.y - measureResult.size.height / 2
            )
        )
    }
}

private fun Int.asHumanRuntime(): String {
    val hours = this / 60
    val min = this % 60
    return "${hours}h${min}min"
}
