package com.rkzmn.apps_data_provider.model

data class App(
    val appName: String,
    val appIcon: String?,
    val versionCode: Long,
    val versionName: String?,
    val packageName: String,
    val isSystemApp: Boolean,
    val isDebuggable: Boolean,
    val installationSource: String?,
    val installedTimestamp: Long,
    val lastUpdatedTimestamp: Long,
    val lastUsedTimestamp: Long?,
    val appSize: Long
)
