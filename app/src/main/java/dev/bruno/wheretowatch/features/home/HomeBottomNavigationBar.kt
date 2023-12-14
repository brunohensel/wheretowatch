package dev.bruno.wheretowatch.features.home

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.runtime.screen.Screen

@Composable
fun HomeBottomNavigationBar(
    selectedTab: Screen,
    modifier: Modifier = Modifier,
    navTabs: List<HomeNavigationTabItems> = createHomeBottomBarItems(),
) {
    NavigationBar(
        modifier = modifier,
        windowInsets = WindowInsets.navigationBars,
    ) {
        for (tab in navTabs) {
            NavigationBarItem(
                selected = selectedTab == tab.screen,
                onClick = { /*TODO*/ },
                icon = { Icon(imageVector = tab.icon, contentDescription = tab.title) }
            )
        }
    }
}

@Immutable
data class HomeNavigationTabItems(
    val screen: Screen,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector? = null,
)

private fun createHomeBottomBarItems(): List<HomeNavigationTabItems> = listOf(
    HomeNavigationTabItems(
        screen = HomeScreen,
        title = "Movies",
        icon = Icons.Outlined.Movie,
        selectedIcon = Icons.Filled.Movie,
    ),
)
