package dev.bruno.wheretowatch.network.api.discover

import io.ktor.resources.Resource

@Resource("collection")
class CollectionRequest(
    val language: String? = null,
) {
    @Resource("{collectionId}")
    class Id(val parent: CollectionRequest = CollectionRequest(), val collectionId: String)
}
