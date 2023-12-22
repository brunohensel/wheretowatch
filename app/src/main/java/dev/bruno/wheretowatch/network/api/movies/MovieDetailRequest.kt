package dev.bruno.wheretowatch.network.api.movies

import io.ktor.resources.Resource

@Resource("movie")
class MovieDetailRequest {
    @Resource("{movieId}")
    class Id(val parent: MovieDetailRequest = MovieDetailRequest(), val movieId: Int)
}
