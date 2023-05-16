package com.rkzmn.appscatalog.presentation.apps.options

import androidx.annotation.DrawableRes
import com.rkzmn.appscatalog.domain.model.AppSortOption
import com.rkzmn.appscatalog.utils.android.UiString
import com.rkzmn.appscatalog.utils.app.AppDrawables
import com.rkzmn.appscatalog.utils.app.AppStrings

data class AppSortFilter(
    val label: UiString,
    @DrawableRes val icon: Int,
    val options: List<AppSortOption>,
) {

    companion object {
        val filters = arrayOf(
            AppSortFilter(
                label = UiString.from(AppStrings.lbl_sort_by_name),
                icon = AppDrawables.ic_sort_alphabetically_24,
                options = listOf(
                    AppSortOption.NAME_ASC,
                    AppSortOption.NAME_DESC
                )
            ),
            AppSortFilter(
                label = UiString.from(AppStrings.lbl_sort_by_size),
                icon = AppDrawables.ic_storage_24,
                options = listOf(
                    AppSortOption.SIZE_ASC,
                    AppSortOption.SIZE_DESC
                ),
            ),
            AppSortFilter(
                label = UiString.from(AppStrings.lbl_sort_by_update),
                icon = AppDrawables.ic_update_24,
                options = listOf(
                    AppSortOption.LAST_UPDATED_DESC,
                    AppSortOption.LAST_UPDATED_ASC
                ),
            ),
            AppSortFilter(
                label = UiString.from(AppStrings.lbl_sort_by_installed),
                icon = AppDrawables.ic_install_24,
                options = listOf(
                    AppSortOption.INSTALLED_DATE_DESC,
                    AppSortOption.INSTALLED_DATE_ASC
                ),
            ),
            AppSortFilter(
                label = UiString.from(AppStrings.lbl_sort_by_usage),
                icon = AppDrawables.ic_app_use_24,
                options = listOf(
                    AppSortOption.LAST_USED_DESC,
                    AppSortOption.LAST_USED_ASC
                ),
            ),
        )
    }
}