package com.rkzmn.appscatalog.domain.model

import com.rkzmn.appscatalog.utils.android.UiString
import com.rkzmn.appscatalog.utils.app.AppStrings

enum class AppsListType(val label: UiString) {
    ALL(UiString.from(AppStrings.lbl_all_apps)),
    INSTALLED(UiString.from(AppStrings.lbl_installed_apps)),
    SYSTEM(UiString.from(AppStrings.lbl_system_apps)),
}