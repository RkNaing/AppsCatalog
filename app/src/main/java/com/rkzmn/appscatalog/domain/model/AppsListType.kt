package com.rkzmn.appscatalog.domain.model

import com.rkzmn.appscatalog.utils.android.UiString
import com.rkzmn.appscatalog.utils.app.Strings

enum class AppsListType(val label: UiString) {
    ALL(UiString.from(Strings.lbl_all_apps)),
    INSTALLED(UiString.from(Strings.lbl_installed_apps)),
    SYSTEM(UiString.from(Strings.lbl_system_apps)),
}