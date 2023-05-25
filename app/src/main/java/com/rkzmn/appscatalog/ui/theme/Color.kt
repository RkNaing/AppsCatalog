package com.rkzmn.appscatalog.ui.theme

import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.runtime.Composable

/**
 * Returns the card colors and elevation accordingly as follows
 *
 * Light Theme => Uses Elevated Card design
 * Dark Theme => Uses Filled Card design
 */
val cardColorAndElevation: Pair<CardColors, CardElevation>
    @Composable
    get() {
        val cardColors: CardColors
        val cardElevation: CardElevation

        if (isAppInDarkTheme) {
            cardColors = CardDefaults.cardColors()
            cardElevation = CardDefaults.cardElevation()
        } else {
            cardColors = CardDefaults.elevatedCardColors()
            cardElevation = CardDefaults.elevatedCardElevation()
        }
        return cardColors to cardElevation
    }
