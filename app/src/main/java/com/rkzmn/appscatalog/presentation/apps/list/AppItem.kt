package com.rkzmn.appscatalog.presentation.apps.list

import com.rkzmn.appscatalog.domain.mappers.displayName
import com.rkzmn.appscatalog.domain.mappers.indicators
import com.rkzmn.appscatalog.domain.mappers.readableSize
import com.rkzmn.appscatalog.domain.model.AppInfo
import com.rkzmn.appscatalog.domain.model.AppTypeIndicator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AppItem(
    val appName: String? = null,
    val appIcon: String? = null,
    val readableSize: String? = null,
    val packageName: String,
    val version: String = "",
    val appTypeIndicators: ImmutableList<AppTypeIndicator> = persistentListOf(),
    val isSelected: Boolean = false,
) {

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

            return AppItem(
                appName = appInfo.displayName,
                appIcon = appInfo.appIcon,
                packageName = appInfo.packageName,
                version = displayVersion,
                appTypeIndicators = appInfo.indicators,
                readableSize = appInfo.readableSize

            )
        }
    }
}
