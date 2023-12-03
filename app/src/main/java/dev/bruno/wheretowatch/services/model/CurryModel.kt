package dev.bruno.wheretowatch.services.model

import dev.bruno.wheretowatch.ds.components.ImageType

interface CurryModel<T> {
    fun curried(): (ImageType) -> T
}
