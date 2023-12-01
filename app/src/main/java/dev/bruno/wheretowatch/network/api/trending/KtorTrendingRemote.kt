package dev.bruno.wheretowatch.network.api.trending

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.trending.TrendingRemote
import dev.bruno.wheretowatch.services.trending.TrendingResultDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class KtorTrendingRemote @Inject constructor(
    private val httpClient: HttpClient,
) : TrendingRemote {

    override suspend fun getTrending(timeWindow: String): TrendingResultDto {
        require(timeWindow == "day" || timeWindow == "week") {
            "Wrong time window! Expected: day or week. Received: $timeWindow"
        }

        val resource = TrendingRequest.TimeWindow(timeWindow = timeWindow)
        val response = httpClient.get(resource).body<TrendingResultDto>()
        val noAdultTrending = response.results.filterNot { it.adult }
        return response.copy(results = noAdultTrending)
    }
}