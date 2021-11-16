package com.anorlddroid.mi_todo.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.anorlddroid.mi_todo.R



val NotoSerifDisplay = FontFamily(
    Font(R.font.notoserifdisplay_thinitalic, FontWeight.Thin),
    Font(R.font.notoserifdisplay_light, FontWeight.Light),
    Font(R.font.notoserifdisplay_extracondensed_regular, FontWeight.Normal),
    Font(R.font.notoserifdisplay_mediumitalic, FontWeight.Medium),
    Font(R.font.notoserifdisplay_bolditalic, FontWeight.Bold),
)
val Typography = Typography(
    h3 = TextStyle(
        fontFamily = NotoSerifDisplay,
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 59.sp
    ),

    h5 = TextStyle(
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp
    ),
    h6 = TextStyle(
//        fontFamily = NotoSerifDisplay,
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = NotoSerifDisplay,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = NotoSerifDisplay,
        fontSize = 14.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp
    ) ,
    button = TextStyle(
        fontFamily = NotoSerifDisplay,
        fontSize = 14.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 16.sp,
        letterSpacing = 1.25.sp
    ),
)