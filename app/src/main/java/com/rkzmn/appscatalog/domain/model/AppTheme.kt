package com.rkzmn.appscatalog.domain.model

import com.rkzmn.appscatalog.utils.android.UiString
import com.rkzmn.appscatalog.utils.app.AppStrings

enum class AppTheme(val label: UiString) {
    LIGHT(UiString.from(AppStrings.lbl_theme_light)),
    DARK(UiString.from(AppStrings.lbl_theme_dark)),
    FOLLOW_SYSTEM(UiString.from(AppStrings.lbl_theme_follow_system)),
}

