package dev.bruno.wheretowatch.services.country

import dev.bruno.wheretowatch.CountryEntity

interface CountryDao {

    suspend fun getCountriesEntity(): List<CountryEntity>

    suspend fun insertCountries(countries: List<CountryEntity>)

    suspend fun insertCountry(
        code: String,
        countryName: String,
    )
}