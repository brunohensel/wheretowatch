package dev.bruno.wheretowatch.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.features.settings.SettingsScreen
import dev.bruno.wheretowatch.services.trending.TrendingSupplier
import kotlinx.collections.immutable.persistentListOf

class HomePresenter @AssistedInject constructor(
    private val homeContentLists: HomeContentLists,
    @Assisted private val navigator: Navigator,
) : Presenter<HomeScreen.State> {

    @Composable
    override fun present(): HomeScreen.State {

        val trendingWindow by rememberRetained { mutableStateOf(TrendingSupplier.TrendWindow.DAY) }
        val flowContents = homeContentLists.contents

        val trendingItems by flowContents.tendingContent.collectAsRetainedState(persistentListOf())

        LaunchedEffect(key1 = trendingWindow) {
            homeContentLists.getContent(HomeContentType.Trending(trendingWindow))
        }

        return HomeScreen.State(
            trendingItems = trendingItems,
        ) { event ->
            when (event) {
                HomeScreen.Event.OpenSettings -> navigator.goTo(SettingsScreen)
            }
        }
    }

    interface HomeContentLists {
        val contents: HomeContentFlows
        suspend fun getContent(contentType: HomeContentType)
    }

    @CircuitInject(HomeScreen::class, AppScope::class)
    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }
}
