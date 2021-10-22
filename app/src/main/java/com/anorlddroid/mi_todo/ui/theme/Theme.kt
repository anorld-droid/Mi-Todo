package com.anorlddroid.mi_todo.ui.theme

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anorlddroid.mi_todo.MiTodoViewModel

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
fun MiTodoTheme(content: @Composable() () -> Unit) {
    val viewModel: MiTodoViewModel = viewModel()
    val themeState = viewModel.themeState.collectAsState()
    val selectedTheme = themeState.value
    Log.d("THEME", " Theme state value :${themeState.value}")

    val colors = if (selectedTheme == "On") {
        DarkColorPalette
    } else if (selectedTheme == "Off") {
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