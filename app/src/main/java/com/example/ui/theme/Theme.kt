package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = ChaitanyaBluePrimary,        // Bluish for buttons & component accents
    onPrimary = ColorTextWhite,            // #FFFFFF
    primaryContainer = ChaitanyaSkyContainer,
    onPrimaryContainer = ChaitanyaBlueDark,
    secondary = ChaitanyaBlueLight,
    onSecondary = ColorTextWhite,          // #FFFFFF
    secondaryContainer = ChaitanyaSkyContainer,
    onSecondaryContainer = ChaitanyaBluePrimary,
    tertiary = ColorActiveStatus,          // #FD0031 reserved for navigation active status
    onTertiary = ColorTextWhite,
    tertiaryContainer = ColorTopHeading,   // #FFFFCB for top heading component
    onTertiaryContainer = ChaitanyaBlueDark,
    background = ColorBackground,          // #F7F7F7 for background
    onBackground = ChaitanyaTextPrimary,
    surface = ColorComponentWhite,         // #FFFFFF for white components
    onSurface = ChaitanyaBlueDark,         // Bluish text for components
    surfaceVariant = ChaitanyaSkyContainer,
    onSurfaceVariant = ChaitanyaTextSecondary,
    outline = ChaitanyaCardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
