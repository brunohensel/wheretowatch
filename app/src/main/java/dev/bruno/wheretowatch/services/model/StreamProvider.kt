package dev.bruno.wheretowatch.services.model

import dev.bruno.wheretowatch.ds.components.ImageType
import dev.bruno.wheretowatch.services.streamproviders.StreamProviderImageModel

data class StreamProvider(
    val id: Int = -1,
    val logoPath: String = "",
    val name: String = "",
) : CurryModel<StreamProviderImageModel> {
    override fun curried(): (ImageType) -> StreamProviderImageModel = { type ->
        StreamProviderImageModel(logoPath, type)
    }
}
