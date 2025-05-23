package com.example.pantryplan.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Immutable
data class ExtendedColorScheme(
    val protein: ColorFamily,
    val carbohydrates: ColorFamily,
    val fats: ColorFamily,
    val itemStatusOk: ColorFamily,
    val itemStatusExpiringSoon: ColorFamily,
    val itemStatusExpired: ColorFamily,
    val itemStatusFrozen: ColorFamily,
)

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

val extendedLight = ExtendedColorScheme(
    protein = ColorFamily(
        proteinLight,
        onProteinLight,
        proteinContainerLight,
        onProteinContainerLight,
    ),
    carbohydrates = ColorFamily(
        carbohydratesLight,
        onCarbohydratesLight,
        carbohydratesContainerLight,
        onCarbohydratesContainerLight,
    ),
    fats = ColorFamily(
        fatsLight,
        onFatsLight,
        fatsContainerLight,
        onFatsContainerLight,
    ),
    itemStatusOk = ColorFamily(
        itemStatusOkLight,
        onItemStatusOkLight,
        itemStatusOkContainerLight,
        onItemStatusOkContainerLight,
    ),
    itemStatusExpiringSoon = ColorFamily(
        itemStatusExpiringSoonLight,
        onItemStatusExpiringSoonLight,
        itemStatusExpiringSoonContainerLight,
        onItemStatusExpiringSoonContainerLight,
    ),
    itemStatusExpired = ColorFamily(
        itemStatusExpiredLight,
        onItemStatusExpiredLight,
        itemStatusExpiredContainerLight,
        onItemStatusExpiredContainerLight,
    ),
    itemStatusFrozen = ColorFamily(
        itemStatusFrozenLight,
        onItemStatusFrozenLight,
        itemStatusFrozenContainerLight,
        onItemStatusFrozenContainerLight,
    ),
)

val extendedDark = ExtendedColorScheme(
    protein = ColorFamily(
        proteinDark,
        onProteinDark,
        proteinContainerDark,
        onProteinContainerDark,
    ),
    carbohydrates = ColorFamily(
        carbohydratesDark,
        onCarbohydratesDark,
        carbohydratesContainerDark,
        onCarbohydratesContainerDark,
    ),
    fats = ColorFamily(
        fatsDark,
        onFatsDark,
        fatsContainerDark,
        onFatsContainerDark,
    ),
    itemStatusOk = ColorFamily(
        itemStatusOkDark,
        onItemStatusOkDark,
        itemStatusOkContainerDark,
        onItemStatusOkContainerDark,
    ),
    itemStatusExpiringSoon = ColorFamily(
        itemStatusExpiringSoonDark,
        onItemStatusExpiringSoonDark,
        itemStatusExpiringSoonContainerDark,
        onItemStatusExpiringSoonContainerDark,
    ),
    itemStatusExpired = ColorFamily(
        itemStatusExpiredDark,
        onItemStatusExpiredDark,
        itemStatusExpiredContainerDark,
        onItemStatusExpiredContainerDark,
    ),
    itemStatusFrozen = ColorFamily(
        itemStatusFrozenDark,
        onItemStatusFrozenDark,
        itemStatusFrozenContainerDark,
        onItemStatusFrozenContainerDark,
    ),
)

val extendedLightMediumContrast = ExtendedColorScheme(
    protein = ColorFamily(
        proteinLightMediumContrast,
        onProteinLightMediumContrast,
        proteinContainerLightMediumContrast,
        onProteinContainerLightMediumContrast,
    ),
    carbohydrates = ColorFamily(
        carbohydratesLightMediumContrast,
        onCarbohydratesLightMediumContrast,
        carbohydratesContainerLightMediumContrast,
        onCarbohydratesContainerLightMediumContrast,
    ),
    fats = ColorFamily(
        fatsLightMediumContrast,
        onFatsLightMediumContrast,
        fatsContainerLightMediumContrast,
        onFatsContainerLightMediumContrast,
    ),
    itemStatusOk = ColorFamily(
        itemStatusOkLightMediumContrast,
        onItemStatusOkLightMediumContrast,
        itemStatusOkContainerLightMediumContrast,
        onItemStatusOkContainerLightMediumContrast,
    ),
    itemStatusExpiringSoon = ColorFamily(
        itemStatusExpiringSoonLightMediumContrast,
        onItemStatusExpiringSoonLightMediumContrast,
        itemStatusExpiringSoonContainerLightMediumContrast,
        onItemStatusExpiringSoonContainerLightMediumContrast,
    ),
    itemStatusExpired = ColorFamily(
        itemStatusExpiredLightMediumContrast,
        onItemStatusExpiredLightMediumContrast,
        itemStatusExpiredContainerLightMediumContrast,
        onItemStatusExpiredContainerLightMediumContrast,
    ),
    itemStatusFrozen = ColorFamily(
        itemStatusFrozenLightMediumContrast,
        onItemStatusFrozenLightMediumContrast,
        itemStatusFrozenContainerLightMediumContrast,
        onItemStatusFrozenContainerLightMediumContrast,
    ),
)

