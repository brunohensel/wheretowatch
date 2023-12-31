package dev.bruno.wheretowatch.network.api.discover

import io.ktor.resources.Resource
import kotlinx.serialization.SerialName

@Resource("discover/movie")
class MovieRequest(
    val page: Int,
    val language: String,
    val region: String,
    @SerialName("sort_by")
    val sortBy: String,
    @SerialName("watch_region")
    val watchRegion: String? = null,
    @SerialName("with_watch_providers")
    val streamProviderId: String? = null,
    @SerialName("with_genres")
    val genre: String? = null,
    @SerialName("vote_count.gte")
    val voteCountGTE: Int? = null,
    @SerialName("release_date.gte")
    val releaseGTE: String? = null,
    @SerialName("release_date.lte")
    val releaseLTE: String? = null,
)
