package com.zeta.anywhere.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = ZetaPurple,
    secondary = ZetaViolet,
    tertiary = ZetaIndigo,
)

private val LightColors = lightColorScheme(
    primary = ZetaPurple,
    secondary = ZetaViolet,
    tertiary = ZetaIndigo,
)

@Composable
fun ZetaAnywhereTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
