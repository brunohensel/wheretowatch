package dev.bruno.wheretowatch.features.movies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.MovieDetail
import dev.bruno.wheretowatch.di.ViewModelKey
import dev.bruno.wheretowatch.di.ViewModelScope
import dev.bruno.wheretowatch.services.movies.detail.MovieDetailsSupplier
import javax.inject.Inject

@ContributesMultibinding(ViewModelScope::class)
@ViewModelKey(MovieDetailViewModel::class)
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieDetailsSupplier: MovieDetailsSupplier,
) : ViewModel() {

    private val movieDetail: MovieDetail = savedStateHandle.toRoute()

    @Composable
    fun present(): MovieDetailState {
        val movieDetail by produceState(initialValue = MovieDetailsItem.EMPTY_ITEM) {
            val detail = movieDetailsSupplier.get(movieDetail.movieId)
            value = detail
        }

        return MovieDetailState(movieDetail)
    }
}
