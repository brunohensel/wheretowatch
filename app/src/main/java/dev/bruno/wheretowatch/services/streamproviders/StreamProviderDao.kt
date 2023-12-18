package dev.bruno.wheretowatch.services.streamproviders

import dev.bruno.wheretowatch.services.model.StreamProvider

interface StreamProviderDao {
    suspend fun insert(providers: List<StreamProvider>)
    suspend fun getProviders(): List<StreamProvider>
}
