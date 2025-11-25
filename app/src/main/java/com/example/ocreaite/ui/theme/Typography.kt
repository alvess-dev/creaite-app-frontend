package com.example.ocreaite.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.ocreaite.R

// Família de fontes Inter
val InterFont = FontFamily(
    Font(R.font.inter_18pt_regular, FontWeight.Normal),
    Font(R.font.inter_18pt_medium, FontWeight.Medium),
    Font(R.font.inter_18pt_semibold, FontWeight.SemiBold),
    Font(R.font.inter_18pt_bold, FontWeight.Bold),
)


// Tipografia do app
val AppTypography = Typography(
    bodyLarge = Typography().bodyLarge.copy(fontFamily = InterFont),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = InterFont),
    bodySmall = Typography().bodySmall.copy(fontFamily = InterFont),

    titleLarge = Typography().titleLarge.copy(fontFamily = InterFont),
    titleMedium = Typography().titleMedium.copy(fontFamily = InterFont),
    titleSmall = Typography().titleSmall.copy(fontFamily = InterFont)
)
