package dev.bruno.wheretowatch.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.isAtRoot
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.runtime.Navigator
import kotlin.math.roundToInt

private val BottomNavHeight = 56.dp

@Composable
fun Home(
    navigator: Navigator,
    backstack: SaveableBackStack,
    modifier: Modifier = Modifier,
) {
    val currentScreen by remember(backstack) {
        derivedStateOf { backstack.last().screen }
    }

    val (bottomBarOffsetHeightPx, bottomBarScrollConnection: NestedScrollConnection) = nestedScrollConnectionPair()

    Scaffold(
        modifier = modifier.nestedScroll(bottomBarScrollConnection),
        bottomBar = {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = 0,
                            y = -bottomBarOffsetHeightPx.floatValue.roundToInt()
                        )
                    }
            ) {
                HomeBottomBar(
                    currentScreen = currentScreen,
                    tabs = HomeNavigationTabItems.entries,
                    onSelected = { screen ->
                        // prevent to create a cycle in the backstack between tabs
                        if (!backstack.isAtRoot || backstack.topRecord?.screen != screen) {
                            navigator.resetRoot(screen)
                        }
                    },
                    modifier = Modifier
                        .height(BottomNavHeight)
                        .offset {
                            IntOffset(
                                x = 0,
                                y = -(bottomBarOffsetHeightPx.floatValue * 0.5f).roundToInt()
                            )
                        }
                        .fillMaxWidth(),
                )
            }
        },
    ) { paddingValues ->
        NavigableCircuitContent(
            navigator = navigator,
            backStack = backstack,
            modifier = Modifier
                // this don't do anything since we are not setting top bar in this scaffold
                // the bottom bar collapse when user scroll the feed, so we don't need to
                // reserve an space there by passing padding value for the bottom slot.
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize(),
        )
    }
}

@Composable
private fun nestedScrollConnectionPair(): Pair<MutableFloatState, NestedScrollConnection> {
    val bottomBarHeightPx = with(LocalDensity.current) { BottomNavHeight.roundToPx().toFloat() }
    val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }

    val bottomBarScrollConnection: NestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = bottomBarOffsetHeightPx.floatValue + delta
                bottomBarOffsetHeightPx.floatValue = newOffset.coerceIn(
                    minimumValue = -bottomBarHeightPx,
                    maximumValue = 0f
                )
                // We're basically watching scroll without taking it
                return Offset.Zero
            }
        }
    }
    return Pair(bottomBarOffsetHeightPx, bottomBarScrollConnection)
}
