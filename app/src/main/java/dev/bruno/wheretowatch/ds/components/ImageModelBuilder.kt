package dev.bruno.wheretowatch.ds.components

interface ImageModelBuilder<T> {
    val buildImgModel: (type: ImageType) ->  T
}
