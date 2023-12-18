package dev.bruno.wheretowatch.network.api.streamprovider

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.model.StreamProvider
import dev.bruno.wheretowatch.services.streamproviders.StreamProviderRemote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class KtorStreamProviderRemote @Inject constructor(
    private val httpClient: HttpClient,
) : StreamProviderRemote {
    override suspend fun getProviders(): List<StreamProvider> {
        val res = StreamRequest(
            language = "en-US", // TODO get it from preferences,
            watchRegion = "DE", // TODO get it from preferences
        )
        val response = httpClient.get(res).body<StreamProviderResponse>()
        val mostLikelyWatched = response.results.take(5)

        return mostLikelyWatched.toStreamProvider()
    }

    private fun List<StreamProviderDto>.toStreamProvider(): List<StreamProvider> = map { dto ->
        StreamProvider(
            id = dto.id,
            logoPath = dto.logoPath,
            name = dto.name,
        )
    }
}
