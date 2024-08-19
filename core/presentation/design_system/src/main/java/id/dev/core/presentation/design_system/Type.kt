package id.dev.core.presentation.design_system

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Lora = FontFamily(
    Font(
        resId = R.font.lora_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.lora_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.lora_semi_bold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.lora_bold,
        weight = FontWeight.Bold
    ),
)

val Typography = Typography(
    bodySmall = TextStyle(
        fontFamily = Lora,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        color = StoryActDarkBlue
    ),
    bodyMedium = TextStyle(
        fontFamily = Lora,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Lora,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = Lora,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = Lora,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        color = StoryActWhite
    ),
)