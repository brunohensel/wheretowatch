package dev.bruno.wheretowatch.services.trending


fun interface TrendingRemote {
    suspend fun getTrending(timeWindow: String): TrendingResultDto
}