package com.rkzmn.appscatalog.domain.model

data class AppDetails(
    val appName: String? = null,
    val appIcon: String? = null,
    val versionCode: Long = 0L,
    val versionName: String? = null,
    val packageName: String = "",
    val appTypeIndicators: List<AppTypeIndicator> = emptyList(),
    val installationSource: String? = null,
    val installedTimestamp: String? = null,
    val lastUpdatedTimestamp: String? = null,
    val lastUsedTimestamp: String? = null,
    val appSize: String? = null,
    val minAndroidVersion: String,
    val targetAndroidVersion: String,
    val compileSdkAndroidVersion: String,
    val activities: List<AppComponentInfo> = emptyList(),
    val services: List<AppComponentInfo> = emptyList(),
    val broadcastReceivers: List<AppComponentInfo> = emptyList(),
    val permissions: List<AppPermissionInfo> = emptyList(),
)
