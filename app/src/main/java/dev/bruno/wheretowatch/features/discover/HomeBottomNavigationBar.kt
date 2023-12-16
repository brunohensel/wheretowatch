package dev.bruno.wheretowatch.features.discover

import androidx.annotation.FloatRange
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.slack.circuit.runtime.screen.Screen
import dev.bruno.wheretowatch.features.settings.SettingsScreen
import kotlinx.parcelize.Parcelize

// Ported from Jetsnack https://github.com/android/compose-samples/blob/main/Jetsnack/app/src/main/java/com/example/jetsnack/ui/home/Home.kt
@Composable
fun HomeBottomBar(
    modifier: Modifier = Modifier,
    tabs: List<HomeNavigationTabItems>,
    currentScreen: Screen,
    onSelected: (Screen) -> Unit,
) {
    val currentSection = tabs.first { it.screen == currentScreen }
    Surface {
        val springSpec = SpringSpec<Float>(
            // Determined experimentally
            stiffness = 800f,
            dampingRatio = 0.8f
        )

        HomeBottomBarLayout(
            selectedIndex = currentSection.ordinal,
            tabsCount = remember { tabs.size },
            indicator = { HomeBottomBarIndicator() },
            animSpec = springSpec,
            modifier = modifier
                .navigationBarsPadding(),
        ) {
            for (tab in tabs) {
                val selected = currentScreen == tab.screen
                val tint by animateColorAsState(
                    if (selected) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = .3f)
                    },
                    label = "tint animation",
                )

                HomeBottomBarItem(
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            tint = tint,
                            contentDescription = tab.title
                        )
                    },
                    text = {
                        Text(
                            text = tab.title,
                            color = tint,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    },
                    selected = selected,
                    onSelected = { onSelected(tab.screen) },
                    animSpec = springSpec,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(percent = 50))
                )
            }
        }
    }
}

@Composable
private fun HomeBottomBarLayout(
    tabsCount: Int,
    selectedIndex: Int,
    animSpec: AnimationSpec<Float>,
    modifier: Modifier = Modifier,
    indicator: @Composable BoxScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    // Track how "selected" each item is [0, 1]
    val selectionFractions = remember(tabsCount) {
        List(tabsCount) { i ->
            Animatable(if (i == selectedIndex) 1f else 0f)
        }
    }
    selectionFractions.forEachIndexed { index, selectionFraction ->
        val target = if (index == selectedIndex) 1f else 0f
        LaunchedEffect(target, animSpec) {
            selectionFraction.animateTo(target, animSpec)
        }
    }

    // Animate the position of the indicator
    val indicatorIndex = remember { Animatable(0f) }
    val targetIndicatorIndex = selectedIndex.toFloat()
    LaunchedEffect(targetIndicatorIndex) {
        indicatorIndex.animateTo(targetIndicatorIndex, animSpec)
    }

    Layout(
        modifier = modifier,
        content = {
            content()
            Box(Modifier.layoutId("indicator"), content = indicator)
        }
    ) { measurables, constraints ->
        val unselectedWidth = constraints.maxWidth / (tabsCount + 1)
        val selectedWidth = 2 * unselectedWidth
        val indicatorMeasurable = measurables.first { it.layoutId == "indicator" }

        val placeables = measurables
            .filterNot { it == indicatorMeasurable }
            .mapIndexed { index, measurable ->
                // Animate item's width based upon the selection amount
                val width = lerp(unselectedWidth, selectedWidth, selectionFractions[index].value)
                measurable.measure(
                    constraints
                        .copy(
                            minWidth = width,
                            maxWidth = width,
                        )
                )
            }

        val indicatorPlaceable = indicatorMeasurable.measure(
            constraints.copy(
                minWidth = selectedWidth,
                maxWidth = selectedWidth
            )
        )

        layout(
            width = constraints.maxWidth,
            height = placeables.maxByOrNull { it.height }?.height ?: 0
        ) {
            val indicatorLeft = indicatorIndex.value * unselectedWidth
            indicatorPlaceable.placeRelative(x = indicatorLeft.toInt(), y = 0)
            var x = 0
            for (placeable in placeables) {
                placeable.placeRelative(x = x, y = 0)
                x += placeable.width
            }
        }
    }
}

