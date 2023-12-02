package dev.bruno.wheretowatch.services.images

interface ImagesConfigurationRemote {
    suspend fun getNewConfig(): ImagesConfiguration
}