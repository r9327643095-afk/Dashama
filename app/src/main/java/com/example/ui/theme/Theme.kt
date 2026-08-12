package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DevotionalColorScheme = darkColorScheme(
    primary = DevotionalGold,
    onPrimary = DevotionalDeepDark,
    primaryContainer = DevotionalMaroon,
    onPrimaryContainer = DevotionalCream,
    secondary = DevotionalVermilion,
    onSecondary = DevotionalCream,
    tertiary = DevotionalMutedGold,
    onTertiary = DevotionalDeepDark,
    background = DevotionalDeepDark,
    onBackground = DevotionalCream,
    surface = DevotionalCardBg,
    onSurface = DevotionalCream,
    surfaceVariant = DevotionalSurfaceContainer,
    onSurfaceVariant = DevotionalMutedGold,
    outline = DevotionalLine
)

@Composable
fun DashamaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DevotionalColorScheme,
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
    DashamaTheme(darkTheme = darkTheme, content = content)
}

