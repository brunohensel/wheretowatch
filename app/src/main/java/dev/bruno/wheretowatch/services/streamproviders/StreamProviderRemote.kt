package dev.bruno.wheretowatch.services.streamproviders

import dev.bruno.wheretowatch.services.model.StreamProvider

interface StreamProviderRemote {
    suspend fun getProviders() : List<StreamProvider>
}
