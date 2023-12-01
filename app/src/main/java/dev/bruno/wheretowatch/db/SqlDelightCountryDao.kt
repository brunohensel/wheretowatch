package dev.bruno.wheretowatch.db

import com.squareup.anvil.annotations.ContributesBinding
import dagger.Reusable
import dev.bruno.wheretowatch.CountryEntity
import dev.bruno.wheretowatch.WhereToWatchDatabase
import dev.bruno.wheretowatch.di.AppScope
import dev.bruno.wheretowatch.services.country.CountryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Reusable
@ContributesBinding(AppScope::class)
class SqlDelightCountryDao @Inject constructor(
    private val db: WhereToWatchDatabase,
) : CountryDao {

    override suspend fun getCountriesEntity(): List<CountryEntity> {
        return db.countryEntityQueries.getCountries().executeAsList()
    }

    override suspend fun insertCountries(countries: List<CountryEntity>) {
        withContext(Dispatchers.Default) {
            db.countryEntityQueries
                .transaction {
                    for (country in countries) {
                        db.countryEntityQueries.insertCountries(
                            code = country.code,
                            country_name = country.country_name,
                        )
                    }
                }
        }
    }

    override suspend fun insertCountry(
        code: String,
        countryName: String,
    ) {
        db.countryEntityQueries.insertCountries(
            code = code,
            country_name = countryName,
        )
    }
}