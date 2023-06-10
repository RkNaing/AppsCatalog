package com.rkzmn.appsdataprovider.model

import com.rkzmn.appsdataprovider.utils.getAndroidVersionLabel

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
    val appSize: Long,
    val minSdk: Int,
    val targetSdk: Int,
    val compileSdk: Int,
) {

    val minAndroidVersion: String
        get() = getAndroidVersionLabel(minSdk)

    val targetAndroidVersion: String
        get() = getAndroidVersionLabel(targetSdk)

    val compileSdkAndroidVersion: String
        get() = getAndroidVersionLabel(compileSdk)
    companion object
}
