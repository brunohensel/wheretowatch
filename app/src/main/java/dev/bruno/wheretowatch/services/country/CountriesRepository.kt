package dev.bruno.wheretowatch.services.country

import com.squareup.anvil.annotations.ContributesBinding
import dev.bruno.wheretowatch.CountryEntity
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.country.model.Country
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class CountriesRepository @Inject constructor(
    private val countryDao: CountryDao,
    private val countryRemote: CountryRemote,
) : CountriesSupplier {

    override suspend fun get(): ImmutableList<Country> {
        val countries = getLocalCountries()

        if (countries.isNotEmpty()) return countries.toImmutableList()

        val response = countryRemote.getCountries()
        val countryEntities = getEntityFrom(response)
        countryDao.insertCountries(countryEntities)

        return getLocalCountries()
    }

    private suspend fun getLocalCountries() = countryDao
        .getCountriesEntity()
        .map { Country(it.code, it.country_name) }
        .toImmutableList()

    private fun getEntityFrom(countries: List<CountryDto>): List<CountryEntity> {
        val countryEntities = countries.map { countryDto ->
            CountryEntity(
                code = countryDto.code,
                country_name = countryDto.name,
            )
        }
        return countryEntities
    }
}