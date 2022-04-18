package com.anorlddroid.mi_todo.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.anorlddroid.mi_todo.R


val Gothic = FontFamily(
    Font(R.font.gothic_a1_regular, FontWeight.Normal),
    Font(R.font.gothic_a1_medium, FontWeight.Medium),
    Font(R.font.gothic_a1_bold, FontWeight.Bold),

    )

val NotoSerif = FontFamily(
    Font(R.font.noto_serif_display_medium_italic, FontWeight.Medium)
)
val Typography = Typography(
    h3 = TextStyle(
        fontFamily = Gothic,
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 59.sp
    ),

    h5 = TextStyle(
        fontFamily = Gothic,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = Gothic,
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Gothic,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Gothic,
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
        fontFamily = Gothic,
        fontSize = 14.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 16.sp,
        letterSpacing = 1.25.sp
    ),
)