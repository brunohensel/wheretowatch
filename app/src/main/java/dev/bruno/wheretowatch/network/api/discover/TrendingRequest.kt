package dev.bruno.wheretowatch.network.api.discover

import io.ktor.resources.Resource

@Resource("trending")
class TrendingRequest(
    val language: String? = null,
    val region: String? = null,
) {
    @Resource("{trendType}")
    class TrendType(val parent: TrendingRequest = TrendingRequest(), val trendType: String) {
        @Resource("{timeWindow}")
        class TimeWindow(
            val parent: TrendType,
            val timeWindow: String,
        )
    }
}
