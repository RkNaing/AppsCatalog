package com.rkzmn.appscatalog.utils.android.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalDarkThemeFlag = compositionLocalOf { false }

val isAppInDarkTheme: Boolean
    @Composable
    get() = LocalDarkThemeFlag.current