package dev.bruno.wheretowatch.services.country

import dev.bruno.wheretowatch.services.country.model.Country
import kotlinx.collections.immutable.ImmutableList

fun interface CountriesSupplier {
    suspend fun get(): ImmutableList<Country>
}