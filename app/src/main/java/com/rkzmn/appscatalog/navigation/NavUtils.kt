package com.rkzmn.appscatalog.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

// /////////////////////////////////////////////////////////////////////////
// Named Navigation Argument Builders
// /////////////////////////////////////////////////////////////////////////
fun stringNavArgument(
    key: String,
    isNullable: Boolean = true,
    default: String? = null,
) = navArgument(key) {
    type = NavType.StringType
    nullable = isNullable
    defaultValue = default
}
