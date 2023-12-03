package dev.bruno.wheretowatch.ds.components

interface ImageModelBuilder<out T : Any> {
    val buildImgModel: (type: ImageType) -> T
}
