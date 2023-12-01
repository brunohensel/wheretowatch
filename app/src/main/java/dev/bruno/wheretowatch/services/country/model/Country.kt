package dev.bruno.wheretowatch.services.country.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class Country(val code: String, val name: String) : Parcelable
