package com.rkzmn.appscatalog.domain.model

data class AppInfo(
    val appName: String = "",
    val appIcon: String? = null,
    val versionCode: Long = 0L,
    val versionName: String? = null,
    val packageName: String = "",
    val isSystemApp: Boolean = false,
    val isDebuggable: Boolean = false,
    val installationSource: String? = null,
    val installedTimestamp: Long = 0L,
    val lastUpdatedTimestamp: Long = 0L,
    val lastUsedTimestamp: Long? = 0L,
    val appSize: Long = 0L,
) {

    val displayVersion: String = buildString {
        val components = mutableListOf<String>()
        if (!versionName.isNullOrBlank()) {
            components.add(versionName)
        }

        if (versionCode != 0L) {
            components.add("($versionCode)")
        }

        if (components.isNotEmpty()){
            append(components.joinToString(separator = " "))
        }
    }

}