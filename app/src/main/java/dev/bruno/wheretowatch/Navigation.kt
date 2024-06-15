package dev.bruno.wheretowatch

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.bruno.wheretowatch.features.discover.MovieScreen
import dev.bruno.wheretowatch.features.movies.MovieDetailScreen
import kotlinx.serialization.Serializable

@Serializable
object Movie

@Serializable
data class MovieDetail(val movieId: Int)

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Movie
    ) {
        composable<Movie> {
            MovieScreen(navController)
        }
        composable<MovieDetail> { backStackEntry ->
            val movieDetail: MovieDetail = backStackEntry.toRoute()
            MovieDetailScreen(movieId = movieDetail.movieId)
        }
    }
}

fun interface Navigator {
    fun goTo(destination: Any)
}
