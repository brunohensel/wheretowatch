package dev.bruno.wheretowatch.ds.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.bruno.wheretowatch.ds.components.ImageType.Backdrop
import dev.bruno.wheretowatch.ds.components.ImageType.Poster
import dev.bruno.wheretowatch.ds.theme.WhereToWatchTheme

@Composable
fun WhereToWatchCard(
    model: ImageModelBuilder<Any>,
    type: ImageType,
    title: String,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
) {
    when (type) {
        Backdrop -> {
            BackdropCard(
                title = title,
                model = model.buildImgModel(type),
                modifier = modifier,
                onClick = {},
                alignment = alignment,

                )
        }

        Poster -> TODO()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BackdropCard(
    model: Any,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        WhereToWatchTheme {
            BackdropContent(
                model = model,
                title = title,
                alignment = alignment,
            )
        }
    }
}

@Composable
private fun BackdropContent(
    model: Any,
    title: String,
    alignment: Alignment = Alignment.Center,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidAsyncImage(
            model = model,
            contentDescription = "Card of $title",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            alignment = alignment,
        )

        Spacer(modifier = Modifier.matchParentSize())

        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomStart),
        )
    }
}
