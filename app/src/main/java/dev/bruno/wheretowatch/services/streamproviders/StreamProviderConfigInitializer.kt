package dev.bruno.wheretowatch.services.streamproviders

import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.Initializers
import dev.bruno.wheretowatch.di.AppScope
import javax.inject.Inject

private const val PROVIDERS_THRESHOLD = 5

@ContributesMultibinding(AppScope::class, boundType = Initializers::class)
class StreamProviderConfigInitializer @Inject constructor(
    private val streamProviderRemote: StreamProviderRemote,
    private val streamProviderDao: StreamProviderDao,
) : Initializers {
    override suspend fun init() {
        val storedProviders = streamProviderDao.getProviders()

        if (storedProviders.size < PROVIDERS_THRESHOLD) {
            val topProviders = streamProviderRemote.getProviders()
            streamProviderDao.insert(topProviders)
        }
    }
}
