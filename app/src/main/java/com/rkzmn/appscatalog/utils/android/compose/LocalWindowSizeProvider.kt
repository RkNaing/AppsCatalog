package com.rkzmn.appscatalog.utils.android.compose

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.compositionLocalOf

val LocalWindowSize = compositionLocalOf<WindowSizeClass> {
    error("CompositionLocal LocalWindowSize not present")
}
