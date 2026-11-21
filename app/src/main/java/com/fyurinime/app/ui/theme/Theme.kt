package com.fyurinime.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FyuriDarkColors = darkColorScheme(
    primary = FyuriOrange,
    onPrimary = FyuriBackground,
    secondary = FyuriGold,
    background = FyuriBackground,
    onBackground = FyuriOnBackground,
    surface = FyuriSurface,
    onSurface = FyuriOnBackground,
    surfaceVariant = FyuriSurfaceVariant,
    error = FyuriError
)

@Composable
fun FyuriNimeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FyuriDarkColors,
        typography = FyuriTypography,
        content = content
    )
}
