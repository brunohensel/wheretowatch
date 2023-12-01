package dev.bruno.wheretowatch.ds.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal val BlackRock900 = Color(0xFF27272f)
internal val BlackRock800 = Color(0xFF33333D)
internal val BlackRock700 = Color(0xFF5c5c64)
internal val BlackRock600 = Color(0xFF7d7d83)
internal val BlackRock500 = Color(0xFF97979c)
internal val BlackRock400 = Color(0xFFacacb0)
internal val BlackRock300 = Color(0xFFbdbdc0)
internal val BlackRock200 = Color(0xFFcacacd)
internal val BlackRock100 = Color(0xFFd5d5d7)
internal val BlackRock50 = Color(0xFFdddddf)

internal val MountainGreen500 = Color(0xFF0c4b35)
internal val MountainGreen400 = Color(0xFF0f5e42)
internal val MountainGreen300 = Color(0xFF137652)
internal val MountainGreen200 = Color(0xFF189466)
internal val MountainGreen100 = Color(0xFF1eb980)

val DarkColors = darkColorScheme(
    primary = MountainGreen100,
    background = BlackRock800,
    onBackground = BlackRock50,
    surfaceVariant = BlackRock900,
)

val LightColors = lightColorScheme(
    primary = MountainGreen400,
    background = Color.White,
    onBackground = BlackRock800,
    surfaceVariant = BlackRock100,
)
