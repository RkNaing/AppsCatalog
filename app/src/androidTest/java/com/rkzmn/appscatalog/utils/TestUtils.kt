package com.rkzmn.appscatalog.utils

import androidx.annotation.StringRes
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

fun AndroidComposeTestRule<*, *>.getString(
    @StringRes resId: Int,
    vararg args: Any
): String = activity.getString(resId, args)

fun AndroidComposeTestRule<*, *>.onNodeWithContentDescriptionRes(
    @StringRes resId: Int,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction = onNodeWithContentDescription(
    label = getString(resId),
    substring = substring,
    ignoreCase = ignoreCase,
    useUnmergedTree = useUnmergedTree
)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun mockWindowSizeClass(
    width: WindowWidthSizeClass,
    height: WindowHeightSizeClass,
): WindowSizeClass {
    /**
     * @see [WindowWidthSizeClass.fromWidth]
     */
    val dummyWidthValue = when (width) {
        WindowWidthSizeClass.Medium -> 830.dp
        WindowWidthSizeClass.Expanded -> 1200.dp
        else -> 590.dp
    }

    /**
     * @see [WindowHeightSizeClass.fromHeight]
     */
    val dummyHeightValue = when (height) {
        WindowHeightSizeClass.Medium -> 890.dp
        WindowHeightSizeClass.Expanded -> 1200.dp
        else -> 470.dp
    }

    return WindowSizeClass.calculateFromSize(DpSize(dummyWidthValue, dummyHeightValue))
}
