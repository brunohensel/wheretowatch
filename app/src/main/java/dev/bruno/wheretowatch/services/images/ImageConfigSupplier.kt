package dev.bruno.wheretowatch.services.images

fun interface ImageConfigSupplier {
    fun get(): ImagesConfiguration
}
