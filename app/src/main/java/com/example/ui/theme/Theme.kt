package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = TernivaGreen,
    onPrimary = Color.White,
    primaryContainer = TernivaGreenContainer,
    onPrimaryContainer = TernivaGreenText,
    secondary = TernivaHarvestGold,
    onSecondary = Color.White,
    secondaryContainer = TernivaHarvestGoldLight,
    onSecondaryContainer = TernivaHarvestGoldDark,
    tertiary = TernivaTealAgri,
    background = TernivaBg,
    onBackground = TernivaTextPrimary,
    surface = TernivaCard,
    onSurface = TernivaTextPrimary,
    surfaceVariant = Color(0xFFF1F6F2),
    onSurfaceVariant = TernivaTextSecondary,
    outline = TernivaBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = TernivaGreen,
    onPrimary = Color.White,
    primaryContainer = TernivaGreenDark,
    onPrimaryContainer = Color.White,
    secondary = TernivaHarvestGold,
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onSurface = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
