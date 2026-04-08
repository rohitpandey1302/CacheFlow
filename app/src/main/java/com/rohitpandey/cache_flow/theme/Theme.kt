package com.rohitpandey.cache_flow.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
internal fun CacheFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content,
    )
}

private val DarkColorScheme = darkColorScheme(
    primary          = Brand400,
    onPrimary        = Gray900,
    primaryContainer = Brand700,
    secondary        = Teal600,
    onSecondary      = Gray900,
    background       = Gray900,
    surface          = Color(0xFF1C1C2E),
    onBackground     = Gray100,
    onSurface        = Gray100,
    error            = ErrorRed,
)

private val LightColorScheme = lightColorScheme(
    primary          = Brand500,
    onPrimary        = Color.White,
    primaryContainer = Brand400,
    secondary        = Teal400,
    onSecondary      = Color.White,
    background       = Gray50,
    surface          = Color.White,
    onBackground     = Gray900,
    onSurface        = Gray900,
    error            = ErrorRed,
)