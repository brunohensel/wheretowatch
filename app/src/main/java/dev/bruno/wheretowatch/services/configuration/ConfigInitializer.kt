package dev.bruno.wheretowatch.services.configuration

import com.squareup.anvil.annotations.ContributesMultibinding
import dev.bruno.wheretowatch.Initializers
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.images.ImagesConfigurationRemote
import javax.inject.Inject

@ContributesMultibinding(AppScope::class, boundType = Initializers::class)
class ConfigInitializer @Inject constructor(
    private val configRemote: ImagesConfigurationRemote,
) : Initializers {
    override suspend fun init() {
        configRemote.getNewConfig()
    }
}
