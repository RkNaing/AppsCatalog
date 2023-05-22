package com.rkzmn.appscatalog.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.rkzmn.appscatalog.utils.app.AppFonts

// Set of Material typography styles to start with
val Typography = createTypography()

private fun createTypography(): Typography {

    val montserrat = montserratFontFamily

    val defaultTypography = Typography()

    return Typography(

        displayLarge = defaultTypography.displayLarge.copy(fontFamily = montserrat),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = montserrat),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = montserrat),

        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = montserrat),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = montserrat),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = montserrat),

        titleLarge = defaultTypography.titleLarge.copy(fontFamily = montserrat),
        titleMedium = defaultTypography.titleLarge.copy(fontFamily = montserrat),
        titleSmall = defaultTypography.titleLarge.copy(fontFamily = montserrat),

        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = montserrat),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = montserrat),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = montserrat),

        labelLarge = defaultTypography.labelLarge.copy(fontFamily = montserrat),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = montserrat),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = montserrat)
    )
}


private val montserratFontFamily: FontFamily
    get() = FontFamily(
        Font(
            resId = AppFonts.montserrat_thin,
            weight = FontWeight.Thin,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.montserrat_thin_italic,
            weight = FontWeight.Thin,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.montserrat_light,
            weight = FontWeight.Light,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.montserrat_light_italic,
            weight = FontWeight.Light,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.montserrat_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.montserrat_italic,
            weight = FontWeight.Normal,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.montserrat_medium,
            weight = FontWeight.Medium,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.montserrat_medium_italic,
            weight = FontWeight.Medium,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.montserrat_semi_bold,
            weight = FontWeight.SemiBold,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.montserrat_semi_bold_italic,
            weight = FontWeight.SemiBold,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.montserrat_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.montserrat_bold_italic,
            weight = FontWeight.Bold,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.montserrat_extra_bold,
            weight = FontWeight.ExtraBold,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.montserrat_extra_bold_italic,
            weight = FontWeight.ExtraBold,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.montserrat_black,
            weight = FontWeight.Black,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.montserrat_black_italic,
            weight = FontWeight.Black,
            style = FontStyle.Italic,
        ),
    )