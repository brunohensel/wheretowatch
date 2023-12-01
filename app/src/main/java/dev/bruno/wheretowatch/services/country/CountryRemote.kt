package dev.bruno.wheretowatch.services.country

interface CountryRemote {
    suspend fun getCountries(): List<CountryDto>
}