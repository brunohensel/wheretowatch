package dev.bruno.wheretowatch.network.api.country

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.country.CountryDto
import dev.bruno.wheretowatch.services.country.CountryRemote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class KtorCountryRemote @Inject constructor(
    private val httpClient: HttpClient,
) : CountryRemote {

    override suspend fun getCountries(): List<CountryDto> {
        val resource = CountriesRequest()
        return httpClient.get(resource).body()
    }
}