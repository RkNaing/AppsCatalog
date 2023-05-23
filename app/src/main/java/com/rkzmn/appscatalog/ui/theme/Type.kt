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

    val montserratAlternates = montserratAlternatesFontFamily

    val defaultTypography = Typography()

    return Typography(

        displayLarge = defaultTypography.displayLarge.copy(fontFamily = montserratAlternates),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = montserratAlternates),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = montserratAlternates),

        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = montserratAlternates),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = montserratAlternates),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = montserratAlternates),

        titleLarge = defaultTypography.titleLarge.copy(fontFamily = montserratAlternates),
        titleMedium = defaultTypography.titleLarge.copy(fontFamily = montserratAlternates),
        titleSmall = defaultTypography.titleLarge.copy(fontFamily = montserratAlternates),

        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = montserratAlternates),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = montserratAlternates),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = montserratAlternates),

        labelLarge = defaultTypography.labelLarge.copy(fontFamily = montserratAlternates),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = montserratAlternates),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = montserratAlternates)
    )
}


private val montserratAlternatesFontFamily: FontFamily
    get() = FontFamily(
        Font(
            resId = AppFonts.poppins_thin,
            weight = FontWeight.Thin,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.poppins_thin_italic,
            weight = FontWeight.Thin,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.poppins_light,
            weight = FontWeight.Light,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.poppins_light_italic,
            weight = FontWeight.Light,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.poppins_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.poppins_italic,
            weight = FontWeight.Normal,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.poppins_medium,
            weight = FontWeight.Medium,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.poppins_medium_italic,
            weight = FontWeight.Medium,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.poppins_semibold,
            weight = FontWeight.SemiBold,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.poppins_semibold_italic,
            weight = FontWeight.SemiBold,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.poppins_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.poppins_bold_italic,
            weight = FontWeight.Bold,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.poppins_extra_bold,
            weight = FontWeight.ExtraBold,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.poppins_extra_bold_italic,
            weight = FontWeight.ExtraBold,
            style = FontStyle.Italic,
        ),
        Font(
            resId = AppFonts.poppins_black,
            weight = FontWeight.Black,
            style = FontStyle.Normal,
        ),
        Font(
            resId = AppFonts.poppins_black_italic,
            weight = FontWeight.Black,
            style = FontStyle.Italic,
        ),
    )