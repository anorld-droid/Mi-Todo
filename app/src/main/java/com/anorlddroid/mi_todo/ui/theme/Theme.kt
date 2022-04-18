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

private val DarkColorPalette = darkColors(
    primary = brand,
    primaryVariant = primaryLightColor,
    secondary = primaryTextColor,
    background = primaryDarkColor,
    surface = primaryColor,
    onPrimary = primaryLightColor,
    onSecondary = primaryColor,
    onBackground = onBackgroundDark,
    onSurface = darkDialog

)
private val LightColorPalette = lightColors(
    primary = brand,
    primaryVariant = primaryColor,
    secondary = primaryDarkColor,
    background = backgroundWhite,
    surface = primaryTextColor.copy(alpha = 0.75F),
    onPrimary = primaryTextColor,
    onSecondary = lightTextFieldColor,
    onBackground = WhiteSmoke,
    onSurface = whiteDialog,

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