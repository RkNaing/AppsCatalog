package com.rkzmn.appscatalog.presentation.apps.list.states

import androidx.annotation.DrawableRes
import com.rkzmn.appscatalog.utils.android.UiString
import com.rkzmn.appscatalog.utils.app.AppDrawables
import com.rkzmn.appscatalog.utils.app.AppStrings

enum class AppsDisplayType(
    val label: UiString,
    @DrawableRes val icon: Int,
) {
    LIST(
        label = UiString.from(AppStrings.lbl_app_display_type_list),
        icon = AppDrawables.ic_list_24
    ),
    GRID(
        label = UiString.from(AppStrings.lbl_app_display_type_grid),
        icon = AppDrawables.ic_grid_24
    )
}