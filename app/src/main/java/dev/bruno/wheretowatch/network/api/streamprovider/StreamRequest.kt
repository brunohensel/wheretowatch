package dev.bruno.wheretowatch.network.api.streamprovider

import io.ktor.resources.Resource
import kotlinx.serialization.SerialName

@Resource("watch/providers/movie")
class StreamRequest(
    val language: String,
    @SerialName("watch_region")
    val watchRegion: String,
)
