package dev.bruno.wheretowatch.features.movies

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dev.bruno.wheretowatch.di.AppScope

class MovieDetailPresenter @AssistedInject constructor(
    @Assisted private val screen: MovieDetailScreen,
) : Presenter<MovieDetailScreen.State> {
    @Composable
    override fun present(): MovieDetailScreen.State {
        return MovieDetailScreen.State()
    }

    @CircuitInject(MovieDetailScreen::class, AppScope::class)
    @AssistedFactory
    interface Factory {
        fun create(
            screen: MovieDetailScreen,
        ): MovieDetailPresenter
    }
}
