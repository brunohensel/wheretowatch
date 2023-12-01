package dev.bruno.wheretowatch.network.api.country

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("configuration/countries")
class CountriesRequest