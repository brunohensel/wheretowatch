package dev.bruno.wheretowatch.features.movies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.movies.detail.MovieDetailsSupplier

class MovieDetailPresenter @AssistedInject constructor(
    @Assisted private val screen: MovieDetailScreen,
    private val movieDetailsSupplier: MovieDetailsSupplier,
) : Presenter<MovieDetailScreen.State> {
    @Composable
    override fun present(): MovieDetailScreen.State {
        val movieDetail by produceState(initialValue = MovieDetailsItem.EMPTY_ITEM) {
            val detail = movieDetailsSupplier.get(screen.movieId)
            value = detail
        }

        return MovieDetailScreen.State(movieDetail)
    }

    @CircuitInject(MovieDetailScreen::class, AppScope::class)
    @AssistedFactory
    interface Factory {
        fun create(
            screen: MovieDetailScreen,
        ): MovieDetailPresenter
    }
}
