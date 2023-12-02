package dev.bruno.wheretowatch.services.images

data class ImagesConfiguration(
    val baseUrl: String = BASE_IMG_URL,
    val backdropSizes: List<String> = defaultBackdropSizes,
    val logoSizes: List<String> = defaultLogoSizes,
    val posterSizes: List<String> = defaultPosterSizes,
)

private const val BASE_IMG_URL = "https://image.tmdb.org/t/p/"

private val defaultBackdropSizes = listOf(
    "w300",
    "w780",
    "w1280",
    "original"
)

private val defaultLogoSizes = listOf(
    "w45",
    "w92",
    "w154",
    "w185",
    "w300",
    "w500",
    "original"
)

private val defaultPosterSizes = listOf(
    "w92",
    "w154",
    "w185",
    "w342",
    "w500",
    "w780",
    "original"
)