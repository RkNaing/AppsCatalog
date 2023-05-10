package com.rkzmn.appscatalog.presentation.apps.list

import androidx.annotation.DrawableRes
import com.rkzmn.appscatalog.domain.model.AppInfo
import com.rkzmn.appscatalog.utils.android.UiString
import com.rkzmn.appscatalog.utils.app.Drawables
import com.rkzmn.appscatalog.utils.app.Strings
import com.rkzmn.appscatalog.utils.kotlin.byteCountToDisplaySize

data class AppItem(
    val appName: String? = null,
    val appIcon: String? = null,
    val readableSize: String? = null,
    val packageName: String,
    val version: String = "",
    val indicators: List<Indicator> = emptyList(),
    val isSelected: Boolean = false,
) {

    data class Indicator(
        @DrawableRes val icon: Int,
        val contentDescription: UiString = UiString.empty
    ) {
        companion object {
            val systemAppIndicator = Indicator(
                icon = Drawables.ic_system_app,
                contentDescription = UiString.from(Strings.lbl_system_app)
            )
            val debuggableIndicator = Indicator(
                icon = Drawables.ic_debugable,
                contentDescription = UiString.from(Strings.lbl_debuggable)
            )
        }
    }

    companion object {
        fun from(appInfo: AppInfo): AppItem {

            val displayVersion = buildString {
                val components = mutableListOf<String>()
                val versionName = appInfo.versionName
                val versionCode = appInfo.versionCode

                if (!versionName.isNullOrBlank()) {
                    components.add(versionName)
                }

                if (versionCode != 0L) {
                    components.add("($versionCode)")
                }

                if (components.isNotEmpty()) {
                    append(components.joinToString(separator = " "))
                }
            }

            val indicators = mutableListOf<Indicator>()
            if (appInfo.isSystemApp) {
                indicators += Indicator.systemAppIndicator
            }
            if (appInfo.isDebuggable) {
                indicators += Indicator.debuggableIndicator
            }

            return AppItem(
                appName = appInfo.appName.takeIf { it.isNotBlank() && it != appInfo.packageName },
                appIcon = appInfo.appIcon,
                packageName = appInfo.packageName,
                version = displayVersion,
                indicators = indicators.toList(),
                readableSize = appInfo.appSize.takeIf { it > 0 }?.let { byteCountToDisplaySize(it) }
            )
        }
    }


}


