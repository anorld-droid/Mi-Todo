package com.anorlddroid.mi_todo.ui.theme

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

object ThemeState {
    var selectedTheme by mutableStateOf("")
}
@Composable
fun MiTodoTheme(content: @Composable() () -> Unit) {
    Log.d("THEME", " Theme state value :${ThemeState.selectedTheme}")
    val colors = if (ThemeState.selectedTheme == "On") {
        DarkColorPalette
    } else if (ThemeState.selectedTheme == "Off") {
        LightColorPalette
    } else {
        if (isSystemInDarkTheme()) {
            DarkColorPalette
        } else {
            LightColorPalette
        }
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}