@Composable
private fun HomeBottomBarItem(
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit,
    selected: Boolean,
    onSelected: () -> Unit,
    animSpec: AnimationSpec<Float>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.selectable(selected = selected, onClick = onSelected),
        contentAlignment = Alignment.Center
    ) {
        // Animate the icon/text positions within the item based on selection
        val animationProgress by animateFloatAsState(
            targetValue = if (selected) 1f else 0f,
            animationSpec = animSpec,
            label = "SelectionAnimation"
        )
        HomeBottomBarItemLayout(
            icon = icon,
            text = text,
            animationProgress = animationProgress
        )
    }
}

@Composable
private fun HomeBottomBarItemLayout(
    icon: @Composable (BoxScope.() -> Unit),
    text: @Composable (BoxScope.() -> Unit),
    @FloatRange(from = 0.0, to = 1.0) animationProgress: Float,
) {
    Layout(
        content = {
            Box(
                modifier = Modifier
                    .layoutId("icon")
                    .padding(horizontal = 2.dp),
                content = icon
            )
            val scale = lerp(0.6f, 1f, animationProgress)
            Box(
                modifier = Modifier
                    .layoutId("text")
                    .padding(horizontal = 2.dp)
                    .graphicsLayer {
                        alpha = animationProgress
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = TransformOrigin(0f, 0.5f)
                    },
                content = text
            )
        }
    ) { measurables, constraints ->
        val iconPlaceable = measurables.first { it.layoutId == "icon" }.measure(constraints)
        val textPlaceable = measurables.first { it.layoutId == "text" }.measure(constraints)

        placeTextAndIcon(
            textPlaceable,
            iconPlaceable,
            constraints.maxWidth,
            constraints.maxHeight,
            animationProgress = animationProgress,
        )
    }
}

private fun MeasureScope.placeTextAndIcon(
    textPlaceable: Placeable,
    iconPlaceable: Placeable,
    width: Int,
    height: Int,
    @FloatRange(from = 0.0, to = 1.0) animationProgress: Float,
): MeasureResult {
    val iconY = (height - iconPlaceable.height) / 2
    val textY = (height - textPlaceable.height) / 2

    val textWidth = textPlaceable.width * animationProgress
    val iconX = (width - textWidth - iconPlaceable.width) / 2
    val textX = iconX + iconPlaceable.width

    return layout(width, height) {
        iconPlaceable.placeRelative(iconX.toInt(), iconY)
        if (animationProgress != 0f) {
            textPlaceable.placeRelative(textX.toInt(), textY)
        }
    }
}

@Composable
fun HomeBottomBarIndicator(
    strokeWidth: Dp = 2.dp,
    color: Color = MaterialTheme.colorScheme.onBackground,
    shape: Shape = RoundedCornerShape(percent = 50)
) {
    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .then(Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            .border(strokeWidth, color, shape)
    )
}

@Immutable
enum class HomeNavigationTabItems(
    val screen: Screen,
    val title: String,
    val icon: ImageVector,
) {
    HOME(
        screen = HomeScreen,
        title = "Movies",
        icon = Icons.Outlined.Movie,
    ),
    TV_SHOW(
        screen = DummyShowScreen,
        title = "Tv Shows",
        icon = Icons.Outlined.Tv,
    ),
    Search(
        screen = DummySearch,
        title = "Search",
        icon = Icons.Outlined.Search,
    ),
    Settings(
        screen = SettingsScreen,
        title = "Settings",
        icon = Icons.Outlined.Settings,
    )
}

@Parcelize
data object DummyShowScreen : Screen

@Parcelize
data object DummySearch : Screen
