package com.rkzmn.appscatalog.domain.model

import androidx.annotation.DrawableRes
import com.rkzmn.appscatalog.utils.android.UiString
import com.rkzmn.appscatalog.utils.app.AppDrawables
import com.rkzmn.appscatalog.utils.app.AppStrings

data class AppTypeIndicator(
    @DrawableRes val icon: Int,
    val contentDescription: UiString = UiString.empty
) {
    companion object {
        val system = AppTypeIndicator(
            icon = AppDrawables.ic_system_app,
            contentDescription = UiString.from(AppStrings.lbl_system)
        )
        val debuggable = AppTypeIndicator(
            icon = AppDrawables.ic_debugable,
            contentDescription = UiString.from(AppStrings.lbl_debuggable)
        )
        val installed = AppTypeIndicator(
            icon = AppDrawables.ic_install_24,
            contentDescription = UiString.from(AppStrings.lbl_user_installed)
        )
    }
}