package dev.bruno.wheretowatch.network.api.streamprovider

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class StreamProviderResponse(val results: List<StreamProviderDto>)

@Serializable
data class StreamProviderDto(
    @SerialName("provider_id")
    val id: Int,
    @SerialName("logo_path")
    val logoPath: String,
    @SerialName("provider_name")
    val name: String,
)