val extendedLightHighContrast = ExtendedColorScheme(
    protein = ColorFamily(
        proteinLightHighContrast,
        onProteinLightHighContrast,
        proteinContainerLightHighContrast,
        onProteinContainerLightHighContrast,
    ),
    carbohydrates = ColorFamily(
        carbohydratesLightHighContrast,
        onCarbohydratesLightHighContrast,
        carbohydratesContainerLightHighContrast,
        onCarbohydratesContainerLightHighContrast,
    ),
    fats = ColorFamily(
        fatsLightHighContrast,
        onFatsLightHighContrast,
        fatsContainerLightHighContrast,
        onFatsContainerLightHighContrast,
    ),
    itemStatusOk = ColorFamily(
        itemStatusOkLightHighContrast,
        onItemStatusOkLightHighContrast,
        itemStatusOkContainerLightHighContrast,
        onItemStatusOkContainerLightHighContrast,
    ),
    itemStatusExpiringSoon = ColorFamily(
        itemStatusExpiringSoonLightHighContrast,
        onItemStatusExpiringSoonLightHighContrast,
        itemStatusExpiringSoonContainerLightHighContrast,
        onItemStatusExpiringSoonContainerLightHighContrast,
    ),
    itemStatusExpired = ColorFamily(
        itemStatusExpiredLightHighContrast,
        onItemStatusExpiredLightHighContrast,
        itemStatusExpiredContainerLightHighContrast,
        onItemStatusExpiredContainerLightHighContrast,
    ),
    itemStatusFrozen = ColorFamily(
        itemStatusFrozenLightHighContrast,
        onItemStatusFrozenLightHighContrast,
        itemStatusFrozenContainerLightHighContrast,
        onItemStatusFrozenContainerLightHighContrast,
    ),
)

val extendedDarkMediumContrast = ExtendedColorScheme(
    protein = ColorFamily(
        proteinDarkMediumContrast,
        onProteinDarkMediumContrast,
        proteinContainerDarkMediumContrast,
        onProteinContainerDarkMediumContrast,
    ),
    carbohydrates = ColorFamily(
        carbohydratesDarkMediumContrast,
        onCarbohydratesDarkMediumContrast,
        carbohydratesContainerDarkMediumContrast,
        onCarbohydratesContainerDarkMediumContrast,
    ),
    fats = ColorFamily(
        fatsDarkMediumContrast,
        onFatsDarkMediumContrast,
        fatsContainerDarkMediumContrast,
        onFatsContainerDarkMediumContrast,
    ),
    itemStatusOk = ColorFamily(
        itemStatusOkDarkMediumContrast,
        onItemStatusOkDarkMediumContrast,
        itemStatusOkContainerDarkMediumContrast,
        onItemStatusOkContainerDarkMediumContrast,
    ),
    itemStatusExpiringSoon = ColorFamily(
        itemStatusExpiringSoonDarkMediumContrast,
        onItemStatusExpiringSoonDarkMediumContrast,
        itemStatusExpiringSoonContainerDarkMediumContrast,
        onItemStatusExpiringSoonContainerDarkMediumContrast,
    ),
    itemStatusExpired = ColorFamily(
        itemStatusExpiredDarkMediumContrast,
        onItemStatusExpiredDarkMediumContrast,
        itemStatusExpiredContainerDarkMediumContrast,
        onItemStatusExpiredContainerDarkMediumContrast,
    ),
    itemStatusFrozen = ColorFamily(
        itemStatusFrozenDarkMediumContrast,
        onItemStatusFrozenDarkMediumContrast,
        itemStatusFrozenContainerDarkMediumContrast,
        onItemStatusFrozenContainerDarkMediumContrast,
    ),
)

val extendedDarkHighContrast = ExtendedColorScheme(
    protein = ColorFamily(
        proteinDarkHighContrast,
        onProteinDarkHighContrast,
        proteinContainerDarkHighContrast,
        onProteinContainerDarkHighContrast,
    ),
    carbohydrates = ColorFamily(
        carbohydratesDarkHighContrast,
        onCarbohydratesDarkHighContrast,
        carbohydratesContainerDarkHighContrast,
        onCarbohydratesContainerDarkHighContrast,
    ),
    fats = ColorFamily(
        fatsDarkHighContrast,
        onFatsDarkHighContrast,
        fatsContainerDarkHighContrast,
        onFatsContainerDarkHighContrast,
    ),
    itemStatusOk = ColorFamily(
        itemStatusOkDarkHighContrast,
        onItemStatusOkDarkHighContrast,
        itemStatusOkContainerDarkHighContrast,
        onItemStatusOkContainerDarkHighContrast,
    ),
    itemStatusExpiringSoon = ColorFamily(
        itemStatusExpiringSoonDarkHighContrast,
        onItemStatusExpiringSoonDarkHighContrast,
        itemStatusExpiringSoonContainerDarkHighContrast,
        onItemStatusExpiringSoonContainerDarkHighContrast,
    ),
    itemStatusExpired = ColorFamily(
        itemStatusExpiredDarkHighContrast,
        onItemStatusExpiredDarkHighContrast,
        itemStatusExpiredContainerDarkHighContrast,
        onItemStatusExpiredContainerDarkHighContrast,
    ),
    itemStatusFrozen = ColorFamily(
        itemStatusFrozenDarkHighContrast,
        onItemStatusFrozenDarkHighContrast,
        itemStatusFrozenContainerDarkHighContrast,
        onItemStatusFrozenContainerDarkHighContrast,
    ),
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColorScheme(
        protein = unspecified_scheme,
        carbohydrates = unspecified_scheme,
        fats = unspecified_scheme,
        itemStatusOk = unspecified_scheme,
        itemStatusExpiringSoon = unspecified_scheme,
        itemStatusExpired = unspecified_scheme,
        itemStatusFrozen = unspecified_scheme
    )
}

@Composable
fun PantryPlanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }
    val extendedColors = when {
        darkTheme -> extendedDark
        else -> extendedLight
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}

object PantryPlanTheme {
    val colorScheme: ExtendedColorScheme
        @Composable
        get() = LocalExtendedColors.current
}

