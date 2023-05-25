package com.rkzmn.appscatalog.domain.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AppDetails(
    val appName: String? = null,
    val appIcon: String? = null,
    val versionCode: Long = 0L,
    val versionName: String? = null,
    val packageName: String = "",
    val appTypeIndicators: ImmutableList<AppTypeIndicator> = persistentListOf(),
    val installationSource: String? = null,
    val installedTimestamp: String? = null,
    val lastUpdatedTimestamp: String? = null,
    val lastUsedTimestamp: String? = null,
    val appSize: String? = null,
    val minAndroidVersion: String,
    val targetAndroidVersion: String,
    val compileSdkAndroidVersion: String,
    val activities: ImmutableList<AppComponentInfo> = persistentListOf(),
    val services: ImmutableList<AppComponentInfo> = persistentListOf(),
    val broadcastReceivers: ImmutableList<AppComponentInfo> = persistentListOf(),
    val permissions: ImmutableList<AppPermissionInfo> = persistentListOf(),
)
