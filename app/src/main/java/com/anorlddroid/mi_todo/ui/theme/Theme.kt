package com.anorlddroid.mi_todo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = brand,
    primaryVariant = primaryLightColor,
    secondary = primaryTextColor,
    background = Color.Black.copy(alpha = 0.85F),
    surface = primaryColor,
    onPrimary = primaryLightColor,
    onSecondary = primaryColor

)
private val LightColorPalette = lightColors(
    primary = brand,
    primaryVariant = primaryColor,
    secondary = primaryDarkColor,
    background = Color.White.copy(alpha = 0.85F),
    surface = primaryTextColor.copy(alpha = 0.75F),
    onPrimary = primaryTextColor,
    onSecondary = lightTextFieldColor,
    onBackground = Color.Black,
    onSurface = Color.Black,

    )

@Composable
fun MiTodoTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}