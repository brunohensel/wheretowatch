package dev.bruno.wheretowatch.network.api.configuration

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.di.SingleIn
import dev.bruno.wheretowatch.services.images.ImagesConfiguration
import dev.bruno.wheretowatch.services.images.ImagesConfigurationRemote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import javax.inject.Inject

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class KtorConfigurationRemote @Inject constructor(
    private val httpClient: HttpClient,
) : ImagesConfigurationRemote {

    // It's unlikely for the purposes of this app that during its lifecycle the configuration
    // will change. Another approach would be save it to DB alongside with timestamp of the last
    // request and decide when to trigger a new one.
    private lateinit var cached: ImagesConfiguration

    override suspend fun getNewConfig(): ImagesConfiguration {
        if (this::cached.isInitialized) return cached

        val resource = ConfigurationRequest()
        val result = runCatching { httpClient.get(resource).body<TmdbConfigResponse>() }

        return result
            .map { newConfig ->
                ImagesConfiguration(
                    baseUrl = newConfig.images.baseUrl,
                    backdropSizes = newConfig.images.backdropSizes,
                    logoSizes = newConfig.images.logoSizes,
                    posterSizes = newConfig.images.posterSizes,
                )
            }.getOrDefault(ImagesConfiguration())
            .also { cached = it }
    }
}