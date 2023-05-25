package com.rkzmn.appscatalog.domain.model

import com.rkzmn.appscatalog.utils.android.UiString
import com.rkzmn.appscatalog.utils.app.AppStrings

enum class AppSortOption(val label: UiString) {

    NAME_ASC(label = UiString.from(AppStrings.lbl_ascending)),
    NAME_DESC(label = UiString.from(AppStrings.lbl_descending)),

    INSTALLED_DATE_ASC(label = UiString.from(AppStrings.lbl_recent_installed)),
    INSTALLED_DATE_DESC(label = UiString.from(AppStrings.lbl_oldest_installed)),

    LAST_UPDATED_ASC(label = UiString.from(AppStrings.lbl_recently_updated)),
    LAST_UPDATED_DESC(label = UiString.from(AppStrings.lbl_least_update)),

    SIZE_ASC(label = UiString.from(AppStrings.lbl_smaller_to_bigger)),
    SIZE_DESC(label = UiString.from(AppStrings.lbl_bigger_to_smaller)),

    LAST_USED_ASC(label = UiString.from(AppStrings.lbl_recently_used)),
    LAST_USED_DESC(label = UiString.from(AppStrings.lbl_rarely_used)),
}
