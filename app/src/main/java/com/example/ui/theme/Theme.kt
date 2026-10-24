package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AgriGreenPrimaryDark,
    onPrimary = AgriGreenOnPrimaryDark,
    primaryContainer = AgriGreenContainerDark,
    secondary = AgriGreenSecondaryDark,
    background = AgriBackgroundDark,
    surface = AgriSurfaceDark,
    error = AgriRedDisease
)

private val LightColorScheme = lightColorScheme(
    primary = AgriGreenPrimary,
    onPrimary = AgriGreenOnPrimary,
    primaryContainer = AgriGreenContainer,
    onPrimaryContainer = AgriGreenOnContainer,
    secondary = AgriGreenSecondary,
    onSecondary = AgriGreenOnSecondary,
    secondaryContainer = AgriGreenSecondaryContainer,
    onSecondaryContainer = AgriGreenOnSecondaryContainer,
    tertiary = AgriAmberTertiary,
    tertiaryContainer = AgriAmberContainer,
    background = AgriBackgroundLight,
    surface = AgriSurfaceLight,
    surfaceVariant = AgriSurfaceVariant,
    outline = AgriOutline,
    error = AgriRedDisease
)

@Composable
fun CropDocTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to prioritize our signature agricultural branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    CropDocTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
