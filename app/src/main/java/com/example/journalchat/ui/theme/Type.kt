package com.example.journalchat.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.journalchat.R

val raleWay = FontFamily(
    Font(R.font.raleway_light, FontWeight.Light),
    Font(R.font.raleway_regular, FontWeight.Normal),
    Font(R.font.raleway_bold, FontWeight.Bold),
)

val robotoSlab = FontFamily(
    Font(R.font.robotoslab_semibold, FontWeight.SemiBold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = raleWay,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = raleWay,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    displayLarge = TextStyle(
        fontFamily = robotoSlab,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp
    ),
    displayMedium = TextStyle(
        fontFamily = robotoSlab,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    displaySmall = TextStyle(
        fontFamily = robotoSlab,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    )
)