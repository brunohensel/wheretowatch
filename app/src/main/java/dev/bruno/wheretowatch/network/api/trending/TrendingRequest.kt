package dev.bruno.wheretowatch.network.api.trending

import io.ktor.resources.Resource

@Resource("trending/all")
class TrendingRequest {
    @Resource("{timeWindow}")
    class TimeWindow(
        val parent: TrendingRequest = TrendingRequest(),
        val timeWindow: String,
    )
}