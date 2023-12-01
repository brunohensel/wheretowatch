package dev.bruno.wheretowatch.ds.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.bruno.wheretowatch.R

private val RobotoCondensedFamily: FontFamily
    @Composable get() = FontFamily(
        Font(R.font.roboto_condensed_regular, FontWeight.Normal),
        Font(R.font.roboto_condensed_medium, FontWeight.Medium),
        Font(R.font.roboto_condensed_bold, FontWeight.Bold),
    )

val CustomTypography: Typography
    @Composable get() {
        val default = Typography()
        val fontFamily = RobotoCondensedFamily

        return Typography(
            displayLarge = default.displayLarge.copy(fontFamily = fontFamily),
            displayMedium = default.displayMedium.copy(fontFamily = fontFamily),
            displaySmall = default.displaySmall.copy(fontFamily = fontFamily),
            headlineLarge = default.headlineLarge.copy(fontFamily = fontFamily),
            headlineMedium = default.headlineMedium.copy(fontFamily = fontFamily),
            headlineSmall = default.headlineSmall.copy(fontFamily = fontFamily),
            titleLarge = default.titleLarge.copy(fontFamily = fontFamily),
            titleMedium = default.titleMedium.copy(fontFamily = fontFamily),
            titleSmall = default.titleSmall.copy(fontFamily = fontFamily),
            bodyLarge = default.bodyLarge.copy(fontFamily = fontFamily),
            bodyMedium = default.bodyMedium.copy(fontFamily = fontFamily),
            bodySmall = default.bodySmall.copy(fontFamily = fontFamily),
            labelLarge = default.labelLarge.copy(fontFamily = fontFamily),
            labelMedium = default.labelMedium.copy(fontFamily = fontFamily),
            labelSmall = default.labelSmall.copy(fontFamily = fontFamily),
        )
    }
